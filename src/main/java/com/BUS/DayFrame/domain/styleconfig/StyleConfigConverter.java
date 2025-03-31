package com.BUS.DayFrame.domain.styleconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class StyleConfigConverter implements AttributeConverter<StyleConfig, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(StyleConfig styleConfig) {
        if(Objects.isNull(styleConfig)){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(styleConfig);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("StyleConfig => JSON 변환 실패");
        }
    }

    @Override
    public StyleConfig convertToEntityAttribute(String dbData) {
        if(Objects.isNull(dbData)){
            return null;
        }
        try {
            return objectMapper.readValue(dbData, StyleConfig.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON => StyleConfig 변환 실패");
        }
    }
}
