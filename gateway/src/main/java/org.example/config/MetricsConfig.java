package org.example.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    // 或者，如果你使用的是Java 8及更高版本，你可以直接使用Lambda表达式来创建Bean
    // @Bean
    // public MeterRegistryCustomizer<MeterRegistry> metricsCommonTagsLambda() {
    //     return registry -> registry.config().commonTags("application", applicationName);
    // }
}
