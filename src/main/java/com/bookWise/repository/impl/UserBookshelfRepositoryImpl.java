package com.bookWise.repository.impl;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.UserBookshelf;
import com.bookWise.repository.UserBookshelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserBookshelfRepositoryImpl implements UserBookshelfRepository {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Override
    public UserBookshelf save(UserBookshelf bookshelf) {
        bookWiseDAO.saveOrUpdate(bookshelf);
        return bookshelf;
    }

    @Override
    public Optional<UserBookshelf> findByUserIdAndBookEncounterId(Integer userId, Integer bookEncounterId) {
        String hql = "FROM UserBookshelf ub WHERE ub.userId = " + userId + " AND ub.bookEncounterId = " + bookEncounterId;
        List<UserBookshelf> results = bookWiseDAO.findBy(hql);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<UserBookshelf> findByUserIdAndStatus(Integer userId, UserBookshelf.BookshelfStatus status) {
        String hql = "FROM UserBookshelf ub WHERE ub.userId = " + userId + " AND ub.status = '" + status.name() + "'";
        return bookWiseDAO.findBy(hql);
    }

    @Override
    public boolean existsByUserIdAndBookEncounterIdAndStatus(Integer userId, Integer bookEncounterId, UserBookshelf.BookshelfStatus status) {
        String hql = "FROM UserBookshelf ub WHERE ub.userId = " + userId + 
                    " AND ub.bookEncounterId = " + bookEncounterId + 
                    " AND ub.status = '" + status.name() + "'";
        List<UserBookshelf> results = bookWiseDAO.findBy(hql);
        return !results.isEmpty();
    }

    @Override
    public void delete(UserBookshelf bookshelf) {
        bookWiseDAO.delete(bookshelf);
    }

    @Override
    public List<UserBookshelf> findByUserId(Integer userId) {
        String hql = "FROM UserBookshelf ub WHERE ub.userId = " + userId;
        return bookWiseDAO.findBy(hql);
    }
} 