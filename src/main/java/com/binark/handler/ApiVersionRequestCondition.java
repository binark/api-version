package com.binark.handler;

import com.binark.configuration.ApiVersionProperties;
import com.binark.configuration.ApiVersionProperties.Type;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

public class ApiVersionRequestCondition implements RequestCondition<ApiVersionRequestCondition> {

  private final Set<String> apiVersions;
  private final ApiVersionProperties properties;

  public ApiVersionRequestCondition(@Nonnull Collection<String> apiVersions,
      @Nonnull ApiVersionProperties properties) {
    this.apiVersions = new HashSet<>(apiVersions);
    this.properties = properties;
  }

  @Override
  public ApiVersionRequestCondition combine(ApiVersionRequestCondition other) {
    return new ApiVersionRequestCondition(other.getApiVersions(), other.getProperties());
  }

  @Override
  public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
    Type type = properties.getType();
    String version = null;
    switch (type) {
      case HEADER -> {
        version = request.getHeader(properties.getHeader());
      }
      case PARAM -> {
        version = request.getParameter(properties.getParam());
      }
    }
    return StringUtils.hasText(version) && apiVersions.contains(version.trim()) ? this : null;
  }

  @Override
  public int compareTo(ApiVersionRequestCondition other, HttpServletRequest request) {
    return getApiVersions().containsAll(other.getApiVersions()) && other.getApiVersions()
        .containsAll(getApiVersions()) ? 1 : 0;
  }

  public Set<String> getApiVersions() {
    return apiVersions;
  }

  public ApiVersionProperties getProperties() {
    return properties;
  }
}
