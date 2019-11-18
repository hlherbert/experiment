package com.hl.experiment.security.xss;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

public class XssDetector {
    private static Policy policy = null;
    private static AntiSamy antiSamy = new AntiSamy();

    static {
        String path = XssRequestWrapper.class.getClassLoader().getResource("antisamy-anythinggoes.xml").getFile();
        System.out.println("policy_filepath:" + path);
        if (path.startsWith("file")) {
            path = path.substring(6);
        }
        try {
            policy = Policy.getInstance(path);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    public static String xssClean(String html) {
        try {
            final CleanResults cr = antiSamy.scan(html, policy);
            // 安全的HTML输出
            return cr.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static boolean detectXssProblem(String html) {
        try {
            final CleanResults cr = antiSamy.scan(html, policy);
            // 安全的HTML输出
            if (cr.getNumberOfErrors() == 0) {
                return true;
            }
        } catch (ScanException e) {
            e.printStackTrace();
            return false;
        } catch (PolicyException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
