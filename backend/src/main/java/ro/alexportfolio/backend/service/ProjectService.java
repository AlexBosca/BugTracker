package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.dto.request.ProjectRequestDTO;
import ro.alexportfolio.backend.model.Project;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void createProject(Project project) {
        project.setCreatedAt(LocalDateTime.now());

        projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectByProjectKey(String projectKey) {
        return projectRepository.findByProjectKey(projectKey)
                .orElseThrow(() -> new IllegalStateException("Project not found"));
    }

    public void updateProject(String projectKey, Project project) {
        Project existingProject = projectRepository.findByProjectKey(projectKey)
                .orElseThrow(() -> new IllegalStateException("Project not found"));

        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());

        projectRepository.save(existingProject);
    }

    public void deleteProject(String projectKey) {
        projectRepository.deleteByProjectKey(projectKey);
    }
}
