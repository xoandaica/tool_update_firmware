<%-- 
    Document   : deviceMgtFirmwareHistoryForm
    Created on : Jun 10, 2015, 9:14:18 AM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var deviceMgtFirmwareHistoryForm_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'policyName', 'firmwareOldVersion', 'firmwareNewVersion', 'startTime', 'endTime', 'status', 'description'],
        proxy: {
            type: 'ajax',
            url: 'deviceUpdateFirmwareHistory',
            reader: {
                root: 'list',
                type: 'json',
                totalProperty: 'totalCount'
            }
        },
        pageSize: 10,
        autoLoad: false,
        listeners: {
            'beforeload': function (store, options) {
                this.proxy.extraParams.itemId = policyHistoryId;
            },
            load: function (store, records, success) {
                if (!success) {
                    var msgr = 'DeviceUpdateFirmwareHistory fail !!!';
                    Ext.MessageBox.show({title: 'Warning', msg: msgr, buttons: Ext.MessageBox.OK, icon: Ext.Msg.WARNING});
                }
            }
        }
    });
    var deviceMgtFirmwareHistoryForm_Panel = Ext.create('Ext.grid.Panel', {
        store: deviceMgtFirmwareHistoryForm_Store,
        minHeight: 200,
//        tbar: [
//            {//add 
//                text: '<fmt:message key="management.deviceHistory.gridPanel.tbar.text"/>',
//                icon: iconForder + 'delete.png',
//                handler: function () {
//                }
//            }
//        ], //end of tbar
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
                text: 'Id',
                dataIndex: 'id',
                hidden: true
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.policyName"/>',
                dataIndex: 'deviceMAC',
                align: 'center',
                hidden: true
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.firmwareOldVersion"/>',
                dataIndex: 'firmwareOldVersion',
                align: 'center',
                flex: 1
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.firmwareNewVersion"/>',
                dataIndex: 'firmwareNewVersion',
                align: 'center',
                flex: 1
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.startTime"/>',
                dataIndex: 'startTime',
                align: 'center',
                flex: 2
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.endTime"/>',
                dataIndex: 'endTime',
                align: 'center',
                flex: 2
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.status"/>',
                dataIndex: 'status',
                align: 'center',
                flex: 2,
                renderer: function (value, meta, record) {
                    if (value == 0) {
                        return '<fmt:message key="message.fail"/>';
                    } else if (value == 1) {
                        return '<fmt:message key="message.success"/>';
                    }
                }
            }, {
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.description"/>',
                dataIndex: 'description',
                align: 'center',
                flex: 3
            }
        ],
        dockedItems: [{
                xtype: 'pagingtoolbar',
                store: deviceMgtFirmwareHistoryForm_Store, // same store GridPanel is using
                dock: 'bottom',
                displayInfo: true
            }]
    });
    var deviceMgtFirmwareHistoryForm = Ext.create('Ext.window.Window', {
        closeAction: 'show',
        title: '<fmt:message key="management.deviceHistory.gridPanel.title"/>',
        autoHeight: true,
        width: 1000,
        autoScroll: true,
        layout: 'anchor',
        items: [deviceMgtFirmwareHistoryForm_Panel]
    });
</script>