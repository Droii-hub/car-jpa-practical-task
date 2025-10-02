package com.walking.carpractice.service;

import com.walking.carpractice.dto.brand.BrandCreateDto;
import com.walking.carpractice.dto.brand.BrandUpdateDto;
import com.walking.carpractice.dto.model.ModelCreateDto;
import com.walking.carpractice.model.BrandEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

public class BrandServiceTest {
    private EntityManagerFactory emf;
    private BrandService brandService;
    private ModelService modelService;


    @BeforeEach
    void setUp(){
        emf= Persistence.createEntityManagerFactory("Hibernate");
        FluentConfiguration flywayConfiguration= Flyway.configure().dataSource(getDataSource(emf));
        Flyway flyway = flywayConfiguration.load();
        flyway.migrate();
        brandService=BrandService.getInstance(emf);
        modelService=ModelService.getInstance(emf);
    }

    private DataSource getDataSource(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.unwrap(SessionFactoryServiceRegistry.class)
                .getService(ConnectionProvider.class)
                .unwrap(DataSource.class);
    }

    @AfterEach
    void destructor(){
        EntityManager entityManager= emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("""
                drop table flyway_schema_history, owner_car, owner, car, model, brand
                """).executeUpdate();
        entityManager.getTransaction().commit();
        //emf.close();
    }

    @Test
    void create_success(){
        //given
        BrandCreateDto newBrand=new BrandCreateDto("Reno");
        //when
        BrandEntity brand=brandService.create(newBrand);
        //then
        Assertions.assertEquals("Reno", brand.getName());
    }

    @Test
    void read_success(){
        //given
        BrandCreateDto newBrand=new BrandCreateDto("Reno");
        brandService.create(newBrand);
        //when
        List<BrandEntity> brands=brandService.read();
        //then
        Assertions.assertEquals("Reno", brands.getFirst().getName());
    }

    @Test
    void update_success(){
        //given
        BrandCreateDto newBrand=new BrandCreateDto("Reno");
        var brand=brandService.create(newBrand);
        BrandUpdateDto updatedBrandData=new BrandUpdateDto(brand.getId(), "Nissan");
        //when
        var timestamp= LocalDateTime.now();
        var updatedBrand=brandService.update(updatedBrandData);
        //then
        Assertions.assertEquals("Nissan", updatedBrand.getName());
        Assertions.assertTrue(timestamp.isBefore(updatedBrand.getUpdated()));
    }

    @Test
    void delete_success(){
        //given
        BrandCreateDto newBrand=new BrandCreateDto("Reno");
        long brandId=brandService.create(newBrand).getId();
        //when
        brandService.delete(brandId);
        List<BrandEntity> brands=brandService.read();
        //then
        Assertions.assertTrue(brands.isEmpty());
    }

    @Test
    void getModels_success(){
        //given
        BrandCreateDto newBrand=new BrandCreateDto("Reno");
        var brand=brandService.create(newBrand);

        modelService.create(new ModelCreateDto("Megan",brand));
        modelService.create(new ModelCreateDto("Duster", brand));
        //when
        var models=brandService.getModels(brand.getId());
        //then
        Assertions.assertEquals("Megan", models.getFirst().getName());
        Assertions.assertEquals("Duster", models.getLast().getName());
    }

}
