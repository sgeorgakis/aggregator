package com.pollfish.consumer.service.mapper;

import com.pollfish.consumer.domain.LoggingEvent;
import com.pollfish.consumer.service.dto.LoggingEventDTO;
import com.pollfish.consumer.web.rest.vm.LoggingEventVM;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.pollfish.consumer.config.Constants.DATE_PARTITION_FORMAT;

@Mapper(componentModel = "spring")
public interface LoggingEventMapper {

    @Mapping(source = "time", target = "dateCreated")
    @Mapping(target = "datePartition", ignore = true)
    LoggingEvent toEntity(LoggingEventDTO dto);

    @InheritInverseConfiguration(name = "toEntity")
    LoggingEventDTO toDto(LoggingEvent entity);

    LoggingEventVM toVm(LoggingEventDTO dto);

    LoggingEventDTO toDto(LoggingEventVM vm);

    List<LoggingEvent> toEntity(List<LoggingEventDTO> dto);

    List<LoggingEventDTO> toDto(List<LoggingEvent> entity);

    List<LoggingEventVM> toVm(List<LoggingEventDTO> dto);

    List<LoggingEventDTO> toDtoList(List<LoggingEventVM> vm);

    /**
     * Map the datePartition attribute of the {@link LoggingEvent} object
     * using the time attribute of the {@link LoggingEventDTO} object
     *
     * @param entity the {@link LoggingEvent} object
     * @param dto the {@link LoggingEventDTO} object
     */
    @AfterMapping
    default void mapToEntity(@MappingTarget LoggingEvent entity, LoggingEventDTO dto) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PARTITION_FORMAT);
        entity.setDatePartition(dateFormat.format(dto.getTime()));
    }
}
