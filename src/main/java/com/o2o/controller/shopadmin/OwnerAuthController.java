package com.o2o.controller.shopadmin;

import com.o2o.dto.LocalAuthExecution;
import com.o2o.enums.LocalAuthStateEnum;
import com.o2o.pojo.LocalAuth;
import com.o2o.pojo.PersonInfo;
import com.o2o.service.LocalAuthService;
import com.o2o.utils.CodeUtil;
import com.o2o.utils.HttpServletRequestUtil;
import com.o2o.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shop")
public class OwnerAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    @PostMapping("ownerregister")
    public Map<String, Object> ownerRegister(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            map.put("success", false);
            map.put("errMsg", "输入了错误的验证码");
            return map;
        }
        String localAuthStr = HttpServletRequestUtil.getString(request, "localAuthStr");
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartFile profileImg = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)){
            multipartRequest = (MultipartHttpServletRequest) request;
            profileImg = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        } else {
            map.put("success", false);
            map.put("errMsg", "上传图片不能为空");
            return map;
        }
        LocalAuth localAuth = JsonUtils.jsonToPojo(localAuthStr, LocalAuth.class);
        if (localAuth != null && localAuth.getPassword() != null
                && localAuth.getUserName() != null) {
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user != null && localAuth.getPersonInfo() != null) {
                localAuth.getPersonInfo().setUserId(user.getUserId());
            }
            localAuth.getPersonInfo().setCustomerFlag(1);
            localAuth.getPersonInfo().setShopOwnerFlag(1);
            localAuth.getPersonInfo().setAdminFlag(0);
            LocalAuthExecution le = localAuthService.register(localAuth, profileImg);
            if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", le.getStateInfo());
            }
        } else {
            map.put("success", false);
            map.put("errMsg", "请输入注册信息");
        }
        return map;
    }
}
