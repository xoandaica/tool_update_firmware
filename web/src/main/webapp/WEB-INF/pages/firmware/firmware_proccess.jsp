<%-- 
    Document   : actionlog_proccess
    Created on : May 29, 2015, 1:56:56 PM
    Author     : longdq
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script>
    Ext.onReady(function () {

        Ext.tip.QuickTipManager.init();

        Ext.create('Ext.Viewport', {
            layout: 'border',
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
                                    title: '<fmt:message key="management.firmware.tittle"/>',
                                    autoScroll: true
                                },
                            ]
                        })
            ],
            listeners: {
                resize: function (width, height, oldWidth, oldHeight, eOpts) {
                    setTimeout(function () {
                        //DO SOMETHING
                        Ext.getCmp("firmwareGridId").setHeight(Ext.getCmp("tabs1Id").getHeight() - Ext.getCmp("formSearchId").getHeight());
                        uploadForm.doLayout();
                        formSearch.doLayout();
                        firmwareGrid.doLayout();
                    }, 1);
                },
            },
// 				        renderTo: 'alarmTypeConfigId'
        });

        uploadForm.render('uploadFile');
        formSearch.render('searchCriteria');
        firmwareGrid.render('frimwareGrid');
        modelStore.load();
        firmwareGridStore.load();
    });


</script>
