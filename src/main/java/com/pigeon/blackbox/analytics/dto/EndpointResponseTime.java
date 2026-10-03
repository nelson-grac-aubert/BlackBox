package com.pigeon.blackbox.analytics.dto;

public record EndpointResponseTime(
    String endpoint,
    long callCount,
    double avgResponseTimeMs,
    double p95ResponseTimeMs
    ) {}
