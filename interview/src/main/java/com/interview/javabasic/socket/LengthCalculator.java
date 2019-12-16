package com.interview.javabasic.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LengthCalculator extends Thread {
    private Socket socket;

    public LengthCalculator(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            // 获取socket的输出流
            OutputStream os = socket.getOutputStream();
            // 获取socket的输入流
            InputStream is = socket.getInputStream();
            int ch = 0;
            byte[] buff = new byte[1024];
            // buff主要用来读取输入的内容，存成byte数组，ch主要用来获取数组的长度
            ch = is.read(buff);
            // 将接受流的byte数组转换成字符串，这里获取的内容是客户端发送过来的字符串
            String content = new String(buff, 0 , ch);
            System.out.println(content);
            // 往输出流里写入获得的字符串的长度，回发客户端
            os.write(String.valueOf(content.length()).getBytes());
            // 关闭输入流，输出流和socket
            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
