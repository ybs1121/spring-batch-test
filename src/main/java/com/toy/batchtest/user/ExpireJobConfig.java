package com.toy.batchtest.user;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class ExpireJobConfig {

    private final Step expireStep;

    @Bean
    public Job expireJob(JobRepository jobRepository) {
        return new JobBuilder("expireJob", jobRepository)
                .start(expireStep)
                .build();
    }
}
