package ro.alexportfolio.backend.mapper;

import org.mapstruct.Mapper;
import ro.alexportfolio.backend.dto.request.ProjectRequestDTO;
import ro.alexportfolio.backend.dto.response.ProjectResponseDTO;
import ro.alexportfolio.backend.model.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectRequestDTO request);
    ProjectResponseDTO toResponse(Project entity);
    List<ProjectResponseDTO> toResponseList(List<Project> entities);
}
