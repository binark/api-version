package com.binark.handler;

import com.binark.annotation.ApiVersion;
import com.binark.configuration.ApiVersionProperties;
import com.binark.configuration.ApiVersionProperties.Type;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

  private final ApiVersionProperties properties;
  private final String versionPrefix;

  public ApiVersionRequestMappingHandlerMapping(ApiVersionProperties properties,
      String versionPrefix) {
    this.properties = properties;
    this.versionPrefix = versionPrefix;
  }

  public ApiVersionRequestMappingHandlerMapping(ApiVersionProperties properties) {
    this(properties, "V");
  }

  @Override
  protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
    return createRequestCondition(handlerType);
  }

  @Override
  protected RequestCondition<?> getCustomMethodCondition(Method method) {
    return createRequestCondition(method);
  }

  private RequestCondition<ApiVersionRequestCondition> createRequestCondition(
      AnnotatedElement annotatedElement) {
    if (Type.URI.equals(properties.getType())) {
      return null;
    }
    ApiVersion annotation = AnnotationUtils.findAnnotation(annotatedElement, ApiVersion.class);
    if (annotation == null) {
      return null;
    }
    String[] values = annotation.value();
    return new ApiVersionRequestCondition(Arrays.stream(values).toList(), properties);
  }

  @Override
  protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
    RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
    if (info != null) {
      RequestMappingInfo typeInfo = this.createRequestMappingInfo(handlerType);
      if (typeInfo != null) {
        info.combine(typeInfo);
      }

      if (Type.URI.equals(properties.getType())) {
        ApiVersion annotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if (annotation == null) {
          annotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        }
        if (annotation != null) {
          String[] values = annotation.value();
          String uriPrefix =
              StringUtils.hasText(properties.getUriPrefix()) ? properties.getUriPrefix().trim()
                  : "";
          String[] patterns = new String[values.length];
          for (int i = 0; i < values.length; i++) {
            patterns[i] = uriPrefix + versionPrefix + values[i];
          }
          info = RequestMappingInfo.paths(patterns).options(getBuilderConfiguration()).build()
              .combine(info);
        }
      }
    }
    return info;
  }

  private RequestMappingInfo createRequestMappingInfo(AnnotatedElement annotatedElement) {
    RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(annotatedElement,
        RequestMapping.class);
    RequestCondition<?> condition = annotatedElement instanceof Class<?> ? getCustomTypeCondition(
        (Class<?>) annotatedElement) : getCustomMethodCondition((Method) annotatedElement);
    return requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null;
  }
}
