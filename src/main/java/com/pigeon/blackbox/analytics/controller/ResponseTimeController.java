package com.pigeon.blackbox.analytics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pigeon.blackbox.analytics.dto.EndpointResponseTime;
import com.pigeon.blackbox.analytics.service.ResponseTimeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping("/api/analytics/response-times")
@Tag(name = "Response time analytics", description = "API performance by endpoint")
public class ResponseTimeController {
    private final ResponseTimeService responseTimeService; 

    public ResponseTimeController(ResponseTimeService responseTimeService){
        this.responseTimeService = responseTimeService;
    }

    @GetMapping
    @Operation(summary = "Response time per endpoint", description = "Mean and 95th percentile of API response times per endpoint. The p95 means 95% of the calls answered faster than this value.")
    public List<EndpointResponseTime> getEndpointResponseTimes(
        @Parameter(description = "Start date, inclusive", example = "2025-03-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "End date, exclusive", example = "2025-04-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return responseTimeService.getResponseTimeByEndpoint(from, to);
    }

}
