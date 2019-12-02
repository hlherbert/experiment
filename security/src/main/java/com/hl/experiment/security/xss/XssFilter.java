package com.hl.experiment.security.xss;

import com.alibaba.fastjson.JSON;
import com.cecdsc.platform.common.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XssFilter.class);

    @Autowired
    private XssConfig xssConfig;

    /**
     * 普通检查器
     */
    private XssDetector xssDetector = XssDetector.getNormalDetector();

    /**
     * 格式化空字符串
     */
    private static String formatNull(Object str) {
        return null == str || "null".equals(str) ? "" : str.toString();
    }

    /**
     * 获取请求body文本
     */
    private static String getBodyTxt(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str, wholeStr = "";

        while ((str = br.readLine()) != null) {
            wholeStr += str;
        }
        return wholeStr;
    }

    /**
     * 拦截所有请求，并检查请求参数和请求体是否含有XSS风险
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!xssConfig.getEnabled()) {
            return true;
        }
        String queryString = formatNull(request.getQueryString());
        if (xssConfig.getCheckParam() && !queryString.isEmpty()) {
            queryString = java.net.URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());
            // 请求参数都是简单文本
            if (!xssDetector.passXssCheck(queryString)) {
                logger.error("refuse request xss: [param] ip={}, url={}, params={}",
                        request.getRemoteAddr(), request.getRequestURL(),
                        queryString);
                response.getWriter().print(JSON.toJSONString(new ResponseData("request params not pass xss check", false)));
                return false;
            }
        }

        // 请求体可能提交富文本，用普通检查器
        String contentType = request.getContentType();
        if (xssConfig.getCheckBody()
                && contentType != null
                && (contentType.contains("plain/text") || contentType.contains("application/json"))
                && request.getContentLength() > 0) {
            String body = getBodyTxt(request);
            if (body.isEmpty()) {
                return true;
            }
            boolean pass = false;
            if (contentType.contains("application/json")) {
                pass = xssDetector.passXssCheckJson(body);
            } else {
                pass = xssDetector.passXssCheck(body);
            }
            if (!pass) {
                logger.error("refuse xss attack: [body] ip={}, url={}, params={}, body={}",
                        request.getRemoteAddr(), request.getRequestURL(), queryString, body);
                response.getWriter().print(JSON.toJSONString(new ResponseData("request body not pass xss check", false)));
                return false;
            }

        }
        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest newRequest = new XssRequestWrapper((HttpServletRequest) servletRequest);
            if (!preHandle(newRequest, (HttpServletResponse) servletResponse)) {
                return;
            }
            filterChain.doFilter(new XssRequestWrapper(newRequest), servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
