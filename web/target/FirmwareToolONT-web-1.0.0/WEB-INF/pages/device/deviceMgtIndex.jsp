<%-- 
    Document   : deviceMgtIndex.jsp
    Created on : Jun 8, 2015, 6:13:20 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css"
	href="/scripts/extjs/ux/css/BoxSelect.css" />
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="management.device.mainPanel.title" /></title>
<meta name="menu" content="DeviceMenu" />
</head>

<body class="home">
	<div id="tabs-1" class="x-hide-display" style="height: 200px">
		<p>
		<div class="groupItem">
			<div class="itemContent">
				<div align="center" style="width: 100%">
					<div id="deviceSearchCriteria" align="center" style="width: 100%;"></div>
					<div id="deviceGridPanel" align="center" style="width: 100%;"></div>
				</div>
			</div>
		</div>
		</p>
	</div>
	<jsp:include page="../common/sampleItem.jsp" />
	<jsp:include page="deviceMgtFunction.jsp" />
	<jsp:include page="deviceMgtSearchForm.jsp" />
	<jsp:include page="deviceMgtFirmwareHistoryForm.jsp" />
	<jsp:include page="deviceMgtPopupForm.jsp" />
	<jsp:include page="deviceMgtConfigForm.jsp" />
	<jsp:include page="deviceMgtForm.jsp" />
	<jsp:include page="deviceMgtLayout.jsp" />

</body>
</html>

