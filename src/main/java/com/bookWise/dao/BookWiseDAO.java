package com.bookWise.dao;

import org.hibernate.Session;

public interface BookWiseDAO extends BaseDAO {

    Session getSesstion();

    Session openSesstion();
}