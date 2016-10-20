<%-- 
    Document   : areaMgtLayout
    Created on : May 15, 2015, 11:06:51 AM
    Author     : Dell
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>




<script type="text/javascript">

    Ext.onReady(function () {
        var mainPanel = Ext.create('Ext.tab.Panel', {
            region: 'center',
            margins: '43 0 30 0',
            activeTab: 0, // first tab initially active
            listeners: {
                resize: function (width, height, oldWidth, oldHeight, eOpts) {
                    setTimeout(function () {
                        if (oldHeight) {
                            Ext.getCmp("areaManagementViewPort").doLayout( );
                        }
                    }, 1);
                }
            },
            items: [{
                    contentEl: 'tabs-1',
                    title: '<fmt:message key="management.area.mainPanel.title"/>',
                    //  closable: true,
                    autoScroll: true,
                    items : [areaMgtSearch_Form, areaMgtForm_Panel]
                }]
        });

        Ext.create('Ext.Viewport', {
            title: 'Ext Layout Browser',
            id: 'areaManagementViewPort',
            xtype: 'container',
            layout: {
                type: 'border',
                padding: 1
            },
            items: [
                mainPanel
            ]
        });
    });

</script>

