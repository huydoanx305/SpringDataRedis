package com.example.redis.config.redis;

public enum RedisType {
    CLUSTER,
    SENTINEL,
    STANDALONE;

    private RedisType() {
    }
}
