package ro.alexportfolio.backend.mapper;

import org.mapstruct.Mapper;
import ro.alexportfolio.backend.dto.request.IssueRequestDTO;
import ro.alexportfolio.backend.dto.response.IssueResponseDTO;
import ro.alexportfolio.backend.model.Issue;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    Issue toEntity(IssueRequestDTO request);
    IssueResponseDTO toResponse(Issue entity);
}
