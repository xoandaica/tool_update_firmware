<%@ include file="/common/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- 
    Document   : testLayout
    Created on : May 24, 2015, 5:38:35 PM
    Author     : Dell
--%>

<script type="text/javascript">
Ext.Loader.setPath('Ext.ux', '/scripts/extjs/ux');
Ext.require([ 'Ext.grid.*', 'Ext.data.*', 'Ext.ux.form.MultiSelect',
              'Ext.ux.form.ItemSelector', 'Ext.ux.ajax.JsonSimlet',
              'Ext.ux.ajax.SimManager',
//	              , 'Ext.ux.grid.FiltersFeature'
,
		'Ext.toolbar.Paging', 'Ext.ModelManager',
		'Ext.tip.QuickTipManager', 'Ext.util.*', 'Ext.state.*' ]);


    Ext.onReady(function () {
    	ActionLogComponents.build();
    	UserComponents.build();
    	
    	var mainPanel = Ext.create('Ext.tab.Panel', {
            region: 'center', 
        	margins     : '43 0 30 0', 
            activeTab: 0,     // first tab initially active
       
            listeners: {
                resize: function( width, height, oldWidth, oldHeight, eOpts){
                    setTimeout(function() {
//                     	if (oldHeight){
//                     		//Ext.getCmp("filterGridId").doLayout( ) ;
//                     	}
                    	Ext.getCmp("userGrid").setHeight(Ext.getCmp("tabs1Id").getHeight() - Ext.getCmp("formSearchId").getHeight());
//                         formSearch.doLayout();
//                         firmwareGrid.doLayout();
                    	UserComponents.searchForm.doLayout();
                    	UserComponents.panel.doLayout();
                    }, 1);
                }
            },
            items: [{
                contentEl: 'tabs-1',
                id: 'tabs1Id',
                title: '<fmt:message key="management.user.title"/>',
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

		UserComponents.searchForm.render("searchCriteria");
		UserComponents.panel.render("gridView");
		
		UserController.loadUserStore(1);
		UserController.loadRoles();
    });

</script>