package com.project.scheduler.utils;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
public class JobResponse {

    private String response;
    private LocalDateTime timestamp;

    public JobResponse(String response, LocalDateTime timestamp) {
        this.response = response;
        this.timestamp = timestamp;
    }


    public String getResponse() {
        return response;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }



}

