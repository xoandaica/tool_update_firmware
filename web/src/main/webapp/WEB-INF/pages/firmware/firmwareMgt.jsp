<%-- 
    Document   : actionlog
    Created on : May 29, 2015, 11:09:06 AM
    Author     : longdq
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" type="text/css" href="/scripts/extjs/ux/css/BoxSelect.css" />
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="management.firmware.tittle"/></title>
        <meta name="menu" content="ManagementMenu"/>
    </head>

    <body class="home">
        <div id='firmwareMgtId' style="width: 100%"></div>
        <div id="tabs-1" class="x-hide-display" style="height: 200px">
            <p>
            <div class="groupItem" id="ifjodsaoifd'/>">
                <div class="itemContent" id="jfdpsajfpdosjapoj">
                    <div align="center" style="width: 100%">
                        <!--<div id="searchCriteria" align="center" style="width: 100%;"></div>-->
                        <div align="center" style="width: 100%;">
                            <div id="searchCriteria" align="left" style="width: 50%; float: left"></div>
                            <div id="uploadFile" align="right" style="width: 50%; float:right "></div>
                        </div>
                        <div id="frimwareGrid" align="center"
                             style="width: 100%; float: left"></div>
                    </div>
                    <div align="center" style="width: 100%">
                        <div id="deviceReturn"	align="center" style="width: 100%"></div>
                    </div>
                </div>
            </div>
        </p>
    </div>

    <jsp:include page="firmwareController.jsp" />
    <jsp:include page="firmware_form.jsp" />
    <jsp:include page="firmware_proccess.jsp" />

</body>
</html>
