package com.binark.configuration;

import com.binark.handler.ApiVersionRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class ApiVersionWebMvcRegistrations implements WebMvcRegistrations {

  private final ApiVersionProperties properties;

  public ApiVersionWebMvcRegistrations(ApiVersionProperties properties) {
    this.properties = properties;
  }

  @Override
  public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
    return new ApiVersionRequestMappingHandlerMapping(properties);
  }
}
