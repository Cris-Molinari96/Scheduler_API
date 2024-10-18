package com.project.scheduler.controller;

import com.project.scheduler.dto.JobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.scheduler.entity.Job;
import com.project.scheduler.scheduler.SchedulerService;
import com.project.scheduler.services.JobService;
import com.project.scheduler.utils.ResponseHttp;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    public final SchedulerService schedulerService;
    public JobService jobService;
    public ResponseHttp responseHttp;

    @Autowired
    public SchedulerController(
            JobService jobService,
            SchedulerService schedulerService
    ) {
        this.jobService = jobService;
        this.schedulerService = schedulerService;
    }


    @PostMapping("/test-api")
    @Operation(summary = "Crea un job temporaneo senza salvarlo nel database, esempio settaggio cron che viene eseguito ogni 30s  --> cron: */30 * * * * * ")
    public ResponseEntity<ResponseHttp> creaTemporaryScheduler(@RequestBody JobDto job) {
        boolean response;
        if (job != null) {
            response = schedulerService.createTemporaryScheduler(job);
            if (response) {
                responseHttp.setMessage("Lo scheduler " + job.getJobName() + " è stato creato con successo!");
                return new ResponseEntity<>(responseHttp, HttpStatus.BAD_REQUEST);
            } else {
                responseHttp.setMessage("Si è verificato un errore durante la creazione!");
                return new ResponseEntity<>(responseHttp, HttpStatus.BAD_REQUEST);
            }
        } else {
            responseHttp.setMessage("Non puoi avviare un qualcosa che è ''null' inserisci i campi id-job");
            return new ResponseEntity<>(responseHttp, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status/{jobName}")
    @Operation(summary = "Ottieni lo status di un job")
    public ResponseEntity<String> getJobStatus(@PathVariable String jobName) {
        if (jobName.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il nome del job non può essere vuoto");
        } else {
            if (schedulerService.getStatusJob(jobName)) {
                return ResponseEntity.ok("Job '" + jobName + "' è in esecuzione.");
            } else {
                return ResponseEntity.ok("Job '" + jobName + "' non è attualmente in esecuzione.");
            }
        }
    }

 /*   @GetMapping("/list")
    @Operation(summary = "Ottieni la lista dei job in esecuzione")
    public ResponseEntity<ResponseHttp> listJobs() {
        Map<String, String> jobStatus = schedulerService.getStatusAllJobs();
        if (jobStatus.isEmpty()) {
            responseHttp = new ResponseHttp();
            responseHttp.setMessage("Nessun job in esecuzione");
            return new ResponseEntity<>(responseHttp, HttpStatus.OK);
        } else {
            responseHttp = new ResponseHttp();
            responseHttp.setDataSource(jobStatus);
            return new ResponseEntity<>(responseHttp, HttpStatus.OK);
        }
    }*/

    // Start Job
    @PostMapping("/start/{jobId}")
    @Operation(summary = "Esegui un singolo job")
    public ResponseEntity<ResponseHttp> startJob(@PathVariable Long jobId) {
        JobDto job = schedulerService.startOnlyJob(jobId);
        if (job != null) {
            boolean successOrNotStart = schedulerService.scheduleJob(job);
            if (successOrNotStart) {
                responseHttp = new ResponseHttp();
                responseHttp.setMessage("Scheduler creato con successo " + job.getJobName());
                return new ResponseEntity<>(responseHttp, HttpStatus.OK);
            } else {
                responseHttp = new ResponseHttp();
                responseHttp.setMessage("Si è verificato un errore nella creazione del job" + job.getJobName());
                return new ResponseEntity<>(responseHttp, HttpStatus.OK);
            }
        } else {
            responseHttp.setMessage("Non puoi avviare un qualcosa che è ''null' inserisci i campi id-job");
            return new ResponseEntity<>(responseHttp, HttpStatus.BAD_REQUEST);
        }
    }


    // Stop Job
    @PostMapping("/stop/{jobName}")
    @Operation(summary = "Ferma l'esecuzione di un job")
    public ResponseEntity<?> stopJob(@PathVariable String jobName) {
        boolean isCancel = schedulerService.stopJob(jobName);
        responseHttp = new ResponseHttp();
        if (isCancel) {
            responseHttp.setMessage("Fermato con successo");
            return new ResponseEntity<>(responseHttp, HttpStatus.OK);
        } else {
            responseHttp.setMessage("Impossibile fermare il job " + jobName);
            return new ResponseEntity<>(responseHttp, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/execute-all-jobs")
    @Operation(summary = "Esegui tutti i jobs memorizzati nel database")
    public ResponseEntity<ResponseHttp> executeAllJobs() {
        responseHttp = new ResponseHttp();
        schedulerService.startAllJobs();
        responseHttp.setMessage("Jobs eseguiti");
        return new ResponseEntity<ResponseHttp>(responseHttp, HttpStatus.OK);
    }

    @PostMapping("/stop-all-jobs")
    @Operation(summary = "Ferma tutti i jobs")
    public ResponseEntity<ResponseHttp> unregisterAllJobs() {
        boolean operationConfirm = schedulerService.stopAllJob();
        if (operationConfirm) {
            responseHttp = new ResponseHttp();
            responseHttp.setMessage("Tutti i jobs sono stati arrestati!");
            return new ResponseEntity<>(responseHttp, HttpStatus.OK);
        } else {
            responseHttp = new ResponseHttp();
            responseHttp.setMessage("Si è verificato un errore");
            return new ResponseEntity<>(responseHttp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
