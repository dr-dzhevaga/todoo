<!DOCTYPE html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Log-in</title>

    <spring:url value="/resources/css/login.css" var="loginCss"/>
    <link rel="stylesheet" href="${loginCss}">
</head>
<body>
<div class="login-card">
    <h1>Log-in</h1>

    <c:if test="${param.error != null}">
        <div class="login-error">The username or password you have entered is invalid</div>
    </c:if>

    <c:if test="${param.logout != null}">
        <div class="login-info">You have been successfully logged out</div>
    </c:if>

    <spring:url value="/login" var="login"/>
    <form method="post" action="${login}">
        <input type="text" name="username" placeholder="Username">
        <input type="password" name="password" placeholder="Password">
        <input type="submit" name="login" value="login" class="login login-submit">
    </form>

    <div class="login-help">
        <a href="registration.jsp">Register</a>
    </div>
</div>
</body>
</html>
