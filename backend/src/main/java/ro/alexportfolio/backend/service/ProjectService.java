package ro.alexportfolio.backend.service;

import org.springframework.stereotype.Service;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.exception.ProjectNotFoundException;
import ro.alexportfolio.backend.model.Project;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final Clock clock;

    public ProjectService(ProjectRepository projectRepository,
                          Clock clock) {
        this.projectRepository = projectRepository;
        this.clock = clock;
    }

    public void createProject(Project project) {
        project.setCreatedAt(LocalDateTime.now(clock));

        projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectByProjectKey(String projectKey) {
        return projectRepository.findByProjectKey(projectKey)
                .orElseThrow(ProjectNotFoundException::new);
    }

    public void updateProject(String projectKey, Project project) {
        Project existingProject = projectRepository.findByProjectKey(projectKey)
                .orElseThrow(ProjectNotFoundException::new);

        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());

        projectRepository.save(existingProject);
    }

    public void deleteProject(String projectKey) {
        projectRepository.deleteByProjectKey(projectKey);
    }
}
