package com.hl.experiment.security.xss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
public class XssInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(XssInterceptor.class);

    @Autowired
    private XssConfig xssConfig;

    /**
     * 严格检查器
     */
    private XssDetector xssStrictDetector = XssDetector.getStrictDetector();

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!xssConfig.getEnabled()) {
            return true;
        }
        String queryString = formatNull(request.getQueryString());
        if (xssConfig.getCheckParam() && !queryString.isEmpty()) {
            queryString = java.net.URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());

            // 请求参数都是简单文本，用严格检查器
            if (!xssStrictDetector.passXssCheck(queryString)) {
                logger.error("refuse request xss: [param] ip={}, url={}, params={}",
                        request.getRemoteAddr(), request.getRequestURL(),
                        queryString);
                response.getWriter().println("request params not pass xss check");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }
        }


        // 请求体可能提交富文本，用普通检查器
        if (xssConfig.getCheckBody()
                && request.getContentType() != null
                && request.getContentType().contains("text/plain")
                && request.getContentLength() > 0) {
            String body = getBodyTxt(request);
            if (!body.isEmpty() && !xssDetector.passXssCheck(body)) {
                logger.error("refuse xss attack: [body] ip={}, url={}, params={}, body={}",
                        request.getRemoteAddr(), request.getRequestURL(), queryString, body);
                response.getWriter().println("request body not pass xss check");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }
        }
        return true;
    }
}
