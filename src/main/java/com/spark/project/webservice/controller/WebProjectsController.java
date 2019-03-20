package com.spark.project.webservice.controller;

import com.spark.project.webservice.service.WebProjectsService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udemy.learning.ppmtool.entity.Project;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/webservice")
@Profile("webservice")
public class WebProjectsController {

	private WebProjectsService accountsService;

	private Logger logger = Logger.getLogger(WebProjectsController.class.getName());

	public WebProjectsController(WebProjectsService accountsService) {
		this.accountsService = accountsService;
	}

	@GetMapping("/project/{projectIdentifier}")
	public ResponseEntity<?> byProjectIdentifier(@PathVariable("projectIdentifier") String projectIdentifier) {

		logger.info("web-service byProjectIdentifier invoked: " + projectIdentifier);

		Project project = accountsService.getByProjectIdentifier(projectIdentifier);
		if (project != null) {
			logger.info("web-service byProjectIdentifier found: " + project);
		} else {
			logger.info("web-service byProjectIdentifier found nothing");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@GetMapping("/project/all")
	public ResponseEntity<?> allProjects() {
		logger.info("web-service allProjects invoked");

		List<Project> projects = accountsService.getAllProjects();
		logger.info("web-service allProjects found: " + projects);

		return new ResponseEntity<>(projects, HttpStatus.OK);
	}
}
