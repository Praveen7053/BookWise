package com.bookWise.repository;

import com.bookWise.model.UserBookshelf;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookshelfRepository {
    
    /**
     * Save or update a bookshelf entry
     */
    UserBookshelf save(UserBookshelf bookshelf);
    
    /**
     * Find a bookshelf entry by user ID and book encounter ID
     */
    Optional<UserBookshelf> findByUserIdAndBookEncounterId(Integer userId, Integer bookEncounterId);
    
    /**
     * Find all active bookshelf entries for a user
     */
    List<UserBookshelf> findByUserIdAndStatus(Integer userId, UserBookshelf.BookshelfStatus status);
    
    /**
     * Check if a book is in user's active shelf
     */
    boolean existsByUserIdAndBookEncounterIdAndStatus(Integer userId, Integer bookEncounterId, UserBookshelf.BookshelfStatus status);
    
    /**
     * Delete a bookshelf entry
     */
    void delete(UserBookshelf bookshelf);
    
    /**
     * Find all bookshelf entries for a user (regardless of status)
     */
    List<UserBookshelf> findByUserId(Integer userId);
} 