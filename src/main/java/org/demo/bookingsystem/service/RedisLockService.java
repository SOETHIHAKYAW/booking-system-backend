package org.demo.bookingsystem.service;

public interface RedisLockService {
    /**
     * Try to acquire a distributed lock with a given key and expiration time in seconds.
     *
     * @param key           Lock key
     * @param expireSeconds Expiration time in seconds
     * @return true if lock acquired, false otherwise
     */
    boolean acquireLock(String key, int expireSeconds);

    /**
     * Release the distributed lock for the given key.
     *
     * @param key Lock key to release
     */
    void releaseLock(String key);
}