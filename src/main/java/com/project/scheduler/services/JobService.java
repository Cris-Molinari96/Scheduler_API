package com.project.scheduler.services;

import com.project.scheduler.dto.JobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.scheduler.repository.JobRepository;
import com.project.scheduler.entity.Job;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    public JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobDto> getAllJobs() {
        List<Job> jobList = jobRepository.findAll();
        List<JobDto> jobDtoList = new ArrayList<JobDto>();
        for (Job job : jobList) {
            jobDtoList.add(new JobDto(job.getJobName(),job.getCron(),job.getApiURL(),job.getBaseURL(),job.getMethod()));
        }
        return jobDtoList;
    }

    public Job getJob(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public void insertJob(JobDto jobDto) {
        Job job = jobDto.custInJob(jobDto);
        jobRepository.save(job);
    }

    public void insertMethod(String method){

    }

    public void updateJob(JobDto jobDto) {
        jobRepository.save(jobDto.custInJob(jobDto));
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

}
