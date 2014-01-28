package ac.tuwien.sa13.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic DAO operations dependent with Hibernate's specific classes
 * @see SessionFactory
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
public class HibernateDAO<E, K extends Serializable> implements IGenericDAO<E, K> {
	
	private SessionFactory sessionFactory;
    protected Class<? extends E> daoType;

    public HibernateDAO() {
        daoType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void add(E entity) {
        currentSession().save(entity);
    }

    public void update(E entity) {
        currentSession().update(entity);
    }

    public void remove(E entity) {
        currentSession().delete(entity);
    }

    public E find(K key) {
        return (E) currentSession().get(daoType, key);
    }

    public List<E> list() {
        return currentSession().createCriteria(daoType).list();
    }
}
