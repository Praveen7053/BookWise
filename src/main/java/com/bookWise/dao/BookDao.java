package com.bookWise.dao;

import org.hibernate.Session;

public interface BookDao extends BaseDAO{

    public Session getSesstion();

    public Session openSesstion();
}
