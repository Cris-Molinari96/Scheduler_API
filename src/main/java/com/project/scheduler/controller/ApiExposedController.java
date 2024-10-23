package com.project.scheduler.controller;

import com.project.scheduler.entity.Job;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-test")
public class ApiExposedController {


    @GetMapping("/test1")
    public String testApi1() {
        return "Recupero informazioni da info-camere report, status OK";
    }

    @GetMapping("/test2")
    public String testApi2() {
        return "Recupero informazioni da frpc-service, status OK";
    }

    @GetMapping("/test3")
    public String testApi3() {
        return "Recupero informazioni da cdor-service, status OK";
    }


    @PostMapping("/test-post")
    public String testPost(@RequestBody Job job) {
        System.out.println(job.getJobName().toString());
        return "Recupero informazioni da post-job , status OK";
    }

}
