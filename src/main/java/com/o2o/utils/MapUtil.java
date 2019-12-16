package com.o2o.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MapUtil {
    public String[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //System.out.println(address);
        String url = "http://api.map.baidu.com/geocoding/v3/?output=json&ak=Q3C6zgBdSLAUf1Ls0iGlltG6OUSjVnHA&address=上海市徐汇区枫林路180号";
        URL myURL = null;

        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                while((data= br.readLine())!=null){
                    System.out.println(data);
                    JSONObject json = JSONObject.parseObject(data);
                    lng = json.getJSONObject("result").getJSONObject("location").getString("lng");
                    lat = json.getJSONObject("result").getJSONObject("location").getString("lat");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new String[]{lng,lat};
    }

    public String[] getAddr(String lng,String lat) throws IOException {

        String url = "http://api.map.baidu.com/geocoding/v3/?output=json&ak=Bl8UXGeNAhbxBVWEdy6rFFdrvd8nHcKN&location="+lat+","+lng;
        URL myURL = null;
        String city = "";
        String qx = "";
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                while((data= br.readLine())!=null){
                    JSONObject json = JSONObject.parseObject(data);
                    city = json.getJSONObject("result").getJSONObject("addressComponent").getString("city");
                    qx= json.getJSONObject("result").getJSONObject("addressComponent").getString("district");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new String[]{city,qx};
    }

    public static void main(String[] args) throws Exception {
        MapUtil getLatAndLngByBaidu = new MapUtil();
        String[] o = getLatAndLngByBaidu.getCoordinate("涛飞国际广场");
        // String[] o1 = getLatAndLngByBaidu.getAddr(o[0], o[1]);
        // System.out.println(o1[0]);
        // System.out.println(o1[1]);
    }
}
