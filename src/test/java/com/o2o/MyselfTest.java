package com.o2o;

import com.o2o.pojo.PersonInfo;
import com.o2o.pojo.WechatAuth;

public class MyselfTest {
    public static void main(String[] args) {
        WechatAuth we = new WechatAuth();
        PersonInfo user = new PersonInfo();
        we.setPersonInfo(user);
        we.setUserId(1l);
        we.setOpenId("ovLbns9oD5K4g712TW63dgSHxC3o");
        System.out.println(we);
        PersonInfo personInfo = we.getPersonInfo();
        personInfo.setEmail("401287775@qq.com");
        System.out.println(we);
        we.getPersonInfo().setName("栋哥");
        System.out.println(we);
    }
}
