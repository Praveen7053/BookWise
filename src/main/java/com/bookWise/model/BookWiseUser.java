package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_PHONE_NUMBER")
    private String userPhoneNumber;

    @Column(name = "USER_TYPE")
    private String userType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID")
    )
    private Set<Authority> authorities;
}
