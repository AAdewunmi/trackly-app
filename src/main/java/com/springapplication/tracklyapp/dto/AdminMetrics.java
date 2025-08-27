package com.springapplication.tracklyapp.dto;

import java.io.Serializable;

/**
 * AdminMetrics represents the system metrics displayed on the admin dashboard.
 */
public class AdminMetrics implements Serializable {
    private int totalUsers;
    private Integer deltaUsers;
    private int activeSessions;
    private double errorRate;
    private int queueSize;

    public AdminMetrics() {}

    public AdminMetrics(int totalUsers, Integer deltaUsers, int activeSessions, double errorRate, int queueSize) {
        this.totalUsers = totalUsers;
        this.deltaUsers = deltaUsers;
        this.activeSessions = activeSessions;
        this.errorRate = errorRate;
        this.queueSize = queueSize;
    }

    /** Total number of users in the system. */
    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    /** Change in user count over a period (e.g., this week). */
    public Integer getDeltaUsers() {
        return deltaUsers;
    }

    public void setDeltaUsers(Integer deltaUsers) {
        this.deltaUsers = deltaUsers;
    }

    /** Number of active user sessions. */
    public int getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(int activeSessions) {
        this.activeSessions = activeSessions;
    }

    /** System error rate, as a percentage. */
    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    /** Number of jobs in the queue (pending). */
    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}



