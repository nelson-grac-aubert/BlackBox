package com.pigeon.blackbox.analytics.repository;

import java.time.Instant;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.pigeon.blackbox.analytics.dto.UserCountByFunnelStep;
import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.NotificationTemplate;
import com.pigeon.blackbox.domain.enums.PaymentStatus;

@Repository
public class FunnelRepository {

    private final MongoTemplate mongoTemplate;
   
    public FunnelRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public UserCountByFunnelStep findUserCountsByFunnelStep(Instant from, Instant to) {
        
        // MATCH the 3 kinds of funnel events in the time period
        MatchOperation matchFunnelEvents = Aggregation.match(new Criteria().andOperator(
        Criteria.where("timestamp").gte(from).lt(to),
        new Criteria().orOperator(
            Criteria.where("eventType").is(EventType.NOTIFICATION.name()).and("template").is(NotificationTemplate.PAYMENT_EXPIRED.name()),
            Criteria.where("eventType").is(EventType.LOGIN.name()),
            Criteria.where("eventType").is(EventType.PAYMENT.name()).and("paymentStatus").is(PaymentStatus.SUCCESS.name()))));
        
        // GROUP by user: first notification date, all login and payment dates
        AggregationOperation groupByUser = context -> Document.parse("""
            { $group: {
                _id: "$userId",
                notifiedAt: { $min: { $cond: [{ $eq: ["$eventType", "NOTIFICATION"] }, "$timestamp", null] } },
                loginDates: { $push: { $cond: [{ $eq: ["$eventType", "LOGIN"] }, "$timestamp", "$$REMOVE"] } },
                paymentDates: { $push: { $cond: [{ $eq: ["$eventType", "PAYMENT"] }, "$timestamp", "$$REMOVE"] } }
            } }
            """);

        // ADD FIELDS first login after the notification, null if not notified
        AggregationOperation addLoggedInAt = context -> Document.parse("""
            { $addFields: {
                loggedInAt: { $cond: [
                    { $eq: ["$notifiedAt", null] }, null,
                    { $min: { $filter: { input: "$loginDates", as: "date", cond: { $gt: ["$$date", "$notifiedAt"] } } } }
                ] }
            } }
            """);
        
        // ADD FIELDS first payment after that login, null if never came back
        AggregationOperation addPaidAt = context -> Document.parse("""
            { $addFields: {
                paidAt: { $cond: [
                    { $eq: ["$loggedInAt", null] }, null, 
                    { $min: { $filter: { input : "$paymentDates", as: "date", cond: {$gt: ["$$date", "$loggedInAt"] } } } }
                ] }
            } }
            """);
        
        // GROUP everyone together: number of users who reached each step
        AggregationOperation countUsersByStep = context -> Document.parse("""
            { $group: {
                _id: null,
                notifiedUsersCount: { $sum: { $cond: [{ $ne: ["$notifiedAt", null] }, 1, 0] } },
                loggedInUsersCount: { $sum: { $cond: [{ $ne: ["$loggedInAt", null] }, 1, 0] } },
                resubscribedUsersCount: { $sum: { $cond: [{ $ne: ["$paidAt", null] }, 1, 0] } }
            } }
            """);
        
        // PROJECT drop the technical _id
        ProjectionOperation removeId = Aggregation.project("notifiedUsersCount", "loggedInUsersCount", "resubscribedUsersCount")
            .andExclude("_id");

        // Chain all stages and return the single result (null if no event in the period)
        Aggregation aggregation = Aggregation.newAggregation(matchFunnelEvents, groupByUser, addLoggedInAt, addPaidAt, countUsersByStep, removeId);
        return mongoTemplate.aggregate(aggregation, "events", UserCountByFunnelStep.class).getUniqueMappedResult();

    }
}  
