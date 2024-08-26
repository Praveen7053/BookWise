package com.bookWise.dao.impl;

import com.bookWise.dao.AbstractHibernateDAO;
import com.bookWise.dao.BookWiseDAO;
import org.hibernate.Session;

public class BookWiseDAOImpl extends AbstractHibernateDAO implements BookWiseDAO {

    public Session getSesstion() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    public Session openSesstion() {
        return getHibernateTemplate().getSessionFactory().openSession();
    }
}
