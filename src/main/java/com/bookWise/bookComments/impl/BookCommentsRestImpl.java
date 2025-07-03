package com.bookWise.bookComments.impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.bookComments.dto.BookCommentDTO;
import com.bookWise.bookComments.dto.NewCommentRequest;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookComment;
import com.bookWise.model.BookEncounter;
import com.bookWise.model.BookWiseUser;
import com.bookWise.service.book.BookService;
import com.bookWise.util.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookCommentsRestImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookUtils bookUtils;

    @Autowired
    private BookService bookService;

    @Transactional(readOnly = true)
    public List<BookCommentDTO> getCommentsForBook(Integer bookEncounterId) {
        BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, bookEncounterId);
        if (book == null || book.getComments() == null) {
            return Collections.emptyList();
        }

        return book.getComments().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookCommentDTO addComment(NewCommentRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
            if (user == null) {
                throw new IllegalStateException("No authenticated user found. Cannot post comment.");
            }

            BookWiseUser currentUser = (BookWiseUser) bookWiseDAO.find(BookWiseUser.class, user.getUserId());
            if (currentUser == null) {
                throw new IllegalStateException("No authenticated user found. Cannot post comment.");
            }

            BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, request.getBookEncounterId());
            if (book == null) {
                throw new IllegalArgumentException("Book not found with ID: " + request.getBookEncounterId());
            }

            BookComment newComment = new BookComment();
            newComment.setBookEncounter(book);
            newComment.setUser(currentUser);
            newComment.setCommentText(request.getCommentText());
            bookWiseDAO.saveOrUpdate(newComment);
            return convertToDto(newComment);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private BookCommentDTO convertToDto(BookComment comment) {
        return new BookCommentDTO(
                comment.getCommentId(),
                comment.getCommentText(),
                comment.getUser().getUserName(),
                comment.getCreatedAt()
        );
    }
}
