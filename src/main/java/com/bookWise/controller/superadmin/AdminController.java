package com.bookWise.controller.superadmin;

import com.bookWise.dao.userRole.UserRoleDTO;
import com.bookWise.dao.userRole.dashboard.DashboardStatsDTO;
import com.bookWise.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<List<UserRoleDTO>> getAllUsers() {
        List<UserRoleDTO> users = userService.getAllUsersWithRoles();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<List<String>> getAllRoles() {
        List<String> roles = userService.getAllAvailableRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/user/saveRoles")  // Changed to POST and updated path
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<Map<String, Object>> updateUserRoles(@RequestBody UserRoleDTO userRoleUpdateDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            userService.updateUserRoles(userRoleUpdateDTO.getUserId(), userRoleUpdateDTO.getRoles());
            response.put("success", true);
            response.put("message", "User roles updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/dashboard/stats")
    public DashboardStatsDTO getDashboardStats() {
        return userService.getDashboardStats();
    }

}
