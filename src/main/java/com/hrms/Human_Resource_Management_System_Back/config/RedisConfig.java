package com.hrms.Human_Resource_Management_System_Back.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Configuration class for setting up Redis caching in the application.
 * <p>
 * Enables Spring's annotation-driven cache management capability using Redis
 * as the underlying cache provider.
 * </p>
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Configures and returns a {@link RedisCacheManager} for managing Redis-based caches.
     * <p>
     * The cache manager uses a {@link GenericJackson2JsonRedisSerializer} to serialize
     * cache values into JSON format, which ensures compatibility with various object types.
     * </p>
     *
     * @param connectionFactory the factory to establish Redis connections
     * @return a configured {@link RedisCacheManager} instance
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RedisSerializationContext.SerializationPair<Object> valueSerializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(valueSerializationPair);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
