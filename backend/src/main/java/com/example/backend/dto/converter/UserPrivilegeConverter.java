package com.example.backend.dto.converter;

import com.example.backend.enums.IssuePriority;
import com.example.backend.enums.UserPrivilege;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserPrivilegeConverter implements AttributeConverter<UserPrivilege, String> {
    @Override
    public String convertToDatabaseColumn(UserPrivilege privilege) {
        if(privilege == null) {
            return null;
        }

        return privilege.getCode();
    }

    @Override
    public UserPrivilege convertToEntityAttribute(String code) {
        if(code == null) {
            return null;
        }

        return Stream.of(UserPrivilege.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
