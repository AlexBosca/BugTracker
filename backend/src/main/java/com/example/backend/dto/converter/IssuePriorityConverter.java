package com.example.backend.dto.converter;

import com.example.backend.enums.IssuePriority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class IssuePriorityConverter implements AttributeConverter<IssuePriority, String> {

    @Override
    public String convertToDatabaseColumn(IssuePriority status) {
        if(status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public IssuePriority convertToEntityAttribute(String code) {
        if(code == null) {
            return null;
        }

        return Stream.of(IssuePriority.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
