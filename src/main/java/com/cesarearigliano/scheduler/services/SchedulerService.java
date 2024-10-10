package com.cesarearigliano.scheduler.services;

import com.cesarearigliano.scheduler.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private TaskScheduler taskScheduler;
    private WebClient webClient;
    private Map<String, Job> schedulerMap = new HashMap<>();
    private Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private Map<String, String> jobOutputs = new ConcurrentHashMap<>();

    @Autowired
    SchedulerService(TaskScheduler taskScheduler, WebClient webClient){
        this.taskScheduler = taskScheduler;
        this.webClient = webClient;
    }

    public Map<String, Job> getScheduledJobs(){
        return schedulerMap;
    }

    public void scheduleGetJob(Job job) {
        log.info("scheduling get job: " + job.toString());
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                () -> {
                    ResponseEntity<String> response = webClient.get()
                            .uri(job.getBaseURL() + job.getApiURL())
                            .header("Content-Type", "application/json")
                            .retrieve()
                            .toEntity(String.class)
                            .block();
                    if (response.getStatusCode().isError()) {
                        String errorMessage = "Failure calling Job: " + job.getJobName() + " with URL: " + job.getBaseURL() + job.getApiURL();
                        log.error(errorMessage);
                        saveJobOutput(job.getJobName(), errorMessage);
                    } else {
                        String outputMessage = "Job: " + job.getJobName() + " executed successfully. Response: " + response.getBody();
                        log.info(outputMessage);
                        saveJobOutput(job.getJobName(), outputMessage);
                    }
                },
                new CronTrigger(job.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().toZoneId()))
        );
        String jobId = job.getJobName() + "-" + System.currentTimeMillis();
        scheduledTasks.put(jobId, scheduledTask);
        schedulerMap.put(jobId, job);
    }

    public boolean deleteScheduledJob(String jobId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.remove(jobId);
        if (scheduledTask != null) {
            boolean cancelled = scheduledTask.cancel(true);
            if (cancelled) {
                schedulerMap.remove(jobId);
                log.info("Job cancelled: " + jobId);
            }
            return cancelled;
        }
        return false;
    }
  

    public void schedulePostJob(Job job){
    	// da modificare come fatto per scheduleGetJob
    	// testare che è possibile uccidere il processo
        log.info("scheduling post job:"+ job.toString());
        taskScheduler.schedule(
                () -> {
                    ResponseEntity<String> response = webClient.post()
                            .uri(job.getBaseURL() + job.getApiURL())
                            .header("Content-Type", "application/json")
                            .bodyValue(job)
                            .retrieve()
                            .toEntity(String.class)
                            .block();
    
                    if(response.getStatusCode().isError()){
                        log.error("Failure calling Job:"+ job.getJobName()+ "with URL:"+ job.getBaseURL()+job.getApiURL());
                    } else {
                        log.info(response.getBody());
                    }
                },
                new CronTrigger(job.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().toZoneId()))
        );
        schedulerMap.put(job.getJobName() + "-" + System.currentTimeMillis(), job);
    }
    
    private void saveJobOutput(String jobName, String output) {
        // Qui puoi implementare la logica per salvare l'output
        // Ad esempio, puoi salvarlo in un database o in un file di log
        // Per questo esempio, lo salveremo in una Map in memoria
        jobOutputs.put(jobName + "-" + System.currentTimeMillis(), output);
    }
    
    public List<String> getJobOutputs(String jobName) {
        return jobOutputs.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(jobName))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
}
