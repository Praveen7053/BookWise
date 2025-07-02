package com.bookWise.service.book;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.util.BookUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookUtils bookUtils;

    @Transactional
    public List<BookEncounter> loadAllBookEncounter(int userId, int start, int size, String title, String bookId, String timeFilter, String sortBy) {
        Session session = null;
        try {
            session = bookWiseDAO.openSesstion();
            StringBuilder queryBuilder = new StringBuilder("FROM BookEncounter WHERE 1=1");
            Map<String, Object> params = new HashMap<>();

            // ✅ Add userId condition only if valid
            if (userId > 0) {
                queryBuilder.append(" AND updatedById = :userId");
                params.put("userId", String.valueOf(userId));
            }

            // ✅ Add filters
            if (StringUtils.isNotBlank(title)) {
                queryBuilder.append(" AND LOWER(bookTitle) LIKE :title");
                params.put("title", "%" + title.toLowerCase() + "%");
            }

            if (StringUtils.isNotBlank(bookId)) {
                queryBuilder.append(" AND bookEncounterId = :bookId");
                params.put("bookId", Integer.parseInt(bookId));
            }

            if (StringUtils.isNotBlank(timeFilter) && !StringUtils.equalsIgnoreCase(timeFilter, "all")) {
                queryBuilder.append(" AND uploadedTime >= :timeStamp");
                params.put("timeStamp", bookUtils.getFilterTimestamp(timeFilter));
            }

            // ✅ Add sorting
            queryBuilder.append(" ORDER BY ");
            if (StringUtils.isBlank(sortBy)) {
                queryBuilder.append("uploadedTime DESC");
            } else {
                switch (sortBy) {
                    case "oldest":
                        queryBuilder.append("uploadedTime ASC");
                        break;
                    case "title":
                        queryBuilder.append("bookTitle ASC");
                        break;
                    case "price":
                        queryBuilder.append("CAST(REPLACE(bookPrice, '$', '') AS double) ASC");
                        break;
                    default:
                        queryBuilder.append("uploadedTime DESC");
                        break;
                }
            }

            var query = session.createQuery(queryBuilder.toString(), BookEncounter.class);
            params.forEach(query::setParameter);

            return query.setFirstResult(Math.max(0, start))
                    .setMaxResults(size)
                    .list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long countBooks(int userId, String title, String bookId, String timeFilter) {
        Session session = null;
        try {
            session = bookWiseDAO.openSesstion();
            StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM BookEncounter WHERE 1=1");
            Map<String, Object> params = new HashMap<>();

            if (userId > 0) {
                queryBuilder.append(" AND updatedById = :userId");
                params.put("userId", String.valueOf(userId));
            }

            if (StringUtils.isNotBlank(title)) {
                queryBuilder.append(" AND LOWER(bookTitle) LIKE :title");
                params.put("title", "%" + title.toLowerCase() + "%");
            }

            if (StringUtils.isNotBlank(bookId)) {
                queryBuilder.append(" AND bookEncounterId = :bookId");
                params.put("bookId", Integer.parseInt(bookId));
            }

            if (StringUtils.isNotBlank(timeFilter) && !StringUtils.equalsIgnoreCase(timeFilter, "all")) {
                queryBuilder.append(" AND uploadedTime >= :timeStamp");
                params.put("timeStamp", bookUtils.getFilterTimestamp(timeFilter));
            }

            var query = session.createQuery(queryBuilder.toString());
            params.forEach(query::setParameter);

            return (Long) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public BookEncounter loadBookEncounter(int bookEncounterId) {
        try {
            BookEncounter bookEncounter = (BookEncounter) bookWiseDAO.find(BookEncounter.class, bookEncounterId);
            return bookEncounter;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
