package com.project.scheduler.utils;

import java.time.LocalDateTime;

public class JobInfo {
    private String status;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime nextExecutionTime;

    public JobInfo(String status, String description, LocalDateTime startTime, LocalDateTime nextExecutionTime) {
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.nextExecutionTime = nextExecutionTime;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(LocalDateTime nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

}
