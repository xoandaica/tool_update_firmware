<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html lang="en">
    <head>
        <meta http-equiv="Cache-Control" content="no-store" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="<c:url value="/images/favicon.ico"/>" />
        <title><decorator:title /> | <fmt:message key="webapp.name" /></title>

        <%-- <link rel="stylesheet" type="text/css" media="all"
              href="<c:url value='/styles/lib/bootstrap-2.2.1.min.css'/>" />  --%>
        <link rel="stylesheet" type="text/css" media="all"
              href="<c:url value='/styles/bootstrap/bootstrap.css'/>" />
        <link rel="stylesheet" type="text/css" media="all"
              href="<c:url value='/styles/style.css'/>" />

        <decorator:head />


        <link rel="stylesheet" type="text/css" media="screen"
              href="<c:url value='/scripts/jquery-ui/css/redmond/jquery-ui-1.8.18.custom.css'/>" />
        <link rel="stylesheet" type="text/css" media="screen"
              href="<c:url value='/styles/wnms.css'/>" />
        <link rel="stylesheet" type="text/css" media="screen"
              href="<c:url value='/scripts/extjs/ux/css/CheckHeader.css'/>" />
        <link rel="stylesheet" type="text/css"
              href="<c:url value='/scripts/extjs/resources/css/ext-all.css'/>" />
        <link rel="stylesheet" type="text/css"
              href="<c:url value='/scripts/extjs/resources/css/ext-all-slate.css'/>" />
        <link rel="stylesheet" type="text/css" media="screen"
              href="<c:url value='/styles/nms.css'/>" />

        <script type="text/javascript"
        src="<c:url value='/scripts/lib/jquery-1.7.1.min.js'/>"></script>
        <script type="text/javascript"
        src="<c:url value='/scripts/lib/jquery-1.7.1.min.js'/>"></script>
        <script type="text/javascript"
        src="<c:url value='/scripts/lib/jquery-1.8.2.min.js'/>"></script>
        <script type="text/javascript"
        src="<c:url value='/scripts/lib/bootstrap-2.2.1.min.js'/>"></script>
        <script type="text/javascript"
        src="<c:url value='/scripts/lib/plugins/jquery.cookie.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/script.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/extjs/ext-all.js'/>"></script>
        <script
        src="<c:url value='/scripts/ajax/Ajax.js" type="text/javascript'/>"></script>

        <script type="text/javascript" src="/scripts/extjs/ux/BoxSelect.js"></script>
        <script
        src="<c:url value='/scripts/extjs_wnms.js" type="text/javascript'/>"></script>

        <script type="text/javascript" src="/scripts/extjs/ux/Component.js"></script>
        <!-- 	 QuyenNV: add file JS -->


        <script type="text/javascript" src="/scripts/extjs/ux/Notification.js"></script>
        <script type="text/javascript">
            Ext.QuickTips.init();
        </script>

        <!-- GC -->

        <style>
            body {
                padding-top: 43px;
            }
        </style>

        <!-- xuannv -->
        <link rel="stylesheet" href="/styles/lib/jquery-ui.css" />
        <script src="/scripts/lib/jquery-1.8.2.js"></script>
        <script src="/scripts/lib/jquery-ui.js"></script>
        <style>
            .bg123 {
                background-color: #eaaf51;
                border-color: #d99a36;
                -webkit-background-size: 40px 40px;
                background-size: 40px 40px;
                background-image: linear-gradient(135deg, rgba(255, 255, 255, .05) 25%,
                    transparent 25%, transparent 50%, rgba(255, 255, 255, .05) 50%,
                    rgba(255, 255, 255, .05) 75%, transparent 75%, transparent );
                -webkit-box-shadow: inset 0 -1px 0 rgba(255, 255, 255, .4);
                box-shadow: inset 0 -1px 0 rgba(255, 255, 255, .4);
                width: 100%;
                border: 1px solid;
                color: #fff;
                padding: 15px;
                position: fixed;
                text-shadow: 0 1px 0 rgba(0, 0, 0, .5);
                -webkit-animation: animate-bg 5s linear infinite;
                display: block;
                font: 12px Arial, Helvetica, sans-serif;
            }

            .toggler {
                width: 100%;
                height: 0px;
            }

            .bt123 {
                background: #ddd;
                display: inline-block;
                border: 1px solid #777;
                padding: 3px 7px;
                margin: 0 5px;
                font: bold 12px Arial, Helvetica;
                text-decoration: none;
                color: #333;
                -webkit-border-radius: 3px;
                border-radius: 3px;
                text-align: -webkit-match-parent;
                list-style-type: disc;
            }

            .bt123:hover {
                background: #ffffff;
            }

            #effect {
                width: 200px;
                height: 50px;
                padding: 0.4em;
                position: relative;
                float: right
            }
        </style>
<!--        <script type="text/javascript">
            $(function () {

                $("body")
                        .append(
                                "<div class=\"toggler\" style=\"position:absolute;top:20px;left:0px;z-index:20000\">"
                                + "<div id=\"effect\" class=\"ui-widget-content ui-corner-all bg123\">"
                                + "<p id=palarm style=\"position:relative;margin-top:0px; margin-left:8px;font-weight:bold;font-size:14px\">There are 3 alarm</p>"
                                + "<a href=\"#\" id =\"alarm_go\" class=\"bt123\" style=\"position:absolute;margin-left:30px;\">View</a>"
                                + "<a href=\"#\" id=\"alarm_hide\" class=\"bt123\" style=\"position:absolute;margin-left:120px;\">Hide</a>"
                                + "</div>" + "</div>");

                $("#effect").hide();
            });
        </script>-->


    </head>
    <body
        <decorator:getProperty property="body.id" writeEntireProperty="true"/>
        <decorator:getProperty property="body.class" writeEntireProperty="true"/>>
        <!-- xuannv -->
        <!-- xuannv -->
        <c:set var="currentMenu" scope="request">
            <decorator:getProperty property="meta.menu" />
        </c:set>

        <div class="navbar navbar-fixed-top" id='menubarid'>
            <div class="navbar-inner">
                <div class="container-fluid">
                    <%-- For smartphones and smaller screens --%>
                    <button type="button" class="btn btn-navbar" data-toggle="collapse"
                            data-target=".nav-collapse">
                        <span class="icon-bar"></span> <span class="icon-bar"></span> <span
                            class="icon-bar"></span>
                    </button>
                    <a class="brand" href="<c:url value='/'/>"><fmt:message
                            key="webapp.name" /></a>

                    <div id="switchLocale">
                        <a href="<c:url value='/?locale=en'/>"><img alt="English" src="/images/flag/english_flag.png"></a>
                        <a href="<c:url value='/?locale=vi'/>"><img alt="Vietnamese" src="/images/flag/vietnam_flag.png"></a>
                    </div>
                    <%@ include file="/common/menu.jsp"%>
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <%@ include file="/common/messages.jsp"%>
            <div class="row-fluid">
                <decorator:body />

                <c:if test="${currentMenu == 'AdminMenu'}">
<!--                    <div class="span2">
                        <menu:useMenuDisplayer name="Velocity" config="navlistMenu.vm"
                                               permissions="rolesAdapter">
                            <menu:displayMenu name="AdminMenu" />
                        </menu:useMenuDisplayer>
                    </div>-->
                </c:if>
            </div>
        </div>

        <div id="footer"
             style="bottom: 0; position: fixed; z-index: 150; _top: expression(eval(document.documentElement.scrollTop +( document.documentElement.clientHeight-this.offsetHeight) ) ); height: 35px;">
            <span class="left"><fmt:message key="webapp.version" /> <c:if
                    test="${pageContext.request.remoteUser != null}">
                    | <fmt:message key="user.status" /> ${pageContext.request.remoteUser}
                </c:if> </span> <span class="right"> &copy; <fmt:message
                    key="copyright.year" /> <a href="<fmt:message key="company.url"/>"><fmt:message
                        key="company.name" /></a>
            </span>
        </div>
        <%=(request.getAttribute("scripts") != null) ? request
                .getAttribute("scripts") : ""%>

    </body>
</html>
