<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <jsp:include page="fragments/static-dependencies.jsp"/>

    <spring:url value="/resources/js/templates/ui.js/" var="ui"/>
    <script src="${ui}"></script>

    <spring:url value="/resources/js/templates/logic.js/" var="logic"/>
    <script src="${logic}"></script>

    <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
        <spring:url value="/resources/js/templates/logic-admin.js/" var="logicAdmin"/>
        <script src="${logicAdmin}"></script>

        <spring:url value="/resources/js/templates/ui-admin.js/" var="uiAdmin"/>
        <script src="${uiAdmin}"></script>
    </c:if>
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<script>
    webix.ready(function () {
        <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
        admin_ui.init();
        </c:if>
        ui.init();
        <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
        admin_logic.init();
        </c:if>
        logic.init();
    });
</script>
</body>

</html>