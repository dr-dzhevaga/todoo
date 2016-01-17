<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <jsp:include page="fragments/static-dependencies.jsp"/>

    <spring:url value="/resources/js/tasks/ui.js/" var="ui"/>
    <script src="${ui}"></script>

    <spring:url value="/resources/js/tasks/logic.js/" var="logic"/>
    <script src="${logic}"></script>
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<script>
    webix.ready(function () {
        ui.init();
        logic.init();
    });
</script>
</body>

</html>