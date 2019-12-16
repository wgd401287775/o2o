package com.o2o.service.impl;

import com.o2o.dto.WechatAuthExecution;
import com.o2o.enums.WechatAuthStateEnum;
import com.o2o.mapper.PersonInfoMapper;
import com.o2o.mapper.WechatAuthMapper;
import com.o2o.pojo.PersonInfo;
import com.o2o.pojo.WechatAuth;
import com.o2o.service.WechatAuthService;
import com.o2o.utils.ImageUtil;
import com.o2o.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    @Autowired
    private WechatAuthMapper wechatAuthMapper;
    @Autowired
    private PersonInfoMapper personInfoMapper;

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth, CommonsMultipartFile profileImg) throws RuntimeException {
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            wechatAuth.setCreateTime(new Date());
            if (wechatAuth.getPersonInfo() != null
                    && wechatAuth.getPersonInfo().getUserId() == null) {
                if (profileImg != null) {
                    try {
                        addProfileImg(wechatAuth, profileImg);
                    } catch (Exception e) {
                        throw new RuntimeException("addUserProfileImg error: " + e.getMessage());
                    }
                }
                try {
                    PersonInfo user = wechatAuth.getPersonInfo();
                    user.setCreateTime(new Date());
                    user.setLastEditTime(new Date());
                    user.setAdminFlag(0);
                    user.setEnableStatus(1);
                    int i = personInfoMapper.insertPersonInfo(user);
                    if(i <= 0) {
                        throw new RuntimeException("添加用户信息失败");
                    } else {
                        wechatAuth.setUserId(user.getUserId());
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("insertPersonInfo error: " + e.getMessage());
                }
            }
            int i = wechatAuthMapper.insertWechatAuth(wechatAuth);
            if (i > 0) {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
            } else {
                throw new RuntimeException("帐号创建失败");
            }
        } catch (Exception e){
            throw new RuntimeException("insertWechatAuth error: " + e.getMessage());
        }
    }

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthMapper.queryWechatInfoByOpenId(openId);
    }

    private void addProfileImg(WechatAuth wechatAuth, CommonsMultipartFile profileImg) throws IOException {
        String personInfoImagePath = PathUtil.getPersonInfoImagePath();
        String profileImgAddr = ImageUtil.generateNoWatermarkThumbnail(profileImg.getInputStream(), profileImg.getOriginalFilename(), personInfoImagePath);
        wechatAuth.getPersonInfo().setProfileImg(profileImgAddr);
    }
}
