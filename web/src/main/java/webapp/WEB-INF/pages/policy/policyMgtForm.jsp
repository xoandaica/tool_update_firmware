<%-- 
    Document   : policyMgtForm
    Created on : Jun 5, 2015, 3:31:39 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var policyMgtForm_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'name', 'policyStatus', 'modelId', 'model', 'firmwareId', 'firmware', 'provinceId', 'districtId', 'enable', 'startTime', 'endTime', 'deviceSuccessNo', 'deviceFailedNo', 'totalDeviceNo'],
        proxy: {
            type: 'ajax',
            url: 'loadPolicy',
            reader: {
                root: 'list',
                type: 'json',
                totalProperty: 'totalCount'
            }
        },
        pageSize: 20,
        autoLoad: false,
        listeners: {
            'beforeload': function (store, options) {
                this.proxy.extraParams.policyName = policyName_Search.getValue();
                this.proxy.extraParams.policyStatus = policyStatus_Search.getValue();
                this.proxy.extraParams.policyEnable = policyEnable_Search.getValue();
                this.proxy.extraParams.startTime = startTime_Search.getValue();
                this.proxy.extraParams.endTime = endTime_Search.getValue();
            },
            load: function (store, records, success) {
                if (!success) {
                    var msgr = 'LoadPolicy fail !!!';
                    Ext.MessageBox.show({title: 'Warning', msg: msgr, buttons: Ext.MessageBox.OK, icon: Ext.Msg.WARNING});
                }
            }
        }
    });
    policyMgtForm_Store.loadPage(1, {});
    var smPolicy = Ext.create('Ext.selection.CheckboxModel', {
        checkOnly: true
    });
    var policyMgtForm_Panel = Ext.create('Ext.grid.Panel', {
        title: '<fmt:message key="management.policy.gridPanel.title"/>',
        store: policyMgtForm_Store,
        selType: 'cellmodel',
        selModel: smPolicy,
        id: 'policyMgtForm_PanelId',
        tbar: [
            {//add 
                text: '<fmt:message key="management.policy.gridPanel.tbar.text"/>',
                icon: iconForder + 'add.png',
                handler: function () {
                    actionType = 1; //create
                    policyId.setValue('');
                    policyName.setValue('');
                    policyModelType.setValue(null);
                    policyFirmware.setValue(null);
                    policyProvince.setValue(null);
                    policyDistrict.setValue(null);
                    policyStartTime.setValue(null);
                    policyEndTime.setValue(null);
                    policyEnable.setValue('1');
                    policyMgtPopupForm.setTitle('<fmt:message key="management.policy.popupPanel.title.edit"/>');
                    policyMgtPopupForm.show();
                    policyMgtPopupForm.setTitle('<fmt:message key="management.policy.popupPanel.title.create"/>');
                    policyMgtPopupForm.show();
                }
            },
            {//delete
                text: '<fmt:message key="button.delete"/>',
                icon: iconForder + 'delete.gif', // Use a URL in the icon config			                
                handler: function () {
                    var idList = '';
                    var itemNo = 0;
                    selected = policyMgtForm_Panel.getView().getSelectionModel().getSelection();
                    Ext.each(selected, function (item) {
                        if (item.data.policyStatus == 0) {//cannot delete running or done policy 
                            itemNo++;
                            if (idList == '')
                                idList = idList + (item.data.id);
                            else
                                idList = idList + ',' + (item.data.id);
                        }
                    });
                    if (itemNo == 0)
                        return;
                    var msg = '<fmt:message key="management.policy.message.delete.confirm"/>';
                    Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', msg, function (btn) {
                        if (btn == 'yes')
                            deletePolicy(idList);
                        if (btn == 'no' || btn == 'cancel')
                            smPolicy.deselectAll(true);
                    });
                }
            }

        ], //end of tbar
        columns: [
            Ext.create('Ext.grid.RowNumberer', {
                header: 'No.',
                width: 30,
                align: 'center',
                renderer: function (v, p, record, rowIndex) {
                    if (this.rowspan) {
                        p.cellAttr = 'rowspan="' + this.rowspan + '"';
                    }
                    return rowIndex + 1;
                }
            }),
            {
                xtype: 'actioncolumn',
                text: '<fmt:message key="management.policy.gridPanel.columns.action"/>',
                width: 60,
                align: 'center',
                items: [
                    {//view history
                        icon: iconForder + 'application_view_list.png', // Use a URL in the icon config			                
                        tooltip: '<fmt:message key="management.policyHistory.gridPanel.title"/>',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            policyHistoryId = rec.get('id');
                            policyMgtHistoryForm_Store.loadPage(1, {
                                page: 1,
                                params: {
                                    itemId: policyHistoryId
                                }
                            });
                            policyMgtHistoryForm.show();
                        }
                    }, {//edit
                        icon: iconForder + 'edit.png', // Use a URL in the icon config			                
                        tooltip: '<fmt:message key="button.edit"/>',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            if (rec.get('policyStatus') == 2) {
                                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.edit.fail"/>', function () {
                                });
                                return;
                            } else {
                                actionType = 2; //edit
                                policyId.setValue(rec.get('id'));
                                policyName.setValue(rec.get('name'));
                                policyModelType.setValue(rec.get('modelId'));
                                policyFirmware_Store.getProxy().extraParams = {
                                    modelTypeId: rec.get('modelId')
                                };
                                policyFirmware_Store.load();
                                policyFirmware.setValue(rec.get('firmwareId'));
                                policyProvince.setValue("");
                                if (rec.get('provinceId') != "") {
                                    var provinceList = rec.get('provinceId').split(',').map(function (item) {
                                        return parseInt(item, 10);
                                    });
                                    policyProvince.setValue(provinceList);
                                }
                                policyDistrict.setValue(null);
                                policyDistrict_Store.getProxy().extraParams = {
                                    provinceListId: policyProvince.getValue().toString()
                                };
                                policyDistrict_Store.load();
                                if (rec.get('districtId') != "") {
                                    var districtList = rec.get('districtId').split(',').map(function (item) {
                                        return parseInt(item, 10);
                                    });
                                    policyDistrict.setValue(districtList);
                                }
                                policyStartTime.setValue(rec.get('startTime'));
                                policyEndTime.setValue(rec.get('endTime'));
                                policyEnable.setValue(rec.get('enable'));
                                policyMgtPopupForm.setTitle('<fmt:message key="management.policy.popupPanel.title.edit"/>');
                                policyMgtPopupForm.show();
                            }
                        }
                    }, {//delete
                        icon: iconForder + 'delete.gif', // Use a URL in the icon config			                
                        tooltip: '<fmt:message key="button.delete"/>',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="management.policy.message.delete.confirm"/>', function (btn) {
                                if (btn == 'yes') {
                                    if (rec.get('policyStatus') != 0) {
                                        Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.delete.fail"/>', function () {
                                        });
                                    } else {
                                        var idList = rec.get('id');
                                        deletePolicy(idList);
                                    }

                                }
                            });
                        }
                    }]
            },
            {
                text: 'Id',
                dataIndex: 'id',
                hidden: true
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.name"/>',
                dataIndex: 'name',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.model"/>',
                dataIndex: 'model',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.firmware"/>',
                dataIndex: 'firmware',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.area"/>',
                dataIndex: 'areaList',
                hidden: true
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.startTime"/>',
                dataIndex: 'startTime',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.endTime"/>',
                dataIndex: 'endTime',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.policyStatus"/>',
                dataIndex: 'policyStatus',
                flex: 1,
                renderer: function (value, meta, record) {
                    if (value == 0) {
                        return '<fmt:message key="management.policy.searchForm.status.notRun"/>';
                    } else if (value == 1) {
                        return '<fmt:message key="management.policy.searchForm.status.running"/>';
                    } else if (value == 2) {
                        return '<fmt:message key="management.policy.searchForm.status.done"/>';
                    }
                }
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.enable"/>',
                dataIndex: 'enable',
                flex: 1,
                renderer: function (value, meta, record) {
                    if (value == 0) {
                        return '<fmt:message key="management.policy.searchForm.enable.inactive"/>';
                    } else if (value == 1) {
                        return '<fmt:message key="management.policy.searchForm.enable.active"/>';
                    }
                }
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.totalDeviceNo"/>',
                dataIndex: 'totalDeviceNo',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.deviceSuccessNo"/>',
                dataIndex: 'deviceSuccessNo',
                flex: 1
            }, {
                text: '<fmt:message key="management.policy.gridPanel.columns.deviceFailedNo"/>',
                dataIndex: 'deviceFailedNo',
                flex: 1
            }
        ],
        listeners: {
            itemdblclick: function (dv, record, item, index, e) {
                var rec = record;
                if (rec.get('policyStatus') == 2) {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.edit.fail"/>', function () {
                    });
                    return;
                } else {
                    actionType = 2; //edit
                    policyId.setValue(rec.get('id'));
                    policyName.setValue(rec.get('name'));
                    policyModelType.setValue(rec.get('modelId'));
                    policyFirmware_Store.getProxy().extraParams = {
                        modelTypeId: rec.get('modelId')
                    };
                    policyFirmware_Store.load();
                    policyFirmware.setValue(rec.get('firmwareId'));
                    policyProvince.setValue("");
                    if (rec.get('provinceId') != "") {
                        var provinceList = rec.get('provinceId').split(',').map(function (item) {
                            return parseInt(item, 10);
                        });
                        policyProvince.setValue(provinceList);
                    }
                    policyDistrict.setValue(null);
                    policyDistrict_Store.getProxy().extraParams = {
                        provinceListId: policyProvince.getValue().toString()
                    };
                    policyDistrict_Store.load();
                    if (rec.get('districtId') != "") {
                        var districtList = rec.get('districtId').split(',').map(function (item) {
                            return parseInt(item, 10);
                        });
                        policyDistrict.setValue(districtList);
                    }
                    policyStartTime.setValue(rec.get('startTime'));
                    policyEndTime.setValue(rec.get('endTime'));
                    policyEnable.setValue(rec.get('enable'));
                    policyMgtPopupForm.setTitle('<fmt:message key="management.policy.popupPanel.title.edit"/>');
                    policyMgtPopupForm.show();
                }
            }
        },
        dockedItems: [{
                xtype: 'pagingtoolbar',
                store: policyMgtForm_Store, // same store GridPanel is using
                dock: 'bottom',
                displayInfo: true
            }]
    });
</script>