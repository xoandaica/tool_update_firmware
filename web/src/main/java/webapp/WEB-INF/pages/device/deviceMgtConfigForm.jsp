<%-- 
    Document   : deviceMgtConfigForm
    Created on : Jun 9, 2015, 4:06:22 PM
    Author     : Dell
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var deviceId_Config = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Id',
        width: 550,
        labelWidth: 150,
        padding: 10,
        labelAlign: 'left',
        anchor: '99%'
    });
    var deviceSerialNumber_Config = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.serialNumber"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
     var deviceIP_Config = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.ip"/>',
        labelAlign: 'left',
        readOnly: true,
        anchor: '99%'
    });
    var deviceUsername_Config = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.username"/>',
        labelAlign: 'left',
        anchor: '99%',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.username"/>'
    });
    var devicePassword_Config = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.password"/>',
        labelAlign: 'left',
        anchor: '99%',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.password"/>'
    });
    var deviceUrl_Config = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.url"/>',
        labelAlign: 'left',
        anchor: '99%',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.url"/>'
    });
    var deviceUpgradeUrl_Config = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.device.gridPanel.columns.upgradeUrl"/>',
        labelAlign: 'left',
        anchor: '99%',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.upgradeUrl"/>'
    });
    var applyBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.apply"/>',
        handler: function () {
            if (validateDeviceMgtConfigForm() != '') {
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', validateDeviceMgtConfigForm(), function () {
                });
                return;
            }
            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="management.device.message.configService.confirm"/>', function (btn) {
                if (btn == 'yes') {
                    configDevice(deviceId_Config.getValue(), deviceSerialNumber_Config.getValue(), deviceUsername_Config.getValue(),
                            devicePassword_Config.getValue(), deviceUrl_Config.getValue());
                }
            });
        }
    });
    var cancelBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.cancel"/>',
        handler: function () {
            deviceMgtConfigForm.hide();
        }
    });
     var restartBtn = Ext.create('Ext.Button', {
        text: '<fmt:message key="management.device.gridPanel.columns.restartDevice.text"/>',
        handler: function() {
            var serialNo = deviceSerialNumber_Config.getValue();
            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="management.device.gridPanel.columns.restartDevice.confirm"/>', function(btn) {
                if (btn == 'yes') {
                    restartDevice(serialNo);
                }
            });


        }
    });
    var deviceMgtConfigForm = Ext.create('Ext.window.Window', {
        closeAction: 'show',
        autoHeight: true,
        title: '<fmt:message key="management.device.configForm.title"/>',
        autoWidth: true,
        autoScroll: true,
        layout: 'vbox',
        items: [deviceIP_Config,deviceSerialNumber_Config, deviceUsername_Config, devicePassword_Config, deviceUrl_Config],
        buttons: [applyBnt, restartBtn,cancelBnt]
    });

</script>
