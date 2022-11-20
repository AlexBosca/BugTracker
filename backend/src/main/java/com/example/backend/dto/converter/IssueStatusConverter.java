package com.example.backend.dto.converter;

import com.example.backend.enums.IssueStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class IssueStatusConverter implements AttributeConverter<IssueStatus, String> {

    @Override
    public String convertToDatabaseColumn(IssueStatus status) {
        if(status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public IssueStatus convertToEntityAttribute(String code) {
        if(code == null) {
            return null;
        }

        return Stream.of(IssueStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
