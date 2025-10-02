package com.walking.carpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.walking.carpractice.service.BrandService;
import com.walking.carpractice.service.CarService;
import com.walking.carpractice.service.ModelService;
import com.walking.carpractice.service.OwnerService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import javax.sql.DataSource;

public class InitParamsListener implements ServletContextListener {
    private final Logger log= LogManager.getLogger(InitParamsListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event){
        try {
            ServletContext context = event.getServletContext();
            EntityManagerFactory emf= Persistence.createEntityManagerFactory("Hibernate");
            FluentConfiguration flywayConfiguration= Flyway.configure().dataSource(getDataSource(emf));
            Flyway flyway = flywayConfiguration.load();
            flyway.migrate();

            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.registerModule(new JavaTimeModule());

            context.setAttribute("emf", emf);
            context.setAttribute("carService", CarService.getInstance(emf));
            context.setAttribute("ownerService", OwnerService.getInstance(emf));
            context.setAttribute("brandService", BrandService.getInstance(emf));
            context.setAttribute("modelService", ModelService.getInstance(emf));
            context.setAttribute("objectMapper", objectMapper);
            log.info("Object mapper initialised");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    private DataSource getDataSource(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.unwrap(SessionFactoryServiceRegistry.class)
                .getService(ConnectionProvider.class)
                .unwrap(DataSource.class);
    }
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        var context = event.getServletContext();
        EntityManagerFactory emf=(EntityManagerFactory) context.getAttribute("emf");
        emf.close();
    }
}
