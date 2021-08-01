    <%--
    JSP是Java Server Pages的缩写
    包含在<%--和--%>之间的是JSP的注释，它们会被完全忽略；
    包含在<\%和\%>之间的是Java代码，可以编写任意Java代码；
    如果使用<\%= xxx \%>则可以快捷输出一个变量的值。
    JSP页面内置了几个变量：
        out：表示HttpServletResponse的PrintWriter；
        session：表示当前HttpSession对象；
        request：表示HttpServletRequest对象。
    JSP和Servlet有什么区别？
        其实它们没有任何区别，因为JSP在执行前首先被编译成一个Servlet
        在Tomcat的临时目录下,可以找到一个xxx_jsp.java的源文件,就是jsp编译之后的Servlet源文件
    --%>
<html>
<head>
    <title>Hello World - JSP</title>
</head>
<body>
    <%-- JSP Comment --%>
    <h1>Hello World!</h1>
    <p>
    <%
         out.println("Your IP address is ");
    %>
    <span style="color:red">
        <%= request.getRemoteAddr() %>
    </span>
    </p>
</body>
</html>