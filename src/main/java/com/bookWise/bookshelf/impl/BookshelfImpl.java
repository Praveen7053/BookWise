package com.bookWise.bookshelf.impl;

import com.bookWise.bookshelf.dto.BookshelfRequest;
import com.bookWise.bookshelf.dto.BookshelfResponse;
import com.bookWise.common.dto.ImageResponse;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.model.BookWiseUser;
import com.bookWise.model.UserBookshelf;
import com.bookWise.repository.UserBookshelfRepository;
import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookshelfImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;
    
    @Autowired
    private UserBookshelfRepository userBookshelfRepository;
    
    // Debug method to check if repository is properly autowired
    public void checkRepositoryInjection() {
        if (userBookshelfRepository == null) {
            System.err.println("ERROR: UserBookshelfRepository is null!");
        } else {
            System.out.println("SUCCESS: UserBookshelfRepository is properly injected");
        }
    }

    @Transactional
    public ResponseEntity<BookshelfResponse> addToShelf(BookshelfRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BookshelfResponse(false, "User not authenticated", false, null));
            }

            // Check if book exists
            BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, request.getBookEncounterId());
            if (book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BookshelfResponse(false, "Book not found", false, null));
            }

            // Check if already in shelf
            Optional<UserBookshelf> existingEntryOpt = userBookshelfRepository.findByUserIdAndBookEncounterId(currentUser.getUserId(), request.getBookEncounterId());
            if (existingEntryOpt.isPresent()) {
                UserBookshelf existingEntry = existingEntryOpt.get();
                if (existingEntry.getStatus() == UserBookshelf.BookshelfStatus.ACTIVE) {
                    return ResponseEntity.ok(new BookshelfResponse(false, "Book is already in your shelf", true, existingEntry.getId()));
                } else {
                    // Reactivate removed entry
                    existingEntry.setStatus(UserBookshelf.BookshelfStatus.ACTIVE);
                    existingEntry.setAddedDate(new Timestamp(System.currentTimeMillis()));
                    userBookshelfRepository.save(existingEntry);
                    return ResponseEntity.ok(new BookshelfResponse(true, "Book added to your shelf", true, existingEntry.getId()));
                }
            }

            // Create new bookshelf entry
            UserBookshelf bookshelfEntry = new UserBookshelf(currentUser.getUserId(), request.getBookEncounterId());
            bookshelfEntry.setNotes(request.getNotes());
            userBookshelfRepository.save(bookshelfEntry);

            return ResponseEntity.ok(new BookshelfResponse(true, "Book added to your shelf", true, bookshelfEntry.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BookshelfResponse(false, "Error adding book to shelf: " + e.getMessage(), false, null));
        }
    }

    @Transactional
    public ResponseEntity<BookshelfResponse> removeFromShelf(Integer bookEncounterId) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BookshelfResponse(false, "User not authenticated", false, null));
            }

            // Find and remove the bookshelf entry
            Optional<UserBookshelf> bookshelfEntryOpt = userBookshelfRepository.findByUserIdAndBookEncounterId(currentUser.getUserId(), bookEncounterId);
            if (!bookshelfEntryOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BookshelfResponse(false, "Book not found in your shelf", false, null));
            }

            UserBookshelf bookshelfEntry = bookshelfEntryOpt.get();
            // Soft delete by setting status to REMOVED
            bookshelfEntry.setStatus(UserBookshelf.BookshelfStatus.REMOVED);
            userBookshelfRepository.save(bookshelfEntry);

            return ResponseEntity.ok(new BookshelfResponse(true, "Book removed from your shelf", false, bookshelfEntry.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BookshelfResponse(false, "Error removing book from shelf: " + e.getMessage(), false, null));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<BookshelfResponse> checkInShelf(Integer bookEncounterId) {
        try {
            // Debug repository injection
            checkRepositoryInjection();
            
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BookshelfResponse(false, "User not authenticated", false, null));
            }

            // Test if we can access the database
            try {
                List<UserBookshelf> allEntries = userBookshelfRepository.findByUserId(currentUser.getUserId());
                System.out.println("Database connection test successful. Found " + allEntries.size() + " entries for user " + currentUser.getUserId());
            } catch (Exception dbError) {
                System.err.println("Database connection test failed: " + dbError.getMessage());
                dbError.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BookshelfResponse(false, "Database connection failed: " + dbError.getMessage(), false, null));
            }

            // Check if book is in shelf
            Optional<UserBookshelf> bookshelfEntryOpt = userBookshelfRepository.findByUserIdAndBookEncounterId(currentUser.getUserId(), bookEncounterId);
            boolean isInShelf = (bookshelfEntryOpt.isPresent() && bookshelfEntryOpt.get().getStatus() == UserBookshelf.BookshelfStatus.ACTIVE);

            return ResponseEntity.ok(new BookshelfResponse(true, "Check completed", isInShelf, 
                isInShelf ? bookshelfEntryOpt.get().getId() : null));

        } catch (Exception e) {
            e.printStackTrace(); // Add stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BookshelfResponse(false, "Error checking shelf status: " + e.getMessage(), false, null));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> getMyShelf() {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Get all active bookshelf entries for the user
            List<UserBookshelf> bookshelfEntries = userBookshelfRepository.findByUserIdAndStatus(currentUser.getUserId(), UserBookshelf.BookshelfStatus.ACTIVE);

            // Convert to DTO format
            List<Map<String, Object>> shelfBooks = bookshelfEntries.stream()
                .map(entry -> {
                    Map<String, Object> bookData = new HashMap<>();
                    bookData.put("bookshelfId", entry.getId());
                    bookData.put("addedDate", entry.getAddedDate());
                    bookData.put("notes", entry.getNotes());
                    
                    // Get book details
                    BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, entry.getBookEncounterId());
                    if (book != null) {
                        bookData.put("bookEncounterId", book.getBookEncounterId());
                        bookData.put("bookTitle", book.getBookTitle());
                        bookData.put("bookAuthor", book.getBookAuthor());
                        bookData.put("bookDescription", book.getBookDescription());
                        bookData.put("bookCategory", book.getBookCategory());
                        bookData.put("frontPageImagePath", book.getFrontPageImagePath());
                        
                        // Convert image to Base64 like other endpoints
                        if (StringUtils.isNotBlank(book.getFrontPageImagePath())) {
                            try {
                                ImageResponse imageResponse = FileUtils.getImageContentAndMimeType(book.getFrontPageImagePath(), "BookUpload");
                                if (imageResponse != null) {
                                    bookData.put("coverImageContent", imageResponse.getImageContent());
                                    bookData.put("coverImageMimeType", imageResponse.getImageMimeType());
                                }
                            } catch (Exception e) {
                                System.err.println("Error converting image to Base64 for book " + book.getBookEncounterId() + ": " + e.getMessage());
                            }
                        }
                    }
                    
                    return bookData;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(shelfBooks);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


} 