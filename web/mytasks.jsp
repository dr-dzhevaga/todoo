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

    <script type="text/javascript" src='mytasks/logic.js'></script>
    <script type="text/javascript" src='mytasks/ui.js'></script>

    <header class="header-basic">
        <div class="header-limiter">
            <h1><a>Tod<span>oo</span></a></h1>
            <nav>
                <a href="/">Templates</a>
                <a href="/mytasks.jsp" class="selected">My tasks</a>
            </nav>
        </div>
    </header>
</head>

<body>
<style type="text/css">

</style>
<script>
    webix.ready(function () {
        ui.init();
        logic.init();
    });
</script>
</body>

</html>