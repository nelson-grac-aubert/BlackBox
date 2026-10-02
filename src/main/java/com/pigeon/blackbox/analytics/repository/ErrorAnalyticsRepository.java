package com.pigeon.blackbox.analytics.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;

import com.pigeon.blackbox.analytics.dto.ErrorCountByDay;
import com.pigeon.blackbox.domain.enums.EventType;

@Repository
public class ErrorAnalyticsRepository {

    private final MongoTemplate mongoTemplate;

    public ErrorAnalyticsRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ErrorCountByDay> countErrorsByDayAndType(Instant from, Instant to) {

        // MATCH only errors in the time period
        MatchOperation matchErrors = Aggregation.match(Criteria.where("eventType").is(EventType.ERROR.name())
        .and("timestamp").gte(from).lt(to));

        // PROJECT prepare the field "day"
        ProjectionOperation projectDay = Aggregation.project("errorType")
        .and(DateOperators.dateOf("timestamp")
                .withTimezone(DateOperators.Timezone.valueOf("Europe/Paris"))
                .toString("%Y-%m-%d"))
                .as("day");

        // GROUP regroup and count 
        GroupOperation groupByDayAndType = Aggregation.group("day", "errorType").count().as("count");

        // PROJECT put all fields on the same level
        ProjectionOperation flatten = Aggregation.project("day", "errorType", "count").andExclude("_id");

        // SORT dates chronologically, and then error types alphabetically 
        SortOperation sortByDayAndType = Aggregation.sort(Sort.by("day", "errorType"));

        // Chain all stages and return the query result
        Aggregation aggregation = Aggregation.newAggregation(matchErrors, projectDay, groupByDayAndType, flatten, sortByDayAndType);
        return mongoTemplate.aggregate(aggregation, "events", ErrorCountByDay.class).getMappedResults();

    }

}
