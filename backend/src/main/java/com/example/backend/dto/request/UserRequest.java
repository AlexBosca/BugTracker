package com.example.backend.dto.request;

import javax.validation.constraints.NotBlank;

import com.example.backend.validation.constraint.NullOrNotBlankConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {

    @NullOrNotBlankConstraint
    @JsonProperty("firstName")
    private String firstName;

    @NullOrNotBlankConstraint
    @JsonProperty("lastName")
    private String lastName;

    @NullOrNotBlankConstraint
    @JsonProperty("email")
    private String email;

    @NullOrNotBlankConstraint
    @JsonProperty("password")
    private String password;

    @NullOrNotBlankConstraint
    @JsonProperty("avatarUrl")
    private String avatarUrl;

    @NullOrNotBlankConstraint
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @NullOrNotBlankConstraint
    @JsonProperty("jobTitle")
    private String jobTitle;

    @NullOrNotBlankConstraint
    @JsonProperty("department")
    private String department;

    @NullOrNotBlankConstraint
    @JsonProperty("timezone")
    private String timezone;
}
