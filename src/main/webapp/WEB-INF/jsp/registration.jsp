<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
    <link rel="stylesheet" href="login/style.css">
</head>
<body>
<div class="login-card">
    <h1>Registration</h1>
    <c:if test="${not empty requestScope.error}">
        <div class="login-error">${requestScope.error}</div>
    </c:if>
    <form method="POST" action="${pageContext.request.contextPath}/registration">
        <input type="text" name="username" placeholder="Username">
        <input type="password" name="password" placeholder="Password">
        <input type="submit" name="create" class="login login-submit" value="register">
    </form>

</div>
</body>
</html>