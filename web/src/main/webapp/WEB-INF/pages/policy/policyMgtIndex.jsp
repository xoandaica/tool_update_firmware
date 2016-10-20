<%-- 
    Document   : policyMgtIndex
    Created on : Jun 5, 2015, 3:23:06 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="menu" content="ManagementMenu" />
        <title><fmt:message key="management.policy.mainPanel.title"/></title>
        <link rel="stylesheet" type="text/css" href="/scripts/extjs/ux/css/BoxSelect.css" />  
    </head>

    <body class="home">
        <div id="tabs-1" class="x-hide-display" style="height: 200px">
            <p>
            <div class="groupItem">
                <div class="itemContent">
                    <div align="center" style="width: 100%">
                        <div id="policySearchCriteria" align="center" style="width: 100%;"></div>
                        <div id="policyGridPanel" align="center" style="width: 100%;"></div>  
                    </div>
                </div>
            </div>
        </p>
    </div>
    <iframe id="hiddenDownloader" style="display: none"></iframe>

    <jsp:include page="../common/sampleItem.jsp" />
    <jsp:include page="policyMgtFunction.jsp" />
    <jsp:include page="policyMgtSearchForm.jsp" />
    <jsp:include page="policyMgtHistoryForm.jsp" />
    <jsp:include page="policyMgtPopupForm.jsp" />
    <jsp:include page="policyMgtForm.jsp" />
    <jsp:include page="policyMgtLayout.jsp" />

</body>
</html>

