package com.pigeon.blackbox.analytics.dto;

import com.pigeon.blackbox.domain.enums.ErrorType;

public record ErrorCountByDay (
    String day,
    ErrorType errorType, 
    long count
) {}
