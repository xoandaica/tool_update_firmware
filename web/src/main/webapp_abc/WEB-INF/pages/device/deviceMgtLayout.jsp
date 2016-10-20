<%-- 
    Document   : deviceMgtLayout
    Created on : Jun 8, 2015, 6:23:32 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var actionType = 0;
    var policyHistoryId = 0;
    Ext.onReady(function () {

        Ext.tip.QuickTipManager.init();
        Ext.create('Ext.Viewport', {
            title: 'Ext Layout Browser',
            hideCollapseTool: true,
            titleCollapse: true,
            layout: {
                type: 'border',
                padding: 1
            },
            collapsible: true,
            defaults: {
                collapsible: true,
                split: true
            },
            items: [
                Ext.create('Ext.tab.Panel',
                        {
                            region: 'center', // a center region is ALWAYS required for border layout
                            deferredRender: false,
                            margins: '45 0 30 0',
                            activeTab: 0, // first tab initially active
                            items: [
                                {
                                    contentEl: 'tabs-1',
                                    id: 'tabs1Id',
                                    title: '<fmt:message key="management.device.mainPanel.title"/>',
                                    autoScroll: true
                                }
                            ]
                        })
            ],
            listeners: {
                resize: function (width, height, oldWidth, oldHeight, eOpts) {
                    setTimeout(function () {
                        //DO SOMETHING
                        Ext.getCmp("deviceMgtForm_PanelId").setHeight(Ext.getCmp("tabs1Id").getHeight() - Ext.getCmp("deviceMgtSearch_FormId").getHeight());
                        deviceMgtSearch_Form.doLayout();
                        deviceMgtForm_Panel.doLayout();
                    }, 1);
                }
            }
        });
        deviceMgtSearch_Form.render('deviceSearchCriteria');
        deviceMgtForm_Panel.render('deviceGridPanel');
    });


</script>