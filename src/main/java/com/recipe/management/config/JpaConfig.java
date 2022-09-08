package com.recipe.management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DataSourceConfiguration.class)
@Profile("local-mysql")
@RequiredArgsConstructor
public class JpaConfig {

    private final DataSourceConfiguration dataSourceConfiguration;

    @Bean
    public DataSource getDataSource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dataSourceConfiguration.getUrl());
        dataSourceBuilder.username(dataSourceConfiguration.getUsername());
        dataSourceBuilder.password(dataSourceConfiguration.getPassword());
        return dataSourceBuilder.build();
    }
}
