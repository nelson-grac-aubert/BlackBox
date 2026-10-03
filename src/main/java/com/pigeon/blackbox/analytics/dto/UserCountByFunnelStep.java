package com.pigeon.blackbox.analytics.dto;

public record UserCountByFunnelStep(
    long notifiedUsersCount,
    long loggedInUsersCount, 
    long resubscribedUsersCount) {}
