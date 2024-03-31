package com.binark.annotation;

import com.binark.configuration.ApiVersionAutoConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ApiVersionAutoConfiguration.class)
public @interface EnableApiVersioning {

}
