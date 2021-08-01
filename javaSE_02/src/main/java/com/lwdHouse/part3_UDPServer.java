package com.lwdHouse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class part3_UDPServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket(6666);
        for(;;){
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet);     // 保存了address信息
            System.out.println("address:" + packet.getAddress());       // packet里包含了ip
            String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("接受到消息：" + s);
            // 发送数据
            byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);
            packet.setData(data);
            ds.send(packet);
        }
    }
}
