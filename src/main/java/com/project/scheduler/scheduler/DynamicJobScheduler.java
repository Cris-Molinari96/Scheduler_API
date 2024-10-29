package com.project.scheduler.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


//! Utilizziamo il componente solo per i scheduler che devono essere avviati direttamente all'istanza dell'applicazione
@Component
public class DynamicJobScheduler {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private SchedulerService schedulerService;

    @Autowired
    public DynamicJobScheduler(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    // Recupero di tutti i JOB nel database e li avvia
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        applicationStart();
    }

    // Esecuzione dei singoli job, questo metodo viene utilizzato per eseguire i JOB anche negli altri controller
    public void applicationStart() {
        boolean isExecuted = schedulerService.startAllJobs();
       if(isExecuted){
           log.info("Scheduler avviati");
       }else{
           log.error("Si è verificato un errore nell'eseguire tutti i job");
       }
    }
}
