package com.pigeon.blackbox.analytics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pigeon.blackbox.analytics.dto.ErrorCountByDay;
import com.pigeon.blackbox.analytics.service.ErrorAnalyticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping("/api/analytics/errors")
@Tag(name = "Error analytics", description = "Distribution of application errors")
public class ErrorAnalyticsController {
    private final ErrorAnalyticsService errorAnalyticsService; 

    public ErrorAnalyticsController(ErrorAnalyticsService errorAnalyticsService){
        this.errorAnalyticsService = errorAnalyticsService;
    }

    @GetMapping
    @Operation(summary = "Errors by type and by day", description = "...")
    public List<ErrorCountByDay> getErrorsByDayAndType(
        @Parameter(description = "Start date, inclusive", example = "2025-03-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "End date, exclusive", example = "2025-04-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    return errorAnalyticsService.getErrorsByDayAndType(from, to);
    }

}
