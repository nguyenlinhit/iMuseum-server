package com.tma.imuseum.model.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tma.imuseum.model.pojo.Artifact;

// Abstract class to do default CRUD action with database
@Transactional
public abstract class DAO<PK extends Serializable, T> {
    private final Class<T> persistentClass;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public DAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Session getOpenSession() {
        return sessionFactory.openSession();
    }

    void flushSession() {
        getOpenSession().flush();
    }

    void closeSession() {
        getOpenSession().close();
    }

    // Get element by primary key
    public T getByKey(PK key) {
        Session temp = getSession();
        temp.clear();
        T t = temp.get(persistentClass, key);
        temp.flush();
        return t;
    }

    // Create new element
    public void persist(T entity) {
        Session temp = getSession();
        temp.save(entity);
    }

    T persist1(T entity) {
        Session temp = getSession();
        temp.save(entity);
        temp.flush();
        temp.refresh(entity);
        temp.evict(entity);
        return entity;
    }

    // Update element
    public void update(T entity) {
        Session temp = getSession();
        temp.merge(entity);
    }

    // Delete element
    public void delete(T entity) {
        getSession().delete(entity);
    }

    // Delete element by Id
    public void deleteById(PK key) {
        T temp = getSession().get(persistentClass, key);
        if (null != temp) {
            delete(temp);
        }
    }

    List<T> callSPDAO(String spName, Map<String, Object> inputValue) {
        Query query = getSession().getNamedQuery(spName);
        String logString = spName + "- ";
        for (String para : inputValue.keySet()) {
            query.setParameter(para, inputValue.get(para));
            logString += para + ": " + inputValue.get(para) + ", ";
        }
        List<T> temp = query.getResultList();
        logString += "size: " + temp.size();
        logger.info(logString);
        return temp;
    }

    // Get all element
    protected List<T> createEntityCriteria(int page, int num, int active) {
        Criteria cr = getSession().createCriteria(persistentClass);
        try {
            // For class has no isActive attribute
            if (!Class.forName("com.tma.imuseum.model.pojo.Survey").equals(persistentClass)
                    && !Class.forName("com.tma.imuseum.model.pojo.Media").equals(persistentClass)
                    && !Class.forName("com.tma.imuseum.model.pojo.Location").equals(persistentClass)
                    && !Class.forName("com.tma.imuseum.model.pojo.Log").equals(persistentClass)
                    && !Class.forName("com.tma.imuseum.model.pojo.Request").equals(persistentClass)
                    && !Class.forName("com.tma.imuseum.model.pojo.Category").equals(persistentClass)) {
                if (0 == active) {
                    cr.add(Restrictions.eq("isActive", false));
                } else {
                    cr.add(Restrictions.eq("isActive", true));
                }
            }
        } catch (Exception ignored) {
        }
        if (page > 0) {
            cr.setFirstResult((page - 1) * num);
            cr.setMaxResults(num);
        }
        List<T> result = cr.list();
        logger.info("Dad: " + Integer.toString(result.size()));
        return result;
    }
}