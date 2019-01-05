package com.boot.ignite.bootignite.config;


import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class IgniteCacheConfig {


    @Profile("dev")
    @Bean(name = "bootIgniteCacheManager")
    public SpringCacheManager cacheManager() {
        SpringCacheManager springCacheManager = new SpringCacheManager();
        return bootIgniteCacheManager(springCacheManager,igniteDevConfiguration());
    }

    public IgniteConfiguration igniteDevConfiguration() {
        Ignition.setClientMode(true);
        IgniteConfiguration igniteCfg = new IgniteConfiguration();
        igniteCfg.setIgniteInstanceName("boot-ignite");
        igniteCfg.setWorkDirectory(System.getenv("IGNITE_HOME"));
        igniteCfg.setMetricsLogFrequency(0);

        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        spi.setIpFinder(ipFinder);
        igniteCfg.setDiscoverySpi(spi);
        return igniteCfg;
    }

    @NotNull
    private SpringCacheManager bootIgniteCacheManager(SpringCacheManager cacheManager, IgniteConfiguration igniteCfg) {
        CacheConfiguration<Object, Object> bootIgniteCacheCfg = new CacheConfiguration();
        bootIgniteCacheCfg.setName("boot_ignite_cache");
        bootIgniteCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        bootIgniteCacheCfg.setBackups(0);
        bootIgniteCacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        bootIgniteCacheCfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 5)));
        igniteCfg.setCacheConfiguration(bootIgniteCacheCfg);
        cacheManager.setConfiguration(igniteCfg);
        cacheManager.setDynamicCacheConfiguration(bootIgniteCacheCfg);
        cacheManager.setIgniteInstanceName("boot-ignite");
        return cacheManager;
    }
}
