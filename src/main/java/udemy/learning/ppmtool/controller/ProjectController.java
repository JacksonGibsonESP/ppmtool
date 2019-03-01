package udemy.learning.ppmtool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import udemy.learning.ppmtool.entity.Project;
import udemy.learning.ppmtool.service.ProjectService;
import udemy.learning.ppmtool.service.ValidationService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;
    private ValidationService validationService;

    public ProjectController(ProjectService projectService,
                             ValidationService validationService) {
        this.projectService = projectService;
        this.validationService = validationService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<Map<String, String>> validationResult = validationService.validate(result);
        if (validationResult != null) {
            return validationResult;
        }

        Project projectPersisted = projectService.saveOrUpdate(project);
        return new ResponseEntity<>(projectPersisted, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {

        Project project = projectService.findProjectByIdentifier(projectId);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
        projectService.deleteProjectByIdentifier(projectId);

        return new ResponseEntity<>("Project with Id: '" + projectId.toUpperCase() + "' was deleted.", HttpStatus.OK);
    }
}
