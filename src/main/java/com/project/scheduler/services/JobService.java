package com.project.scheduler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.scheduler.repository.JobRepository;
import com.project.scheduler.entity.Job;

import java.util.List;

@Service
public class JobService {

        public JobRepository jobRepository;

        @Autowired
        public JobService(JobRepository jobRepository) {
            this.jobRepository = jobRepository;
        }

        public List<Job> getAllJobs() {
            return jobRepository.findAll();
        }

        public Job getJob(Long id) {
            return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        }

        public void insertJob(Job job) {
            jobRepository.save(job);
        }

        public void updateJob(Job job) {
            jobRepository.save(job);
        }

        public void deleteJob(Long id) {
            jobRepository.deleteById(id);
        }

        public void scheduleJob(Job job) {
            
        }
}
