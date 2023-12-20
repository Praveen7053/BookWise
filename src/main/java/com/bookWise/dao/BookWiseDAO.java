package com.bookWise.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class BookWiseDAO extends HibernateDaoSupport {
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

    public void saveOrUpdateAll(Collection<? extends AbstractBaseDomain> collection) {
        collection.stream().forEach( clazz -> {getHibernateTemplate().saveOrUpdate(clazz);} );
        //getHibernateTemplate().saveOrUpdateAll(collection);
        //getHibernateTemplate().flush();
    }


    public Object find(Class clazz, Serializable id) {
        return (Object) getHibernateTemplate().get(clazz, id);
    }


    public Object load(Class clazz, Serializable id) {
        return (Object) getHibernateTemplate().load(clazz, id);
    }

    /*
     * This method will be called by implementing DAOs only and
     * will be hidden from the BaseDAO interface.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findBy(String hsql) {
        return  (List<T> )getHibernateTemplate().find(hsql);

    }

    /*
     * This method will be called by implementing DAOs only and
     * will be hidden from the BaseDAO interface.
     * This will use DetachedCriteria for query
     */
    @SuppressWarnings("unchecked")
    protected List<? extends AbstractBaseDomain> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
        return  (List<? extends AbstractBaseDomain>)getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
    }

    /*
     * This method will be called by implementing DAOs only and
     * will be hidden from the BaseDAO interface.
     * This will use DetachedCriteria for query with out paging
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByCriteria(DetachedCriteria criteria) {
        return  (List<T>)  getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class clazz) {
        return (List<T>) getHibernateTemplate().find("from " + clazz.getName());
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    public void evict(Object obj) {
        getHibernateTemplate().evict(obj);
    }

    public <T extends Object> T get(Class<T> clazz, Serializable id){
        return clazz.cast(getHibernateTemplate().get(clazz, id));
    }
}
