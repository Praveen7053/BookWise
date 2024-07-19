package com.bookWise.SecurityConfig.loginUserConfig;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Getter
public class BookWiseLoginUser extends BookWiseLoginUserImpl {

    private int userId;
    private String userName;
    private String userEmail;
    private String userPhoneNumber;
    private String userType;

    public BookWiseLoginUser(int userId, String userName, String userEmail, String userPhoneNumber,
                             String userType, String password, Set<GrantedAuthority> authorities,
                             boolean accountNonExpired, boolean accountNonLocked,
                             boolean credentialsNonExpired, boolean enabled) {
        super(password, userName, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userType = userType;
    }
}
