package com.pigeon.blackbox.domain.model;

import com.pigeon.blackbox.domain.enums.DeviceType;

// Record : immutable object, no identity, only here to hold attributes
// No annotation @Document or @Id : it lives inside a LoginEvent
public record Device(DeviceType deviceType, String os, String browser) {

}
