package udemy.learning.ppmtool.service;

import org.springframework.stereotype.Service;
import udemy.learning.ppmtool.entity.Backlog;
import udemy.learning.ppmtool.entity.Project;
import udemy.learning.ppmtool.entity.User;
import udemy.learning.ppmtool.exception.ProjectIdException;
import udemy.learning.ppmtool.exception.ProjectNotFoundException;
import udemy.learning.ppmtool.repository.BacklogRepository;
import udemy.learning.ppmtool.repository.ProjectRepository;
import udemy.learning.ppmtool.repository.UserRepository;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private BacklogRepository backlogRepository;
    private UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
        this.userRepository = userRepository;
    }

    public Project saveOrUpdate(Project project, String username) {

        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be " +
                        "updated because it doesn't exist.");
            }
        }

        try {
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier());
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project Id: '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project Id: '" + projectId.toUpperCase() + "' does not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account.");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Cannot delete project with id: '" + projectId.toUpperCase()
                    + "'. This project does not exist.");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account.");
        }

        projectRepository.delete(project);
    }
}
