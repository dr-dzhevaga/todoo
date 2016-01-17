<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="header-login-signup">
    <div class="header-limiter">
        <h1><a>Tod<span>oo</span></a></h1>
        <nav>
            <spring:url value="/templates" var="templates"/>
            <spring:url value="/tasks" var="tasks"/>
            <a href="${templates}" ${requestScope.springViewName == 'templates' ? 'class="selected"' : ''}>Templates</a>
            <a href="${tasks}" ${requestScope.springViewName == 'tasks' ? 'class="selected"' : ''}>My tasks</a>
        </nav>
        <ul>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal eq null}">
                    <li>
                        <a href="${pageContext.request.contextPath}/login?from=${pageContext.request.requestURL}">Log-in</a>
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
