package com.pigeon.blackbox.analytics.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.pigeon.blackbox.analytics.dto.EndpointResponseTime;
import com.pigeon.blackbox.domain.enums.EventType;

@Repository 
public class ResponseTimeRepository {

    private final MongoTemplate mongoTemplate;

    public ResponseTimeRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<EndpointResponseTime> findResponseTimesByEndpoint(Instant from, Instant to) {
        
        // MATCH only API calls in the time period
        MatchOperation matchApiCalls = Aggregation.match(Criteria.where("eventType").is(EventType.API_CALL.name())
            .and("timestamp").gte(from).lt(to));

        // GROUP API calls by endpoint: count, mean and 95th percentile of response times
        GroupOperation groupByEndpoint = Aggregation.group("endpoint")
            .count().as("callCount")
            .avg("responseTimeMs").as("avgResponseTimeMs")
            .and("p95", AccumulatorOperators.Percentile.percentileOf("responseTimeMs").percentages(0.95));

        // PROJECT rename _id to endpoint, round the mean, extract p95 from its array
        ProjectionOperation flatten = Aggregation.project("callCount")
            .and("_id").as("endpoint")
            .and(ArithmeticOperators.Round.roundValueOf("avgResponseTimeMs").place(1)).as("avgResponseTimeMs")
            .and(ArrayOperators.ArrayElemAt.arrayOf("p95").elementAt(0)).as("p95ResponseTimeMs")
            .andExclude("_id");

        // Order endpoints by alphabetical order 
        SortOperation sortByEndpoint = Aggregation.sort(Sort.by("endpoint"));

        Aggregation aggregation = Aggregation.newAggregation(matchApiCalls, groupByEndpoint, flatten, sortByEndpoint);
        return mongoTemplate.aggregate(aggregation, "events", EndpointResponseTime.class).getMappedResults();
    }

}
