<%@ page import="ru.todoo.utils.ServletUtil" %>
<%
    boolean isAdmin = ServletUtil.isAdmin(request);
%>
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
    <%if (isAdmin) {%>
    <script type="text/javascript" src='templates/admin_ui.js'></script>
    <script type="text/javascript" src='templates/admin_logic.js'></script>
    <%}%>
    <header class="header-basic">
        <div class="header-limiter">
            <h1><a>Tod<span>oo</span></a></h1>
            <nav>
                <a href="/" class="selected">Templates</a>
                <a href="/mytasks.jsp">My tasks</a>
            </nav>
        </div>
    </header>
</head>

<body>
<style type="text/css">

</style>
<script>
    webix.ready(function () {
        <%if(isAdmin) {%>
        admin_ui.init();
        <%}%>
        ui.init();
        <%if(isAdmin) {%>
        admin_logic.init();
        <%}%>
        logic.init();
    });
</script>
</body>

</html>