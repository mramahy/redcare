package com.redcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig is a configuration class that defines beans for the application context. It
 * provides a RestTemplate bean for making HTTP requests.
 */
@Configuration
public class RestTemplateConfig {

  /**
   * Creates and configures a RestTemplate bean.
   *
   * @return a new instance of RestTemplate
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}