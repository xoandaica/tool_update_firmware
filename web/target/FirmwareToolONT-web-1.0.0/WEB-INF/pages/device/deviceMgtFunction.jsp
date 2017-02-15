<%-- 
    Document   : deviceMgtFunction
    Created on : Jun 9, 2015, 3:04:09 PM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

    function validateDeviceMgtPopupForm() {
        var mess = "";
        if (checkFieldIsEmpty(deviceProvince.getValue())) {
            mess = '<fmt:message key="message.validate.form.requiredField.province"/>';
        }
        return mess;
    }
    function validateDeviceMgtConfigForm() {
        var mess = false;
        var requiredField = false;
        deviceMgtConfigForm.items.each(function(x) {
            if (x.validate() === false) {
                requiredField = true;
            }
        });
        if (requiredField !== false)
            mess = '<fmt:message key="message.validate.form.requiredField"/>';
        return mess;
    }
    function searchDevice(deviceMAC_Search, deviceSerialNumber_Search, deviceStatus_Search, deviceFirmwareStatus_Search,
            deviceModel_Search, deviceFirmware_Search, deviceProvince_Search, deviceDistrict_Search, deviceIP_Search, deviceUsername_Search) {
        deviceMgtForm_Store.loadPage(1, {
            page: 1,
            params: {
                deviceMAC: deviceMAC_Search,
                deviceSerialNumber: deviceSerialNumber_Search,
                deviceStatus: deviceStatus_Search,
                deviceFirmwareStatus: deviceFirmwareStatus_Search,
                deviceModel: deviceModel_Search,
                deviceFirmware: deviceFirmware_Search,
                deviceProvince: deviceProvince_Search,
                deviceDistrict: deviceDistrict_Search,
                deviceIpAddress: deviceIP_Search,
                deviceUsername: deviceUsername_Search
            }
        });
    }
    ;//End
    function deleteDevice(itemIdList) {
        myMask.show();
        Ext.Ajax.request({
            url: "deleteDevice",
            method: "GET",
            params: {
                itemIdList: itemIdList
            },
            success: function(result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.deleteStatus === "success") {
                    deviceMgtForm_Store.loadPage(1);
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.device.message.delete.success"/>', function() {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.delete.fail"/>', function() {
                    });
                }
            },
            failure: function(response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.delete.fail"/>', function() {
                });
            }
        });
    }
    ;//End
    function configDevice(deviceId_Config, deviceSerialNumber_Config, deviceUsername_Config, devicePassword_Config, deviceUrl_Config) {
        myMask.show();
        Ext.Ajax.request({
            url: "configDevice",
            method: "GET",
            params: {
                deviceId: deviceId_Config,
                deviceSerialNumber: deviceSerialNumber_Config,
                deviceUsername: deviceUsername_Config,
                devicePassword: devicePassword_Config,
                deviceUrl: deviceUrl_Config
            },
            success: function(result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                Ext.MessageBox.alert('<fmt:message key="message.status"/>', jsonData.message, function() {
                });
                deviceMgtConfigForm.hide();
            },
            failure: function(response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.configService.systemFail"/>', function() {
                });
            }
        });
    }
    ;//End
    function restartDevice(deviceSerialNumber) {
        myMask.show();
        Ext.Ajax.request({
            url: "restartDevice",
            method: "GET",
            params: {
                deviceSerialNumber: deviceSerialNumber
            },
            success: function(result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                Ext.MessageBox.alert('<fmt:message key="message.status"/>', jsonData.message, function() {
                });
            },
            failure: function(response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.restartDevice.systemFail"/>', function() {
                });
            }
        });
    }
    ;//End

    function upgradeFirmware() {
        myMask.show();
        Ext.Ajax.request({
            url: "upgradeFirmware",
            method: "GET",
//            params: {
//                itemIdList: itemIdList
//            },
            success: function(result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.deleteStatus === "success") {
                    deviceMgtForm_Store.loadPage(1);
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.device.message.upgrade.success"/>', function() {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.upgrade.fail"/>', function() {
                    });
                }
            },
            failure: function(response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.upgrade.fail"/>', function() {
                });
            }
        });
    }
    ;//End
    function saveDevice(actionType, deviceId, deviceProvince, deviceDistrict) {
        myMask.show();
        Ext.Ajax.request({
            url: "saveDevice",
            method: "GET",
            params: {
                actionType: actionType,
                deviceId: deviceId,
                deviceProvince: deviceProvince,
                deviceDistrict: deviceDistrict
            },
            success: function(result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.saveStatus === "success") {
                    deviceMgtPopupForm.hide();
                    searchDevice(deviceMAC_Search.getValue(), deviceSerialNumber_Search.getValue(),
                            deviceStatus_Search.getValue(), deviceFirmwareStatus_Search.getValue(),
                            deviceModel_Search.getValue(), deviceFirmware_Search.getRawValue(),
                            deviceProvince_Search.getValue(), deviceDistrict_Search.getValue());
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.device.message.save.success"/>', function() {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.save.fail"/>', function() {
                    });
                }
            },
            failure: function(response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.device.message.save.fail"/>', function() {
                });
            }
        });
    }//End

</script>