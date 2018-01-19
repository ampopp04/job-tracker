package com.job.tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:job-tracker-runner.properties"})
public class Config {
}