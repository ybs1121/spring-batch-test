package com.toy.batchtest.user;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ExpireJobScheduler {

    private final Job expireJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시
    public void run() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addDate("date", new Date()) // 매일 다른 파라미터 → 재실행 가능
                .toJobParameters();

        jobLauncher.run(expireJob, params);
    }
}