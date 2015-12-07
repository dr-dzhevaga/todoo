<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Log-in</title>
    <link rel="stylesheet" href="login/style.css">
</head>
<body>
<div class="login-card">
    <h1>Log-in</h1>

    <c:if test="${not empty param.error}">
        <div class="login-error">The username or password you have entered is invalid</div>
    </c:if>
    <form method="POST" action="j_security_check">
        <input type="text" name="j_username" placeholder="Username">
        <input type="password" name="j_password" placeholder="Password">
        <input type="submit" name="login" class="login login-submit" value="login">
    </form>

    <div class="login-help">
        <a href="registration.jsp">Register</a>
    </div>
</div>
</body>
</html>
