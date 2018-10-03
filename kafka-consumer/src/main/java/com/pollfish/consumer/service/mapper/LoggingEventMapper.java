package com.pollfish.consumer.service.mapper;

import com.pollfish.consumer.domain.LoggingEvent;
import com.pollfish.consumer.service.dto.LoggingEventDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LoggingEventMapper {

    @Mapping(source = "time", target = "dateCreated")
    @Mapping(target = "datePartition", ignore = true)
    LoggingEvent toEntity(LoggingEventDTO dto);

    @InheritConfiguration
    LoggingEventDTO toDto(LoggingEvent entity);

    List<LoggingEvent> toEntity(List<LoggingEventDTO> dto);

    List<LoggingEventDTO> toDto(List<LoggingEvent> entity);

    @AfterMapping
    default void mapToEntity(@MappingTarget LoggingEvent entity, LoggingEventDTO dto) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD");
        entity.setDatePartition(dateFormat.format(dto.getTime()));
    }

//
//    @AfterMapping
//    default void mapToDto(@MappingTarget LoggingEventDTO dto, LoggingEvent entity) {
//        entity.setLevel(dto.getLevel().toString());
//        dto.getLevel().name()
//    }
}
