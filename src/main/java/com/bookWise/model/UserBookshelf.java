package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "USER_BOOKSHELF")
public class UserBookshelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Column(name = "BOOK_ENCOUNTER_ID", nullable = false)
    private Integer bookEncounterId;

    @Column(name = "ADDED_DATE")
    private Timestamp addedDate;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BookshelfStatus status;

    // Many-to-One relationship with BookWiseUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private BookWiseUser user;

    // Many-to-One relationship with BookEncounter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ENCOUNTER_ID", insertable = false, updatable = false)
    private BookEncounter bookEncounter;

    // Default constructor
    public UserBookshelf() {
        this.addedDate = new Timestamp(System.currentTimeMillis());
        this.status = BookshelfStatus.ACTIVE;
    }

    // Constructor with required fields
    public UserBookshelf(Integer userId, Integer bookEncounterId) {
        this();
        this.userId = userId;
        this.bookEncounterId = bookEncounterId;
    }

    // Enum for status
    public enum BookshelfStatus {
        ACTIVE, REMOVED
    }
} 