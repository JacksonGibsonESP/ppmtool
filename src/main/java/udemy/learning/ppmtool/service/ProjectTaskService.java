package udemy.learning.ppmtool.service;

import org.springframework.stereotype.Service;
import udemy.learning.ppmtool.entity.Backlog;
import udemy.learning.ppmtool.entity.Project;
import udemy.learning.ppmtool.entity.ProjectTask;
import udemy.learning.ppmtool.exception.ProjectNotFoundException;
import udemy.learning.ppmtool.repository.BacklogRepository;
import udemy.learning.ppmtool.repository.ProjectRepository;
import udemy.learning.ppmtool.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;
    private ProjectRepository projectRepository;

    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
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
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        Project project = projectRepository.findByProjectIdentifier(id);

        if (project == null) {
            throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist.");
        }

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
