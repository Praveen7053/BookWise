package com.bookWise.bookRating.impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.bookRating.dto.BookRatingDTO;
import com.bookWise.bookRating.dto.SubmitRatingRequest;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.model.BookRating;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class BookRatingRestImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Transactional(readOnly = true)
    public BookRatingDTO getRatingForBook(Integer bookEncounterId) {
        BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, bookEncounterId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        double average = book.getRatings().stream()
                .mapToInt(BookRating::getRatingValue)
                .average()
                .orElse(0.0);

        int total = book.getRatings().size();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
        int currentUserRating = 0;
        if (user != null) {
            currentUserRating = book.getRatings().stream()
                    .filter(r -> r.getUser().getUserId() == user.getUserId())
                    .mapToInt(BookRating::getRatingValue)
                    .findFirst()
                    .orElse(0);
        }

        return new BookRatingDTO(average, total, currentUserRating);
    }

    @Transactional
    public BookRatingDTO submitRating(SubmitRatingRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
        if (user == null) {
            throw new IllegalStateException("User must be logged in to rate a book.");
        }

        BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, request.getBookEncounterId());
        BookWiseUser currentUser = (BookWiseUser) bookWiseDAO.find(BookWiseUser.class, user.getUserId());

        if (book == null || currentUser == null) {
            throw new IllegalArgumentException("Invalid book or user.");
        }

        // Check if a rating already exists (for update)
        Optional<BookRating> existingRatingOpt = book.getRatings().stream()
                .filter(r -> r.getUser().getUserId() == currentUser.getUserId())
                .findFirst();

        if (existingRatingOpt.isPresent()) {
            // Update existing rating
            BookRating existingRating = existingRatingOpt.get();
            existingRating.setRatingValue(request.getRatingValue());
            bookWiseDAO.saveOrUpdate(existingRating);
        } else {
            // Create new rating
            BookRating newRating = new BookRating();
            newRating.setBookEncounter(book);
            newRating.setUser(currentUser);
            newRating.setRatingValue(request.getRatingValue());
            bookWiseDAO.saveOrUpdate(newRating);
        }

        return getRatingForBook(request.getBookEncounterId());
    }
}