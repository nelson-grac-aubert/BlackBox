package com.pigeon.blackbox.analytics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pigeon.blackbox.analytics.dto.TopUser;
import com.pigeon.blackbox.analytics.service.TopUsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

// HTTP entry point: returns JSON, every route of this class starts with the prefix below
@RestController
@RequestMapping("/api/analytics/top-users")
@Tag(name = "Top users analytics", description = "Most active users over a period")
public class TopUsersController {
    // Business layer, injected by Spring through the constructor
    private final TopUsersService topUsersService;

    public TopUsersController(TopUsersService topUsersService) {
        this.topUsersService = topUsersService;
    }

    // GET /api/analytics/top-users?from=2025-03-01&to=2025-04-01
    @GetMapping
    @Operation(
            summary = "Top 10 most active users",
            description = "Ranks users by number of events of any type over the period. Ties are broken by lowest user id.")
    public List<TopUser> getTopActiveUsers(
            // URL parameters, converted from ISO text (yyyy-MM-dd) to LocalDate
            @Parameter(description = "Start date, inclusive", example = "2025-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date, exclusive", example = "2025-04-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        // No logic here: validation and conversion are done by the service
        return topUsersService.getTopActiveUsers(from, to);
    }
}