package com.binark.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiVersionProperties.class)
public class ApiVersionAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ApiVersionWebMvcRegistrations apiVersionWebMvcRegistrations(
      ApiVersionProperties properties) {
    return new ApiVersionWebMvcRegistrations(properties);
  }
}
