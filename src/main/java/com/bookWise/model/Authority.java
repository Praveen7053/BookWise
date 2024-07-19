package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "AUTHORITY")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private int authorityId;

    @Column(name = "AUTHORITY_NAME")
    private String authorityName;

    @Override
    public String getAuthority() {
        return authorityName;
    }
}
