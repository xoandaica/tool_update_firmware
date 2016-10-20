<%-- 
    Document   : policyMgtHistoryForm
    Created on : Jun 7, 2015, 11:40:24 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var policyMgtHistoryForm_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'deviceMAC', 'firmwareOldVersion', 'firmwareNewVersion', 'startTime', 'endTime', 'status', 'description'],
        proxy: {
            type: 'ajax',
            url: 'loadPolicyHistory',
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
                    var msgr = 'LoadPolicyHistory fail !!!';
                    Ext.MessageBox.show({title: 'Warning', msg: msgr, buttons: Ext.MessageBox.OK, icon: Ext.Msg.WARNING});
                }
            }
        }
    });

    var policyMgtHistoryForm_Panel = Ext.create('Ext.grid.Panel', {
        store: policyMgtHistoryForm_Store,
        minHeight: 200,
//        tbar: [
//            {//add 
//                text: '<fmt:message key="management.policyHistory.gridPanel.tbar.text"/>',
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
                text: '<fmt:message key="management.policyHistory.gridPanel.columns.deviceMAC"/>',
                dataIndex: 'deviceMAC',
                align: 'center',
                flex: 1
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
                store: policyMgtHistoryForm_Store, // same store GridPanel is using
                dock: 'bottom',
                displayInfo: true
            }]
    });



    var exportToExcelBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.export.excel"/>',
        handler: function () {
            var createReportUrl = "/management/createPolicyReport?_dc=1436150036005&policyId=" + policyHistoryId + "&fileType=" + 1;
            window.location.href = createReportUrl;
        }
    });
    var exportToPdfBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.export.pdf"/>',
        handler: function () {
            var createReportUrl = "/management/createPolicyReport?_dc=1436150036005&policyId=" + policyHistoryId + "&fileType=" + 0;
            window.location.href = createReportUrl;
//            setTimeout(function () {
//                myMask.show();
//                var createReportUrl = "/management/createPolicyReport?_dc=1436150036005&policyId=" + policyHistoryId + "&fileType=" + 0;
//                window.location.href = createReportUrl;
//            }, 10000);
//            myMask.hide();
        }
    });
    var policyMgtHistoryForm = Ext.create('Ext.window.Window', {
        closeAction: 'show',
        title: '<fmt:message key="management.policyHistory.gridPanel.title"/>',
        autoHeight: true,
        width: 1000,
        autoScroll: true,
        layout: 'anchor',
        items: [policyMgtHistoryForm_Panel],
        buttons: [exportToExcelBnt, exportToPdfBnt]
    });
</script>