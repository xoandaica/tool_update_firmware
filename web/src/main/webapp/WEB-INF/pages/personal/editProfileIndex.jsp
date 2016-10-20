<%-- 
    Document   : editProfileIndex
    Created on : Jun 5, 2015, 3:23:06 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="menu" content="UserMenu" />
        <title><fmt:message key="userProfile.title"/></title>
        <link rel="stylesheet" type="text/css" href="/scripts/extjs/ux/css/BoxSelect.css" />  
    </head>

    <body class="home">
        <div id="tabs-1" class="x-hide-display" style="height: 200px">
            <p>
            <div class="groupItem">
                <div class="itemContent">
                    <div align="center" style="width: 100%" >
                        <div id="editProfilePanel" align="center" style="width: 100%;"></div>  
                    </div>
                </div>
            </div>
        </p>
    </div>
    <jsp:include page="../common/sampleItem.jsp" />
    <jsp:include page="editProfileFunction.jsp" />
    <jsp:include page="editProfileForm.jsp" />
    <jsp:include page="editProfileLayout.jsp" />

</body>
</html>

