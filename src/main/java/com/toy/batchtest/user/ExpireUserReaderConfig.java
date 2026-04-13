package com.toy.batchtest.user;

import com.toy.batchtest.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class ExpireUserReaderConfig {

    private final DataSource dataSource;


    @Bean
    public JdbcCursorItemReader<User> expiredUserReader() {
        return new JdbcCursorItemReaderBuilder<User>()
                .name("expiredUserReader")
                .dataSource(dataSource)
                .sql("select * from users where expire_date < NOW()")
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .build();
    }

}
