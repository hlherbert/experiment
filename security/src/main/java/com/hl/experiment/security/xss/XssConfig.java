package com.hl.experiment.security.xss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"/xss.properties"})
@Configuration
public class XssConfig {

    @Value("${xss.enabled}")
    private boolean enabled;

    @Value("${xss.excludes}")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;

    @Value("${xss.checkParam}")
    private boolean checkParam;

    @Value("${xss.checkBody}")
    private boolean checkBody;

    public boolean getEnabled() {
        return enabled;
    }

    public String getExcludes() {
        return excludes;
    }

    public String getUrlPatterns() {
        return urlPatterns;
    }

    public boolean getCheckParam() {
        return checkParam;
    }

    public boolean getCheckBody() {
        return checkBody;
    }
}
