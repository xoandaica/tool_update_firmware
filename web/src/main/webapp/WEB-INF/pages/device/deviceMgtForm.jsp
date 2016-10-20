<%-- 
    Document   : deviceMgtForm
    Created on : Jun 8, 2015, 6:27:12 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var deviceMgtForm_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'mac', 'serialNumber', 'modelId', 'modelName',
            'provinceId', 'province', 'districtId', 'district', 'ram', 'cpu', 'rom',
            'firmwareVersion', 'firmwareStatus', 'cpeStatus', 'stbUsername', 'stbPassword', 'ipAddress', 'homepageUrl', 'upgradeUrl', 'connectionReq'],
        proxy: {
            type: 'ajax',
            url: 'loadDevice',
            reader: {
                root: 'list',
                type: 'json',
                totalProperty: 'totalCount'
            }
        },
        pageSize: 500,
        autoLoad: false,
        listeners: {
            'beforeload': function(store, options) {
                this.proxy.extraParams.deviceMAC = deviceMAC_Search.getValue();
                this.proxy.extraParams.deviceSerialNumber = deviceSerialNumber_Search.getValue();
                this.proxy.extraParams.deviceStatus = deviceStatus_Search.getValue();
                this.proxy.extraParams.deviceFirmwareStatus = deviceFirmwareStatus_Search.getValue();
                this.proxy.extraParams.deviceModel = deviceModel_Search.getValue();
                this.proxy.extraParams.deviceFirmware = deviceFirmware_Search.getRawValue();
                this.proxy.extraParams.deviceProvince = deviceProvince_Search.getValue();
                this.proxy.extraParams.deviceDistrict = deviceDistrict_Search.getValue();
            },
            load: function(store, records, success) {
                if (!success) {
                    var msgr = 'LoadDevice fail !!!';
                    Ext.MessageBox.show({title: 'Warning', msg: msgr, buttons: Ext.MessageBox.OK, icon: Ext.Msg.WARNING});
                }
            }
        }
    });
    deviceMgtForm_Store.loadPage(1, {});
    var smDevice = Ext.create('Ext.selection.CheckboxModel', {
        checkOnly: true
    });
    var deviceMgtForm_Panel = Ext.create('Ext.grid.Panel', {
        title: '<fmt:message key="management.device.gridPanel.title"/>',
        store: deviceMgtForm_Store,
        selType: 'cellmodel',
        selModel: smDevice,
        id: 'deviceMgtForm_PanelId',
        tbar: [
            {
                text: '<fmt:message key="button.delete"/>',
                icon: iconForder + 'delete.gif', // Use a URL in the icon config			                
                handler: function() {
                    var idList = '';
                    var itemNo = 0;
                    selected = deviceMgtForm_Panel.getView().getSelectionModel().getSelection();
                    Ext.each(selected, function(item) {
                        itemNo++;
                        if (idList == '')
                            idList = idList + (item.data.id);
                        else
                            idList = idList + ',' + (item.data.id);
                    });
                    if (itemNo == 0)
                        return;
                    var msg = '<fmt:message key="message.delete.confirm"/>';
                    Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', msg, function(btn) {
                        if (btn == 'yes')
                            deleteDevice(idList);
                        if (btn == 'no' || btn == 'cancel')
                            smDevice.deselectAll(true);
                    });
                }
            },
            {
                text: 'Upgrade Firmware',
                icon: iconForder + 'cog_edit.png', // Use a URL in the icon config			                
                handler: function() {
                    var idList = '';
                    var itemNo = 0;
                    selected = deviceMgtForm_Panel.getView().getSelectionModel().getSelection();
                    Ext.each(selected, function(item) {
                        itemNo++;
                        if (idList == '')
                            idList = idList + (item.data.id);
                        else
                            idList = idList + ',' + (item.data.id);
                    });
                    //if (itemNo == 0)
                       // alert("no action");
                    var msg = '<fmt:message key="message.upgrade.confirm"/>';
                    Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', msg, function(btn) {
                        if (btn == 'yes')
                            upgradeFirmware(idList);
                        if (btn == 'no' || btn == 'cancel')
                            smDevice.deselectAll(true);
                    });


                }
            },
            {
                text: 'Set DNS- Gateway',
                icon: iconForder + 'connect.png', // Use a URL in the icon config			                
                handler: function() {
                    var idList = '';
                    var itemNo = 0;
                    selected = deviceMgtForm_Panel.getView().getSelectionModel().getSelection();
                    Ext.each(selected, function(item) {
                        itemNo++;
                        if (idList == '')
                            idList = idList + (item.data.id);
                        else
                            idList = idList + ',' + (item.data.id);
                    });
                    //if (itemNo == 0)
                       // alert("no action");
                    var msg = '<fmt:message key="message.gwdns.confirm"/>';
                    Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', msg, function(btn) {
                        if (btn == 'yes')
                            setGatewayDNS(idList);
                        if (btn == 'no' || btn == 'cancel')
                            smDevice.deselectAll(true);
                    });
                    

                }
            },{
                text: 'Set Static Route',
                icon: iconForder + 'add.png', // Use a URL in the icon config			                
                handler: function() {
                    var idList = '';
                    var itemNo = 0;
                    selected = deviceMgtForm_Panel.getView().getSelectionModel().getSelection();
                    Ext.each(selected, function(item) {
                        itemNo++;
                        if (idList == '')
                            idList = idList + (item.data.id);
                        else
                            idList = idList + ',' + (item.data.id);
                    });
                     setStaticRoute(idList);

                    
                    

                }
            },
                {
                text: 'Change ACS URL',
                icon: iconForder + 'url.png', // Use a URL in the icon config			                
                handler: function() {
                    var idList = '';
                    var itemNo = 0;
                    selected = deviceMgtForm_Panel.getView().getSelectionModel().getSelection();
                    Ext.each(selected, function(item) {
                        itemNo++;
                        if (idList == '')
                            idList = idList + (item.data.id);
                        else
                            idList = idList + ',' + (item.data.id);
                    });
                   //if (itemNo == 0)
                       // alert("no action");
                    var msg = '<fmt:message key="message.changeacs.confirm"/>';
                    Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', msg, function(btn) {
                        if (btn == 'yes')
                            changeAcsUrl(idList);
                        if (btn == 'no' || btn == 'cancel')
                            smDevice.deselectAll(true);
                    });
                    

                }
            }
        ],
        columns: [
            Ext.create('Ext.grid.RowNumberer', {
                header: 'No.',
                width: 30,
                align: 'center',
                sortable: false,
                renderer: function(v, p, record, rowIndex) {
                    if (this.rowspan) {
                        p.cellAttr = 'rowspan="' + this.rowspan + '"';
                    }
                    return rowIndex + 1;
                }
            }),
            {
                text: 'Id',
                dataIndex: 'id',
                hidden: true,
                sortable: false,
            },
            {
                text: '<fmt:message key="management.device.gridPanel.columns.ip"/>',
                dataIndex: 'ipAddress',
                sortable: false,
                flex: .5,
                hidden: true,
                renderer: function(value, metadata) {
                    metadata.tdAttr = 'data-qtip="' + value + '"';
                    return value;
                }
            },
            {
                text: '<fmt:message key="management.device.gridPanel.columns.serialNumber"/>',
                dataIndex: 'serialNumber',
                flex: .5,
                sortable: false,
                renderer: function(value, metadata) {
                    metadata.tdAttr = 'data-qtip="' + value + '"';
                    return value;
                }
            }, {
                text: '<fmt:message key="management.device.gridPanel.columns.modelName"/>',
                dataIndex: 'modelName',
                flex: .5,
                sortable: false,
                renderer: function(value, metadata) {
                    metadata.tdAttr = 'data-qtip="' + value + '"';
                    return value;
                }
            }, {
                text: '<fmt:message key="management.device.gridPanel.columns.connectionRequest"/>',
                dataIndex: 'connectionReq',
                flex: 1,
                sortable: false,
                renderer: function(value, metadata) {
                    metadata.tdAttr = 'data-qtip="' + value + '"';
                    return value;
                }
            }, {
                text: '<fmt:message key="management.device.gridPanel.columns.firmwareVersion"/>',
                dataIndex: 'firmwareVersion',
                flex: 1,
                sortable: false,
                renderer: function(value, metadata) {
                    metadata.tdAttr = 'data-qtip="' + value + '"';
                    return value;
                }
            }
        ],
        listeners: {
        },
        dockedItems: [{
                xtype: 'pagingtoolbar',
                store: deviceMgtForm_Store, // same store GridPanel is using
                dock: 'bottom',
                displayInfo: true
            }]
    });
</script>