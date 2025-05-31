package com.bookWise.controller.superadmin;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.Authority;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
        model.addAttribute("userEmail", user.getUserEmail());
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userId", user.getUserId());
        return "superadminDashboard";
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/updateUserRole")
    @ResponseBody
    public Map<String, Object> updateUserRole(@RequestParam("userId") int userId,
                                              @RequestParam("roles") List<Integer> roleIds) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookWiseUser user = (BookWiseUser) bookWiseDAO.find(BookWiseUser.class, userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }

            Set<Authority> newAuthorities = new HashSet<>();
            for (Integer roleId : roleIds) {
                Authority authority = (Authority) bookWiseDAO.find(Authority.class, roleId);
                if (authority != null) {
                    newAuthorities.add(authority);
                }
            }

            user.setAuthorities(newAuthorities);
            bookWiseDAO.saveOrUpdate(user);

            response.put("success", true);
            response.put("message", "User roles updated successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating user roles: " + e.getMessage());
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/users")
    @ResponseBody
    public List<Map<String, Object>> getAllUsers() {
        List<BookWiseUser> users = bookWiseDAO.findBy("from BookWiseUser");
        List<Map<String, Object>> userList = new ArrayList<>();

        for (BookWiseUser user : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("userName", user.getUserName());
            userMap.put("email", user.getUserEmail());
            userMap.put("roles", user.getAuthorities().stream()
                    .map(Authority::getAuthorityName)
                    .collect(Collectors.toList()));
            userList.add(userMap);
        }

        return userList;
    }
}
