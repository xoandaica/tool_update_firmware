<%-- 
    Document   : deviceMgtPopupForm
    Created on : Jun 9, 2015, 4:07:05 PM
    Author     : Dell
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var deviceId = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        labelAlign: 'left',
        anchor: '99%'
    });
    var deviceMAC = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.mac"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
    var deviceSerialNumber = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.serialNumber"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
 
    var deviceModel = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.modelName"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
    var deviceUsername = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.username"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
    var devicePassword = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.password"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
    var deviceHomepageUrl = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.homepageUrl"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
     var deviceUpgradeUrl = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.upgradeUrl"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });

    var deviceDistrict_Store = Ext.create('Ext.data.Store', {
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
    var deviceDistrict = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.district"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: deviceDistrict_Store,
        queryMode: 'local'
    });
    var deviceProvince_Store = Ext.create('Ext.data.Store', {
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
    var deviceProvince = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.province"/>',
        labelAlign: 'left',
        anchor: '99%',
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: deviceProvince_Store,
        queryMode: 'local',
        listeners: {
            'change': function(combo, newValue, oldValue, eOpts) {
                deviceDistrict.setValue(null);
                deviceDistrict_Store.removeAll();
                if (deviceProvince.getValue() != null) {
                    deviceDistrict_Store.getProxy().extraParams = {
                        provinceId: deviceProvince.getValue()
                    };
                    deviceDistrict_Store.load();
                }
            }
        }
    });
    var saveBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.save"/>',
        handler: function() {
            if (validateDeviceMgtPopupForm() != '') {
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', validateDeviceMgtPopupForm(), function() {
                });
                return;
            }
            saveDevice(actionType, deviceId.getValue(), deviceProvince.getValue(), deviceDistrict.getValue());
        }
    });
    var cancelBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.cancel"/>',
        handler: function() {
            deviceMgtPopupForm.hide();
        }
    });
   
    var deviceMgtPopupForm = Ext.create('Ext.window.Window', {
        closeAction: 'show',
        autoHeight: true,
        autoWidth: true,
        autoScroll: true,
        layout: 'vbox',
        items: [deviceMAC, deviceSerialNumber, deviceModel, deviceUsername,devicePassword,deviceHomepageUrl,deviceUpgradeUrl,deviceProvince, deviceDistrict],
        buttons: [saveBnt, cancelBnt]
    });

</script>
