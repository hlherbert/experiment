package com.hl.experiment.security.xss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Xss检查器
 */
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

    private static final int REGEX_FLAGS_SI = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

    // XSS 敏感模式
    private static final Pattern P_SCRIPT = Pattern.compile("<.*\\s+script", REGEX_FLAGS_SI);
    private static final Pattern P_JAVASCRIPT = Pattern.compile("<.*\\s+javascript:", REGEX_FLAGS_SI);
    private static final Pattern P_ONEVENT = Pattern.compile("<.*\\s+on.+=", REGEX_FLAGS_SI);
    private static final Pattern P_IFRAME = Pattern.compile("<.*\\s+iframe", REGEX_FLAGS_SI);
    private static final Pattern P_OBJECT = Pattern.compile("<.*\\s+object", REGEX_FLAGS_SI);

    private static final Pattern[] DANGER_PATTERNS = new Pattern[]{P_SCRIPT, P_JAVASCRIPT, P_ONEVENT, P_IFRAME, P_OBJECT};

    static {
        STRICT_DETECTOR = new XssDetector("/antisamy/antisamy-tinymce.xml");
        NORMAL_DETECTOR = new XssDetector("/antisamy/antisamy.xml");
    }

    private AntiSamy antiSamy = null;

    protected XssDetector(String policyPath) {
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
            return html;
        } catch (PolicyException e) {
            logger.error("policy read error.", e);
            return html;
        }
    }

    /**
     * 检查html是否含有xss风险
     *
     * @param html 受检的html
     * @return true:通过检查; false:未通过检查,含有xss风险
     */
    public boolean passXssCheck(String html) {
        return checkText(html);
    }

    /**
     * 检查json串是否含有xss风险
     *
     * @param json 受检的json
     * @return true:通过检查; false:未通过检查,含有xss风险
     */
    public boolean passXssCheckJson(String json) {
        if (StringUtils.isBlank(json)) {
            return true;
        }

        if (JSON.isValidArray(json)) {
            JSONArray jsonArray = JSON.parseArray(json);
            Iterator<Object> it = jsonArray.iterator();
            while (it.hasNext()) {
                Object value = it.next();
                if (value instanceof String) {
                    boolean valuePass = passXssCheckJson((String) value);
                    if (!valuePass) {
                        return false;
                    }
                }
            }
        } else if (JSON.isValidObject(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            Collection<Object> values = jsonObject.values();
            for (Object value : values) {
                if (value instanceof String) {
                    boolean valuePass = passXssCheckJson((String) value);
                    if (!valuePass) {
                        return false;
                    }
                }
            }
        } else if (!JSON.isValid(json)) {
            // 普通字符串，但带有特殊字符如< >的
            return passXssCheck(json);
        }

        return true;
    }

    /**
     * 检查文本中是否有xss敏感词汇
     *
     * @param html 文本
     * @return false: 未通过检查; true:通过检查
     */
    private boolean checkText(String html) {
        for (Pattern pattern : DANGER_PATTERNS) {
            boolean foundDanger = pattern.matcher(html).find();
            if (foundDanger) {
                logger.warn("found danger pattern in request={}, pattern={}", html, pattern.toString());
                return false;
            }
        }

        return true;
    }
}
