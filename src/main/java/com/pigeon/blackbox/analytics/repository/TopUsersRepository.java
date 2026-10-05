package com.pigeon.blackbox.analytics.repository;

import com.pigeon.blackbox.analytics.dto.TopUsers;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class TopUsersRepository {
    private static final long TOP_SIZE = 10 ;
    private final MongoTemplate mongoTemplate;

    public TopUsersRepository(MongoTemplate  mongoTemplate){
         this.mongoTemplate = mongoTemplate;

    }

    public List<TopUsers> topUsers(Instant from, Instant to){

        // MATCH all events in the time period, whatever their type
        MatchOperation matchPeriod = Aggregation.match(Criteria.where("timestamp").gte(from).lt(to));

        // GROUP events by user and count them, the userId becomes the _id of each group
        GroupOperation groupByUser = Aggregation.group("userId").count().as("eventCount");

        // SORT most active users first, then by lowest id to keep ties in a stable order
        SortOperation sortByActivity = Aggregation.sort(Sort.by(Sort.Direction.DESC, "eventCount")
                .and(Sort.by(Sort.Direction.ASC, "_id")));

        // LIMIT to the top users, before the lookup so only TOP_SIZE joins are made
        LimitOperation keepTopUsers = Aggregation.limit(TOP_SIZE);

        // LOOKUP the matching user in the users collection, result stored in a "user" array
        LookupOperation joinUser = Aggregation.lookup("users", "_id", "_id", "user");

        // UNWIND the one-element "user" array into a plain sub-document
        UnwindOperation unwindUser = Aggregation.unwind("user");

        // PROJECT keep the count, rename _id to userId, lift the names out of "user"
        ProjectionOperation flatten = Aggregation.project("eventCount")
                .and("_id").as("userId")
                .and("user.firstName").as("firstName")
                .and("user.lastName").as("lastName")
                .andExclude("_id");

        // Chain all stages and return the query result
        Aggregation aggregation = Aggregation.newAggregation(matchPeriod, groupByUser, sortByActivity, keepTopUsers, joinUser, unwindUser, flatten);
        return mongoTemplate.aggregate(aggregation, "events", TopUsers.class).getMappedResults();

        }
    }

