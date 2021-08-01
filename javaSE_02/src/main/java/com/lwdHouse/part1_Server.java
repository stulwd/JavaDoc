package com.lwdHouse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP编程：Server端
 *
 */
public class part1_Server
{
    public static void main( String[] args ) throws IOException {
        ServerSocket ss = new ServerSocket(6666);
        System.out.println("server is running");
        for(;;){
            Socket sock = ss.accept();      // 接受客户端的连接
            System.out.println("connected from " + sock.getRemoteSocketAddress());      // 连接成功
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread{
    // 没有权限修饰符的成员变量，作用域是当前包？
    Socket sock;

    public Handler(Socket sock){
        this.sock = sock;
    }
    @Override
    public void run() {
        try(InputStream input = this.sock.getInputStream()) {       // 获取输入输出流
            try(OutputStream output = this.sock.getOutputStream()){
                handle(input, output);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.sock.close();
            } catch (IOException ioException) {
            }
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        /* BufferedReader和BufferedWriter为默认带有缓冲的字符输出输入流，因为有缓冲区所以效率比没有缓冲区的高。 */
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("hello\n");        // 服务器先发送一个 hello
        writer.flush();
        for(;;){
            String s = reader.readLine();   // 等待接受客户端信息
            System.out.println("receive: " + s);
            if(s.equals("bye")){
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("ok: " + s + "\n");    // 把接受的回写
            writer.flush();
        }

    }
}
