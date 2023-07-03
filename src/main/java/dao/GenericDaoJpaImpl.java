package dao;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.JpaFactory;

import jakarta.persistence.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class GenericDaoJpaImpl<T, K> implements GenericDao<T, K> {

    private final Class<T> type;

    public GenericDaoJpaImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public void save(T t) {
        Session session = getSessionFactory().openSession();
        session.getTransaction().begin();
        session.persist(t);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(T t) {
        Session session = getSessionFactory().openSession();
        session.getTransaction().begin();
        t = session.merge(t);
        session.remove(t);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(T t) {
        Session session = getSessionFactory().openSession();
        session.getTransaction().begin();
        session.merge(t);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Optional<T> findById(K id) {
        Session session = getSessionFactory().openSession();
        T dto = session.find(type, id);
        session.close();
        return Optional.ofNullable(dto);
    }

    @Override
    public List<T> findAll() {
        Session session = getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> rootEntry = cq.from(type);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }

    protected SessionFactory getSessionFactory() {
        return JpaFactory.getSessionFactory();
    }
}
