<%-- 
    Document   : policyMgtFunction
    Created on : Jun 5, 2015, 3:31:03 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    function checkTimesNestedTimes(startTime1InString, endTime1InString, startTime2InString, endTime2InString) {
        var check = true; //default true - nested
        var match = startTime1InString.match(/^(\d+)-(\d+)-(\d+) (\d+)\:(\d+)\:(\d+)$/);
        var startTime1 = new Date(match[3], match[2] - 1, match[1], match[4], match[5], match[6]);
        match = endTime1InString.match(/^(\d+)-(\d+)-(\d+) (\d+)\:(\d+)\:(\d+)$/);
        var endTime1 = new Date(match[3], match[2] - 1, match[1], match[4], match[5], match[6]);
        match = startTime2InString.match(/^(\d+)-(\d+)-(\d+) (\d+)\:(\d+)\:(\d+)$/);
        var startTime2 = new Date(match[3], match[2] - 1, match[1], match[4], match[5], match[6]);
        match = endTime2InString.match(/^(\d+)-(\d+)-(\d+) (\d+)\:(\d+)\:(\d+)$/);
        var endTime2 = new Date(match[3], match[2] - 1, match[1], match[4], match[5], match[6]);
        if ((startTime1 < endTime1 && endTime1 < startTime2 && startTime2 < endTime2) || (startTime2 < endTime2 && endTime2 < startTime1 && startTime1 < endTime1))
            check = false;
        return check;
    }
    function validatePolicyMgtPopupForm() {
        var mess = false;
        var requiredField = false;
        policyMgtPopupForm.items.each(function (x) {
            if (x.validate() === false) {
                requiredField = true;
            }
        });
        if (requiredField !== false) {
            mess = '<fmt:message key="message.validate.form.requiredField"/>';
        } else if ((policyName.getValue().length > 100) || (policyName.getValue().length < 1)) {
            mess = '<fmt:message key="message.validate.form.policy.policyName.length"/>';
        } else if (policyStartTime.getValue() > policyEndTime.getValue()) {
            mess = '<fmt:message key="message.validate.form.checkTime"/>';
        } else { //check time policy
            if (actionType == 1) {
                var nestedPolicyName = "";
                policyMgtForm_Store.queryBy(function (record, id) {
                    if (record.get('policyStatus') != 2) {
                        if ((checkTimesNestedTimes(policyStartTime.getRawValue(), policyEndTime.getRawValue(),
                                record.get('startTime'), record.get('endTime')) == true) && (record.get('modelId') == policyModelType.getValue())) {
                            nestedPolicyName = record.get('name');
                            return;
                        }
                    }
                });
                if (nestedPolicyName !== "")
                    mess = '<fmt:message key="message.validate.form.nestedPolicy"/>' + " " + nestedPolicyName + " !";
            } else if (actionType == 2) {
                var nestedPolicyName = "";
                policyMgtForm_Store.queryBy(function (record, id) {
                    if (record.get('policyStatus') != 2) {
                        if ((checkTimesNestedTimes(policyStartTime.getRawValue(), policyEndTime.getRawValue(),
                                record.get('startTime'), record.get('endTime')) == true) && (policyId.getValue() != record.get('id')) && (record.get('modelId') == policyModelType.getValue())) {
                            nestedPolicyName = record.get('name');
                            return;
                        }
                    }
                });
                if (nestedPolicyName !== "")
                    mess = '<fmt:message key="message.validate.form.nestedPolicy"/>' + " " + nestedPolicyName + " !";
            }

        }
        return mess;
    }
    function searchPolicy(policyName_Search, policyStatus_Search, policyEnable_Search,
            startTime_Search, endTime_Search) {
        policyMgtForm_Store.loadPage(1, {
            page: 1,
            params: {
                policyName: policyName_Search,
                policyStatus: policyStatus_Search,
                policyEnable: policyEnable_Search,
                startTime: startTime_Search,
                endTime: endTime_Search,
            }
        });
    }
    ;//End
    function deletePolicy(itemIdList) {
        myMask.show();
        Ext.Ajax.request({
            url: "deletePolicy",
            method: "GET",
            params: {
                itemIdList: itemIdList
            },
            success: function (result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.deleteStatus === "success") {
                    policyMgtForm_Store.loadPage(1);
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.policy.message.delete.success"/>', function () {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.delete.fail"/>', function () {
                    });
                }
            },
            failure: function (response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.delete.fail"/>', function () {
                });
            }
        });
    }
    ;//End
    function savePolicy(actionType, policyId, policyName, policyModelType,
            policyFirmware, policyProvince, policyDistrict, policyStartTime, policyEndTime, policyEnable) {
        myMask.show();
        Ext.Ajax.request({
            url: "savePolicy",
            method: "GET",
            params: {
                actionType: actionType,
                policyId: policyId,
                policyName: policyName,
                policyModelType: policyModelType,
                policyFirmware: policyFirmware,
                policyProvince: policyProvince,
                policyDistrict: policyDistrict,
                policyStartTime: policyStartTime,
                policyEndTime: policyEndTime,
                policyEnable: policyEnable
            },
            success: function (result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.saveStatus === "success") {
                    policyMgtPopupForm.hide();
                    searchPolicy(policyName_Search.getValue(), policyStatus_Search.getValue(),
                            policyEnable_Search.getValue(), startTime_Search.getValue(), endTime_Search.getValue());
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.policy.message.save.success"/>', function () {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.save.fail"/>', function () {
                    });
                }
            },
            failure: function (response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.save.fail"/>', function () {
                });
            }
        });
    }//End
//    function createPolicyReport(policyId, fileType) {
//        myMask.show();
//        Ext.Ajax.request({
//            url: "createPolicyReport",
//            method: "GET",
//            params: {
//                policyId: policyId,
//                fileType: fileType
//            },
//            timeout: 120000, //120s
//            success: function (result, request) {
//                myMask.hide();
//            },
//            failure: function (response, opts) {
//                myMask.hide();
//                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.policy.message.createReport.fail"/>', function () {
//                });
//            }
//        });
//    }




</script>