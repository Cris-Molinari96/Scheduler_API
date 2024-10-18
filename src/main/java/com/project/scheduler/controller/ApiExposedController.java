package com.project.scheduler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
