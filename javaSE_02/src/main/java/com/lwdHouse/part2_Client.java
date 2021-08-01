package com.lwdHouse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * TCP编程：Client端
 */
public class part2_Client {
    public static void main(String[] args) throws IOException {
        Socket sock = new Socket("localhost", 6666);    // 链接服务器
        try(InputStream input = sock.getInputStream()){             // 获取输入输出流
            try(OutputStream output = sock.getOutputStream()){
                handle(input, output);
            }
        }
        sock.close();
        System.out.println("disconnected.");
    }

    private static void handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Server]"+reader.readLine());       // 接受服务器的第一条消息hello
        for(;;){
            System.out.println(">>> ");
            String s = scanner.nextLine();
            writer.write(s);                                    // 给服务器发送消息
            writer.newLine();
            writer.flush();
            String resp = reader.readLine();                    // 读取响应
            System.out.println("<<< "+ resp);
            if (resp.equals("bye")){
                break;
            }
        }
    }
}
