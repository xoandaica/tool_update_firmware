<%-- 
    Document   : policyMgtLayout
    Created on : Jun 5, 2015, 3:31:52 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var actionType = 0;
    var policyHistoryId = "";
    var thisMask = new Ext.LoadMask(Ext.getBody(), {
        msg: "Please wait..."
    });
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
                                    title: '<fmt:message key="management.policy.mainPanel.title"/>',
                                    autoScroll: true
                                }
                            ]
                        })
            ],
            listeners: {
                resize: function (width, height, oldWidth, oldHeight, eOpts) {
                    setTimeout(function () {
                        //DO SOMETHING
                        Ext.getCmp("policyMgtForm_PanelId").setHeight(Ext.getCmp("tabs1Id").getHeight() - Ext.getCmp("policyMgtSearch_FormId").getHeight());
                        policyMgtSearch_Form.doLayout();
                        policyMgtForm_Panel.doLayout();
                    }, 1);
                }
            }
        });
        policyMgtSearch_Form.render('policySearchCriteria');
        policyMgtForm_Panel.render('policyGridPanel');
        policyMgtForm_Store.loadPage(1);
    });


</script>