package com.bookWise.SecurityConfig;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Override
    public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {
        List<BookWiseUser> userList = bookWiseDAO.findBy("from BookWiseUser where userEmail = '"+userLoginId+"' or userPhoneNumber = '"+userLoginId+"' ");
        BookWiseUser user = userList.stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());

        return new BookWiseLoginUser(user.getUserId(), user.getUserName(), user.getUserEmail(),
                user.getUserPhoneNumber(), user.getUserType(), user.getUserPassword(), authorities,
                true, true, true, true);
    }
}
