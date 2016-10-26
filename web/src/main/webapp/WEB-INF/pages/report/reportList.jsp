<%-- 
    Document   : policyMgtIndex
    Created on : Jun 5, 2015, 3:23:06 PM
    Author     : Dell
--%>

<%@ include file="/common/taglibs.jsp" %>
<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
         contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="menu" content="reportMenu" />
        <title><fmt:message key="menu.report.title"/></title>
        <link rel="stylesheet" type="text/css" href="upgradefw/scripts/extjs/ux/css/BoxSelect.css" />
    </head>
    <body class="home">
        <div id="tabs" class="x-hide-display">
            <div id="tabs-1" class="x-hide-display" style="height: 500px">
                <div id="formReportIdRender"></div>
            </div>
        </div>
        <jsp:include page="reportListLayout.jsp" />
        <jsp:include page="reportListForm.jsp" /> 
    </body>
</html>

