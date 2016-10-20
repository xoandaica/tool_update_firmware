<%@ include file="/common/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- 
    Document   : testLayout
    Created on : May 24, 2015, 5:38:35 PM
    Author     : Dell
--%>

<script type="text/javascript">

    Ext.onReady(function () {
    	ActionLogComponents.build();
    	
    	var mainPanel = Ext.create('Ext.tab.Panel', {
            region: 'center', 
        	margins     : '43 0 30 0', 
            activeTab: 0,     // first tab initially active
       
            listeners: {
                resize: function( width, height, oldWidth, oldHeight, eOpts){
                    setTimeout(function() {
                    	/* if (oldHeight){
                    		//Ext.getCmp("filterGridId").doLayout( ) ;
                    	} */
                    	
                    	Ext.getCmp("actionLogGrid").setHeight(Ext.getCmp("tabs1Id").getHeight() - Ext.getCmp("actionLogformSearchId").getHeight());
                    
                    	ActionLogComponents.searchForm.doLayout();
                    	ActionLogComponents.panel.doLayout();
                    }, 1);
                }
            },
            items: [{
                contentEl: 'tabs-1',
                id: 'tabs1Id',
                title: '<fmt:message key="management.actionlog.title"/>',
              //  closable: true,
                autoScroll: true
            }]
        });
		
		Ext.create('Ext.Viewport', {
	        layout: 'border',
	        title: 'Ext Layout Browser',
	        id:'IdViewPort',
	        xtype: 'container',
	        layout: {
	            type: 'border',
	            padding: 1
	        },

	        items: [ 
	    			mainPanel
	        ],
	        //renderTo: "mainPanel"
	    });

		ActionLogComponents.searchForm.render("searchCriteria");
		ActionLogComponents.panel.render("gridView");
		
		actionLogStore.loadPage(1, {});
    });

</script>