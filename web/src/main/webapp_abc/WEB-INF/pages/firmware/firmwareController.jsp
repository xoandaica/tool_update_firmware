<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

    var FirmwareController = {
        deleteItems: function(ids) {

            if (ids.length > 0) {
                myMask.show();

                var arrayList = '';
                for (var i = 0; i < ids.length; i++) {
                    arrayList = arrayList + '&idDelete=' + (ids[i]);
                }
                Ext.Ajax.request({
                    url: "deleteFirmware?" + arrayList,
                    method: "GET",
                    headers: {
                        'my-header': 'foo'
                    },
                    success: function(res, request) {
                        var result = JSON.parse(res.responseText) || {success: true, msg: '<fmt:message key="message.error"/>'};
                        myMask.hide();
                        if (result.deleteStatus) {
                            firmwareGridStore.load();
                            Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.delete.success"/>', function() {
                            });
                        }
                        if (!result.deleteStatus) {
                            firmwareGridStore.load();
                            ExtWnms.alertError("<fmt:message key="message.error"/>");
                            return false;
                        }
                    },
                    failure: function(response, opts) {
                        myMask.hide();
                        ExtWnms.alertError("<fmt:message key="message.error"/>");
                        return false;

                    }

                });
            }
        },
        // ---- check new firmware--------
        checkNewFirmware: function() {
            myMask.show();
            Ext.Ajax.request({
                url: "checkFirmware",
                method: "GET",
                headers: {
                    'my-header': 'foo'
                },
                success: function(res, request) {
                    var result = JSON.parse(res.responseText) || {success: true, msg: '<fmt:message key="message.error"/>'};
                    myMask.hide();
                    if (result.status) {
                        firmwareGridStore.load();
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', result.msg, function() {
                        });
                    }
                    if (!result.status) {
                        firmwareGridStore.load();
                        ExtWnms.alertError("<fmt:message key="message.error"/>");
                        return false;
                    }
                },
                failure: function(response, opts) {
                    myMask.hide();
                    ExtWnms.alertError("<fmt:message key="message.error"/>");
                    return false;

                }

            });
        },
        //set default
        setDefault: function(id) {
            myMask.show();
            Ext.Ajax.request({
                url: "updateFirmwareDefault",
                method: "POST",
                headers: {
                    'my-header': 'foo'
                },
                params: {
                    fwId: id                    
                },
                success: function(res, request) {
                    var result = JSON.parse(res.responseText) || {success: true, msg: '<fmt:message key="message.error"/>'};
                    myMask.hide();
                    if (result.status) {
                        firmwareGridStore.load();
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.firmware.save.success"/>', function() {
                        });
                        firmwarePopup.hide();
                    }
                    if (!result.status) {
                        firmwareGridStore.load();
                        ExtWnms.alertError(result.message);
                        return false;
                    }
                },
                failure: function(response, opts) {
                    myMask.hide();
                    ExtWnms.alertError("<fmt:message key="message.error"/>");
                    return false;

                }

            });
        },
        //save firmware
        saveFirmware: function() {
            if (FirmwareController.validatePopupForm()) {
                myMask.show();
                Ext.Ajax.request({
                    url: "saveFirmware",
                    method: "POST",
                    headers: {
                        'my-header': 'foo'
                    },
                    params: {
                        fwId: fwId.getValue(),
                        fwReleaseDate: fwReleaseDate.getRawValue(),
                        fwModel: fwModel.getValue(),
                        fwVersion: fwVersion.getValue(),
                        fwPath: fwPath.getValue(),
                        fwReleaseNote: fwReleaseNote.getValue()

                    },
                    success: function(res, request) {
                        var result = JSON.parse(res.responseText) || {success: true, msg: '<fmt:message key="message.error"/>'};
                        myMask.hide();
                        if (result.status) {
                            firmwareGridStore.load();
                            Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.firmware.save.success"/>', function() {
                            });
                            firmwarePopup.hide();
                        }
                        if (!result.status) {
                            firmwareGridStore.load();
                            ExtWnms.alertError(result.message);
                            return false;
                        }
                    },
                    failure: function(response, opts) {
                        myMask.hide();
                        ExtWnms.alertError("<fmt:message key="message.error"/>");
                        return false;

                    }

                });
            } else {
                //show thong bao tai day
                ExtWnms.alertError("<fmt:message key="message.validate.form.requiredField"/>");
                return false;

            }
        },
        // validate form
        validatePopupForm: function() {
            var flag = true;

            if (!ExtWnms.isNotEmpty(fwModel.getValue())) {
                fwModel.setActiveError('<fmt:message key="message.validate.form.requiredField"/>');
                flag = false;
            }
            if (!ExtWnms.isNotEmpty(fwVersion.getValue())) {
                fwVersion.setActiveError('<fmt:message key="message.validate.form.requiredField"/>');
                flag = false;
            }
            if (!ExtWnms.isNotEmpty(fwReleaseDate.getValue())) {
                fwReleaseDate.setActiveError('<fmt:message key="message.validate.form.requiredField"/>');
                flag = false;
            }
            if (!ExtWnms.isNotEmpty(fwPath.getValue())) {
                fwPath.setActiveError('<fmt:message key="message.validate.form.requiredField"/>');
                flag = false;
            }
            //   alert(flag);
            return flag;

        }

    };// end of controller
</script>
