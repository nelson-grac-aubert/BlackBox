package com.pigeon.blackbox.analytics.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.pigeon.blackbox.analytics.dto.UserCountByFunnelStep;
import com.pigeon.blackbox.analytics.repository.FunnelRepository;

@Service 
public class FunnelService {
    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");

    private final FunnelRepository funnelRepository;

    public FunnelService(FunnelRepository funnelRepository) {
        this.funnelRepository = funnelRepository; 
    }
    
    public UserCountByFunnelStep getUserCountByFunnelStep(LocalDate from, LocalDate to) {
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

        UserCountByFunnelStep result = funnelRepository.findUserCountsByFunnelStep(start, end);
        // No event in the period: the final $group produces no document
        return result != null ? result : new UserCountByFunnelStep(0, 0, 0);

    }
}
