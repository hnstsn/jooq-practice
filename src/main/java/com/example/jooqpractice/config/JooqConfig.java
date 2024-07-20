package com.example.jooqpractice.config;

import org.jooq.conf.ExecuteWithoutWhere;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer() {
        return c -> c.settings()
                .withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW)   // update할때 where 조건 필수로
                .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW)   // delete할때 where 조건 필수로
                .withRenderSchema(false);
    }
}
