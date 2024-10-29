package com.project.scheduler.scheduler;

import com.project.scheduler.utils.JobResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class JobRegistry {

    //    private final Map<String, String> jobResponses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ScheduledFuture<?>> runningJobs = new ConcurrentHashMap<>();
    private final Map<String, List<JobResponse>> jobResponses = new ConcurrentHashMap<>();
    List<JobResponse> jobResponsesList = new ArrayList<>();
    // Registra un job nella mappa
    //  Qui possiamo prevedere una lista che riporta tutti i job già in esecuzione
    public void registerJob(String jobName, ScheduledFuture<?> scheduledFuture) {
        if (this.isJobRunning(jobName)) {
            System.out.println("Job " + jobName + " già in esecuzione");
        } else {
            runningJobs.put(jobName, scheduledFuture);
        }
    }

    public boolean isJobRunning(String jobName) {
        return runningJobs.containsKey(jobName);
    }

    public void unregisterJob(String jobName) {
        runningJobs.remove(jobName);
    }

    // Ritorna la lista di tutti i jobs in esecuzione
    public Map<String, ScheduledFuture<?>> getScheduledJobs() {
        return Collections.unmodifiableMap(runningJobs);
    }

    //  Elimina tutti i jobs dalla lista
    public boolean unregisterAllJobs() {
        if (runningJobs.isEmpty()) {
            return true;
        }

        boolean allCancelled = true;
        for (Map.Entry<String, ScheduledFuture<?>> entry : runningJobs.entrySet()) {
            String jobName = entry.getKey();
            unregisterJob(jobName);
        }
        return allCancelled;
    }

    public List<JobResponse> getJobResponse(String jobName) {
        return jobResponses.get(jobName);
    }

    public void updateJobResponse(String jobName, String jobResponse) {
        JobResponse newResponse = new JobResponse(jobResponse, LocalDateTime.now());

        jobResponses.computeIfAbsent(jobName, k -> new ArrayList<>()).add(newResponse);
    }
}
