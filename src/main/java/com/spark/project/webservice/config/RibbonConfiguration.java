package com.spark.project.webservice.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("webservice")
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule() {
        return new RoundRobinRule();
    }
}
