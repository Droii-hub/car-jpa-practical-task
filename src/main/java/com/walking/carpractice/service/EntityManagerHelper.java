package com.walking.carpractice.service;

import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class EntityManagerHelper {
    private final EntityManagerFactory entityManagerFactory;

    public EntityManagerHelper(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void runTransactionalNoResult(Consumer<EntityManager> task) {
        Function<EntityManager, Object> noResultTask = em -> {
            task.accept(em);
            return null;
        };

        runTransactional(noResultTask);
    }

    public <T> T runTransactional(Function<EntityManager, T> task) {
        try (var em = getEntityManager()) {
            try {
                var transaction = em.getTransaction();
                transaction.begin();

                T result = task.apply(em);

                transaction.commit();

                return result;
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }

        } catch (Exception e) {
            //Тут можно обработать известные исключения для более подробных кодов ошибок
            if (e.getMessage().contains("ERROR: duplicate"))
                throw new ApplicationException(ErrorCode.DUPLICATE, e);
            if (e.getMessage().contains("violates check constraint"))
                throw new ApplicationException(ErrorCode.CONSTRAINT_FAIL,e);
            throw new ApplicationException(ErrorCode.TRANSACTION_ERROR, e);
        }
    }

    //    JPA не потокобезопасен.
//    Соответственно, если контейнер не берет задачу контроля за созданием EM на себя - лучше перестраховаться
    public synchronized EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
