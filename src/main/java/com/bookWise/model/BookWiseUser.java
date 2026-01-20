package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "BOOK_WISE_USER")
public class BookWiseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private int userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "MAIN_LANGUAGE")
    private String mainLanguage;

    @Column(name = "USER_EMAIL", unique = true)
    private String userEmail;

    @Column(name = "USER_PHONE_NUMBER", unique = true)
    private String userPhoneNumber;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "PROFILE_PICTURE_PATH")
    private String profilePicturePath;

    @Column(name = "MODIFIED_DATE")
    private Timestamp modifiedDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "STREET")
    private String street;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EMAIL_VERIFIED")
    private Boolean emailVerified = false;

    @Column(name = "VERIFICATION_TOKEN")
    private String verificationToken;

    @Column(name = "LOGIN_USER_ID", unique = true)
    private String loginUserId;

    @Column(name = "RESET_PASSWORD_TOKEN ")
    private String resetPasswordToken;

    @Column(name = "RESET_TOKEN_EXPIRY ")
    private Timestamp resetTokenExpiry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID")
    )
    private Set<Authority> authorities;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<BookComment> comments;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<BookRating> ratings;
}