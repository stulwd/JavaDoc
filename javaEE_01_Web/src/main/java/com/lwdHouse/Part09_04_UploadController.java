package com.lwdHouse;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 通过Filter过滤HttpServletRequest，实现上传文件校验
 *
 * 文件上传Servlet：（这里沿用手撕的mvc框架，即实现Controller方法即可）
 * 为这个servlet可以添加一个过滤器来验证上传文件的完整性，要是文件不完整，不会经过这个Servlet处理
 * 过滤器是 Part09_04_ValicateUploadFilter。
 */
public class Part09_04_UploadController {

    @Part08_05_PostMapping(path = "/upload/file")
    public Part08_02_ModelAndView upload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InputStream input = req.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (;;){
            int len = input.read(buffer);
            if (len == -1){
                break;
            }
            output.write(buffer, 0, len);
        }
        String uploadedText = output.toString(StandardCharsets.UTF_8);
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Uploaded:</h1>");
        pw.write("<pre><code>");
        pw.write(uploadedText);
        pw.write("</code></pre>");
        pw.flush();
        return null;
    }
}
