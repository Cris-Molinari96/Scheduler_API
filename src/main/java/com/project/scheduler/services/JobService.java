package com.project.scheduler.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.scheduler.dto.JobDto;
import com.project.scheduler.scheduler.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.scheduler.repository.JobRepository;
import com.project.scheduler.entity.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    public JobRepository jobRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public JobService(JobRepository jobRepository, ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.objectMapper = objectMapper;
    }

    public Job getJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    public List<JobDto> getAllJobs() {
        List<Job> jobList = jobRepository.findAll();
        List<JobDto> jobDtoList = new ArrayList<>();
        for (Job job : jobList) {
            JobDto jobDto = deserializeBodyReturnDto(job);
            if (jobDto != null) {
                jobDtoList.add(jobDto);
            }
        }
        return jobDtoList;
    }

    public JobDto deserializeBodyReturnDto(Job job) {
        if (job != null) {
            try {
                JobDto jobDto = new JobDto(
                        job.getJobName(),
                        job.getCron(),
                        job.getApiURL(),
                        job.getBaseURL(),
                        job.getMethod()
                );
                if (job.getBodyObject() != null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    Map<String, Object> deserializedBody = objectMapper.readValue(
                            job.getBodyObject(),
                            new TypeReference<Map<String, Object>>() {
                            }
                    );

                    jobDto.setBodyObject(deserializedBody);
                }
                return jobDto;

            } catch (JsonProcessingException e) {
                log.error("Errore nella deserializzazione JSON per il job {}: {}",
                        job.getJobName(), e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }


    public void insertJob(JobDto jobDto) {
        String serializedBody = jobDto.serializeObj(jobDto.getBodyObject());
        if (serializedBody != null) {
            Job job = jobDto.castInJobWithMethod(jobDto, serializedBody);
            jobRepository.save(job);
        }
    }

    public void updateJob(JobDto jobDto) {
        String serializedBody = jobDto.serializeObj(jobDto.getBodyObject());
        if (serializedBody != null) {
            Job job = jobDto.castInJobWithMethod(jobDto, serializedBody);
            jobRepository.save(job);
        }
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

}
