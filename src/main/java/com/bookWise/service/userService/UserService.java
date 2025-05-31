package com.bookWise.service.userService;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.dao.userRole.UserRoleDTO;
import com.bookWise.dao.userRole.dashboard.DashboardStatsDTO;
import com.bookWise.model.Authority;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    public List<UserRoleDTO> getAllUsersWithRoles() {
        List<BookWiseUser> users = bookWiseDAO.findAll(BookWiseUser.class);
        return users.stream()
                .map(user -> {
                    UserRoleDTO dto = new UserRoleDTO();
                    dto.setUserId((long) user.getUserId());
                    dto.setUserName(user.getUserName());
                    dto.setUserEmail(user.getUserEmail());
                    dto.setRoles(user.getAuthorities().stream()
                            .map(Authority::getAuthority)
                            .collect(Collectors.toSet()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<String> getAllAvailableRoles() {
        List<Authority> authorities = bookWiseDAO.findAll(Authority.class);
        return authorities.stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserRoles(Long userId, Set<String> newRoles) {
        BookWiseUser user = (BookWiseUser) bookWiseDAO.find(BookWiseUser.class, Integer.parseInt(String.valueOf(userId)));
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        // Clear existing roles
        user.getAuthorities().clear();

        // Find and add new roles
        String hql = "FROM Authority WHERE authorityName IN (:roles)";
        List<Authority> authorities = bookWiseDAO.findBy(hql.replace(":roles",
                "'" + String.join("','", newRoles) + "'"));

        if (authorities.size() != newRoles.size()) {
            throw new EntityNotFoundException("Some roles were not found");
        }

        user.getAuthorities().addAll(authorities);
        bookWiseDAO.saveOrUpdate(user);
    }


    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        List<BookWiseUser> allUsers = bookWiseDAO.findAll(BookWiseUser.class);

        stats.setTotalUsers(allUsers.size());
        // Since there's no enabled/disabled status, we can either:
        // 1. Remove activeUsers from stats completely, or
        // 2. Consider all users as active
        stats.setActiveUsers(allUsers.size());

        stats.setAdminCount(allUsers.stream()
                .filter(user -> user.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))
                .count());
        stats.setRegularUsers(allUsers.stream()
                .filter(user -> user.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))
                .count());

        return stats;
    }

}
