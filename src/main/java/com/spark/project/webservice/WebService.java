package com.spark.project.webservice;

import com.spark.project.webservice.config.RibbonConfiguration;
import com.spark.project.webservice.config.WebServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters=false) //disable component scan
@Profile("webservice")
@Import(WebServiceConfiguration.class)
@RibbonClient(name = "round-robin", configuration = RibbonConfiguration.class)
public class WebService {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "web-server");
        SpringApplication.run(WebService.class, args);
    }
}
