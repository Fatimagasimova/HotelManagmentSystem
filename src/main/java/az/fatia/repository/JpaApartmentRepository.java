package az.fatia.repository;

import az.fatia.model.Apartment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class JpaApartmentRepository implements ApartmentRepository {

    @PersistenceContext
    private EntityManager em; // Managed automatically by Spring Data JPA

    @Override
    @Transactional // Spring automatically handles EntityTransaction begin/commit/rollback
    public void save(Apartment apartment) {
        em.merge(apartment);
    }

    @Override
    public Apartment findById(int id) {
        return em.find(Apartment.class, id);
    }

    @Override
    public List<Apartment> findAll() {
        return em.createQuery("SELECT a FROM Apartment a", Apartment.class).getResultList();
    }
}