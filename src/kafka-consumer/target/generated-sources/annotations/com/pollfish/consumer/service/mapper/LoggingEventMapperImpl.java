package com.pollfish.consumer.service.mapper;

import com.pollfish.consumer.domain.LoggingEvent;

import com.pollfish.consumer.service.dto.LevelType;

import com.pollfish.consumer.service.dto.LoggingEventDTO;

import com.pollfish.consumer.web.rest.vm.LoggingEventVM;

import java.util.ArrayList;

import java.util.List;

import javax.annotation.Generated;

import org.springframework.stereotype.Component;

@Generated(

    value = "org.mapstruct.ap.MappingProcessor",

    date = "2018-10-04T15:04:35+0300",

    comments = "version: 1.1.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"

)

@Component

public class LoggingEventMapperImpl implements LoggingEventMapper {

    @Override

    public LoggingEvent toEntity(LoggingEventDTO dto) {

        if ( dto == null ) {

            return null;
        }

        LoggingEvent loggingEvent = new LoggingEvent();

        loggingEvent.setDateCreated( dto.getTime() );

        loggingEvent.setApp( dto.getApp() );

        if ( dto.getLevel() != null ) {

            loggingEvent.setLevel( dto.getLevel().name() );
        }

        loggingEvent.setId( dto.getId() );

        loggingEvent.setMessage( dto.getMessage() );

        loggingEvent.setVersion( dto.getVersion() );

        mapToEntity( loggingEvent, dto );

        return loggingEvent;
    }

    @Override

    public LoggingEventDTO toDto(LoggingEvent entity) {

        if ( entity == null ) {

            return null;
        }

        LoggingEventDTO loggingEventDTO = new LoggingEventDTO();

        loggingEventDTO.setTime( entity.getDateCreated() );

        loggingEventDTO.setId( entity.getId() );

        loggingEventDTO.setVersion( entity.getVersion() );

        loggingEventDTO.setMessage( entity.getMessage() );

        if ( entity.getLevel() != null ) {

            loggingEventDTO.setLevel( Enum.valueOf( LevelType.class, entity.getLevel() ) );
        }

        loggingEventDTO.setApp( entity.getApp() );

        return loggingEventDTO;
    }

    @Override

    public LoggingEventVM toVm(LoggingEventDTO dto) {

        if ( dto == null ) {

            return null;
        }

        LoggingEventVM loggingEventVM = new LoggingEventVM();

        loggingEventVM.setId( dto.getId() );

        loggingEventVM.setVersion( dto.getVersion() );

        loggingEventVM.setTime( dto.getTime() );

        loggingEventVM.setMessage( dto.getMessage() );

        loggingEventVM.setLevel( dto.getLevel() );

        loggingEventVM.setApp( dto.getApp() );

        return loggingEventVM;
    }

    @Override

    public LoggingEventDTO toDto(LoggingEventVM vm) {

        if ( vm == null ) {

            return null;
        }

        LoggingEventDTO loggingEventDTO = new LoggingEventDTO();

        loggingEventDTO.setId( vm.getId() );

        loggingEventDTO.setVersion( vm.getVersion() );

        loggingEventDTO.setTime( vm.getTime() );

        loggingEventDTO.setMessage( vm.getMessage() );

        loggingEventDTO.setLevel( vm.getLevel() );

        loggingEventDTO.setApp( vm.getApp() );

        return loggingEventDTO;
    }

    @Override

    public List<LoggingEvent> toEntity(List<LoggingEventDTO> dto) {

        if ( dto == null ) {

            return null;
        }

        List<LoggingEvent> list = new ArrayList<LoggingEvent>();

        for ( LoggingEventDTO loggingEventDTO : dto ) {

            list.add( toEntity( loggingEventDTO ) );
        }

        return list;
    }

    @Override

    public List<LoggingEventDTO> toDto(List<LoggingEvent> entity) {

        if ( entity == null ) {

            return null;
        }

        List<LoggingEventDTO> list = new ArrayList<LoggingEventDTO>();

        for ( LoggingEvent loggingEvent : entity ) {

            list.add( toDto( loggingEvent ) );
        }

        return list;
    }

    @Override

    public List<LoggingEventVM> toVm(List<LoggingEventDTO> dto) {

        if ( dto == null ) {

            return null;
        }

        List<LoggingEventVM> list = new ArrayList<LoggingEventVM>();

        for ( LoggingEventDTO loggingEventDTO : dto ) {

            list.add( toVm( loggingEventDTO ) );
        }

        return list;
    }

    @Override

    public List<LoggingEventDTO> toDtoList(List<LoggingEventVM> vm) {

        if ( vm == null ) {

            return null;
        }

        List<LoggingEventDTO> list = new ArrayList<LoggingEventDTO>();

        for ( LoggingEventVM loggingEventVM : vm ) {

            list.add( toDto( loggingEventVM ) );
        }

        return list;
    }
}

