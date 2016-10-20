<%-- 
    Document   : areaFunction
    Created on : May 15, 2015, 11:03:40 AM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    function validateAreaMgtPopupForm() {
        var mess = '';
        var requiredField = false;
        areaMgtPopupForm.items.each(function (x) {
            if (x.validate() === false) {
                requiredField = true;
            }
        });
        if (requiredField !== false) {
            mess = '<fmt:message key="message.validate.form.requiredField"/>';
        } else if ((areaName.getValue().length > 45) || (areaName.getValue().length < 1)) {
            mess = '<fmt:message key="message.validate.form.area.areaName.length"/>';
        } else if (areaDescription.getValue().length > 45) {
            mess = '<fmt:message key="message.validate.form.area.areaDescription.length"/>';
        } else if (areaType.getValue() == 2) {
            if (areaParent.getValue() == null)
                mess = '<fmt:message key="message.validate.form.area.areaParent"/>';
        }
        var matchingNodes = [];//validate unique name
        areaMgtForm_TreeStore.getRootNode().cascadeBy(function (childNode) {
            if (childNode.get('text') == areaName.getValue().trim())
            {
                matchingNodes.push(childNode);
            }
        }, this);
        if (actionType == 1 && matchingNodes.length > 0) {
             mess = '<fmt:message key="message.validate.form.area.areaName.existed"/>';
        }
        return mess;
    }//End
    function saveArea(actionType, areaId, areaName, areaType, areaParent, areaDescription) {
        myMask.show();
        Ext.Ajax.request({
            url: "saveArea",
            method: "GET",
            params: {
                actionType: actionType,
                areaId: areaId,
                areaName: areaName,
                areaType: areaType,
                areaParent: areaParent,
                areaDescription: areaDescription
            },
            success: function (result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.saveStatus === "success") {
                    areaMgtPopupForm.hide();
                    areaMgtForm_TreeStore.reload();
                    province_Store.load();
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.area.message.save.success"/>', function () {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.area.message.save.fail"/>', function () {
                    });
                }
            },
            failure: function (response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.area.message.save.fail"/>', function () {
                });
            }
        });
    }//End
    function deleteArea(itemId) {
        myMask.show();
        Ext.Ajax.request({
            url: "deleteArea",
            method: "GET",
            params: {
                itemId: itemId
            },
            success: function (result, request) {
                myMask.hide();
                jsonData = Ext.decode(result.responseText);
                if (jsonData.deleteStatus === "success") {
                    areaMgtForm_TreeStore.reload();
                    province_Store.load();
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.area.message.delete.success"/>', function () {
                    });
                } else {
                    Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.area.message.delete.fail"/>', function () {
                    });
                }
            },
            failure: function (response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.error"/>', '<fmt:message key="management.area.message.delete.fail"/>', function () {
                });
            }
        });
    }
    ;//End
    function searchArea(areaName_Search, parentAreaId_Search) {
        searchForm_ResetBnt.setDisabled(true);
        searchForm_SearchBnt.setDisabled(true);
        areaMgtForm_TreeStore.load({
            params: {
                areaName_Search: areaName_Search,
                parentAreaId_Search: parentAreaId_Search,
            },
            scope: this,
            callback: function (records, operation, success) {
                if (success) {
                    searchForm_ResetBnt.setDisabled(false);
                    searchForm_SearchBnt.setDisabled(false);
                } else {
                    searchForm_ResetBnt.setDisabled(false);
                    searchForm_SearchBnt.setDisabled(false);
                }
            }
        });
    }
    ;//End
</script>
