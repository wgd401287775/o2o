package com.o2o.utils;

public class PathUtil {

    private static String separator = System.getProperty("file.separator");// 获取操作系统的/
    /**
     * 获取图片将保存的绝对路径
     *
     * @return
     */
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "E:/img/output";
        } else {
            basePath = "/home/dongge/image";
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    /**
     * 获取图片将保存的相对路径
     *
     * @param shopId
     * @return
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", separator);
    }

    /**
     * 获取头像保存的相对路径
     * @return
     */
    public static String getPersonInfoImagePath() {
        String personInfoImagePath = "/upload/item/personinfo/";
        personInfoImagePath = personInfoImagePath.replace("/", separator);
        return personInfoImagePath;
    }
}
