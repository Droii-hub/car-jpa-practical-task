package com.walking.carpractice.service;

import com.walking.carpractice.CarCountByOwner;
import com.walking.carpractice.PasswordProvider;
import com.walking.carpractice.dto.owner.OwnerCreateDto;
import com.walking.carpractice.dto.owner.OwnerDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.model.CarEntity;
import com.walking.carpractice.model.OwnerEntity;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.SpecHints;

import java.util.List;
import java.util.Map;

public class OwnerService {
    private static OwnerService instance;
    private final EntityManagerHelper helper;
    private final EntityManagerFactory emf;

    private OwnerService(EntityManagerFactory emf){
        this.emf=emf;
        this.helper=new EntityManagerHelper(emf);
    }

    public static OwnerService getInstance(EntityManagerFactory emf){
        if(instance==null){
            instance=new OwnerService(emf);
        }
        return instance;
    }

    public void create(OwnerCreateDto newOwner){
        try {
            helper.runTransactionalNoResult(em -> {
                OwnerEntity owner = new OwnerEntity();
                owner.setEmail(newOwner.getEmail());
                owner.setPassword(PasswordProvider.hashPassword(newOwner.getPassword()));
                em.persist(owner);
            });
        } catch (ApplicationException e){
            if (e.getErrorCode().getInternalCode()==200)
                throw new ApplicationException(ErrorCode.DUPLICATE_OWNER);
            if (e.getErrorCode().getInternalCode()==300)
                throw new ApplicationException(ErrorCode.WRONG_EMAIL_FORMAT);
            throw e;
        }
    }

    public OwnerDto read(String email){
        return helper.runTransactional(em->{
           var owner=(OwnerEntity)em.createQuery("select o from OwnerEntity o where email=?1", OwnerEntity.class)
                   .setParameter(1,email)
                   .getSingleResult();
           if (owner!=null) {
               return new OwnerDto(owner.getId(), owner.getEmail(), owner.getPassword());
           } else {
               throw new ApplicationException(ErrorCode.OWNER_NOT_FOUND);
           }
        });
    }

    public void update(OwnerDto ownerDto){
        helper.runTransactionalNoResult(em->{
            var owner=em.find(OwnerEntity.class, ownerDto.getId());
            owner.setEmail(ownerDto.getEmail());
            owner.setPassword(PasswordProvider.hashPassword(ownerDto.getPassword()));
        });
    }

    public void addCar(long carId, long ownerId){
        helper.runTransactionalNoResult(em->{
            var car=em.find(CarEntity.class, carId);
            var owner=em.find(OwnerEntity.class, ownerId);
            owner.getCars().add(car);
        });
    }

    public void dropCar(long carId, long ownerId){
        helper.runTransactionalNoResult(em->{
            var car=em.find(CarEntity.class, carId);
            var owner=em.find(OwnerEntity.class, ownerId);
            owner.getCars().remove(car);
        });
    }

    public List<CarEntity> getCars(long id){
        return helper.runTransactional(em->{
            EntityGraph entityGraph=em.getEntityGraph("owner-with-cars");
            Map<String, Object> properties=Map.of(SpecHints.HINT_SPEC_LOAD_GRAPH, entityGraph);
            return em.find(OwnerEntity.class, id, properties).getCars();
        });
    }

    public List<CarCountByOwner> carsByOwner(){
        return helper.runTransactional(em->em
                .createQuery("select new com.walking.carpractice.CarCountByOwner(o.email, count(c)) from OwnerEntity o join o.cars c group by o.email", CarCountByOwner.class)
                .getResultList());
    }
}
