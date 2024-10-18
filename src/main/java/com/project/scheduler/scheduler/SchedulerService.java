package com.project.scheduler.scheduler;

import com.project.scheduler.dto.JobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.scheduler.entity.Job;
import com.project.scheduler.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

// Questo gestore comunica con i controller

@Component
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final WebClient webClient;
    private TaskScheduler taskScheduler;
    private JobRegistry jobRegistry;
    private JobService jobService;

    @Autowired
    public SchedulerService(
            JobService jobService,
            JobRegistry jobRegistry,
            TaskScheduler taskScheduler,
            WebClient webClient) {
        this.jobRegistry = jobRegistry;
        this.jobService = jobService;
        this.taskScheduler = taskScheduler;
        this.webClient = webClient;
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

    // ESEGUE IL JOB
//    public void scheduleJob(Job job) {
//        if (job != null) {
//            boolean alreadyExecute = getStatusJob(job.getJobName());
//            if (alreadyExecute) {
//            } else {
//                String jobName = job.getJobName();
//                ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
//                        () -> {
//                            System.out.println("Richiesta " + job.getApiURL()),
//                                    new CronTrigger("*/100 * * * * *")
//                        }
//                );
//                jobRegistry.registerJob(jobName, scheduledTask);
//            }
//        }
//    }

    /*public boolean scheduleJob(Job job) {
        String jobName = job.getJobName();
        if (jobRegistry.isJobRunning(jobName)) {
            System.out.println("Sono qui");
            log.info("Job {} è già in esecuzione", jobName);
            return false;
        }

        try {
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                    () -> executeJob(job),
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
        } catch (Exception e) {
            log.error("Si è verificato un errore {}: {}", jobName, e.getMessage());
            return false;
        }
    }*/

    public boolean scheduleJob(JobDto job) {
        String jobName = job.getJobName();
        if (jobRegistry.isJobRunning(jobName)) {
            System.out.println("Sono qui");
            log.info("Job {} è già in esecuzione", jobName);
            return false;
        }

        try {
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                    () -> executeJob(job),
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
        } catch (Exception e) {
            log.error("Si è verificato un errore {}: {}", jobName, e.getMessage());
            return false;
        }
    }

    private void executeJob(JobDto job) {
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

 /*   private void handleJobSuccess(String jobName, String responseBody) {
        log.info("Job {} completed successfully. Response: {}", jobName, responseBody);
    }

    private void handleJobFailure(String jobName, String errorMessage) {
        log.error("Job {} failed. Error: {}", jobName, errorMessage);
    }*/

    public JobDto startOnlyJob(Long jobId) {
        Job job = jobService.getJob(jobId);
        JobDto jobDto = new JobDto(job.getJobName(),job.getCron(), job.getApiURL(), job.getBaseURL());
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

    /* private Job searchJob(Long jobId){
         return executeJob( jobService.getJob(jobId));
     }*/
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
