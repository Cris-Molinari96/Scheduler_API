package com.project.scheduler.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.scheduler.dto.JobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.scheduler.entity.Job;
import com.project.scheduler.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

// Questo gestore comunica con i controller

@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final WebClient webClient;
    private TaskScheduler taskScheduler;
    private JobRegistry jobRegistry;
    private JobService jobService;
    private ObjectMapper objectMapper;


    @Autowired
    public SchedulerService(
            JobService jobService,
            JobRegistry jobRegistry,
            TaskScheduler taskScheduler,
            WebClient webClient,
            ObjectMapper objectMapper
    ) {
        this.jobRegistry = jobRegistry;
        this.jobService = jobService;
        this.taskScheduler = taskScheduler;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public boolean getStatusJob(String jobName) {
        if (jobRegistry.isJobRunning(jobName)) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String> getStatusAllJobs() {
        Map<String, String> jobStatus = new HashMap<>();
        for (Map.Entry<String, ScheduledFuture<?>> entry : jobRegistry.getScheduledJobs().entrySet()) {
            String jobName = entry.getKey();
            ScheduledFuture<?> future = entry.getValue();
            String status = future.isCancelled() ? "Fermato" : "In esecuzione";
            jobStatus.put(jobName, status);
        }
        return jobStatus;
    }

    public boolean scheduleJob(JobDto job) {
        String jobName = job.getJobName();
        if (jobRegistry.isJobRunning(jobName)) {
            log.info("Job {} è già in esecuzione", jobName);
            return false;
        }
        try {
            ScheduledFuture<?> scheduledTask = null;
            if (job.getMethod().contains("get")) {
                System.out.println("Sto schedulando una GET");
                scheduledTask = taskScheduler.schedule(
                        () -> executeGetJob(job),
                        new CronTrigger(job.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().toZoneId()))
                );
                if (scheduledTask != null) {
                    jobRegistry.registerJob(jobName, scheduledTask);
                    log.info("Job {} schedulato", jobName);
                    return true;
                } else {
                    log.error("Job non schedulato {}", jobName);
                    return false;
                }
            } else if (job.getMethod().contains("post")) {
                System.out.println("Sto schedulando una POST!");
                scheduledTask = taskScheduler.schedule(
                        () -> {
                            executePostJob(job);
                        },
                        new CronTrigger(job.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().toZoneId()))
                );
                if (scheduledTask != null) {
                    jobRegistry.registerJob(jobName, scheduledTask);
                    log.info("Job {} schedulato", jobName);
                    return true;
                } else {
                    log.error("Job non schedulato {}", jobName);
                    return false;
                }
            } else {
                log.error("Job non schedulato {}", jobName, " Si è verificato un errore imprevisto!");
                return false;
            }
        } catch (Exception e) {
            log.error("Si è verificato un errore {}: {}", jobName, e.getMessage());
            return false;
        }
    }

    private void executeGetJob(JobDto job) {
        String jobName = job.getJobName();
        try {
            ResponseEntity<String> response = webClient.get()
                    .uri(job.getBaseURL() + job.getApiURL())
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .toEntity(String.class)
                    .block();
            if (response.getStatusCode().isError()) {
                String errorMessage = "Si è verificato un errore nella schedulazione: " + jobName + " with URL: " + job.getBaseURL() + job.getApiURL();
                log.error(errorMessage);
                jobRegistry.updateJobResponse(jobName, errorMessage);
            } else {
                String responseBody = response.getBody();
                log.info("Job {} eseguito con successo", jobName);
                jobRegistry.updateJobResponse(jobName, responseBody);
            }

        } catch (Exception e) {
            String errorMessage = "Si è verificato un errore nell'esecuzione" + jobName + ": " + e.getMessage();
            log.error(errorMessage);
            jobRegistry.updateJobResponse(jobName, errorMessage);
        }
    }

    public void executePostJob(JobDto jobDto){
        String jobName = jobDto.getJobName();
        Object requestBody = jobDto.getBodyObject();
        try {
            ResponseEntity<String> response = webClient.post()
                    .uri(jobDto.getBaseURL() + jobDto.getApiURL())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .toEntity(String.class)
                    .doOnError(error -> System.err.println("Errore nella richiesta: " + error.getMessage()))
                    .block();

            if (response.getStatusCode().isError()) {
                String errorMessage = "Si è verificato un errore nella schedulazione: " + jobName +
                        " with URL: " + jobDto.getBaseURL() + jobDto.getApiURL() +
                        " Status: " + response.getStatusCode();
                log.error(errorMessage);
                jobRegistry.updateJobResponse(jobName, errorMessage);
            } else {
                String responseBody = response.getBody();
                log.info("Job {} eseguito con successo. Response: {}", jobName, responseBody);
                jobRegistry.updateJobResponse(jobName, responseBody);
            }
        } catch (WebClientResponseException e) {
            String errorMessage = "Errore HTTP " + e.getRawStatusCode() + " per il job " + jobName +
                    ": " + e.getResponseBodyAsString();
            log.error(errorMessage);
            jobRegistry.updateJobResponse(jobName, errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Si è verificato un errore nell'esecuzione " + jobName + ": " + e.getMessage();
            log.error(errorMessage);
            jobRegistry.updateJobResponse(jobName, errorMessage);
        }
    }


    public JobDto startOnlyJob(Long jobId) {
        Job job = jobService.getJob(jobId);
        JobDto jobDto = new JobDto(job.getJobName(), job.getCron(), job.getApiURL(), job.getBaseURL(), job.getMethod());
        return jobDto;
    }

    public boolean startAllJobs() {
        List<JobDto> jobsList = jobService.getAllJobs();
        if (jobsList.size() != 0) {
            for (JobDto job : jobsList) {
                scheduleJob(job);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean stopJob(String jobName) {
        ScheduledFuture<?> scheduledTask = jobRegistry.getScheduledJobs().get(jobName);
        if (scheduledTask != null) {
            boolean cancelled = scheduledTask.cancel(true);
            if (cancelled) {
                jobRegistry.unregisterJob(jobName);
                System.out.println("Job '" + jobName + "' fermato con successo.");
            } else {
                System.out.println("Impossibile fermare il job '" + jobName + "'. Potrebbe essere già completato o in esecuzione.");
            }
            return cancelled;
        } else {
            System.out.println("Job '" + jobName + "' non trovato nel registro.");
            return false;
        }
    }

    public boolean stopAllJob() {
        Map<String, ScheduledFuture<?>> allJobs = jobRegistry.getScheduledJobs();
        if (!allJobs.isEmpty()) {
            for (Map.Entry<String, ScheduledFuture<?>> entry : allJobs.entrySet()) {
                stopJob(entry.getKey());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean createTemporaryScheduler(JobDto job) {
        return scheduleJob(job);
    }
}
