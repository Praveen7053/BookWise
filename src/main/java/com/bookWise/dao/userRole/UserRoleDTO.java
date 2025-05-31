package com.bookWise.dao.userRole;

import lombok.Data;

import java.util.Set;

@Data
public class UserRoleDTO {
    private Long userId;
    private String userName;
    private String userEmail;
    private Set<String> roles;
}
