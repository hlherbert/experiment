//package com.hl.experiment.security.xss;
//
//import com.alibaba.fastjson.JSON;
//import com.cecdsc.platform.common.ResponseData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//
///**
// * Xss拦截器，拦截所有http请求，检查请求参数里是否有xss风险
// * @deprecated 因为读取body调用request.getReader()后， 无法再次被后面的处理再次读取body，因此该类不再使用，改为使用XssFilter，可以通过包装器来重写request。
// */
//@Deprecated
//public class XssInterceptor implements HandlerInterceptor {
//
//    private static final Logger logger = LoggerFactory.getLogger(XssInterceptor.class);
//
//    @Autowired
//    private XssConfig xssConfig;
//
//    /**
//     * 普通检查器
//     */
//    private XssDetector xssDetector = XssDetector.getNormalDetector();
//
//    /**
//     * 格式化空字符串
//     */
//    private static String formatNull(Object str) {
//        return null == str || "null".equals(str) ? "" : str.toString();
//    }
//
//    /**
//     * 获取请求body文本
//     */
//    private static String getBodyTxt(HttpServletRequest request) throws IOException {
//        BufferedReader br = request.getReader();
//        String str, wholeStr = "";
//
//        while ((str = br.readLine()) != null) {
//            wholeStr += str;
//        }
//        return wholeStr;
//    }
//
//    /**
//     * 拦截所有请求，并检查请求参数和请求体是否含有XSS风险
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (!xssConfig.getEnabled()) {
//            return true;
//        }
//        String queryString = formatNull(request.getQueryString());
//        if (xssConfig.getCheckParam() && !queryString.isEmpty()) {
//            queryString = java.net.URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());
//            // 请求参数都是简单文本
//            if (!xssDetector.passXssCheck(queryString)) {
//                logger.error("refuse request xss: [param] ip={}, url={}, params={}",
//                        request.getRemoteAddr(), request.getRequestURL(),
//                        queryString);
//                response.getWriter().print(JSON.toJSONString(new ResponseData("request params not pass xss check", false)));
//                return false;
//            }
//        }
//
//        // 请求体可能提交富文本，用普通检查器
//        String contentType = request.getContentType();
//        if (xssConfig.getCheckBody()
//                && contentType != null
//                && (contentType.contains("plain/text") || contentType.contains("application/json"))
//                && request.getContentLength() > 0) {
//            String body = getBodyTxt(request);
//            if (body.isEmpty()) {
//                return true;
//            }
//            boolean pass = false;
//            if (contentType.contains("application/json")) {
//                pass = xssDetector.passXssCheckJson(body);
//            } else {
//                pass = xssDetector.passXssCheck(body);
//            }
//            if (!pass) {
//                logger.error("refuse xss attack: [body] ip={}, url={}, params={}, body={}",
//                        request.getRemoteAddr(), request.getRequestURL(), queryString, body);
//                response.getWriter().print(JSON.toJSONString(new ResponseData("request body not pass xss check", false)));
//                return false;
//            }
//
//        }
//        return true;
//    }
//}
