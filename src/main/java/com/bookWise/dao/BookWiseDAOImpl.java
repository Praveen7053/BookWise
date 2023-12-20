// GenericDaoImpl.java
package com.bookWise.dao;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BookWiseDAOImpl extends BookWiseDAO implements BookDao{

    public Session getSesstion()
    {
        Session currentSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
        return currentSession;
    }

    @Override
    public Session openSesstion() {
        Session currentSession = getHibernateTemplate().getSessionFactory().openSession();
        return currentSession;
    }

}
