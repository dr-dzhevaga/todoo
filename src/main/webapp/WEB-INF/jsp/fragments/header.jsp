<%--@elvariable id="springViewName" type="java.lang.String"--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="header-login-signup">
    <div class="header-limiter">
        <h1><a>Tod<span>oo</span></a></h1>
        <nav>
            <spring:url value="/templates" var="templates"/>
            <spring:url value="/tasks" var="tasks"/>
            <a href="${templates}" ${springViewName == 'templates' ? 'class="selected"' : ''}>
                Templates
            </a>
            <a href="${tasks}" ${springViewName == 'tasks' ? 'class="selected"' : ''}>
                My tasks
            </a>
        </nav>
        <ul>
            <sec:authorize access="isAnonymous()">
                <li>
                    <spring:url value="/login" var="login"/>
                    <a href="${login}">Log-in</a>
                </li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li class="user">
                    <sec:authentication var="user" property="principal"/>
                    You are welcome, ${user.username}
                </li>
                <li>
                    <spring:url value="/logout" var="logout"/>
                    <a href="${logout}">Log-out</a>
                </li>
            </sec:authorize>
        </ul>
    </div>
</header>
