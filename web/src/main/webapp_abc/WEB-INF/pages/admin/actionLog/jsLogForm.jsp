<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
var ActionLogComponents = {
	build: function(){
		var startTime = Ext.create('Ext.ux.form.DateTimeField', {
	        anchor: '97%',
	        grow: true,
	        labelAlign: 'top',
	        margin : '0 0 0 5',
	        fieldLabel: '<fmt:message key="management.actionlog.starttime"/>',
	        format: 'd-m-Y',
	        name: "startTime"
	    });
	    var endTime = Ext.create('Ext.ux.form.DateTimeField', {
	        anchor: '97%',
	        grow: true,
	        labelAlign: 'top',
	        margin : '0 0 0 0',
	        fieldLabel: '<fmt:message key="management.actionlog.endtime"/>',
	        format: 'd-m-Y',
	        name: "endTime"
	        
	    });
		
		//Search form
		ActionLogComponents.searchForm = new Ext.FormPanel({
	        labelWidth: 130,
	        id: 'actionLogformSearchId',
	        title: '<fmt:message key="common.tab.search"/>',
	        frame: true,
	        border: false,
	        width: '100%',
	        autoHeight: true,
	        defaultType: 'textfield',
	        monitorValid: true,
	        items: [
	            {
	                xtype: 'container',
	                anchor: '100%',
	                columnWidth: 1,
	                layout: 'column',
	                items: [
	                    {
							xtype : 'textfield',
							id : 'actionLog.username',
							columnWidth: .33,
							labelAlign : 'top',
	                        layout: 'anchor',
	                        margin : '0 0 0 5',
							fieldLabel : '<fmt:message key="management.actionlog.username"/>',
							name : 'searchCriteria.username'
						},
						{
	                        xtype: 'container',
	                        columnWidth: .33,
	                        dock: 'right',
	                        layout: 'anchor',
	                        items: [startTime]
	                    },
	                    {
	                        xtype: 'container',
	                        columnWidth: .33,
	                        dock: 'right',
	                        layout: 'anchor',
	                        items: [endTime]
	                    }
	                ]

	            }
	        ],
	        buttons: [{
	                text: '<fmt:message key="button.search"/>',
	                id: 'actionLog.save',
					handler: function() {
						actionLogStore.loadPage(1, {});
	                }
	            },
	            {
	                text: '<fmt:message key="button.reset"/>',
	                id: 'actionLog.reset',
	                handler: function() {
	                	this.up('form').getForm().reset();
	                }
	            },
	        ]
	    });
		
		//Grid
		ActionLogComponents.grid = Ext.create('Ext.grid.Panel',
		{
			//renderTo: formIdRender,
			store : actionLogStore,
			collapsible : false,
			columnLines : true,
			columns : [Ext.create('Ext.grid.RowNumberer', {
	                header: 'No.',
	                width: 30,
	                renderer: function(v, p, record, rowIndex) {
	                    if (this.rowspan) {
	                        p.cellAttr = 'rowspan="' + this.rowspan + '"';
	                    }
	                    return rowIndex + 1;
	                }
	            }),
				{
					text : '<fmt:message key="management.actionlog.username"/>',
					flex : 1,
					sortable : true,
					filter : true,

					dataIndex : 'username'
				},
				{
					text : '<fmt:message key="management.actionlog.actionTime"/>',
					flex : 1,
					sortable : false,
					filter : true,

					dataIndex : 'actionTime'
				},
				{
					text : '<fmt:message key="management.actionlog.actionType"/>',
					flex : 1,
					sortable : false,
					filter : true,

					dataIndex : 'actionType'
				},
				{
					text : '<fmt:message key="management.actionlog.actionObject"/>',
					flex : 1,
					sortable : false,
					filter : true,

					dataIndex : 'actionObject'
				},
				{
					text : '<fmt:message key="management.actionlog.description"/>',
					flex : 1,
					sortable : false,
					filter : true,

					dataIndex : 'description'
				},
			],
					
			// paging bar on the bottom
			bbar : Ext.create('Ext.PagingToolbar',
				{
					store : actionLogStore,
					displayInfo : true,
					displayMsg : '<fmt:message key="message.paging"/>',
					emptyMsg : "<fmt:message key="message.empty"/>"

				}),
			//height : 350,
			width : "90%",
			title : '<fmt:message key="management.actionlog.title"/>',
			id : 'actionLogGrid',
			//overflowX : 'scroll',
			viewConfig : {
				stripeRows : true
			}
		});
		
		//Container
		ActionLogComponents.panel = Ext.create('Ext.form.Panel',
		{
			autoHeight : true,

			frame : true,
			listeners : {

				
			},
			width : '100%',
			bodyPadding : 0,
			layout : {
				pack : 'left'
			},
			defaults : {
				anchor : '100%',
				labelWidth : 100
			},
			items : [
				ActionLogComponents.searchForm,
				ActionLogComponents.grid
			]
		});
	}
}
   
</script>
