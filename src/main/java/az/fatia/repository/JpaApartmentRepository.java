package az.fatia.repository;

import az.fatia.config.JpaConfig;
import az.fatia.model.Apartment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class JpaApartmentRepository implements ApartmentRepository {

    @Override
    public void save(Apartment apartment) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(apartment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("JPA Error during saving apartment", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Apartment findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Apartment.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Apartment> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Apartment a", Apartment.class).getResultList();
        } finally {
            em.close();
        }
    }
}