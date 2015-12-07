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
    <%if (request.getAttribute("error") != null) {%>
    <div class="login-error"><%=request.getAttribute("error")%>
    </div>
    <%}%>

    <form method="POST" action="/registration">
        <input type="text" name="username" placeholder="Username">
        <input type="password" name="password" placeholder="Password">
        <input type="submit" name="create" class="login login-submit" value="register">
    </form>

</div>
</body>
</html>