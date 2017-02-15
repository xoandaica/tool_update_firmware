<%-- 
    Document   : policyMgtPopupForm
    Created on : Jun 5, 2015, 3:31:29 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var policyId = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        labelAlign: 'left',
        anchor: '99%'
    });
    var policyName = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.name"/>',
        labelAlign: 'left',
        anchor: '99%',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.name"/>'
    });
    var policyFirmware_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'version'],
        proxy: {
            type: 'ajax',
            url: 'loadFirmwareByModelCommon',
            reader: {
                root: 'listFirmware',
                type: 'json'
            }
        },
        autoLoad: false
    });
    var policyFirmware = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.firmware"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: false,
        displayField: 'version',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: policyFirmware_Store,
        queryMode: 'local',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.firmware"/>'
    });
    var policyModelType_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'name'],
        proxy: {
            type: 'ajax',
            url: 'loadModelCommon',
            reader: {
                root: 'listDeviceModel',
                type: 'json'
            }
        },
        autoLoad: true
    });
    var policyModelType = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.model"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: policyModelType_Store,
        queryMode: 'local',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.model"/>',
        listeners: {
            'change': function (combo, newValue, oldValue, eOpts) {
                policyFirmware.setValue(null);
                policyFirmware_Store.removeAll();
                if (policyModelType.getValue() != null) {
                    policyFirmware_Store.getProxy().extraParams = {
                        modelTypeId: policyModelType.getValue()
                    };
                    policyFirmware_Store.load();
                }
            }
        }
    });
    var policyDistrict_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'name'],
        proxy: {
            type: 'ajax',
            url: 'loadDistrictByProvinceListIdCommon',
            reader: {
                root: 'listDistrict',
                type: 'json'
            }
        },
        autoLoad: false
    });
    var policyDistrict = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.district"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: true,
        displayField: 'name',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: policyDistrict_Store,
        queryMode: 'local',
        allowBlank: true
    });
    var policyProvince_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'name'],
        proxy: {
            type: 'ajax',
            url: 'loadProvinceCommon',
            reader: {
                root: 'listProvince',
                type: 'json'
            }
        },
        autoLoad: true
    });
    var policyProvince = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.province"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: true,
        displayField: 'name',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: policyProvince_Store,
        queryMode: 'local',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.model"/>',
        listeners: {
            'change': function (combo, newValue, oldValue, eOpts) {
                policyDistrict.setValue(null);
                policyDistrict_Store.removeAll();
                if (policyProvince.getValue().toString() != "") {
                    policyDistrict_Store.getProxy().extraParams = {
                        provinceListId: policyProvince.getValue().toString()
                    };
                    policyDistrict_Store.load();
                }
            }
        }
    });
    var policyStartTime = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '99%',
        width: 550,
        labelWidth: 150,
        padding: 10,
        labelAlign: 'left',
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.startTime"/>',
        format: 'd-m-Y',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.startTime"/>'
    });
    var policyEndTime = Ext.create('Ext.ux.form.DateTimeField', {
        anchor: '99%',
        width: 550,
        labelWidth: 150,
        padding: 10,
        labelAlign: 'left',
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.endTime"/>',
        format: 'd-m-Y',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.endTime"/>'
    });
    var policyEnable_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'value'],
        data: [
            {'id': '0', 'value': '<fmt:message key="management.policy.searchForm.enable.inactive"/>'},
            {'id': '1', 'value': '<fmt:message key="management.policy.searchForm.enable.active"/>'}
        ],
        autoLoad: true
    });
    var policyEnable = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.policy.gridPanel.columns.enable"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: false,
        displayField: 'value',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        value: '1',
        store: policyEnable_Store,
        queryMode: 'local',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.status"/>'
    });
    var saveBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.save"/>',
        handler: function () {
            if (validatePolicyMgtPopupForm() != '') {
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', validatePolicyMgtPopupForm(), function () {
                });
                return;
            }
            savePolicy(actionType, policyId.getValue(), policyName.getValue(), policyModelType.getValue(),
                    policyFirmware.getValue(), policyProvince.getValue().toString(), policyDistrict.getValue().toString(), policyStartTime.getRawValue(), policyEndTime.getRawValue(), policyEnable.getValue());
        }
    });
    var cancelBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.cancel"/>',
        handler: function () {
            policyMgtPopupForm.hide();
        }
    });
    var policyMgtPopupForm = Ext.create('Ext.window.Window', {
        closeAction: 'show',
        autoHeight: true,
        autoWidth: true,
        autoScroll: true,
        layout: 'vbox',
        items: [
            policyName, policyModelType, policyFirmware, policyProvince, policyDistrict, policyStartTime, policyEndTime, policyEnable
        ],
        buttons: [saveBnt, cancelBnt]
    });

</script>
