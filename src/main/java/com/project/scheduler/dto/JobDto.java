package com.project.scheduler.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.scheduler.entity.Job;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobDto {
    private String jobName;
    private String cron;
    private String apiURL;
    private String baseURL;
    private String method;
    private Object bodyObject;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JobDto(String jobName, String cron, String apiURL, String baseURL,String method) {
        this.jobName = jobName;
        this.cron = cron;
        this.apiURL = apiURL;
        this.baseURL = baseURL;
        this.method = method;
    }

    public Job castInJobWithMethod(JobDto jobDto, String serializedBody){
        return new Job(jobDto.getJobName(), jobDto.getCron(), jobDto.getApiURL(),jobDto.getBaseURL(), jobDto.getMethod(),serializedBody);
    }

    public String serializeObj(Object object){
        String serializedBody = null;
        if (object != null) {
            try {
                return serializedBody = objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Errore nella serializzazione del body", e);
            }
        }else{
            return null;
        }
    }

}
