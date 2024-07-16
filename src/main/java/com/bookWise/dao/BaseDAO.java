package com.bookWise.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseDAO {

    void delete(Object obj);

    void deleteAll(Collection collection);

    Serializable save(Object obj);

    void saveOrUpdate(Object obj);

    void saveOrUpdateAll(Collection collection);

    void merge(Object obj);

    void evict(Object obj);

    Object find(Class clazz, Serializable id);

    <T> List<T> findAll(Class clazz);

    <T> List<T> findBy(String hsql);

    Object load(Class clazz, Serializable id);

    void flush();

    void refresh(Object obj);

    <T> T get(Class<T> clazz, Serializable id);
}
