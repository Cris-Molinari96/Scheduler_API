package com.project.scheduler.controller;

import com.project.scheduler.entity.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-test")
public class ApiExposedController {

    @GetMapping("/test1")
    public ResponseEntity<?> testApi1() {
        return ResponseEntity.ok("Successo ");
    }

    @GetMapping("/test2")
    public ResponseEntity<?> testApi2() {
        return ResponseEntity.ok("Successo");
    }

    @GetMapping("/test3")
    public ResponseEntity<?> testApi3() {
        return ResponseEntity.ok("Successo");
    }


    @PostMapping("/test-post")
    public ResponseEntity<?> testPost(@RequestBody Job job) {
        System.out.println(job.getJobName().toString());
        System.out.println("Richiesta ricevuta nel controller");
        System.out.println("Job name: " + job.getJobName());
        System.out.println("Job completo: " + job);
        return ResponseEntity.ok("Successo" + job.getJobName() + " work");
    }

    @PostMapping("/test-body-post")
    public ResponseEntity<?> testPost(@RequestBody Object requestBody) {

        System.out.println("Body ricevuto: " + requestBody);
        if (requestBody == null) {
            return ResponseEntity.badRequest().body("La richiesta contiene un body nullo");
        }
        try {
            return ResponseEntity.ok("Successo!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/test-temp-post")
    public ResponseEntity<?> testTempPost(@RequestBody Object requestBody) {

        System.out.println("Body ricevuto test temp2: -->   " + requestBody);
        if (requestBody == null) {
            return ResponseEntity.badRequest().body("La richiesta contiene un body nullo");
        }
        try {
            // La tua logica qui
            return ResponseEntity.ok("Successo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
