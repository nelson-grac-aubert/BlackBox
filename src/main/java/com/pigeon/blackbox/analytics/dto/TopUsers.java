package com.pigeon.blackbox.analytics.dto;

public record TopUsers(int userId, String firstName, String lastName, long eventCount) {
}
