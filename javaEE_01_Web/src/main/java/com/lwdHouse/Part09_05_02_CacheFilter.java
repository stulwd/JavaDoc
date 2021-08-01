package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebFilter(urlPatterns = "/slow/*")
public class Part09_05_02_CacheFilter implements Filter {
    private Map<String, byte[]> cache = new ConcurrentHashMap<>();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI();
        byte[] data = this.cache.get(url);
        resp.setHeader("X-Cache-Hit", data == null? "No" : "Yes");
        if (data == null){
            CachedHttpServletResponse wrapper = new CachedHttpServletResponse(resp);
            // 调用doFilter()时，不能传入原始的HttpServletResponse，因为这样就会写入Socket
            // 而是传入一个伪造的HttpServletResponse，让下游组件写入到我们预设的ByteArrayOutputStream
            chain.doFilter(request, wrapper);
            data = wrapper.getContent();
            cache.put(url, data);
        }
        // 写入到原始的Response
        ServletOutputStream output = resp.getOutputStream();
        output.write(data);
        output.flush();
    }
}

class CachedHttpServletResponse extends HttpServletResponseWrapper {
    private boolean open = false;
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    public CachedHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (open){
            throw new IllegalStateException("cannot re-open writer.");
        }
        open = true;
        return new PrintWriter(output, false, StandardCharsets.UTF_8);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (open){
            throw new IllegalStateException("cannot re-open output stream.");
        }
        open = true;
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }

            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }
        };
    }

    public byte[] getContent(){
        return output.toByteArray();
    }
}
