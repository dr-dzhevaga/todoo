<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>
    <jsp:include page="fragments/static-dependencies.jsp"/>

    <spring:url value="/resources/js/templates/ui.js/" var="ui"/>
    <script src="${ui}"></script>

    <spring:url value="/resources/js/templates/logic.js/" var="logic"/>
    <script src="${logic}"></script>

    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <spring:url value="/resources/js/templates/logic-admin.js/" var="logicAdmin"/>
        <script src="${logicAdmin}"></script>

        <spring:url value="/resources/js/templates/ui-admin.js/" var="uiAdmin"/>
        <script src="${uiAdmin}"></script>
    </sec:authorize>
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<script>
    webix.ready(function () {
        <sec:authorize access="hasRole('ROLE_ADMIN')">
        admin_ui.init();
        </sec:authorize>
        ui.init();
        <sec:authorize access="hasRole('ROLE_ADMIN')">
        admin_logic.init();
        </sec:authorize>
        logic.init();
    });
</script>
</body>

</html>