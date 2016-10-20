<%-- 
    Document   : deviceMgtSearchForm
    Created on : Jun 9, 2015, 11:31:01 AM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var deviceMAC_Search = Ext.create('Ext.form.TextField', {
        labelWidth: 150,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.mac"/>',
        labelAlign: 'top',
        anchor: '99%'
    });
      var deviceIP_Search = Ext.create('Ext.form.TextField', {
        labelWidth: 150,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.ip"/>',
        labelAlign: 'top',
        anchor: '99%'
    });
       var deviceUsername_Search = Ext.create('Ext.form.TextField', {
        labelWidth: 150,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.username"/>',
        labelAlign: 'top',
        anchor: '99%'
    });
    var deviceSerialNumber_Search = Ext.create('Ext.form.TextField', {
        labelWidth: 150,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.serialNumber"/>',
        labelAlign: 'top',
        anchor: '99%'
    });

    var deviceStatus_Store = Ext.create('Ext.data.Store', {
        fields: ['value', 'display'],
        data: [
            {'value': 0, 'display': '<fmt:message key="management.device.searchForm.status.off"/>'},
            {'value': 1, 'display': '<fmt:message key="management.device.searchForm.status.on"/>'}
        ]
    });
    var deviceStatus_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.cpeStatus"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'display',
        valueField: 'value',
        labelWidth: 150,
        store: deviceStatus_Store,
        queryMode: 'local'
    });
    var deviceFirmwareStatus_Store = Ext.create('Ext.data.Store', {
        fields: ['value', 'display'],
        data: [
            {'value': 0, 'display': '<fmt:message key="management.device.searchForm.firmwareStatus.notUpToDate"/>'},
            {'value': 1, 'display': '<fmt:message key="management.device.searchForm.firmwareStatus.upToDate"/>'}
        ]
    });
    var deviceFirmwareStatus_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.firmwareStatus"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'display',
        valueField: 'value',
        labelWidth: 150,
        store: deviceFirmwareStatus_Store,
        queryMode: 'local'
    });
    var deviceFirmware_Store = Ext.create('Ext.data.Store', {
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
    var deviceFirmware_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.firmwareVersion"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'version',
        valueField: 'id',
        labelWidth: 150,
        store: deviceFirmware_Store,
        queryMode: 'local'
    });
    var deviceModel_Store = Ext.create('Ext.data.Store', {
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
    var deviceModel_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.modelName"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        labelWidth: 150,
        store: deviceModel_Store,
        queryMode: 'local',
        listeners: {
            'change': function (combo, newValue, oldValue, eOpts) {
                deviceFirmware_Search.setValue(null);
                deviceFirmware_Store.removeAll();
                if (deviceModel_Search.getValue() != null) {
                    deviceFirmware_Store.getProxy().extraParams = {
                        modelTypeId: deviceModel_Search.getValue()
                    };
                    deviceFirmware_Store.load();
                }
            }
        }
    });
    var deviceDistrict_Search_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'name'],
        proxy: {
            type: 'ajax',
            url: 'loadDistrictByProvinceIdCommon',
            reader: {
                root: 'listDistrict',
                type: 'json'
            }
        },
        autoLoad: false
    });
    var deviceDistrict_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.district"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        labelWidth: 150,
        store: deviceDistrict_Search_Store,
        queryMode: 'local'
    });
    var deviceProvince_Search_Store = Ext.create('Ext.data.Store', {
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
    var deviceProvince_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.province"/>',
        labelAlign: 'top',
        anchor: '99%',
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        labelWidth: 150,
        store: deviceProvince_Search_Store,
        queryMode: 'local',
        listeners: {
            'change': function (combo, newValue, oldValue, eOpts) {
                deviceDistrict_Search.setValue(null);
                deviceDistrict_Search_Store.removeAll();
                if (deviceProvince_Search.getValue() != null) {
                    deviceDistrict_Search_Store.getProxy().extraParams = {
                        provinceId: deviceProvince_Search.getValue()
                    };
                    deviceDistrict_Search_Store.load();
                }
            }
        }
    });
    var searchForm_ResetBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.reset"/>',
        handler: function () {
            deviceMgtSearch_Form.getForm().reset();
        }
    });
    var searchForm_SearchBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.search"/>',
        handler: function () {
            searchDevice(deviceMAC_Search.getValue(), deviceSerialNumber_Search.getValue(),
                    deviceStatus_Search.getValue(), deviceFirmwareStatus_Search.getValue(),
                    deviceModel_Search.getValue(), deviceFirmware_Search.getRawValue(),
                    deviceProvince_Search.getValue(), deviceDistrict_Search.getValue(),deviceIP_Search.getRawValue(),deviceUsername_Search.getRawValue());
        }
    });
    var deviceMgtSearch_Form = Ext.widget({
        xtype: 'form',
        layout: 'form',
        id: 'deviceMgtSearch_FormId',
        title: '<fmt:message key="management.device.searchForm.title"/>',
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
                        columnWidth: .5,
                        dock: 'right',
                        layout: 'anchor',
                        items: [ deviceSerialNumber_Search]
                    },  {
                        xtype: 'container',
                        columnWidth: .5,
                        dock: 'right',
                        layout: 'anchor',
                        items: [deviceModel_Search]
                    }, 
                ]
            }
        ],
        buttons: [searchForm_SearchBnt, searchForm_ResetBnt]
    });
</script>