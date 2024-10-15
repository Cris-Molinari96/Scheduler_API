package com.project.scheduler.scheduler;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class JobRegistry {

    private final ConcurrentHashMap<String, ScheduledFuture<?>> runningJobs = new ConcurrentHashMap<>();

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


}
