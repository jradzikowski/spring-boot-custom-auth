<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html>

    <head>
    </head>

    <body>
        <h1>
            Hello world page
        </h1>
        <sec:authorize access="isAnonymous()">
            <form action="doLogin" method="post" id="loginForm">

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <p>
                    <input name="customname"/>
                </p>
                <p>
                    <input name="custompassword" type="password"/>
                </p>
            </form>
            <p>
                <button type="submit" form="loginForm" value="Submit">Login</button>
            </p>
        </sec:authorize>
            <sec:authorize access="hasRole('ROLE_USER')">
                <p>
                    You are logged in!!!
                    <form action="logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <p>
                            <button type="submit">Logout</button>
                        </p>
                    </form>
                </p>
            </sec:authorize>
    </body>

</html>