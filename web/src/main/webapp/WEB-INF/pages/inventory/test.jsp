<%-- 
    Document   : test.jsp
    Created on : May 24, 2015, 5:14:20 PM
    Author     : Dell
--%>


<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
         contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Inventory - Test</title>
    </head>
    <body>

        <div id='testSearchPanel' ></div>
        <div id='testGridPanel' ></div>
        <div id='testChart' ></div>
        <jsp:include page="../common/sampleItem.jsp" />
        <jsp:include page="testForm.jsp" />
        <jsp:include page="testLayout.jsp" />
    </body>
</html>