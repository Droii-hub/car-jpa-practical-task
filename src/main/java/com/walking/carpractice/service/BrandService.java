package com.walking.carpractice.service;

import com.walking.carpractice.dto.brand.BrandCreateDto;
import com.walking.carpractice.dto.brand.BrandUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.model.BrandEntity;
import com.walking.carpractice.model.ModelEntity;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.SpecHints;

import java.util.List;
import java.util.Map;

public class BrandService {
    private static BrandService instance;
    private BrandService(EntityManagerFactory emf){
        this.emf=emf;
        this.entityManagerHelper=new EntityManagerHelper(emf);

    }

    public static BrandService getInstance(EntityManagerFactory emf){
        if (instance==null){
            instance=new BrandService(emf);
        }
        return instance;
    }

    private final EntityManagerHelper entityManagerHelper;
    private final EntityManagerFactory emf;

    public BrandEntity create(BrandCreateDto newBrand){
        return entityManagerHelper.runTransactional(em->{
            BrandEntity brandEntity=new BrandEntity(newBrand);
            em.persist(brandEntity);
            return brandEntity;
        });
    }

    public List<BrandEntity> read(){
        return entityManagerHelper.runTransactional(em->em
                .createQuery("select b from BrandEntity b", BrandEntity.class).getResultList());
    }

    public BrandEntity update(BrandUpdateDto updateDto){
        return entityManagerHelper.runTransactional(em->{
           var brand=em.find(BrandEntity.class, updateDto.getId());
           brand.setName(updateDto.getName());
           return brand;
        });
    }

    public void delete(long id){
        entityManagerHelper.runTransactionalNoResult(em->{
            var brand=em.find(BrandEntity.class, id);
            em.remove(brand);
        });
    }

    public List<ModelEntity> getModels(long id){
        return entityManagerHelper.runTransactional(em->{
            EntityGraph entityGraph=em.getEntityGraph("brand-with-models");
            Map<String, Object> properties=Map.of(SpecHints.HINT_SPEC_LOAD_GRAPH, entityGraph);
            var brand=em.find(BrandEntity.class, id, properties);
            return brand.getModels();
        });
    }
}
