package org.demo.bookingsystem.enums;

public enum Status {
    // General Statuses
    ACTIVE,            // For active entities
    INACTIVE,          // For inactive entities
    PENDING,           // For entities awaiting approval or processing
    COMPLETED,         // For completed actions or processes
    CANCELLED,         // For cancelled actions or processes
    FAILED,            // For failed processes
    SUCCESS,           // For successful processes
    IN_PROGRESS,       // Action is currently being processed
    EXPIRED,           // For expired subscriptions or trials
    ON_HOLD,           // For entities temporarily suspended or held
}