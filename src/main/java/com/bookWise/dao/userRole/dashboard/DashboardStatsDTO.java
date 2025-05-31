package com.bookWise.dao.userRole.dashboard;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private long totalUsers;
    private long activeUsers;
    private long adminCount;
    private long regularUsers;

    // Getters and setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getActiveUsers() { return activeUsers; }
    public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
    public long getAdminCount() { return adminCount; }
    public void setAdminCount(long adminCount) { this.adminCount = adminCount; }
    public long getRegularUsers() { return regularUsers; }
    public void setRegularUsers(long regularUsers) { this.regularUsers = regularUsers; }
}