package com.pollfish.consumer.web.rest;

import com.pollfish.consumer.service.LoggingEventService;
import com.pollfish.consumer.service.dto.LevelType;
import com.pollfish.consumer.service.mapper.LoggingEventMapper;
import com.pollfish.consumer.web.rest.vm.LoggingEventVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoggingEventController {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingEventController.class);

    private final LoggingEventService loggingEventService;
    private final LoggingEventMapper mapper;

    public LoggingEventController(LoggingEventService loggingEventService, LoggingEventMapper mapper) {
        this.loggingEventService = loggingEventService;
        this.mapper = mapper;
    }

    @GetMapping("/logging-events")
    public ResponseEntity<List<LoggingEventVM>> getLoggingEvents(@RequestParam(name = "date", required = false) String date,
                                                                 @RequestParam(name = "app", required = false) Integer app,
                                                                 @RequestParam(name = "level", required = false) String level) {
        LOG.debug("Incoming request. Date: {}, App: {}, Level: {}", date, app, level);
        List<LoggingEventVM> loggingEventVMList;
        if (date != null && app != null && level != null) {
            loggingEventVMList = mapper.toVm(loggingEventService
                    .findByDayAndAppAndLevel(date, app, LevelType.valueOf(level.toUpperCase())));
        } else {
            loggingEventVMList = mapper.toVm(loggingEventService.findAll());
        }
        return ResponseEntity.ok(loggingEventVMList);
    }
}
