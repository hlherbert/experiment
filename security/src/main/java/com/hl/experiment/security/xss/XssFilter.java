package com.hl.experiment.security.xss;

import com.alibaba.fastjson.JSON;
import com.cecdsc.platform.common.ResponseData;
import org.apache.commons.lang.StringUtils;
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
import java.util.regex.Pattern;

/**
 * xss过滤器
 * 请在项目启动类加上注解支持自动扫描本包: @ServletComponentScan(basePackages = {"com.cecdsc.platform.common.security"})
 */
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
     *
     * @return 如果请求通过XSS检测，则返回true, 否则返回false
     */
    private boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    /**
     * 将url通配符表达式转化为正则表达式
     * 支持用多个逗号分开的url
     *
     * @param multipleUrl 多个url通配符, 如 /abc/* . 注意是spring的url模式，例如/*表示只匹配当前级别, /**表示匹配多级.
     * @return url模式对应的正则表达式
     */
    private static String getMultipleUrlRegexPattern(String multipleUrl) {
        if (multipleUrl == null) {
            return "";
        }
        String[] urls = multipleUrl.split(",");
        if (urls.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            String urlPattern = getUrlRegexPattern(url.trim());
            sb.append("(" + urlPattern + ")|");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 将url通配符表达式转化为正则表达式
     * 只支持单个url
     *
     * @param urlPattern url模式, 如 /abc/* . 注意是spring的url模式，例如/*表示只匹配当前级别, /**表示匹配多级,
     * @return url模式对应的正则表达式
     */
    private static String getUrlRegexPattern(String urlPattern) {
        char[] chars = urlPattern.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {//遇到*字符
                if (preX) {//如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {//如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");
                } else {//否则单星后面还有字符，则不做任何动作，下一把再做动作
                    preX = true;
                    continue;
                }
            } else {//遇到非*字符
                if (preX) {//如果上一把是*，则先把上一把的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {//接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {//不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 验证url是否符合通配符模式
     *
     * @param urlPattern - 白名单地址
     * @param url        - 请求地址
     * @return 是否匹配url模式
     */
    private static boolean matchUrlPattern(String urlPattern, String url) {
        String regPath = getMultipleUrlRegexPattern(urlPattern);
        return Pattern.compile(regPath).matcher(url).matches();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init xssFilter: xssConfig:\n\tenabled:{}\n\turlPattern:{}\n\texcludes:{}\n\tcheckParam:{}\n\tcheckBody:{}\n", xssConfig.getEnabled(),
                xssConfig.getUrlPatterns(), xssConfig.getExcludes(), xssConfig.getCheckParam(), xssConfig.getCheckBody());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!xssConfig.getEnabled()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (servletRequest instanceof HttpServletRequest) {
            String requestPath = ((HttpServletRequest) servletRequest).getServletPath();
            if (StringUtils.isEmpty(xssConfig.getUrlPatterns()) || !matchUrlPattern(xssConfig.getUrlPatterns(), requestPath)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if (StringUtils.isNotEmpty(xssConfig.getExcludes()) && matchUrlPattern(xssConfig.getExcludes(), requestPath)) {
                logger.info("requestPath is in the excludes of xssFilter: requestPath={}, excludes={}", requestPath, xssConfig.getExcludes());
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
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
