package com.project.scheduler.controller;

import com.project.scheduler.scheduler.JobRegistry;
import com.project.scheduler.scheduler.SchedulerService;
import com.project.scheduler.utils.JobResponse;
import com.project.scheduler.utils.ResponseHttp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JobRegistryController {

    public final SchedulerService schedulerService;
    private final JobRegistry jobRegistry;
    public ResponseHttp responseHttp;

    @Autowired
    public JobRegistryController(
            SchedulerService schedulerService,
            JobRegistry jobRegistry) {
        this.schedulerService = schedulerService;
        this.jobRegistry = jobRegistry;
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

    @GetMapping("/get-response/{jobName}")
    @Operation(summary = "Ottieni la risposta di un job")
    public ResponseEntity<ResponseHttp> getResponseJob(@PathVariable String jobName) {
        responseHttp = new ResponseHttp();
        List<JobResponse> response = jobRegistry.getJobResponse(jobName);
        responseHttp.setDataSource(response);
        return new ResponseEntity<>(responseHttp, HttpStatus.OK);
    }

}
