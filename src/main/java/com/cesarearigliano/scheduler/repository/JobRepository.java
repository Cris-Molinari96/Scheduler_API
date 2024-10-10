package com.cesarearigliano.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cesarearigliano.scheduler.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}