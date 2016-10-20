<%-- 
    Document   : policyMgtSearchForm
    Created on : Jun 5, 2015, 3:31:17 PM
    Author     : Dell
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var policyName_Search = Ext.create('Ext.form.TextField', {
        labelWidth: 150,
        fieldLabel: '<fmt:message key="management.policy.searchForm.nameSearch"/>',
        labelAlign: 'top',
        anchor: '99%'
    });
    var policyStatus_Store = Ext.create('Ext.data.Store', {
        fields: ['value', 'display'],
        data: [
            {'value': 0, 'display': '<fmt:message key="management.policy.searchForm.status.notRun"/>'},
            {'value': 1, 'display': '<fmt:message key="management.policy.searchForm.status.running"/>'},
            {'value': 2, 'display': '<fmt:message key="management.policy.searchForm.status.done"/>'}
        ]
    });
    var policyStatus_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.searchForm.statusSearch"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'display',
        valueField: 'value',
        labelWidth: 150,
        store: policyStatus_Store,
        queryMode: 'local'
    });
    var policyEnable_Store = Ext.create('Ext.data.Store', {
        fields: ['value', 'display'],
        data: [
            {'value': 0, 'display': '<fmt:message key="management.policy.searchForm.enable.inactive"/>'},
            {'value': 1, 'display': '<fmt:message key="management.policy.searchForm.enable.active"/>'}
        ]
    });
    var policyEnable_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.searchForm.enableSearch"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'display',
        valueField: 'value',
        labelWidth: 150,
        store: policyEnable_Store,
        queryMode: 'local'
    });
    var startTime_Search = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '99%',
        labelAlign: 'top',
        fieldLabel: '<fmt:message key="management.policy.searchForm.startTimeSearch"/>',
        format: 'd-m-Y'
    });
    var endTime_Search = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '99%',
        labelAlign: 'top',
        fieldLabel: '<fmt:message key="management.policy.searchForm.endTimeSearch"/>',
        format: 'd-m-Y'
    });

    var searchForm_ResetBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.reset"/>',
        handler: function () {
            policyMgtSearch_Form.getForm().reset();
        }
    });
    var searchForm_SearchBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.search"/>',
        handler: function () {
            searchPolicy(policyName_Search.getValue(), policyStatus_Search.getValue(),
                    policyEnable_Search.getValue(), startTime_Search.getValue(), endTime_Search.getValue());
        }
    });
    var policyMgtSearch_Form = Ext.widget({
        xtype: 'form',
        layout: 'form',
        id: 'policyMgtSearch_FormId',
        title: '<fmt:message key="management.policy.searchForm.title"/>',
        frame: true,
        border: false,
        width: '100%',
        autoHeight: true,
        monitorValid: true,
        items: [{
                xtype: 'container',
                anchor: '100%',
                columnWidth: 1,
                layout: 'column',
                items: [
                    {
                        xtype: 'container',
                        columnWidth: .2,
                        dock: 'right',
                        layout: 'anchor',
                        items: [policyName_Search]
                    }, {
                        xtype: 'container',
                        columnWidth: .2,
                        dock: 'right',
                        layout: 'anchor',
                        items: [policyStatus_Search]
                    }, {
                        xtype: 'container',
                        columnWidth: .2,
                        dock: 'right',
                        layout: 'anchor',
                        items: [policyEnable_Search]
                    }, {
                        xtype: 'container',
                        columnWidth: .2,
                        dock: 'right',
                        layout: 'anchor',
                        items: [startTime_Search]
                    }, {
                        xtype: 'container',
                        columnWidth: .2,
                        dock: 'right',
                        layout: 'anchor',
                        items: [endTime_Search]
                    }
                ]
            }
        ],
        buttons: [searchForm_SearchBnt, searchForm_ResetBnt]
    });
</script>