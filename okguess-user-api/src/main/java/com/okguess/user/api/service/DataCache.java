package com.okguess.user.api.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;


public class DataCache {


    private final static Cache<String, Object> cache = CacheBuilder.newBuilder()
            .initialCapacity(100000)
            .concurrencyLevel(50)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();


    private final static Cache<String, Object> oneMinCache = CacheBuilder.newBuilder()
            .initialCapacity(100000)
            .concurrencyLevel(50)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private final static Cache<String, Object> oneDayCache = CacheBuilder.newBuilder()
            .initialCapacity(100000)
            .concurrencyLevel(50)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    private final static Cache<String, Object> userCache = CacheBuilder.newBuilder()
            .initialCapacity(100000)
            .concurrencyLevel(50)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build();


    private DataCache() {

    }

    public static Cache<String, Object> getTenMinCache() {
        return cache;
    }

    public static Cache<String, Object> getOneDayCache() {
        return oneDayCache;
    }

    public static Cache<String, Object> getOneMinCache() {
        return oneMinCache;
    }

    public static Cache<String, Object> getUserCache() {
        return userCache;
    }

}