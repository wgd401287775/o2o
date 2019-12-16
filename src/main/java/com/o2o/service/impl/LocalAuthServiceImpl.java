package com.o2o.service.impl;

import com.o2o.dto.LocalAuthExecution;
import com.o2o.enums.LocalAuthStateEnum;
import com.o2o.mapper.LocalAuthMapper;
import com.o2o.mapper.PersonInfoMapper;
import com.o2o.pojo.LocalAuth;
import com.o2o.pojo.PersonInfo;
import com.o2o.service.LocalAuthService;
import com.o2o.utils.ImageUtil;
import com.o2o.utils.MD5;
import com.o2o.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthMapper localAuthMapper;
    @Autowired
    private PersonInfoMapper personInfoMapper;
    @Override
    public LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password) {
        return localAuthMapper.queryLocalByUserNameAndPwd(userName, password);
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthMapper.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) throws RuntimeException {
        if (localAuth == null || localAuth.getUserName() == null || localAuth.getPassword() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            if (localAuth.getPersonInfo() != null
                    && localAuth.getPersonInfo().getUserId() == null) {
                if (profileImg != null) {
                    try {
                        addProfileImg(localAuth, profileImg);
                    } catch (Exception e) {
                        throw new RuntimeException("addUserProfileImg error: " + e.getMessage());
                    }
                }
                try {
                    localAuth.getPersonInfo().setCreateTime(new Date());
                    localAuth.getPersonInfo().setLastEditTime(new Date());
                    localAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo user = localAuth.getPersonInfo();
                    int i = personInfoMapper.insertPersonInfo(user);
                    if (i <= 0) {
                        throw new RuntimeException("添加用户信息失败");
                    } else {
                        localAuth.setUserId(user.getUserId());
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("insertPersonInfo error: " + e.getMessage());
                }
            }
            int i = localAuthMapper.insertLocalAuth(localAuth);
            if (i > 0) {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            } else {
                throw new RuntimeException("帐号创建失败");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("insertLocalAuth error: " + e.getMessage());
        }
    }

    private void addProfileImg(LocalAuth localAuth, CommonsMultipartFile profileImg) throws IOException {
        String personInfoImagePath = PathUtil.getPersonInfoImagePath();
        String profileImgAddr = ImageUtil.generateNoWatermarkThumbnail(profileImg.getInputStream(), profileImg.getOriginalFilename(), personInfoImagePath);
        localAuth.getPersonInfo().setProfileImg(profileImgAddr);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws RuntimeException {
        if (localAuth == null || localAuth.getPassword() == null
                || localAuth.getUserName() == null
                || localAuth.getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        LocalAuth tempAuth = localAuthMapper.queryLocalByUserId(localAuth.getUserId());
        if (tempAuth != null) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectedNum = localAuthMapper.insertLocalAuth(localAuth);
            if (effectedNum <= 0) {
                throw new RuntimeException("帐号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,
                        localAuth);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("insertLocalAuth error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword) {
        if (userId != null && userName != null && password != null
                && newPassword != null && !password.equals(newPassword)) {
            try {
                int effectedNum = localAuthMapper.updateLocalAuth(userId,
                        userName, MD5.getMd5(password),
                        MD5.getMd5(newPassword), new Date());
                if (effectedNum <= 0) {
                    throw new RuntimeException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new RuntimeException("更新密码失败:" + e.toString());
            }
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
