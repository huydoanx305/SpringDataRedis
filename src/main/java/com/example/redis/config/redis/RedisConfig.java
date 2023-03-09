package com.example.redis.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class RedisConfig {

  @Value("${redis.config.type}")
  private RedisType type;

  @Value("${redis.config.host}")
  private String host;

  @Value("${redis.config.port}")
  private int port;

  @Value("${redis.config.sentinel.master}")
  private String sentinelMaster;

  @Value("${redis.config.sentinel.nodes}")
  private String sentinelNodes; // host and port

  @Value("${redis.config.cluster.nodes}")
  private String clusterNodes; // host and port

  @Bean(name = "jedisConnectionFactory")
  public JedisConnectionFactory jedisConnectionFactory() {
    if (type.equals(RedisType.STANDALONE)) {
      RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(host, port);
      return new JedisConnectionFactory(standaloneConfiguration);
    } else if (type.equals(RedisType.SENTINEL)) {
      RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
      sentinelConfig.setMaster(sentinelMaster);
      sentinelConfig.setSentinels(Arrays.stream(sentinelNodes.split(";"))
              .map(s -> new RedisNode(s.split(":")[0], Integer.parseInt(s.split(":")[1])))
              .collect(Collectors.toSet()));
      return new JedisConnectionFactory(sentinelConfig, poolConfig());
    } else if (type.equals(RedisType.CLUSTER)) {
      RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(
          Arrays.asList(clusterNodes.split(";"))
      );
      return new JedisConnectionFactory(clusterConfig, poolConfig());
    } else {
      throw new RuntimeException("Please config Redis Type");
    }
  }

  @Bean
  public JedisPoolConfig poolConfig() {
    final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setTestOnBorrow(true);
    jedisPoolConfig.setMaxTotal(100);
    jedisPoolConfig.setMaxIdle(100);
    jedisPoolConfig.setMinIdle(5);
    jedisPoolConfig.setTestOnReturn(true);
    jedisPoolConfig.setTestWhileIdle(true);
    return jedisPoolConfig;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.activateDefaultTyping(new LaissezFaireSubTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

}
