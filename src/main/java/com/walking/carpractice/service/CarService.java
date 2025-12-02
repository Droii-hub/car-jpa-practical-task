package com.walking.carpractice.service;

import com.walking.carpractice.dto.car.CarCreateDto;
import com.walking.carpractice.dto.car.CarUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.model.*;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.*;
import org.hibernate.jpa.SpecHints;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CarService {
    private static CarService instance;
    private CarService(EntityManagerFactory emf){
        this.emf=emf;
        this.helper =new EntityManagerHelper(emf);
    }

    public static CarService getInstance(EntityManagerFactory emf){
        if (instance==null){
            instance=new CarService(emf);
        }
        return instance;
    }

    private final EntityManagerFactory emf;
    private final EntityManagerHelper helper;


    public CarEntity create(CarCreateDto carDto){
        return helper.runTransactional(em->{
            CarEntity car=new CarEntity();
            car.setCreation_year(carDto.getCreation_year());
            car.setNumber(carDto.getNumber());
            car.setColor(carDto.getColor());
            car.setActualTechnicalInspection(carDto.isActualTechnicalInspection());
            car.setModel(em.find(ModelEntity.class, carDto.getModel().getId()));
            em.persist(car);
            return car;
        });
    }

    public CarEntity read(long id){
        return helper.runTransactional(em->em.find(CarEntity.class, id));
    }

    public CarEntity update(CarUpdateDto carDto){
        return helper.runTransactional(em-> {
            CarEntity car = em.find(CarEntity.class, carDto.getId());
            car.setCreation_year(carDto.getCreation_year());
            car.setNumber(carDto.getNumber());
            car.setColor(carDto.getColor());
            car.setActualTechnicalInspection(carDto.isActualTechnicalInspection());
            car.setModel(em.find(ModelEntity.class, carDto.getModel().getId()));
            return car;
        });
    }

    public void delete(long id){
        helper.runTransactionalNoResult(em->{
            var car=em.find(CarEntity.class, id);
            if (car.getOwners().isEmpty()) {
                em.remove(car);
            } else {
                throw new ApplicationException(ErrorCode.DELETING_OWNERS_CAR);
            }
        });
    }

    public List<String> getOwners(long id){
        return helper.runTransactional(em->{
            EntityGraph entityGraph=em.getEntityGraph("car-with-owners");
            Map<String, Object> properties=Map.of(SpecHints.HINT_SPEC_LOAD_GRAPH, entityGraph);
            return em.find(CarEntity.class, id, properties).getOwners()
                    .stream().map(OwnerEntity::getEmail).toList();
        });
    }

    public void disableTechnicalInspectionByYear(int year){
        helper.runTransactionalNoResult(em->em
                .createQuery("update CarEntity c set c.actualTechnicalInspection=false where c.creation_year<?1")
                .setParameter(1,year)
                .executeUpdate());
    }

    public List<CarEntity> find(String color, String brand, String model){
        return helper.runTransactional(em-> {
            CriteriaBuilder builder=em.getCriteriaBuilder();
            CriteriaQuery<CarEntity> query=builder.createQuery(CarEntity.class);

            Root<CarEntity> carRoot=query.from(CarEntity.class);

            //carRoot.join(CarEntity_.model);
            Join<CarEntity, ModelEntity> modelJoin=carRoot.join(CarEntity_.model, JoinType.INNER);


            List<Predicate> predicates=new ArrayList<>();

            if (color!=null)
                query.where(builder.equal(carRoot.get(CarEntity_.color),color));
            if (model!=null){
                modelJoin.on(builder.equal(modelJoin.get(ModelEntity_.name), model));
            }

            return em.createQuery(query).getResultList();
        });
    }
}
