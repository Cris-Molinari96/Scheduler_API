package com.project.scheduler.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class Scheduler {

    public DynamicJobScheduler dynamicJobScheduler;

    @Autowired
    public Scheduler(DynamicJobScheduler dynamicJobScheduler) {
        this.dynamicJobScheduler = dynamicJobScheduler;
    }


    /* * Scheduler di default, avviata l'applicazione parte anche questo
     * Qui e dove possiamo inserire tutti gli scheduler e verranno avviati in automatico.
     * Dynamic Scheduler recupera tutti i JOB dal database ed esegue la logica per elaborarli */
    @Scheduled(fixedRate = 100000)
    public void scheduledTask() {
        dynamicJobScheduler.scheduleJobs();
    }

}
