package com.toy.batchtest.job;

import com.toy.batchtest.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class UserExpiredJob {

    private final DataSource dataSource;

    // Job Bean 추가
    @Bean
    public Job expireJob(JobRepository jobRepository, Step expireStep) {
        return new JobBuilder("expireJob", jobRepository)
                .start(expireStep)
                .build();
    }


    @Bean
    public Step expireStep(JobRepository jobRepository,
                           PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("expireStep", jobRepository)
                .<User, User>chunk(1000, platformTransactionManager)
                .reader(expiredUserReader())
                .writer(expireWriter())
                .faultTolerant()
                .retry(TransientDataAccessException.class)
                .retryLimit(3)
                .skip(IllegalArgumentException.class)
                .skipLimit(10)
                .build()
                ;
    }

    @Bean
    public JdbcCursorItemReader<User> expiredUserReader() {
        return new JdbcCursorItemReaderBuilder<User>()
                .name("expiredUserReader")
                .dataSource(dataSource)
                .sql("select * from users where expired_date < NOW()")
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<User> expireWriter() {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("UPDATE users SET status = 'EXPIRED' WHERE id =:id")
                .beanMapped()
                .build();
    }


}
