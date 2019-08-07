package com.o2o.utils;

import com.google.code.kaptcha.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    private static Logger logger = LoggerFactory.getLogger(CodeUtil.class);

    public static boolean checkVerifyCode(HttpServletRequest request) {
        String verifyCode = HttpServletRequestUtil.getString(request, "verifyCode");
        logger.info("实际验证码为：" + verifyCode);
        String sessionCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        logger.info("预期验证码为：" + sessionCode);
        if(verifyCode != null && verifyCode.equalsIgnoreCase(sessionCode)) {
            return true;
        }
        return false;
    }
}
