package com.pigeon.blackbox.analytics.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pigeon.blackbox.analytics.dto.ErrorCountByDay;
import com.pigeon.blackbox.analytics.repository.ErrorAnalyticsRepository;

@Service 
public class ErrorAnalyticsService {
    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");

    private final ErrorAnalyticsRepository errorAnalyticsRepository;

    public ErrorAnalyticsService(ErrorAnalyticsRepository errorAnalyticsRepository) {
        this.errorAnalyticsRepository = errorAnalyticsRepository; 
    }
    
    public List<ErrorCountByDay> getErrorsByDayAndType(LocalDate from, LocalDate to) {
        // Both date limits must exist 
        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' dates are required");
        }
        // Time intervall must be valid 
        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("'from' must be before 'to'");
        }

        Instant start = from.atStartOfDay(ZONE).toInstant();
        Instant end = to.atStartOfDay(ZONE).toInstant();

        return errorAnalyticsRepository.countErrorsByDayAndType(start, end);

    }
    
}
