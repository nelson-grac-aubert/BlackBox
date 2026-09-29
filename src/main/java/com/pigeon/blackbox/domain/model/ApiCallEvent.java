package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.HttpMethod;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("API_CALL")
public class ApiCallEvent extends Event {
    private String endpoint;

    private Integer responseTimeMs;
    
    private Integer statusCode;

    private HttpMethod httpMethod; 

    public ApiCallEvent(Integer userId, Instant timestamp, String endpoint, Integer responseTimeMs, Integer statusCode, HttpMethod httpMethod) {
        super(EventType.API_CALL, userId, timestamp);
        this.endpoint = endpoint; 
        this.responseTimeMs = responseTimeMs;
        this.statusCode = statusCode;
        this.httpMethod = httpMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
