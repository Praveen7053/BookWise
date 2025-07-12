package com.bookWise.bookshelf.impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.bookshelf.dto.BookshelfRequest;
import com.bookWise.bookshelf.dto.BookshelfResponse;
import com.bookWise.bookshelf.dto.ReadingProgressRequest;
import com.bookWise.common.dto.ImageResponse;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.model.UserBookshelf;
import com.bookWise.repository.UserBookshelfRepository;
import com.bookWise.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    
    // Inject the base storage path from application.properties
    @Value("${bookwise.storage.path}")
    private String bookwiseStoragePath;
    
    // Debug method to check if repository is properly autowired
    public void checkRepositoryInjection() {
        // Repository injection check - silent in production
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
                // Database connection test successful
            } catch (Exception dbError) {
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
                        
                        // Add reading progress data
                        bookData.put("readingProgress", entry.getReadingProgress() != null ? entry.getReadingProgress() : 0);
                        bookData.put("lastReadPage", entry.getLastReadPage());
                        bookData.put("totalPages", entry.getTotalPages());
                        bookData.put("lastReadDate", entry.getLastReadDate());
                        bookData.put("totalReadingTime", entry.getTotalReadingTime());
                        bookData.put("isCompleted", entry.getReadingProgress() != null && entry.getReadingProgress() >= 100);
                        
                        // Convert image to Base64 like other endpoints
                        if (StringUtils.isNotBlank(book.getFrontPageImagePath())) {
                            try {
                                ImageResponse imageResponse = FileUtils.getImageContentAndMimeType(book.getFrontPageImagePath(), "BookUpload");
                                if (imageResponse != null) {
                                    bookData.put("coverImageContent", imageResponse.getImageContent());
                                    bookData.put("coverImageMimeType", imageResponse.getImageMimeType());
                                }
                            } catch (Exception e) {
                                // Silent error handling for image conversion
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

    @Transactional
    public ResponseEntity<BookshelfResponse> updateReadingProgress(ReadingProgressRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BookshelfResponse(false, "User not authenticated", false, null));
            }

            // Find the bookshelf entry
            Optional<UserBookshelf> bookshelfEntryOpt = userBookshelfRepository.findByUserIdAndBookEncounterId(
                currentUser.getUserId(), request.getBookEncounterId());
            
            if (!bookshelfEntryOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BookshelfResponse(false, "Book not found in your shelf", false, null));
            }

            UserBookshelf bookshelfEntry = bookshelfEntryOpt.get();
            
            // Check for backward progress - only allow forward progress or same page
            Integer currentRequestedPage = request.getCurrentPage();
            Integer existingPage = bookshelfEntry.getLastReadPage();
            
            if (currentRequestedPage != null && existingPage != null && currentRequestedPage < existingPage) {
                // Return success but don't update - this prevents backward progress
                return ResponseEntity.ok(new BookshelfResponse(true, "Progress not updated - backward movement detected", false, bookshelfEntry.getId()));
            }
            
            // Update progress only if it's forward or same page
            if (currentRequestedPage != null) {
                bookshelfEntry.setLastReadPage(currentRequestedPage);
            }
            
            // Get total pages from PDF file if not already set
            if (bookshelfEntry.getTotalPages() == null) {
                BookEncounter book = (BookEncounter) bookWiseDAO.find(BookEncounter.class, request.getBookEncounterId());
                if (book != null && book.getPdfPath() != null && !book.getPdfPath().trim().isEmpty()) {
                    try {
                        int totalPages = getTotalPagesFromPdf(book.getPdfPath());
                        bookshelfEntry.setTotalPages(totalPages);
                    } catch (Exception e) {
                        // Log error but continue - total pages will be set later
                    }
                }
            }
            
            // Calculate progress percentage
            if (bookshelfEntry.getTotalPages() != null && bookshelfEntry.getLastReadPage() != null) {
                int progress = Math.min(100, (bookshelfEntry.getLastReadPage() * 100) / bookshelfEntry.getTotalPages());
                bookshelfEntry.setReadingProgress(progress);
            }
            
            // Update reading time
            if (request.getReadingTimeMinutes() != null) {
                Long currentTotalTime = bookshelfEntry.getTotalReadingTime() != null ? bookshelfEntry.getTotalReadingTime() : 0L;
                bookshelfEntry.setTotalReadingTime(currentTotalTime + request.getReadingTimeMinutes());
            }
            
            // Mark as completed if requested
            if (request.getIsCompleted() != null && request.getIsCompleted()) {
                bookshelfEntry.setReadingProgress(100);
                bookshelfEntry.setLastReadPage(bookshelfEntry.getTotalPages());
            }
            
            bookshelfEntry.setLastReadDate(new Timestamp(System.currentTimeMillis()));
            userBookshelfRepository.save(bookshelfEntry);

            return ResponseEntity.ok(new BookshelfResponse(true, "Reading progress updated", true, bookshelfEntry.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BookshelfResponse(false, "Error updating reading progress: " + e.getMessage(), false, null));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getReadingProgress(Integer bookEncounterId) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Find the bookshelf entry
            Optional<UserBookshelf> bookshelfEntryOpt = userBookshelfRepository.findByUserIdAndBookEncounterId(
                currentUser.getUserId(), bookEncounterId);
            
            if (!bookshelfEntryOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            UserBookshelf bookshelfEntry = bookshelfEntryOpt.get();
            Map<String, Object> progressData = new HashMap<>();
            
            progressData.put("readingProgress", bookshelfEntry.getReadingProgress());
            progressData.put("lastReadPage", bookshelfEntry.getLastReadPage());
            progressData.put("totalPages", bookshelfEntry.getTotalPages());
            progressData.put("lastReadDate", bookshelfEntry.getLastReadDate());
            progressData.put("totalReadingTime", bookshelfEntry.getTotalReadingTime());
            progressData.put("isCompleted", bookshelfEntry.getReadingProgress() != null && bookshelfEntry.getReadingProgress() >= 100);

            return ResponseEntity.ok(progressData);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get total pages from PDF file using PDFBox
     */
    private int getTotalPagesFromPdf(String pdfPath) throws Exception {
        try {
            // Construct the full file path using injected storage path
            Path pdfFilePath = Paths.get(bookwiseStoragePath, pdfPath);
            File pdfFile = pdfFilePath.toFile();
            
            if (!pdfFile.exists()) {
                throw new Exception("PDF file not found: " + pdfFile.getAbsolutePath());
            }
            
            // Use PDFBox to get page count
            try (PDDocument document = PDDocument.load(pdfFile)) {
                int pageCount = document.getNumberOfPages();
                return pageCount;
            }
        } catch (Exception e) {
            throw e;
        }
    }


} 