<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/resources/js/webix/webix.css/" var="webixCss"/>
<link href="${webixCss}" rel="stylesheet"/>

<spring:url value="/resources/css/header.css/" var="headerCss"/>
<link href="${headerCss}" rel="stylesheet"/>

<spring:url value="/resources/css/body.css/" var="bodyCss"/>
<link href="${bodyCss}" rel="stylesheet"/>

<spring:url value="/resources/js/webix/webix.js/" var="webix"/>
<script src="${webix}"></script>

<spring:url value="/resources/js/utils/endpoints.js/" var="endpoints"/>
<script src="${endpoints}"></script>

<spring:url value="/resources/js/utils/ajax-util.js/" var="ajaxUtil"/>
<script src="${ajaxUtil}"></script>

<spring:url value="/resources/js/utils/ui-util.js/" var="uiUtil"/>
<script src="${uiUtil}"></script>
