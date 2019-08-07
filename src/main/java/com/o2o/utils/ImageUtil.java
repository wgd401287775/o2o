package com.o2o.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread()
            .getContextClassLoader().getResource("").getPath();// 获取当前执行线程的路径
    private static Random random = new Random();
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 上传文件
     * @param io
     * @param imgName
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(InputStream io, String imgName, String targetAddr){
        String realFileName = getRandomFileName();// 获得文件名
        String extension = imgName.substring(imgName.lastIndexOf("."));
        makeDirPath(targetAddr);// 创建文件夹
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails
                    .of(io)
                    .size(1000, 1000)
                    .watermark(
                            Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "/watermark.jpg")),
                            0.25f).outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片上传失败");
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所需要的文件夹
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 生成随机文件名，以防止重名
     *
     * @return
     */
    private static String getRandomFileName() {
        int rannum = random.nextInt(89999) + 10000;
        String nowTimeStr = ImageUtil.format.format(new Date());
        return nowTimeStr + rannum;
    }

    /**
     * 判断storePath是文件的路径还是目录的路径， 如果是文件路径则删除该文件， 如果是目录路径则删除该目录下的所有文件包括该目录
     *
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File[] files = fileOrPath.listFiles();
                for (File file : files){
                    file.delete();
                }
            }
            fileOrPath.delete();
        }
    }

    public static void main(String[] args) throws Exception {
        Thumbnails.of(new File("E:/img/1.jpg")).size(1000,1000)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
                .outputQuality(0.8f).toFile("E:/img/output/new1.jpg");
    }
}
