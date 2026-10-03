package com.pigeon.blackbox.analytics.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pigeon.blackbox.analytics.dto.EndpointResponseTime;
import com.pigeon.blackbox.analytics.repository.ResponseTimeRepository;

@Service 
public class ResponseTimeService {
    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");

    private final ResponseTimeRepository responseTimeRepository; 

    public ResponseTimeService(ResponseTimeRepository responseTimeRepository) {
        this.responseTimeRepository = responseTimeRepository; 
    }

    public List<EndpointResponseTime> getResponseTimeByEndpoint(LocalDate from, LocalDate to) {
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

        return responseTimeRepository.findResponseTimesByEndpoint(start, end);
    }
    
}
