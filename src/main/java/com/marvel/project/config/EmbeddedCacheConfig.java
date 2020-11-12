package com.marvel.project.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
class EmbeddedCacheConfig {

  @Bean
  Config config() {
    Config config = new Config();

    MapConfig mapConfig = new MapConfig();
    // Change the time accordingly
    mapConfig.setTimeToLiveSeconds(300);
    config.getMapConfigs().put("charactersIdsList", mapConfig);

    return config;
  }

}