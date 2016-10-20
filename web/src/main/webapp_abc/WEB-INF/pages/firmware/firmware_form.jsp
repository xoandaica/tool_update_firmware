<%-- 
    Document   : actionlog_form
    Created on : May 29, 2015, 11:10:45 AM
    Author     : longdq
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script>
    Ext.Loader.setPath('Ext.ux', '/scripts/extjs/ux');
    Ext.require(['Ext.grid.*', 'Ext.data.*', 'Ext.ux.form.MultiSelect',
        'Ext.ux.form.ItemSelector', 'Ext.ux.ajax.JsonSimlet',
        'Ext.ux.ajax.SimManager',
//	              , 'Ext.ux.grid.FiltersFeature'
        ,
                'Ext.toolbar.Paging', 'Ext.ModelManager',
        'Ext.tip.QuickTipManager', 'Ext.util.*', 'Ext.state.*']);
    var version = Ext.create('Ext.form.field.Text', {
        xtype: 'textareafield',
        grow: true,
        fieldLabel: '<fmt:message key="management.firmware.version"/>',
        name: 'version',
        labelAlign: 'top',
        anchor: '97%'
    });
    var modelStore = new Ext.data.JsonStore({
        autoLoad: false,
        storeId: 'modelStoreId',
        fields: [{name: 'id'},
            {name: 'name', type: 'String', sortType: Ext.data.SortTypes.asUCString}
        ],
        proxy: {
            type: 'ajax',
            url: 'getAllModel', // url that will load data with respect to start and limit params
            reader: {
                type: 'json',
                root: 'modelList',
            }
        },
    });
    modelStore.sort('name', 'ASC');
    var modelSearch = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.firmware.model"/>',
        name: 'model',
        id: "model",
        labelAlign: 'top',
        emptyText: '<fmt:message key="common.combobox.emptytext"/>',
        anchor: '99%',
        editable: true,
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        labelWidth: 100,
        store: modelStore,
        queryMode: 'local',
        grow: true
    });
    var startDate = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '97%',
        grow: true,
        labelAlign: 'top',
        fieldLabel: '<fmt:message key="management.firmware.starttime"/>',
        format: 'd-m-Y',
        name: "startDate"
    });
    var endDate = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '97%',
        grow: true,
        labelAlign: 'top',
        fieldLabel: '<fmt:message key="management.firmware.endtime"/>',
        format: 'd-m-Y',
        name: "endDate"
    });
    formSearch = new Ext.FormPanel({
        labelWidth: 130,
        id: 'formSearchId',
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
                        xtype: 'container',
                        columnWidth: .5,
                        dock: 'right',
                        layout: 'anchor',
                        items: [modelSearch]
                    },
                    {
                        xtype: 'container',
                        columnWidth: .5,
                        dock: 'right',
                        layout: 'anchor',
                        items: [version]
                    }


                ]

            }
        ],
        buttons: [{
                text: '<fmt:message key="common.tab.search"/>',
                id: 'searchbtn',
                handler: searchFirmware
            },
            {
                text: '<fmt:message key="common.button.reset"/>',
                id: 'reset',
                handler: function() {
                    formSearch.getForm().reset();
                }
            },
            /* {
             text: key="common.button.checkNew",
             id: 'checkBtn',
             handler: FirmwareController.checkNewFirmware
             },*/
        ]
    });
//=========================================================================grid=========================================
    Ext.define('FirmwareModel', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'no'},
            {name: 'id'},
            {name: 'modelId'},
            {name: 'modelName'},
            {name: 'version'},
            {name: 'releaseDate'},
            {name: 'releaseNote'},
            {name: 'path'},
            {name: 'usageNo'},
            {name: 'policies'},
            {name: 'fwDefault'}
        ],
        idProperty: 'firmwareModel'
    });
    firmwareGridStore = Ext.create('Ext.data.JsonStore', {
        id: 'firmwareStoreId',
        autoLoad: false,
        autoSync: true,
        pageSize: 20,
        model: 'FirmwareModel',
        fields: ['no', 'id', 'modelId', 'modelName', 'version', 'releaseDate', 'releaseNote', 'path', 'usageNo', 'policies'],
        proxy: {
            type: 'ajax',
            url: 'searchFirmware', // url that will load data with respect to start and limit params
            reader: {
                type: 'json',
                root: 'firmwareList',
                id: 'no',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            'beforeload': function(store, options) {
                this.proxy.extraParams.startDate = startDate.getRawValue();
                this.proxy.extraParams.endDate = endDate.getRawValue();
                this.proxy.extraParams.model = modelSearch.getValue();
                this.proxy.extraParams.version = version.getValue();
            },
            load: function(store, records, success) {
//                if (!success) {
//                    var msgr = '<fmt:message key="ajax.failed"/>';
//                    Ext.MessageBox.show({title: 'Warning', msg: msgr, buttons: Ext.MessageBox.OK, icon: Ext.Msg.WARNING});
//                }
            }
        }
    });
    var sm = Ext.create('Ext.selection.CheckboxModel', {
        checkOnly: true
    });
    var firmwareGrid = Ext.create('Ext.grid.Panel', {
        store: firmwareGridStore,
        id: 'firmwareGridId',
        collapsible: false,
        scroll: true,
        height: 200,
        columnLines: true,
        selType: 'cellmodel',
        selModel: sm,
        align: 'center',
        title: '<fmt:message key="management.firmware.gridname"/>',
        viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            getRowClass: function(record) {
                if (record.get('fwDefault') == 1)
                    return "default-color-row";
            }
        },
        columns: [
            Ext.create('Ext.grid.RowNumberer', {
                renderer: function(v, p, record, rowIndex) {
                    if (this.rowspan) {
                        p.cellAttr = 'rowspan="' + this.rowspan + '"';
                    }
                    return rowIndex + 1;
                },
                width: 30
            }),
            {
                xtype: 'actioncolumn',
                width: 65,
                items: [
                    {
                        icon: "/styles/icons/fam/delete.gif",
                        tooltip: '<fmt:message key="button.delete"/>',
                        handler: function(grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            if (rec.get('policies') != "") {
                                ExtWnms.alertError("<fmt:message key="message.firmware.nodelete3"/>" + rec.get('policies'));
                                return;
                            } else {
                                Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', "<fmt:message key="message.delete.confirm"/>", function(btn) {
                                    if (btn == 'yes') {
                                        if (rec.get('usageNo') != 0) {
                                            ExtWnms.alertError("<fmt:message key="message.firmware.nodelete"/>");
                                            return;
                                        }
                                        var ids = [];
                                        var count = 0;
                                        if (rec.get('usageNo') == 0) {
                                            ids.push(rec.get('id'));
                                        }

                                        FirmwareController.deleteItems(ids);
                                    }
                                });
                            }

                        }
                    }, {//edit
                        icon: '/styles/icons/fam/edit_16x16.png',
                        tooltip: '<fmt:message key="button.edit"/>',
                        handler: function(grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            fwModel.setReadOnly(true);
                            fwVersion.setReadOnly(true);
                            fwReleaseDate.setReadOnly(true);
                            fwModel.setValue(rec.get('modelId'));
                            fwVersion.setValue(rec.get('version'));
                            fwReleaseDate.setValue(rec.get('releaseDate'));
                            fwId.setValue(rec.get('id'));
                            fwPath.setValue(rec.get('path'));
                            fwReleaseNote.setValue(rec.get('releaseNote'));
                            firmwarePopup.setTitle('<fmt:message key="management.firmware.edit.title"/>');
                            firmwarePopup.show();
                        }
                    }
                ]
            },
//			 							
            {
                text: '<fmt:message key="management.firmware.version"/>',
                sortable: true,
                flex: 1,
                align: 'left',
                hidden: false,
                dataIndex: 'version',
            },
            {
                text: '<fmt:message key="management.firmware.model"/>',
                flex: 1,
                sortable: false,
                align: 'left',
                dataIndex: 'modelName',
            },
            {
                text: '<fmt:message key="management.firmware.releasedate"/>',
                sortable: true,
                flex: 1,
                align: 'left',
                hidden: true,
                dataIndex: 'releaseDate',
            },
            {
                text: '<fmt:message key="management.firmware.path"/>',
                sortable: true,
                flex: 3,
                align: 'left',
                hidden: false,
                dataIndex: 'path',
            },
            {
                text: '<fmt:message key="management.firmware.releasenote"/>',
                sortable: true,
                flex: 1,
                align: 'left',
                hidden: true,
                dataIndex: 'releaseNote'
            },
            {
                text: '<fmt:message key="management.firmware.default"/>',
                sortable: true,
                flex: 1,
                align: 'left',
                hidden: false,
                dataIndex: 'fwDefault'
            },
            {
                
                sortable: true,
                xtype: 'componentcolumn',
                flex: 1,
                align: 'left',
                hidden: false,
                dataIndex: 'id',
                renderer: function(value) {
                    return {
                        xtype: 'button',
                        text: '<fmt:message key="management.user.reset"/>',
                        tooltip: '<fmt:message key="management.user.resetTooltip"/>',
                        handler: function() {
                            //Code to here
                            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', "<fmt:message key="management.firmware.resetConfirm"/>", function(btn) {
                                if (btn == 'yes') {
                                    // UserController.resetPassword(value);
                                    FirmwareController.setDefault(value);
                                }
                            });
                        }
                    }
                }
            },
        ],
        tbar: [
            {
                text: '<fmt:message key="button.add"/>',
                icon: '/images/icons/add.png',
                handler: function() {
                    fwModel.setReadOnly(false);
                    fwVersion.setReadOnly(false);
                    fwReleaseDate.setReadOnly(false);
                    firmwarePopup.setTitle('<fmt:message key="management.firmware.create.title"/>');
                    firmwarePopup.show();
                }
            },
            {
                text: '<fmt:message key="button.delete"/>',
                icon: '/styles/icons/fam/delete.gif',
                handler: function() {
                    var selected = firmwareGrid.getSelectionModel().getSelection();
                    var ids = [];
                    var isCheck = false;
                    var count = 0;
                    Ext.each(selected, function(item) {
                        isCheck = true;
                        //arrayList =arrayList+ '&deleteIds='+(item.get('id'));
                        if (item.get('usageNo') == 0 && item.get('policies') == "") {
                            ids.push(item.get('id'));
                        }
                        else
                            count++;
                    });

                    if (!isCheck) {
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.delete.select"/>');
                    } else {
                        if (count == 0) {
                            Ext.Msg.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="message.delete.confirm"/>', function(e) {
                                if (e == 'yes') {
                                    FirmwareController.deleteItems(ids);
                                }
                            });
                        } else {
                            Ext.Msg.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="message.firmware.nodelete2"/>', function(e) {
                                if (e == 'yes') {
                                    FirmwareController.deleteItems(ids);
                                }
                            });
                        }

                    }

                }
            }
        ],
        listeners: {
            itemdblclick: function(dv, record, item, index, e) {
                var rec = record;
                fwModel.setReadOnly(true);
                fwVersion.setReadOnly(true);
                fwReleaseDate.setReadOnly(true);
                fwModel.setValue(rec.get('modelId'));
                fwVersion.setValue(rec.get('version'));
                fwReleaseDate.setValue(rec.get('releaseDate'));
                fwId.setValue(rec.get('id'));
                fwPath.setValue(rec.get('path'));
                fwReleaseNote.setValue(rec.get('releaseNote'));
                firmwarePopup.setTitle('<fmt:message key="management.firmware.edit.title"/>');
                firmwarePopup.show();
            }
        },
        dockedItems: [{
                xtype: 'pagingtoolbar',
                store: firmwareGridStore, // same store GridPanel is using
                dock: 'bottom',
                displayInfo: true
            }],
    });


    function searchFirmware() {
        firmwareGridStore.load();
    }
//------------------------------------popup variable----------------------------------------------------



    var fwModelStore = new Ext.data.JsonStore({
        autoLoad: false,
        storeId: 'fwModelStoreId',
        fields: [{name: 'id'},
            {name: 'name', type: 'String', sortType: Ext.data.SortTypes.asUCString}
        ],
//        proxy: {
//            type: 'ajax',
//            url: 'getAllModel', // url that will load data with respect to start and limit params
//            reader: {
//                type: 'json',
//                root: 'modelList',
//            }
//        },
    });
    fwModelStore.sort('name', 'ASC');
    var fwModel = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.firmware.model"/>',
        name: 'model',
        id: "fwModelId",
        labelAlign: 'top',
        emptyText: '<fmt:message key="common.combobox.emptytext"/>',
        anchor: '96%',
        editable: true,
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        labelWidth: 100,
        store: modelStore,
        queryMode: 'local',
        grow: true
    });

    var fwVersion = Ext.create('Ext.form.field.Text', {
        xtype: 'textareafield',
        grow: true,
        fieldLabel: '<fmt:message key="management.firmware.version"/>',
        name: 'fwVersion',
        labelAlign: 'top',
        anchor: '90%'
    });
    var fwId = Ext.create('Ext.form.field.Text', {
        xtype: 'textareafield',
        grow: true,
        fieldLabel: 'ID',
        name: 'fwId',
        labelAlign: 'top',
        anchor: '90%'
    });
    var fwPath = Ext.create('Ext.form.field.Text', {
        xtype: 'textareafield',
        grow: true,
        fieldLabel: '<fmt:message key="management.firmware.path"/>',
        name: 'fwPath',
        labelAlign: 'top',
        anchor: '90%'
    });
    var fwReleaseDate = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '97%',
        grow: true,
        labelAlign: 'top',
        fieldLabel: '<fmt:message key="management.firmware.releasedate"/>',
        format: 'd-m-Y',
        name: "fwReleaseDate"
    });
    var fwReleaseNote = Ext.create('Ext.form.field.Text', {
        xtype: 'textareafield',
        grow: true,
        fieldLabel: '<fmt:message key="management.firmware.releasenote"/>',
        name: 'fwReleaseNote',
        labelAlign: 'top',
        anchor: '90%'
    });



    detailForm = new Ext.form.Panel({
        // xtype: 'form',
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
                        columnWidth: .3,
                        layout: 'anchor',
                        items: [fwModel]
                    },
                    {
                        xtype: 'container',
                        columnWidth: .3,
                        layout: 'anchor',
                        items: [fwVersion]
                    },
                    {
                        xtype: 'container',
                        columnWidth: .38,
                        layout: 'anchor',
                        items: [fwPath]
                    }

                ]

            },
            {xtype: 'container',
                anchor: '100%',
                columnWidth: 1,
                layout: 'column',
                items: [
                    {
                        xtype: 'container',
                        columnWidth: .3,
                        layout: 'anchor',
                        items: [fwReleaseDate]
                    },
                    {
                        xtype: 'container',
                        columnWidth: .3,
                        layout: 'anchor',
                        items: [fwReleaseNote]
                    },
                    {
                        xtype: 'container',
                        columnWidth: .3,
                        layout: 'anchor',
                        items: [fwId],
                        hidden: true
                    }

                ]

            }
        ],
        buttons: [
            {
                text: '<fmt:message key="button.save"/>',
                id: 'btnSave',
                handler: FirmwareController.saveFirmware
            },
            {
                text: '<fmt:message key="button.reset"/>',
                id: 'btnCancel',
                handler: function() {
                    detailForm.getForm().reset();
                    // Ext.getCmp('btnDelete').setDisabled(true);
                }
            }
        ],
        listeners: {
            afterRender: function(thisForm, options) {
                this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                    enter: FirmwareController.saveFirmware
                });
            },
        }, //end of listeners

    });

    var firmwarePopup = new Ext.Window({
        frame: true,
        width: '80%',
        minHeight: 100,
        autoHeight: true,
        modal: true,
        items: [detailForm],
        closeAction: 'hide',
        listeners: {
            'close': function(panel, eOpts) {
            },
            show: function(panel, eOpts) {
                modelStore.clearFilter();
                firmwarePopup.setHeight(detailForm.getHeight() + 30);
            },
            beforehide: function(thisForm, options) {
                console.log("beforehide");
                detailForm.getForm().reset();
            },
        }
    });
</script>
