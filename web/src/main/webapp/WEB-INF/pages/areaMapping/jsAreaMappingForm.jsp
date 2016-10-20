<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
var AreaMappingComponents = {
	build: function(){
		
		//Search form
		AreaMappingComponents.searchForm = new Ext.FormPanel({
	        labelWidth: 130,
	        id: 'areaMappingformSearchId',
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
							id : 'areaMapping.province',
							columnWidth: .25,
							labelAlign : 'top',
	                        layout: 'anchor',
	                        margin : '0 0 0 5',
							fieldLabel : '<fmt:message key="management.areaMapping.province"/>',
							name : 'searchCriteria.province'
						},
						 {
							xtype : 'textfield',
							id : 'areaMapping.district',
							columnWidth: .25,
							labelAlign : 'top',
	                        layout: 'anchor',
	                        margin : '0 0 0 5',
							fieldLabel : '<fmt:message key="management.areaMapping.district"/>',
							name : 'searchCriteria.district'
						},
						 {
							xtype : 'textfield',
							id : 'areaMapping.serialNumber',
							columnWidth: .25,
							labelAlign : 'top',
	                        layout: 'anchor',
	                        margin : '0 0 0 5',
							fieldLabel : '<fmt:message key="management.areaMapping.serialNumber"/>',
							name : 'searchCriteria.serialNumber'
						},
						{
							xtype : 'textfield',
							id : 'areaMapping.macAddress',
							columnWidth: .25,
							labelAlign : 'top',
	                        layout: 'anchor',
	                        margin : '0 0 0 5',
							fieldLabel : '<fmt:message key="management.areaMapping.macAddress"/>',
							name : 'searchCriteria.macAddress'
						},
						 /* {
							xtype : 'textfield',
							id : 'areaMapping.ip',
							columnWidth: .25,
							labelAlign : 'top',
	                        layout: 'anchor',
	                        margin : '0 0 0 5',
							fieldLabel : '<fmt:message key="management.areaMapping.ip"/>',
							name : 'searchCriteria.ip'
						}, */
	                ]

	            }
	        ],
	        buttons: [{
	                text: '<fmt:message key="button.search"/>',
	                id: 'areaMapping.save',
					handler: function() {
						areaMappingStore.loadPage(1, {});
	                }
	            },
	            {
	                text: '<fmt:message key="button.reset"/>',
	                id: 'areaMapping.reset',
	                handler: function() {
	                	this.up('form').getForm().reset();
	                }
	            },
	        ]
	    });
		
		var sm = Ext.create('Ext.selection.CheckboxModel', {
			checkOnly : true
		});
		//Grid
		AreaMappingComponents.grid = Ext.create('Ext.grid.Panel',
		{
			//renderTo: formIdRender,
			store : areaMappingStore,
			collapsible : false,
			selModel : sm,
			columnLines : true,
			tbar : [
					{
						text : '<fmt:message key="button.upload"/>',
						icon: '/styles/icons/fam/add.png',
						handler : function() {
							AreaMappingComponents.uploadWindow.show();
						}
					},
					{
						text : '<fmt:message key="button.delete"/>',
						icon: '/styles/icons/fam/delete.gif',
						handler : function() {
							var selected = AreaMappingComponents.grid.getView().getSelectionModel().getSelection();
			            	var ids = [];
			            	var isCheck = false;
							Ext.each(selected, function (item) {
								isCheck = true;
								//arrayList =arrayList+ '&deleteIds='+(item.get('id'));
								ids.push(item.get('id'));
							});

			            	if (!isCheck){
			            		Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.delete.select"/>');
			            	} else {
			            		Ext.Msg.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="message.delete.confirm"/>', function(e){if(e == 'yes'){
			            			AreaMappingController.deleteItems(ids);
								}});
				            	
			            	}

						}
					} 
				],
			columns : [
				Ext.create('Ext.grid.RowNumberer', {
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
					xtype : 'actioncolumn',
					width : 50,
					items : [
							{
								icon : "/styles/icons/fam/delete.gif", 
								tooltip : '<fmt:message key="button.delete"/>',
								handler : function(grid, rowIndex, colIndex) {
									var rec = grid.getStore().getAt(rowIndex);
				                    Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', "<fmt:message key="message.delete.confirm"/>", function(btn) {
				                        if (btn == 'yes') {
				                        	var ids = [];
				                        	ids.push(rec.get('id'));
				                        	AreaMappingController.deleteItems(ids);
				                        }
				                    });
								}
							},
							/* {
								icon : "/styles/icons/fam/edit_16x16.png",
								tooltip : '<fmt:message key="button.edit"/>',
								handler : function(grid, rowIndex, colIndex) {
									
								}
							},
							*/
					]
				},
				{
					text : '<fmt:message key="management.areaMapping.serialNumber"/>',
					flex : 1,
					sortable : true,
					filter : true,
					dataIndex : 'serialNumber'
				},
				{
					text : '<fmt:message key="management.areaMapping.macAddress"/>',
					flex : 1,
					sortable : true,
					filter : true,
					dataIndex : 'macAddress'
				},
				/* {
					text : '<fmt:message key="management.areaMapping.ip"/>',
					flex : 1,
					sortable : false,
					filter : true,
					dataIndex : 'ip'
				}, */
				{
					text : '<fmt:message key="management.areaMapping.province"/>',
					flex : 1,
					sortable : false,
					filter : true,
					dataIndex : 'province'
				},
				{
					text : '<fmt:message key="management.areaMapping.district"/>',
					flex : 1,
					sortable : false,
					filter : true,
					dataIndex : 'district'
				},
			],
					
			// paging bar on the bottom
			bbar : Ext.create('Ext.PagingToolbar',
				{
					store : areaMappingStore,
					displayInfo : true,
					displayMsg : '<fmt:message key="message.paging"/>',
					emptyMsg : "<fmt:message key="message.empty"/>"

				}),
			//height : 350,
			width : "90%",
			title : '<fmt:message key="management.areaMapping.title"/>',
			id : 'areaMappingGrid',
			//overflowX : 'scroll',
			viewConfig : {
				stripeRows : true
			}
		});
		
		//Container
		AreaMappingComponents.panel = Ext.create('Ext.form.Panel',
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
				AreaMappingComponents.searchForm,
				AreaMappingComponents.grid
			]
		});
		
		/* Upload Form */
		AreaMappingComponents.upLoadForm = new Ext.form.Panel({
			id: 'uploadForm',
			xtype: 'form',
            dock: 'left',
            frame: true,
            columnWidth: 1,
            bodyPadding: 0,
            items: [
                {
                    xtype: 'container',
                    anchor: '100%',
                    columnWidth: 1,
                    layout: 'column',
                    items: [
                        {
                            xtype: 'container',
                            columnWidth: 1,
                            layout: 'anchor',
                            items: [ {
                                    xtype: 'filefield',
                                    name: 'file',
                                    fieldLabel: '<fmt:message key="management.areaMapping.mappingFile"/>',
                                    labelWidth: 100,
                                    msgTarget: 'side',
                                    allowBlank: false,
                                    anchor: '99%',
                                    buttonText: '<fmt:message key="management.areaMapping.browseFile"/>'
                                }, 
                                {
                                    xtype: 'checkboxfield',
                                    checked: true,
                                    fieldLabel: '<fmt:message key="management.areaMapping.mappingSerial"/>',
                                    name: 'mappingSerial',
                                    id: 'mappingSerial',
                                    value: 1,
                                    inputValue: true,
                                    uncheckedValue: false
                                },
                                {
                                    xtype: 'checkboxfield',
                                    checked: false,
                                    fieldLabel: '<fmt:message key="management.areaMapping.mappingMacAddress"/>',
                                    name: 'mappingMacAddress',
                                    id: 'mappingMacAddress',
                                    value: 1,
                                    inputValue: true,
                                    uncheckedValue: false
                                },
                                {
                                    xtype: 'component',
                                    autoEl: {
                                        tag: 'a',
                                        href: '/template/AreaSample.xlsx',
                                        html: '<fmt:message key="management.areaMapping.template"/>'
                                    }
                                },
                                /* {
                                    xtype: 'checkboxfield',
                                    checked: false,
                                    fieldLabel: '<fmt:message key="management.areaMapping.mappingIp"/>',
                                    name: 'mappingIp',
                                    id: 'mappingIp',
                                }, */
                            ]
                        },
                    ]

                }
            ],
            buttons: [
                {
                    text: '<fmt:message key="button.upload"/>',
                    id: 'btnUpload',
                    handler: function() {
                    	var form = this.up('form').getForm();
                    	var fields = form.getFieldValues();
                    	if(!fields.mappingSerial && !fields.mappingMacAddress){
                    		Ext.Msg.alert('<fmt:message key="message.status"/>', "<fmt:message key="management.areaMapping.checkboxValidate"/>");
                    	}else{
//                      if (form.isValid()) {
                          form.submit({
                              url: 'uploadAreaMappingFile',
                              waitMsg: '<fmt:message key="management.areaMapping.uploadWaiting"/>',
                              handleResponse: function(response) {
                                  var res = JSON.parse(response.responseXML.body.textContent);
                                  return res;
                              },
                              success: function(fp, action) {
                            	  var result = JSON.parse(action.response.responseText);
                            	  var numberSuccess = 0;
                            	  for(var i in result.list){
                            		  if(result.list[i].status == true){
                            			  numberSuccess ++;
                            		  }
                            	  }
                            	  
                                  Ext.Msg.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.areaMapping.uploadSuccess"/>' 
                                		  + ' (' + numberSuccess + '/' + result.list.length + ')');
                                  AreaMappingComponents.uploadWindow.close();
                                  areaMappingStore.reload();
                              },
                              failure: function(form, action) {
                            	  Ext.Msg.alert('<fmt:message key="message.status"/>', "<fmt:message key="management.areaMapping.uploadFail"/>");
                              }
                          });
						}
                    }
                },
                {
                    text: '<fmt:message key="button.cancel"/>',
                    id: 'btnCancelUploading',
                    handler: function() {
                    	AreaMappingComponents.uploadWindow.close();
                    }
                },
            ],

        });
		
		AreaMappingComponents.uploadWindow = new Ext.Window({
		    frame: true,
		    title: '<fmt:message key="management.areaMapping.uploadTitle"/>',
		    width: '50%',
		    //minHeight: 200,
		    autoHeight: true,
		    modal: true,
		    items: [AreaMappingComponents.upLoadForm],
		    closeAction: 'hide',
		    listeners: {
		        'close': function(panel, eOpts) {
		            
		        }
		    }
		});
	}
}
   
</script>
