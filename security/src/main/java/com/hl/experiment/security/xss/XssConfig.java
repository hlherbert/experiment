package com.hl.experiment.security.xss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * xss检查配置，可以在nacos的common.properties中或者服务工程的application.yml, application.xml中定义这些配置项
 * 如果未定义采用默认值
 */
@Configuration
public class XssConfig {

    /**
     * 是否开启XSS检查
     */
    @Value("${security.xss.enabled:true}")
    private boolean enabled = true;

    /**
     * 需要检查的URL模式，允许多个用逗号分隔。例如/**
     * 默认: /**
     */
    @Value("${security.xss.urlPatterns:/**}")
    private String urlPatterns = "/**";

    /**
     * 不检查的URL模式，允许多个用逗号分隔
     * 默认: 空
     * 例如 /abc/**
     */
    @Value("${security.xss.excludes:}")
    private String excludes = "";

    /**
     * 是否对请求参数进行检查
     * 默认开启
     */
    @Value("${security.xss.checkParam:true}")
    private boolean checkParam = true;

    /**
     * 是否对请求体进行检查
     * 默认开启
     * 开启后, 只对content-type = application/json, plain/text的请求体检查. 不对字节流型请求体检查
     */
    @Value("${security.xss.checkBody:true}")
    private boolean checkBody = true;

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
