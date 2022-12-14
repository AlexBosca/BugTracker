package com.example.backend.dto.converter;

import com.example.backend.enums.UserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {
    @Override
    public String convertToDatabaseColumn(UserRole role) {
        if(role == null) {
            return null;
        }

        return role.getCode();
    }

    @Override
    public UserRole convertToEntityAttribute(String code) {
        if(code == null) {
            return null;
        }

        return Stream.of(UserRole.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
