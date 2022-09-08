package com.recipe.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfiguration {

    private String url;
    private String username;
    private String password;
}
