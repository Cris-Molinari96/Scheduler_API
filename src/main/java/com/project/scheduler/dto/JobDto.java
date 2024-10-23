package com.project.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.scheduler.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private String jobName;
    private String cron;
    private String apiURL;
    private String baseURL;
    @JsonIgnore
    private String method;


    public JobDto(String jobName, String cron, String apiURL, String baseURL) {
        this.jobName = jobName;
        this.cron = cron;
        this.apiURL = apiURL;
        this.baseURL = baseURL;
    }

    public Job custInJob(JobDto jobDto){
        return new Job(jobDto.getJobName(), jobDto.getCron(), jobDto.getApiURL(),jobDto.getBaseURL());
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
