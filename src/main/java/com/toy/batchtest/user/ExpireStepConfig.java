package com.toy.batchtest.user;

import com.toy.batchtest.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class ExpireStepConfig {

    private final JdbcCursorItemReader<User> expiredUserReader;
    private final JdbcBatchItemWriter<User> expireWriter;

    @Bean
    public Step expireStep(JobRepository jobRepository,
                           PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("expireStep", jobRepository)
                .<User, User>chunk(1000, platformTransactionManager)
                .reader(expiredUserReader)
                .writer(expireWriter)
                .faultTolerant()
                .retry(TransientDataAccessException.class)
                .retryLimit(3)
                .skip(IllegalArgumentException.class)
                .skipLimit(10)
                .build()
                ;
    }

}
