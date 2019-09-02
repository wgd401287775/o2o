package com.o2o.controller.wechat;

import com.o2o.dto.WechatAuthExecution;
import com.o2o.enums.WechatAuthStateEnum;
import com.o2o.mapper.PersonInfoMapper;
import com.o2o.pojo.PersonInfo;
import com.o2o.pojo.WechatAuth;
import com.o2o.service.WechatAuthService;
import com.o2o.utils.wechat.WechatUtil;
import com.o2o.utils.wechat.pojo.UserAccessToken;
import com.o2o.utils.wechat.pojo.WechatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=您的appId&redirect_uri=http://o2o.yitiaojieinfo.com/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoMapper personInfoMapper;

    @RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        String roleType = request.getParameter("state");
        log.debug("weixin login code:" + code);
        WechatAuth auth = null;
        WechatUser user = null;
        String openId = null;
        if (null != code) {
            UserAccessToken token;
            try {
                // 通过code获取access_token
                token = WechatUtil.getUserAccessToken(code);
                log.debug("weixin login token:" + token.toString());
                // 通过token获取accessToken
                String accessToken = token.getAccessToken();
                // 通过token获取openId
                openId = token.getOpenId();
                // 通过access_token和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
                e.printStackTrace();
            }
        }
        if(FRONTEND.equals(roleType)) {
            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
            if (auth == null) {
                personInfo.setCustomerFlag(1);
                auth = new WechatAuth();
                auth.setOpenId(openId);
                auth.setPersonInfo(personInfo);
                WechatAuthExecution we = wechatAuthService.register(auth, null);
                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                    return null;
                }
            }
            personInfo = personInfoMapper.getPersonInfobById(auth.getUserId());
            request.getSession().setAttribute("user", personInfo);
            return "frontend/index";
        } else if (SHOPEND.equals(roleType)) {
            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
            if (auth == null) {
                personInfo.setShopOwnerFlag(1);
                auth = new WechatAuth();
                auth.setOpenId(openId);
                auth.setPersonInfo(personInfo);
                WechatAuthExecution we = wechatAuthService.register(auth, null);
                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                    return null;
                }
            }
            personInfo = personInfoMapper.getPersonInfobById(auth.getUserId());
            request.getSession().setAttribute("user", personInfo);
            return "shop/shoplist";
        }
        return null;
    }
}
