package com.walking.carpractice.service;

import com.walking.carpractice.dto.car.CarCreateDto;
import com.walking.carpractice.dto.car.CarUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.model.CarEntity;
import com.walking.carpractice.model.ModelEntity;
import com.walking.carpractice.model.OwnerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

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
        EntityManager em=emf.createEntityManager();
        try{
            em.getTransaction().begin();
            var car=em.find(CarEntity.class, id);
            var owners=car.getOwners();
            em.getTransaction().commit();
            return owners.stream().map(OwnerEntity::getEmail).toList();
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.TRANSACTION_ERROR, e);
        }
        //Нужно новое представление для владельцев, нужен мапинг в это представление
    }
}
