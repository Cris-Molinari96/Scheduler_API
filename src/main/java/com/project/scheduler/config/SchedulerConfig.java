package com.project.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("my-scheduled-task-pool-");
        scheduler.setErrorHandler(t -> System.err.println("Errore nell'esecuzione del task: " + t.getMessage()));
        scheduler.initialize();
        return scheduler;
    }
}
