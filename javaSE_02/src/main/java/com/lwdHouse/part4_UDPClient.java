package com.lwdHouse;

import java.io.IOException;
import java.net.*;

public class part4_UDPClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(1000);
        ds.connect(InetAddress.getByName("localhost"), 6666);
        byte[] data = "hello".getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        ds.send(packet);
        // 接收
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        String resp = new String(packet.getData(), packet.getOffset(), packet.getLength());
        System.out.println("接受到消息："+resp);
        ds.disconnect();
    }
}
