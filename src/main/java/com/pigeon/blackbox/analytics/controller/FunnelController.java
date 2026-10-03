package com.pigeon.blackbox.analytics.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pigeon.blackbox.analytics.dto.UserCountByFunnelStep;
import com.pigeon.blackbox.analytics.service.FunnelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/analytics/conversion-funnel") 
@Tag(name = "Conversion analytics", description = "Notification - Login - Subscription funnel")
public class FunnelController {
    private final FunnelService funnelService;

    public FunnelController(FunnelService funnelService) {
        this.funnelService = funnelService;
    }

    @GetMapping 
    @Operation(summary = "Conversion funnel", description = "Users who received a PAYMENT_EXPIRED notification, then logged in, then paid successfully. Returns 0 if no events in the period")
    public UserCountByFunnelStep getUsersByFunnelStep(
        @Parameter(description = "Start date, inclusive", example = "2025-03-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "End date, exclusive", example = "2025-04-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) { 
        return funnelService.getUserCountByFunnelStep(from, to); 
    }
}
