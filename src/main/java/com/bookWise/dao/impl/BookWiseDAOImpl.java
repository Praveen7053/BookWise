package com.bookWise.dao.impl;

import com.bookWise.dao.AbstractHibernateDAO;
import com.bookWise.dao.BookWiseDAO;
import org.hibernate.Session;

public class BookWiseDAOImpl extends AbstractHibernateDAO implements BookWiseDAO {

    public Session getSession() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    public Session openSession() {
        return getHibernateTemplate().getSessionFactory().openSession();
    }
}
