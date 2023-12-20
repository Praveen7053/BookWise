package com.bookWise.dao;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


public interface BaseDAO {

    public void delete(Object obj);

    public void deleteAll(Collection collection);

    public Serializable save(Object obj);

    public void saveOrUpdate(Object obj);

    public void saveOrUpdateAll(Collection<? extends AbstractBaseDomain> collection);

    public void merge(Object obj);

    public void evict(Object obj);

    public Object find(Class clazz, Serializable id);

    public <T> List<T> findAll(Class clazz);

    public <T> List<T> findBy(String hsql) ;

    public Object load(Class clazz, Serializable id) ;

    public void flush();

    public void refresh(Object obj);

    public <T extends Object> T get(Class<T> clazz, Serializable id);

}
