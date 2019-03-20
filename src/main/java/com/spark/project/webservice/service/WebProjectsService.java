package com.spark.project.webservice.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import udemy.learning.ppmtool.entity.Project;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
@Profile("webservice")
public class WebProjectsService {
    private Logger logger = Logger.getLogger(WebProjectsService.class.getName());

    private final RestTemplate restTemplate;

    private String serviceUrl;

    public WebProjectsService(String serviceUrl, RestTemplate restTemplate) { // Ribbon
        this.serviceUrl = serviceUrl.startsWith("http") ?
                serviceUrl : "http://" + serviceUrl;
        this.restTemplate = restTemplate;
    }

    public Project getByProjectIdentifier(String projectIdentifier) {
        Project project = null;
        try {
            project = restTemplate.getForObject(serviceUrl
                    + "/api/project/{id}", Project.class, projectIdentifier);
        } catch (HttpClientErrorException e) {
            logger.warning("Project not found with projectIdentifier = " + projectIdentifier);
            logger.warning(e.getMessage());
        }
        return project;
    }

    public List<Project> getAllProjects() {
        logger.info("getAllProjects invoked");
        Project[] projects = restTemplate.getForObject(serviceUrl + "/api/project/all", Project[].class);

        logger.info("result: " + Arrays.toString(projects));

        if (projects == null || projects.length == 0)
            return Collections.emptyList();
        else
            return Arrays.asList(projects);
    }
}
