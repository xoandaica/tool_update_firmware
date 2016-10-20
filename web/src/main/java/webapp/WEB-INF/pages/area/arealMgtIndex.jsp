<%-- 
    Document   : areaMgtIndex
    Created on : May 15, 2015, 11:02:43 AM
    Author     : Dell
--%>

<%@ include file="/common/taglibs.jsp" %>
<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
         contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><fmt:message key="management.area.mainPanel.title"/></title>
         <meta name="menu" content="ManagementMenu"/>
    </head>
    <link rel="stylesheet" type="text/css" href="/scripts/extjs/ux/css/BoxSelect.css" />
    <body class="home">
        <div id='mainPanel' style="width: 100%"></div>
        <div id="tabs" class="x-hide-display">
            <div id="tabs-1" class="x-hide-display" style="height: 200px">
                <p>
                <div class="groupItem">
                    <div class="itemContent">
                        <div id="formIdRender"></div>
                    </div>
                </div>
                </p>
            </div>
        </div>

        <jsp:include page="../common/sampleItem.jsp" />
        <jsp:include page="areaMgtFunction.jsp" />
        <jsp:include page="areaMgtSearchForm.jsp" />
        <jsp:include page="areaMgtPopupForm.jsp" />
        <jsp:include page="areaMgtForm.jsp" />
        <jsp:include page="areaMgtLayout.jsp" />
    </body>
</html>

