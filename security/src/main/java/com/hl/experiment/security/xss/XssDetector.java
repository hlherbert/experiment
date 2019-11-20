package com.hl.experiment.security.xss;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class XssDetector {

    private static final Logger logger = LoggerFactory.getLogger(XssDetector.class);

    private static final String DEFAULT_POLICY = "/antisamy/antisamy.xml";

    /**
     * 严格检查器.
     * 不允许script iframe img video等，允许p a
     */
    private static final XssDetector STRICT_DETECTOR;

    /**
     * 普通检查器
     * 不允许script iframe. 允许img, video, p, a等大部分Html标签， 但不允许其中带有XSS攻击模式
     */
    private static final XssDetector NORMAL_DETECTOR;

    static {
        STRICT_DETECTOR = new XssDetector("/antisamy/antisamy-tinymce.xml");
        NORMAL_DETECTOR = new XssDetector("/antisamy/antisamy.xml");
    }

    private AntiSamy antiSamy = null;

    public XssDetector(String policyPath) {
        try {
            URL policyUrl = XssDetector.class.getResource(policyPath);
            if (policyUrl == null) {
                policyUrl = XssDetector.class.getResource(DEFAULT_POLICY);
            }
            final Policy policy = Policy.getInstance(policyUrl);
            antiSamy = new AntiSamy(policy);
        } catch (PolicyException e) {
            logger.error("policy read error.", e);
        }
    }

    public static XssDetector getStrictDetector() {
        return STRICT_DETECTOR;
    }

    public static XssDetector getNormalDetector() {
        return NORMAL_DETECTOR;
    }

    /**
     * 对html进行过滤，清理其中含有xss风险的文本
     *
     * @param html 原html
     * @return 清理后的html
     */
    public String xssClean(String html) {
        if (antiSamy == null) {
            return html;
        }
        try {
            final CleanResults cr = antiSamy.scan(html);
            // 安全的HTML输出
            return cr.getCleanHTML();
        } catch (ScanException e) {
            logger.error("scan error.", e);
            throw new RuntimeException("scan exception");
        } catch (PolicyException e) {
            logger.error("policy read error.", e);
            throw new RuntimeException("policy exception");
        }
    }

    /**
     * 检查html是否含有xss风险
     *
     * @param html 受检的html
     * @return true:通过检查; false:未通过检查,含有xss风险
     */
    public boolean passXssCheck(String html) {
        if (antiSamy == null) {
            return true;
        }
        try {
            final CleanResults cr = antiSamy.scan(html);
            // 安全的HTML输出
            if (cr.getNumberOfErrors() == 0) {
                return true;
            }
        } catch (ScanException e) {
            logger.error("scan error.",e);
            return false;
        } catch (PolicyException e) {
            logger.error("policy read error.",e);
            return false;
        }
        return false;
    }
}
