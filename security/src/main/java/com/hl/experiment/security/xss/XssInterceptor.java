package com.hl.experiment.security.xss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class XssInterceptor implements HandlerInterceptor {

    private XssConfig xssConfig;

    public XssInterceptor() {

    }

    public XssInterceptor(@Autowired XssConfig xssConfig) {
        System.out.println("create xssINterCeptor(xxCOnfig");
        this.xssConfig = xssConfig;
    }

    /**
     * 功能：格式化空字符串
     *
     * @param str
     * @return String
     */
    private static String formatNull(Object str) {
        return null == str || "null".equals(str) ? "" : str.toString();
    }

    private static String getBodyTxt(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str, wholeStr = "";

        while ((str = br.readLine()) != null) {
            wholeStr += str;
        }
        return wholeStr;
    }

    //在业务处理之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!xssConfig.getEnabled()) {
            return true;
        }
        System.out.println("==进入xss过滤器===");
        String queryString = formatNull(request.getQueryString());
        queryString = java.net.URLDecoder.decode(queryString);

        if (XssDetector.detectXssProblem(queryString)) {
            System.out.println("检测到您发送请求中的参数中含有跨站脚本编制非法字符:" + HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        if (request.getContentType().contains("text/plain")) {
            String body = getBodyTxt(request);
            if (XssDetector.detectXssProblem(body)) {
                System.out.println("检测到您发送请求体中含有跨站脚本编制非法字符:" + HttpServletResponse.SC_BAD_REQUEST);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }
        }
        return true;
    }

    private String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }

}
