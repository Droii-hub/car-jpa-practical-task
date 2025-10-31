package com.walking.carpractice.service;

import com.walking.carpractice.dto.brand.BrandCreateDto;
import com.walking.carpractice.dto.brand.BrandUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.model.BrandEntity;
import com.walking.carpractice.model.ModelEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

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
        //при использовании помощника сессия гибернейта закрывается до обращения к моделям и получается
        //failed to lazily initialize a collection of role: com.walking.carpractice.model.BrandEntity.models:
        // could not initialize proxy - no Session
//        return entityManagerHelper.runTransactional(em->{
//            var brand=em.find(BrandEntity.class, id);
//            return brand.getModels();
//        });
        EntityManager entityManager= emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var brand = entityManager.find(BrandEntity.class, id);
            var models = brand.getModels();
            entityManager.getTransaction().commit();
            return models;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.TRANSACTION_ERROR,e);
        }
    }
}
