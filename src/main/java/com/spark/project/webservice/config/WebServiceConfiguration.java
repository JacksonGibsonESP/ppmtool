package com.spark.project.webservice.config;

import com.spark.project.webservice.controller.WebProjectsController;
import com.spark.project.webservice.service.WebProjectsService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile("webservice")
public class WebServiceConfiguration {
    private static final String PROJECTS_SERVICE_URL = "http://ACCOUNTS-SERVICE";

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebProjectsService accountsService(@LoadBalanced RestTemplate restTemplate) {
        return new WebProjectsService(PROJECTS_SERVICE_URL, restTemplate);
    }

    @Bean
    public WebProjectsController accountsController(@LoadBalanced RestTemplate restTemplate) {
        return new WebProjectsController(accountsService(restTemplate));
    }
}
