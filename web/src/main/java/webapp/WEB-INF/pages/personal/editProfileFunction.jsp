<%-- 
    Document   : policyMgtFunction
    Created on : Jun 5, 2015, 3:31:03 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    function saveEditProfile(
            editProfileForm_Id,
            editProfileForm_FirstName,
            editProfileForm_LastName,
            editProfileForm_Password,
            editProfileForm_Phone,
            editProfileForm_Department,
            editProfileForm_Description
            ) {
        myMask.show();
        Ext.Ajax.request({
            url: 'saveCurrentUserProfile',
            method: "GET",
            params: {
                userId: editProfileForm_Id,
                firstName: editProfileForm_FirstName,
                lastName: editProfileForm_LastName,
                password: editProfileForm_Password,
                phone: editProfileForm_Phone,
                department: editProfileForm_Department,
                description: editProfileForm_Description
            },
            success: function (response, request) {
                myMask.hide();
                jsonData = Ext.decode(response.responseText);
                if (jsonData.success == true) {
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="user.saved"/>', function () {
                    });
                } else {
                    if (jsonData.success == false) {
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.saveFail"/>', function () {
                        });
                    }
                }
            },
            failure: function (response, opts) {
                myMask.hide();
                Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.saveFail"/>', function () {
                });
            }
        });//end of request
    }
</script>