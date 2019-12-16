package com.interview.javabasic.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

    public static void main(String[] args) throws Exception {
        // 服务端接受客户端发送的数据报
        DatagramSocket socket = new DatagramSocket(10001);
        byte[] buff = new byte[100]; // 存储从客户端接受到的内容
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        // 接受客户端发送过来的内容，并将内容封装进DatagramPacket对象中
        socket.receive(packet);
        byte[] data = packet.getData();// 从DatagramPacket对象中获取真正的存储数据
        String content = new String(data, 0, packet.getLength());
        System.out.println(content);
        byte[] sendContent = String.valueOf(content.length()).getBytes();
        // 服务端给客户端发送数据报
        // 从DatagramPacket对象中获取到数据的来源地址与端口号
        DatagramPacket packetToClient = new DatagramPacket(sendContent, sendContent.length,
                packet.getAddress(), packet.getPort());
        socket.send(packetToClient); // 发送数据给客户端
    }
}
