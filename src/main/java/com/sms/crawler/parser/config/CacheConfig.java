package com.sms.crawler.parser.config;

import com.sms.crawler.parser.model.MergeResponse;
import com.sms.crawler.parser.util.CustomKeyGenerator;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public JCacheCacheManager jCacheCacheManager(CacheManager cacheManager) {
        return new JCacheCacheManager(cacheManager);
    }

    @Bean(destroyMethod = "close")
    public CacheManager cacheManager(CacheConfiguration<String, MergeResponse> cacheConfiguration) {
        Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();

        caches.put("crawlResult", cacheConfiguration);

        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching
                .getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");

        String tempFolder = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempFolder);

        DefaultConfiguration configuration = new DefaultConfiguration(caches, provider.getDefaultClassLoader(),
                new DefaultPersistenceConfiguration(tempFile));

        return provider.getCacheManager(provider.getDefaultURI(), configuration);
    }

    @Bean
    public CacheConfiguration<String, MergeResponse> cacheConfiguration() {
        return CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, MergeResponse.class,
                        ResourcePoolsBuilder
                                .newResourcePoolsBuilder().offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(60)))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(300)))
                .withSizeOfMaxObjectSize(100, MemoryUnit.MB)
                .withSizeOfMaxObjectGraph(Integer.MAX_VALUE)
                .build();
    }

    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}
