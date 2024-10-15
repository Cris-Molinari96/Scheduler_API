package com.project.scheduler.controller;

import com.project.scheduler.entity.Job;
import com.project.scheduler.scheduler.JobRegistry;
import com.project.scheduler.scheduler.SchedulerService;
import com.project.scheduler.services.JobService;
import com.project.scheduler.utils.ResponseHttp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;
    public JobService jobService;
    public ResponseHttp responseHttp;

    @Autowired
    public SchedulerController(
            TaskScheduler taskScheduler,
            JobService jobService,
            JobRegistry jobRegistry, SchedulerService schedulerService) {
        this.jobService = jobService;
        this.schedulerService = schedulerService;
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

    @GetMapping("/list")
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
    }

    // Start Job
    @PostMapping("/start/{jobId}")
    @Operation(summary = "Avvia un singolo job")
    public ResponseEntity<ResponseHttp> startJob(@PathVariable Long jobId) {
        Job job = schedulerService.startOnlyJob(jobId);
        if (job != null) {
            responseHttp = new ResponseHttp();
            responseHttp.setMessage("Scheduler avviato " + job.getJobName());
            return new ResponseEntity<>(responseHttp, HttpStatus.OK);
        } else {
            responseHttp.setMessage("Il job richiesto è già in esecuzione");
            return new ResponseEntity<>(responseHttp, HttpStatus.NOT_FOUND);
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
