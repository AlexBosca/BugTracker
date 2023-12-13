package com.example.backend.mapper;

import static com.example.backend.enums.UserRole.ROLE_DEVELOPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.dto.request.UserRequest;
import com.example.backend.dto.response.UserFullResponse;
import com.example.backend.entity.UserEntity;

@Profile("test")
@ActiveProfiles("test")
class MapStructMapperTest {

    private MapStructMapper mapStructMapper;

    @BeforeEach
    void setup() {
        mapStructMapper = new MapStructMapperImpl();
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest in normal circumstances")
    void mapUserEntityToUserRequest_Normal() {
        UserEntity givenUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest.getFirstName()).isEqualTo(expectedRequest.getFirstName());
        assertThat(actualRequest.getLastName()).isEqualTo(expectedRequest.getLastName());
        assertThat(actualRequest.getEmail()).isEqualTo(expectedRequest.getEmail());
        assertThat(actualRequest.getPassword()).isEqualTo(expectedRequest.getPassword());
        assertThat(actualRequest.getRole()).isEqualTo(expectedRequest.getRole());
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest when UserEntity is null")
    void mapUserEntityToUserRequest_NullUserEntity() {
        UserEntity givenUser = null;

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest).isNull();
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest when firstName is null")
    void mapUserEntityToUserRequest_NullFirstName() {
        UserEntity givenUser = UserEntity.builder()
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest.getFirstName()).isNull();
        assertThat(actualRequest.getLastName()).isEqualTo(expectedRequest.getLastName());
        assertThat(actualRequest.getEmail()).isEqualTo(expectedRequest.getEmail());
        assertThat(actualRequest.getPassword()).isEqualTo(expectedRequest.getPassword());
        assertThat(actualRequest.getRole()).isEqualTo(expectedRequest.getRole());
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest when lastName is null")
    void mapUserEntityToUserRequest_NullLastName() {
        UserEntity givenUser = UserEntity.builder()
            .firstName("John")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest.getFirstName()).isEqualTo(expectedRequest.getFirstName());
        assertThat(actualRequest.getLastName()).isNull();
        assertThat(actualRequest.getEmail()).isEqualTo(expectedRequest.getEmail());
        assertThat(actualRequest.getPassword()).isEqualTo(expectedRequest.getPassword());
        assertThat(actualRequest.getRole()).isEqualTo(expectedRequest.getRole());
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest when email is null")
    void mapUserEntityToUserRequest_NullEmail() {
        UserEntity givenUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest.getFirstName()).isEqualTo(expectedRequest.getFirstName());
        assertThat(actualRequest.getLastName()).isEqualTo(expectedRequest.getLastName());
        assertThat(actualRequest.getEmail()).isNull();
        assertThat(actualRequest.getPassword()).isEqualTo(expectedRequest.getPassword());
        assertThat(actualRequest.getRole()).isEqualTo(expectedRequest.getRole());
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest when password is null")
    void mapUserEntityToUserRequest_NullPassword() {
        UserEntity givenUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role(ROLE_DEVELOPER)
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role("ROLE_DEVELOPER")
            .build();

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest.getFirstName()).isEqualTo(expectedRequest.getFirstName());
        assertThat(actualRequest.getLastName()).isEqualTo(expectedRequest.getLastName());
        assertThat(actualRequest.getEmail()).isEqualTo(expectedRequest.getEmail());
        assertThat(actualRequest.getPassword()).isNull();
        assertThat(actualRequest.getRole()).isEqualTo(expectedRequest.getRole());
    }

    @Test
    @DisplayName("Mapping UserEntity to UserRequest when role is null")
    void mapUserEntityToUserRequest_NullRole() {
        UserEntity givenUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .build();

        UserRequest actualRequest = mapStructMapper.toRequest(givenUser);

        assertThat(actualRequest.getFirstName()).isEqualTo(expectedRequest.getFirstName());
        assertThat(actualRequest.getLastName()).isEqualTo(expectedRequest.getLastName());
        assertThat(actualRequest.getEmail()).isEqualTo(expectedRequest.getEmail());
        assertThat(actualRequest.getPassword()).isEqualTo(expectedRequest.getPassword());
        assertThat(actualRequest.getRole()).isNull();
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity in normal circumstances")
    void mapUserRequestToUserEntity_Normal() {
        UserRequest givenRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity when UserRequest is null")
    void mapUserRequestToUserEntity_NullUserEntity() {
        UserRequest givenRequest = null;

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser).isNull();
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity when firstName is null")
    void mapUserRequestToUserEntity_NullFirstName() {
        UserRequest givenRequest = UserRequest.builder()
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isNull();
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity when lastName is null")
    void mapUserRequestToUserEntity_NullLastName() {
        UserRequest givenRequest = UserRequest.builder()
            .firstName("John")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isNull();
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity when email is null")
    void mapUserRequestToUserEntity_NullEmail() {
        UserRequest givenRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .password("Jo#nDo3P4SS")
            .role("ROLE_DEVELOPER")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isNull();
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity when password is null")
    void mapUserRequestToUserEntity_NullPassword() {
        UserRequest givenRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role("ROLE_DEVELOPER")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isNull();
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping UserRequest to UserEntity when role is null")
    void mapUserRequestToUserEntity_NullRole() {
        UserRequest givenRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isNull();
    }

    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity in normal circumstances")
    void mapRegistrationRequestToUserEntity_Normal() {
        RegistrationRequest givenRequest = RegistrationRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("D")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity when RegistrationEntity is null")
    void mapRegistrationRequestToUserEntity_NullUserEntity() {
        RegistrationRequest givenRequest = null;

        UserEntity actualRequest = mapStructMapper.toEntity(givenRequest);

        assertThat(actualRequest).isNull();
    }

    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity when firstName is null")
    void mapRegistrationRequestToUserEntity_NullFirstName() {
        RegistrationRequest givenRequest = RegistrationRequest.builder()
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("D")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isNull();
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity when lastName is null")
    void mapRegistrationRequestToUserEntity_NullLastName() {
        RegistrationRequest givenRequest = RegistrationRequest.builder()
            .firstName("John")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role("D")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isNull();
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity when email is null")
    void mapRegistrationRequestToUserEntity_NullEmail() {
        RegistrationRequest givenRequest = RegistrationRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .password("Jo#nDo3P4SS")
            .role("D")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .password("Jo#nDo3P4SS")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isNull();
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity when password is null")
    void mapRegistrationRequestToUserEntity_NullPassword() {
        RegistrationRequest givenRequest = RegistrationRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role("D")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role(ROLE_DEVELOPER)
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isNull();
        assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
    }

    @Disabled
    @Test
    @DisplayName("Mapping RegistrationRequest to UserEntity when role is null")
    void mapRegistrationRequestToUserEntity_NullRole() {
        RegistrationRequest givenRequest = RegistrationRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .build();

        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("Jo#nDo3P4SS")
            .build();

        UserEntity actualUser = mapStructMapper.toEntity(givenRequest);

        assertThat(actualUser.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUser.getRole()).isNull();
    }

    @Test
    @DisplayName("Mapping UserEntity to UserFullResponse in normal circumstances")
    void mapUserEntityToUserFullResponse_Normal() {
        UserEntity givenUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role(ROLE_DEVELOPER)
            .isAccountLocked(false)
            .isEnabled(true)
            .build();

        UserFullResponse expectedUserFullResponse = UserFullResponse.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .role("ROLE_DEVELOPER")
            .isAccountLocked(false)
            .isEnabled(true)
            .build();

        UserFullResponse actualUserFullResponse = mapStructMapper.toResponse(givenUser);

        assertThat(actualUserFullResponse.getUserId()).isEqualTo(expectedUserFullResponse.getUserId());
        assertThat(actualUserFullResponse.getFirstName()).isEqualTo(expectedUserFullResponse.getFirstName());
        assertThat(actualUserFullResponse.getLastName()).isEqualTo(expectedUserFullResponse.getLastName());
        assertThat(actualUserFullResponse.getEmail()).isEqualTo(expectedUserFullResponse.getEmail());
        assertThat(actualUserFullResponse.getRole()).isEqualTo(expectedUserFullResponse.getRole());
        assertThat(actualUserFullResponse.isAccountLocked()).isEqualTo(expectedUserFullResponse.isAccountLocked());
        assertThat(actualUserFullResponse.isEnabled()).isEqualTo(expectedUserFullResponse.isEnabled());
    }
}
