package com.lwdHouse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 手撕HttpServer: 使用Socket搭建一个Http服务器
 */
public class Part01_Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("server is running");
        for (;;){
            Socket sock = ss.accept();
            System.out.println("Connected from " + sock.getRemoteSocketAddress());
            Handler handler = new Handler(sock);
            handler.start();
        }
    }

}

class Handler extends Thread{
    private Socket sock;
    public Handler(Socket sock){
        this.sock = sock;
    }

    @Override
    public void run() {
        try(InputStream is = this.sock.getInputStream()){
            try(OutputStream os = this.sock.getOutputStream()){
                handle(is, os);
            }
        } catch (IOException e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected!");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(input));
        var writer = new BufferedWriter(new OutputStreamWriter(output));

        // 读取http请求
        boolean requestOk = false;
        String first = reader.readLine();
        if (first.startsWith("GET / HTTP/1.")){
            requestOk = true;
        }
        for (;;){
            String header = reader.readLine();
            if (header.isEmpty()){
                break;
            }
            System.out.println(header);
        }
        System.out.println(requestOk? "Response OK":"Response Error");
        if (!requestOk){
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        }else {
            String data = "<html><body><h1>Hello,World!</html></body></h1>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: "+length + "\r\n");
            writer.write("\r\n");
            writer.write(data);
            writer.flush();
        }
    }
}
