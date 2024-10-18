package com.project.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private String jobName;
    private String cron;
    private String apiURL;
    private String baseURL;

}
