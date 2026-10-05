// Same pipeline as FunnelRepository, over the whole simulated year
const pipeline = [
  { $match: {
      timestamp: { $gte: ISODate("2025-04-01T00:00:00Z"), $lt: ISODate("2025-05-01T00:00:00Z") },
      $or: [
        { eventType: "NOTIFICATION", template: "PAYMENT_EXPIRED" },
        { eventType: "LOGIN" },
        { eventType: "PAYMENT", paymentStatus: "SUCCESS" }
      ]
  } },
  { $group: {
      _id: "$userId",
      notifiedAt: { $min: { $cond: [{ $eq: ["$eventType", "NOTIFICATION"] }, "$timestamp", null] } },
      loginDates: { $push: { $cond: [{ $eq: ["$eventType", "LOGIN"] }, "$timestamp", "$$REMOVE"] } },
      paymentDates: { $push: { $cond: [{ $eq: ["$eventType", "PAYMENT"] }, "$timestamp", "$$REMOVE"] } }
  } },
  { $addFields: {
      loggedInAt: { $cond: [{ $eq: ["$notifiedAt", null] }, null,
        { $min: { $filter: { input: "$loginDates", as: "date", cond: { $gt: ["$$date", "$notifiedAt"] } } } }] }
  } },
  { $addFields: {
      paidAt: { $cond: [{ $eq: ["$loggedInAt", null] }, null,
        { $min: { $filter: { input: "$paymentDates", as: "date", cond: { $gt: ["$$date", "$loggedInAt"] } } } }] }
  } },
  { $group: {
      _id: null,
      notifiedUsersCount: { $sum: { $cond: [{ $ne: ["$notifiedAt", null] }, 1, 0] } },
      loggedInUsersCount: { $sum: { $cond: [{ $ne: ["$loggedInAt", null] }, 1, 0] } },
      resubscribedUsersCount: { $sum: { $cond: [{ $ne: ["$paidAt", null] }, 1, 0] } }
  } }
];

// explain("executionStats"): the plan chosen by MongoDB, and what it really did
const explain = db.events.explain("executionStats").aggregate(pipeline);

// Depending on the plan, the stats are at the root or in the first stage
const query = explain.stages ? explain.stages[0]["$cursor"] : explain;
const stats = query.executionStats;
const plan = JSON.stringify(query.queryPlanner.winningPlan);

print("Plan used :", plan.includes("IXSCAN") ? "IXSCAN (index)" : "COLLSCAN (full scan)");
print("Documents examined :", stats.totalDocsExamined);
print("Index keys examined :", stats.totalKeysExamined);
print("Documents returned :", stats.nReturned);
print("Execution time (ms) :", stats.executionTimeMillis);
