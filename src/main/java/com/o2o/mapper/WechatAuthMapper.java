package com.o2o.mapper;

import com.o2o.pojo.WechatAuth;

public interface WechatAuthMapper {

    WechatAuth queryWechatInfoByOpenId(String openId);

    int insertWechatAuth(WechatAuth wechatAuth);
}
