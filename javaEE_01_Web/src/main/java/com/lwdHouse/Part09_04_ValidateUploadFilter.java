package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件上传过滤器
 *   这个过滤器会把 request的header传来的签名signature和计算出来的签名做对比，
 *   如果不一致，则上传的内容不完整，被过滤掉，如果一致，才放行到servlet
 *
 * 测试: 使用命令：
 *      curl http://localhost:8087/upload/file
 *      -v -d 'test-data'   \
 *      -H 'Signature-Method: SHA-1' \
 *      -H 'Signature: 7115e9890f5b5cc6914bdfa3b7c011db1cdafedb' \
 *      -H 'Content-Type: application/octet-stream'
 * 会得到响应：
 *      < HTTP/1.1 200
 *      < Transfer-Encoding: chunked
 *      < Date: Thu, 30 Jan 2020 13:56:39 GMT
 *      <
 *      * Connection #0 to host localhost left intact
 *      <h1>Uploaded:</h1><pre><code></code></pre>
 *      * Closing connection 0
 *
 * 发现<pre><code>标签下没有东西，原因是对HttpServletRequest进行读取时，只能读取一次
 * 这个过滤器已经获取了 HttpServletRequest.getInputStream(), 并进行了读取
 * 在servlet中继续获取，所读的内容就是空的。
 *
 * 解决办法：伪造一个HttpServletRequest，对getInputStream()和getReader()返回一个新的流
 */
@WebFilter("/upload/*")
public class Part09_04_ValidateUploadFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String digest = req.getHeader("Signature-Method");
        String signature = req.getHeader("Signature");
        if (digest == null || digest.isEmpty() || signature == null || signature.isEmpty()){
            sendErrorPage(resp, "Missing signature");
            return;
        }
        // 获取摘要算法
        MessageDigest md = getMessageDigest(digest);
        // 读取摘要
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new DigestInputStream(request.getInputStream(), md);
        byte[] buffer = new byte[1024];
        for (;;){
            int len = input.read(buffer);
            if (len == -1){
                break;
            }
            output.write(buffer, 0, len);
        }
        // 获取摘要，转换成16进制
        String actual = toHexString(md.digest());
        // 和期望摘要signature做对比
        if (!signature.equals(actual)){
            sendErrorPage(resp, "Invalid signature.");
            return;
        }

        // chain.doFilter(request, response);
        // 为了让过滤器不影响servlet读取InputStream，伪造一个能重读的Servlet
        chain.doFilter(new ReReadableHttpServletRequest(req, output.toByteArray()), response);
    }

    private String toHexString(byte[] digest){
        StringBuilder sb = new StringBuilder();
        for (byte b : digest){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private MessageDigest getMessageDigest(String name) throws ServletException {
        try{
            return MessageDigest.getInstance(name);
        }catch (NoSuchAlgorithmException e){
            throw new ServletException(e);
        }
    }

    private void sendErrorPage(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter pw = resp.getWriter();
        pw.write("<html><body><h1>");
        pw.write(errorMessage);
        pw.write("</h1></body></html>");
        pw.flush();
    }
}


/**
 * 伪造的ServletRequest，解决ServletRequest不能重读问题
 */
class ReReadableHttpServletRequest extends HttpServletRequestWrapper{
    private byte[] body;
    private boolean open = false;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public ReReadableHttpServletRequest(HttpServletRequest request, byte[] body) {
        super(request);
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (open){
            throw new IllegalStateException("cannot reopen input stream");
        }
        open = true;
        return new ServletInputStream() {
            private int offset = 0;
            @Override
            public boolean isFinished() {
                return offset >= body.length;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            /**
             * 因为Servlet中使用的read(byte b[])方法调用了read(b, 0, b.length)方法
             * 调用了read()方法，所以根本问题出在了read()方法上，所以只需要重写read()方法即可
             * @return
             * @throws IOException
             */
            @Override
            public int read() throws IOException {
                if (offset >= body.length){
                    return -1;
                }
                int n = body[offset] & 0xff;
                offset++;
                return n;
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (open){
            throw new IllegalStateException("cannot reopen reader");
        }
        open = true;
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body), "UTF-8"));
    }
}
