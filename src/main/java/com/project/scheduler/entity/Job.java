package com.project.scheduler.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity(name = "JOB_CONFIGURATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(name = "cron", nullable = false)
    private String cron;

    @Column(name = "api_url", nullable = false)
    private String apiURL;

    @Column(name = "base_url", nullable = false)
    private String baseURL;

    public Job(String jobName, String cron, String apiURL, String baseURL) {
        this.jobName = jobName;
        this.cron = cron;
        this.apiURL = apiURL;
        this.baseURL = baseURL;
    }
}