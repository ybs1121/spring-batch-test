package com.toy.batchtest.user;

import com.toy.batchtest.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class ExpireWriterConfig {

    private final DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<User> expireWriter() {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("UPDATE users SET status = 'EXPIRED' WHERE id =:id")
                .beanMapped()
                .build();
    }


}
