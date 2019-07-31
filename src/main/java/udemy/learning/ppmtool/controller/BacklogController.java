package udemy.learning.ppmtool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import udemy.learning.ppmtool.entity.ProjectTask;
import udemy.learning.ppmtool.service.ProjectTaskService;
import udemy.learning.ppmtool.service.ValidationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    private ProjectTaskService projectTaskService;
    private ValidationService validationService;

    public BacklogController(ProjectTaskService projectTaskService, ValidationService validationService) {
        this.projectTaskService = projectTaskService;
        this.validationService = validationService;
    }

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result, @PathVariable String backlogId) {
        ResponseEntity<?> errorMap = validationService.validate(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask);

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlogId) {
        return projectTaskService.findBacklogById(backlogId);
    }

    @GetMapping("/{backlogId}/{PTId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String PTId) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, PTId);
        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{PTId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedProjectTask, BindingResult result,
                                               @PathVariable String backlogId, @PathVariable String PTId) {
        ResponseEntity<?> errorMap = validationService.validate(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(updatedProjectTask, backlogId, PTId);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{PTId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String PTId) {
        projectTaskService.deletePTByProjectSequence(backlogId, PTId);

        return new ResponseEntity<>("Project Task " + PTId + " was deleted successfully", HttpStatus.OK);
    }
}
