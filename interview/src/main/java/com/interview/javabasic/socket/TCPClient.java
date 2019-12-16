package com.interview.javabasic.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {

    public static void main(String[] args) throws Exception {
        // 创建socket，并指定连接的端口和ip
        Socket socket = new Socket("127.0.0.1", 10000);
        // 获取输入流
        InputStream is = socket.getInputStream();
        // 获取输出流
        OutputStream os = socket.getOutputStream();
        os.write(new String("hello world").getBytes());
        int ch = 0;
        byte[] buff = new byte[1024];
        ch = is.read(buff);
        String content = new String(buff, 0, ch);
        System.out.println(content);
        is.close();
        os.close();
        socket.close();
    }
}
