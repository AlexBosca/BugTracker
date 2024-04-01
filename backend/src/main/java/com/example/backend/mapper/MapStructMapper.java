package com.example.backend.mapper;

import com.example.backend.dto.request.*;
import com.example.backend.dto.response.*;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueCommentEntity;
import com.example.backend.entity.issue.IssueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {

    UserRequest toRequest(UserEntity user);
    UserEntity toEntity(UserRequest request);

    @Mapping(target = "role", expression = "java(UserRole.getRoleByCode(request.getRole()))")
    UserEntity toEntity(RegistrationRequest request);

    UserFullResponse toResponse(UserEntity user);
    UserEntity toEntity(UserFullResponse response);

    IssueRequest toRequest(IssueEntity issue);
    IssueEntity toEntity(IssueRequest request);
    IssueFullResponse toResponse(IssueEntity issue);
    List<IssueFullResponse> toResponses(List<IssueEntity> entities);
    IssueEntity toEntity(IssueFullResponse response);

    IssueCommentRequest toRequest(IssueCommentEntity comment);
    IssueCommentEntity toEntity(IssueCommentRequest request);
    IssueCommentFullResponse toResponse(IssueCommentEntity comment);
    IssueCommentEntity toEntity(IssueCommentFullResponse response);

    ProjectRequest toRequest(ProjectEntity project);
    ProjectEntity toEntity(ProjectRequest request);
    ProjectFullResponse toResponse(ProjectEntity project);
    ProjectEntity toEntity(ProjectFullResponse response);
}
