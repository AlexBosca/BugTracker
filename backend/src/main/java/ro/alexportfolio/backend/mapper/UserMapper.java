package ro.alexportfolio.backend.mapper;

import org.mapstruct.Mapper;
import ro.alexportfolio.backend.dto.request.UserRequestDTO;
import ro.alexportfolio.backend.dto.response.UserResponseDTO;
import ro.alexportfolio.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO request);
    UserResponseDTO toResponse(User entity);
}
