<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="common/webix/skins/terrace.css" type="text/css" media="screen" charset="utf-8">
    <link rel="stylesheet" href="common/css/header.css">
    <link rel="stylesheet" href="common/css/body.css">
    <script type="text/javascript" src="common/webix/webix.js" charset="utf-8"></script>
    <script type="text/javascript" src='common/js/endpoints.js'></script>
    <script type="text/javascript" src='common/js/ajax_util.js'></script>
    <script type="text/javascript" src='common/js/ui_util.js'></script>
    <script type="text/javascript" src='templates/ui.js'></script>
    <script type="text/javascript" src='templates/logic.js'></script>
    <c:if test="${pageContext.request.isUserInRole('admin')}">
        <script type="text/javascript" src='templates/admin_ui.js'></script>
        <script type="text/javascript" src='templates/admin_logic.js'></script>
    </c:if>
    <header class="header-login-signup">
        <div class="header-limiter">
            <h1><a>Tod<span>oo</span></a></h1>
            <nav>
                <a href="${pageContext.request.contextPath}/templates.jsp" class="selected">Templates</a>
                <a href="${pageContext.request.contextPath}/mytasks.jsp">My tasks</a>
            </nav>
            <ul>
                <c:choose>
                    <c:when test="${pageContext.request.userPrincipal eq null}">
                        <li><a href="${pageContext.request.contextPath}/login?from=${pageContext.request.requestURL}">Log-in</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="user">You are welcome, ${pageContext.request.remoteUser}</li>
                        <li><a href="${pageContext.request.contextPath}/logout">Log-out</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </header>
</head>
<body>
<script>
    webix.ready(function () {
        <c:if test="${pageContext.request.isUserInRole('admin')}">
        admin_ui.init();
        </c:if>
        ui.init();
        <c:if test="${pageContext.request.isUserInRole('admin')}">
        admin_logic.init();
        </c:if>
        logic.init();
    });
</script>
</body>
</html>