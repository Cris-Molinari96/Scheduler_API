package com.project.scheduler.controller;

import com.project.scheduler.dto.JobDto;
import com.project.scheduler.scheduler.SchedulerService;
import com.project.scheduler.utils.ResponseHttp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.scheduler.services.JobService;

@RestController("/api-job")
@Tag(name = "JobController", description = "/api-job")
public class JobController {


    private ResponseHttp responseHttp;
    private JobService jobService;
    private SchedulerService schedulerService;

    @Autowired
    public JobController(JobService jobService, SchedulerService schedulerService) {
        this.jobService = jobService;
        this.schedulerService = schedulerService;
    }
    @GetMapping("/find-job/{idJob}")
    @Operation(summary = "Ottieni un job memorizzato nel database")
    public ResponseEntity<ResponseHttp> findJob(@PathVariable long idJob) {
        responseHttp = new ResponseHttp();
        responseHttp.setDataSource(jobService.getJob(idJob));
        return new ResponseEntity<>(responseHttp, HttpStatus.OK);
    }

    @GetMapping("/find-all-job")
    @Operation(summary = "Ottieni tutti i job")
    public ResponseEntity<ResponseHttp> findAllJob() {
        responseHttp = new ResponseHttp();
        responseHttp.setDataSource(jobService.getAllJobs());
        return new ResponseEntity<>(responseHttp, HttpStatus.OK);
    }

    @PostMapping("/create-job")
    @Operation(summary = "Crea un job senza eseguirlo")
    public ResponseEntity<ResponseHttp> insertJob(@RequestBody JobDto job) {
        if (job != null) {
            jobService.insertJob(job);
        } else {
            System.out.println("Si è verificato un errore");
        }
        return new ResponseEntity<>(new ResponseHttp("Job created successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/create-and-execute-job")
    @Operation(summary = "Crea un job con esecuzione automatica")
    public ResponseEntity<ResponseHttp> insertAndExecuteJob(@RequestBody JobDto job) {
        if (job != null) {
            jobService.insertJob(job);
            schedulerService.scheduleJob(job);
        } else {
            System.out.println("Si è verificato un errore");
        }
        return new ResponseEntity<>(new ResponseHttp("Job created successfully"), HttpStatus.CREATED);
    }


    @PutMapping("/update")
    @Operation(summary = "Aggiorna un job con esecuzione automatica")
    public ResponseEntity<ResponseHttp> updateJob(@RequestBody JobDto jobDto) {
        responseHttp = new ResponseHttp();
        responseHttp.setMessage("Job aggiornato");
        jobService.updateJob(jobDto);
        return new ResponseEntity<>(responseHttp, HttpStatus.OK);
    }

    @DeleteMapping("/delete-job/{idJob}")
    @Operation(summary = "Elimina un job")
    public ResponseEntity<ResponseHttp> deleteJob(@PathVariable long idJob) {
        responseHttp = new ResponseHttp();
        responseHttp.setMessage(idJob + "  è stato eliminato");
        jobService.deleteJob(idJob);
        return new ResponseEntity<>(responseHttp, HttpStatus.OK);
    }

}
