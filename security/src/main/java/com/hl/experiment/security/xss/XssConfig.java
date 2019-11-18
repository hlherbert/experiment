package com.hl.experiment.security.xss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XssConfig {

    @Value("${xss.enabled}")
    private boolean enabled;

    @Value("${xss.excludes}")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;

    public boolean getEnabled() {
        return enabled;
    }

    public String getExcludes() {
        return excludes;
    }

    public String getUrlPatterns() {
        return urlPatterns;
    }
}
