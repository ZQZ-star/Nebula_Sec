package com.nebula.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {
    @Bean
    public RBloomFilter<String> assetBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("asset_bloom_filter");
        // 初始化：预期插入10万数据，误判率0.01
        bloomFilter.tryInit(100000L, 0.01);
        return bloomFilter;
    }
}
