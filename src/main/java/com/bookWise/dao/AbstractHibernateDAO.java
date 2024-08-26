package com.bookWise.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class AbstractHibernateDAO extends HibernateDaoSupport implements BaseDAO {
    public AbstractHibernateDAO (){

    }

    public void delete(Object obj) {
        getHibernateTemplate().delete(obj);
    }

    public void deleteAll(Collection collection) {
        getHibernateTemplate().deleteAll(collection);
    }

    public Serializable save(Object obj) {
        Serializable generatedId = getHibernateTemplate().save(obj);
        getHibernateTemplate().flush();
        return generatedId;
    }

    public void saveOrUpdate(Object obj) {
        getHibernateTemplate().saveOrUpdate(obj);
        flush();
    }

    public void merge(Object obj) {
        getHibernateTemplate().merge(obj);
        getHibernateTemplate().flush();
    }

    public void refresh(Object obj) {
        getHibernateTemplate().refresh(obj);
    }

    public void saveOrUpdateAll(Collection collection) {
        collection.forEach(clazz -> getHibernateTemplate().saveOrUpdate(clazz));
    }

    public Object find(Class clazz, Serializable id) {
        return getHibernateTemplate().get(clazz, id);
    }

    public Object load(Class clazz, Serializable id) {
        return getHibernateTemplate().load(clazz, id);
    }

    public <T> List<T> findBy(String hsql) {
        return (List<T>) getHibernateTemplate().find(hsql);
    }

    protected List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
        return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
    }

    protected <T> List<T> findByCriteria(DetachedCriteria criteria) {
        return (List<T>) getHibernateTemplate().findByCriteria(criteria);
    }

    public <T> List<T> findAll(Class clazz) {
        return (List<T>) getHibernateTemplate().find("from " + clazz.getName());
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    public void evict(Object obj) {
        getHibernateTemplate().evict(obj);
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        return getHibernateTemplate().get(clazz, id);
    }
}
