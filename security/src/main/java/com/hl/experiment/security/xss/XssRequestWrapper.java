package com.hl.experiment.security.xss;//package com.hl.experiment.security.xss;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    private static XssDetector xssDetector = XssDetector.getNormalDetector();

    private final byte[] body;

    public XssRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = toByteArray(request.getInputStream());
    }

    @Override
    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (str == null)
            return null;
        return xssClean(str);
    }

    @Override
    public String getHeader(String paramString) {
        String str = super.getHeader(paramString);
        if (str == null)
            return null;
        return xssClean(str);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> request_map = super.getParameterMap();
        Iterator iterator = request_map.entrySet().iterator();
        System.out.println("request_map" + request_map.size());
        while (iterator.hasNext()) {
            Map.Entry me = (Map.Entry) iterator.next();
            String[] values = (String[]) me.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssClean(values[i]);
            }
        }
        return request_map;
    }

    @Override
    public String[] getParameterValues(String paramString) {
        String[] arrayOfString1 = super.getParameterValues(paramString);
        if (arrayOfString1 == null)
            return null;
        int i = arrayOfString1.length;
        String[] arrayOfString2 = new String[i];
        for (int j = 0; j < i; j++)
            arrayOfString2[j] = xssClean(arrayOfString1[j]);
        return arrayOfString2;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            boolean ready = true;
            boolean finished = false;

            @Override
            public boolean isFinished() {
                return finished;
            }

            @Override
            public boolean isReady() {
                return ready;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                int b = bais.read();
                if (b == -1) {
                    finished = true;
                    ready = false;
                }
                return b;
            }
        };
    }

    private byte[] toByteArray(ServletInputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    private String xssClean(String value) {
        return xssDetector.xssClean(value);
    }
}
