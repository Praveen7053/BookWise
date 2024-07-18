package com.bookWise.SecurityConfig;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<BookWiseUser> userList = bookWiseDAO.findBy("from BookWiseUser where userEmail = '" + username + "'");
        BookWiseUser user = null;
        if (userList != null && userList.size() > 0) {
            user = userList.get(0);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
        userBuilder.password(user.getUserPassword());
        userBuilder.roles("USER");

        return userBuilder.build();
    }
}
