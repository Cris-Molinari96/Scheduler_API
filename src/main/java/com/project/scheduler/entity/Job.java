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

    @Column(name = "method")
    private String method;

    @Column(name = "body_object", columnDefinition = "TEXT")
    private String bodyObject;

    public Job(String jobName, String cron, String apiURL, String baseURL, String bodyObject) {
        this.jobName = jobName;
        this.cron = cron;
        this.apiURL = apiURL;
        this.baseURL = baseURL;
        this.bodyObject = bodyObject;
    }

    public Job(String jobName, String cron, String apiURL, String baseURL, String method, String bodyObject) {
        this.jobName = jobName;
        this.cron = cron;
        this.apiURL = apiURL;
        this.baseURL = baseURL;
        this.method = method;
        this.bodyObject = bodyObject;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", cron='" + cron + '\'' +
                ", apiURL='" + apiURL + '\'' +
                ", baseURL='" + baseURL + '\'' +
                '}';
    }
}