package com.walking.carpractice.service;

import com.walking.carpractice.dto.model.ModelCreateDto;
import com.walking.carpractice.dto.model.ModelUpdateDto;
import com.walking.carpractice.model.ModelEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ModelService {
    private static ModelService instance;
    private final EntityManagerHelper entityManagerHelper;

    private ModelService(EntityManagerFactory emf){
        this.entityManagerHelper=new EntityManagerHelper(emf);
    }

    public static ModelService getInstance(EntityManagerFactory emf){
        if (instance==null){
            instance=new ModelService(emf);
        }
        return instance;
    }

    public ModelEntity create(ModelCreateDto newModel){
        return entityManagerHelper.runTransactional(em->{
            ModelEntity modelEntity=new ModelEntity(newModel);
            em.persist(modelEntity);
            return modelEntity;
        });
    }

    public ModelEntity readById(long id){
        return entityManagerHelper.runTransactional(em->em.find(ModelEntity.class, id));
    }

    public List<ModelEntity> readByBrand(long brandId){
        return entityManagerHelper.runTransactional(em->em
                .createNativeQuery("select * from model where brand_id=?", ModelEntity.class)
                .setParameter(1,brandId)
                .getResultList());
    }

    public ModelEntity update(ModelUpdateDto modelUpdateDto){
        return entityManagerHelper.runTransactional(em->{
            var model=em.find(ModelEntity.class, modelUpdateDto.getId());
            model.setName(modelUpdateDto.getName());
            model.setBrand(modelUpdateDto.getBrand());
            return model;
        });
    }

    public void delete(long id){
        entityManagerHelper.runTransactionalNoResult(em->{
            var model=em.find(ModelEntity.class, id);
            em.remove(model);
        });
    }
}
