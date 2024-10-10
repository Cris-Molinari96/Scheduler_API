package com.cesarearigliano.scheduler.controller;

import com.cesarearigliano.scheduler.entity.*;
import com.cesarearigliano.scheduler.services.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
public class SchedulerController {

    private static final Logger log = LoggerFactory.getLogger(SchedulerController.class);
    

    @Autowired
    SchedulerService schedulerService;

    @GetMapping("/hello")
    public String sayHello(){
        return "Ola Amigos!";
    }
    
    @PostMapping("/ola")
    public String sayHelloWithName(@RequestBody Job job){
        return "Ola " + job.getJobName();
    }
    
    @PostMapping("/scheduleGet")
    public void scheduleGetJob(@RequestBody Job job){
        if(null!=job){
            schedulerService.scheduleGetJob(job);
        }
    }

    @PostMapping("/schedulePost")
    public void schedulePostJob(@RequestBody Job job){
        if(null!=job){
            schedulerService.schedulePostJob(job);
        }
    }
    
    @GetMapping("/scheduledJobs")
    public Map<String, Job> getScheduledJobs(){
        return schedulerService.getScheduledJobs();
    }
    
    @DeleteMapping("/deleteJob/{jobId}")
    public void deleteScheduledJob(@PathVariable String jobId){
        schedulerService.deleteScheduledJob(jobId);
    }
    
    @GetMapping("/jobOutputs/{jobName}")
    public List<String>  getJobOutputs(@PathVariable String jobName) {
    	return schedulerService.getJobOutputs(jobName);
    }
    
   
    
 
    
}
