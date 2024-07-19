package com.bookWise.SecurityConfig.loginUserConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class BookWiseLoginUserImpl implements UserDetails {
    private final String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public BookWiseLoginUserImpl(String password, String username, Set<GrantedAuthority> authorities,
                                 boolean accountNonExpired, boolean accountNonLocked,
                                 boolean credentialsNonExpired, boolean enabled) {
        this.password = password;
        this.username = username;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookWiseLoginUserImpl that = (BookWiseLoginUserImpl) o;
        return accountNonExpired == that.accountNonExpired && accountNonLocked == that.accountNonLocked &&
                credentialsNonExpired == that.credentialsNonExpired && enabled == that.enabled &&
                Objects.equals(password, that.password) && Objects.equals(username, that.username) &&
                Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
