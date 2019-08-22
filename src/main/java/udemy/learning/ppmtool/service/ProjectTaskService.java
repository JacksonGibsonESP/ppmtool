package udemy.learning.ppmtool.service;

import org.springframework.stereotype.Service;
import udemy.learning.ppmtool.entity.Backlog;
import udemy.learning.ppmtool.entity.ProjectTask;
import udemy.learning.ppmtool.exception.ProjectNotFoundException;
import udemy.learning.ppmtool.repository.BacklogRepository;
import udemy.learning.ppmtool.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;
    private ProjectService projectService;

    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectService projectService) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
        projectTask.setBacklog(backlog);

        Integer backlogSequence = backlog.getPTSequence();
        backlog.setPTSequence(++backlogSequence);

        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {
        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String PTId) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
        if (backlog == null) {
            throw new ProjectNotFoundException("Project with ID: '" + backlogId + "' does not exist.");
        }

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(PTId);

        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task '" + PTId + "' not found");
        }

        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + PTId + "' does not exist in project: " + backlogId);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlogId, String PTId) {
        ProjectTask projectTaskToUpdate = findPTByProjectSequence(backlogId, PTId);

        if (!projectTaskToUpdate.getId().equals(updatedProjectTask.getId())) {
            throw new ProjectNotFoundException("You are trying to modificate project task with different project sequence number.");
        }

        return projectTaskRepository.save(updatedProjectTask);
    }

    public void deletePTByProjectSequence(String backlogId, String PTId) {
        ProjectTask projectTaskToDelete = findPTByProjectSequence(backlogId, PTId);

        projectTaskRepository.delete(projectTaskToDelete);
    }
}
