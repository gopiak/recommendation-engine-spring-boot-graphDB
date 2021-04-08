package com.github.webdevgopi.slash.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration("slashGraphQlProperties")
@ConfigurationProperties("slash-graph-ql")
public class SlashGraphQlProperties {
    private String hostname;
}
