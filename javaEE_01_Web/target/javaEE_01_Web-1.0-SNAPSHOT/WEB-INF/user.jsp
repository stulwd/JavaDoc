<%@ page import="com.lwdHouse.Part07_MVC"%>
<%@ page import="com.lwdHouse.Part07_MVC"%>
<%
    Part07_MVC.User user = (Part07_MVC.User) request.getAttribute("user");
%>

<html>
<head>
    <title>Hello World - JSP</title>
</head>
<body>
    <h1>Hello <%= user.name %>!</h1>
    <p>School Name:
        <span style="color:red">
            <%= user.school.name %>
        </span>
    </p>
    <p>School Address:
        <span style="color:red">
            <%= user.school.address %>
        </span>
    </p>
</body>
</html>
