<!DOCTYPE html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Registration</title>

    <spring:url value="/resources/css/login.css" var="loginCss"/>
    <link rel="stylesheet" href="${loginCss}">
</head>
<body>
<div class="login-card">
    <h1>Registration</h1>

    <%--@elvariable id="error" type="java.lang.String"--%>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <spring:url value="/registration" var="registration"/>
    <form method="POST" action="${registration}">
        <input type="text" name="username" placeholder="Username">
        <input type="password" name="password" placeholder="Password">
        <input type="submit" name="create" class="login login-submit" value="register">
    </form>

</div>
</body>
</html>