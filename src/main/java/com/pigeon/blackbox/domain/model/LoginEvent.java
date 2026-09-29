package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.LoginStatus;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("LOGIN")
public class LoginEvent extends Event {

    private LoginStatus loginStatus;

    private String ipAddress; 

    private Device device; 

    public LoginEvent(Integer userId, Instant timestamp, LoginStatus loginStatus, String ipAddress, Device device) {
        super(EventType.LOGIN, userId, timestamp);
        this.loginStatus = loginStatus;
        this.ipAddress = ipAddress; 
        this.device = device; 
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public String getIpAddress() { 
        return ipAddress;
    }

    public Device getDevice() {
        return device; 
    }
}
