<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

    var UserController = {
        loadUserStore: function (page) {
            userStore.loadPage(page, {});
        },
        save: function (obj) {
            if (obj['user.id'] == "") {
                obj['user.id'] = null;
            }

            var url = "createOrUpdateUser";
            UserComponents.createOrUpdateForm.getEl().mask('<fmt:message key="message.saving"/>');
            Ext.Ajax.request({
                url: url,
                method: "POST",
                params: obj,
                success: function (response, request) {
                    //checkExpiredResponse(response, request);
                    UserComponents.createOrUpdateForm.getEl().unmask('<fmt:message key="message.saving"/>');
                    jsonData = Ext.decode(response.responseText);
                    if (jsonData.success == true) {
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="user.saved"/>', function () {
                        });
                        UserComponents.createOrUpdateWindow.close();
                        userStore.load();
                        UserComponents.createOrUpdateForm.getForm().reset();
                    } else {
                        if (jsonData.success == false) {
                            Ext.MessageBox.alert('<fmt:message key="message.status"/>', jsonData.msg, function () {
                            });
                        }
                    }
                },
                failure: function (response, opts) {
                    UserComponents.createOrUpdateForm.getEl().unmask('Saving ...');
                    Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.saveFail"/>', function () {
                    });
                }

            });//end of request
        },
        deleteItems: function (ids) {
            myMask.show();

            var arrayList = '';
            for (var i = 0; i < ids.length; i++) {
                arrayList = arrayList + '&deleteIds=' + (ids[i]);
            }


            Ext.Ajax.request({
                url: "deleteUser?" + arrayList,
                method: "GET",
                headers: {
                    'my-header': 'foo'
                },
                success: function (response, request) {
                    //checkExpiredResponse(response, request);
                    var result = JSON.parse(response.responseText) || {success: true, msg: '<fmt:message key="message.error"/>'};
                    myMask.hide();
                    if (result.success) {
                        userStore.load();
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.delete.success"/>', function () {
                        });
                    } else {
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', result.msg, function () {
                        });
                    }
                },
                failure: function (response, opts) {
                    myMask.hide();
                    ExtWnms.alertError("<fmt:message key="message.error"/>");
                    return false;

                }

            });
        },
        loadRoles: function () {

            Ext.Ajax.request({
                url: "listRoles",
                method: "GET",
                headers: {
                    'my-header': 'foo'
                },
                success: function (response, request) {
                    //checkExpiredResponse(response, request);
                    jsonData = Ext.decode(response.responseText);
                    roleStore.loadData(jsonData.result.list);

                    var item2 = UserComponents.createOrUpdateForm.down(
                            '#itemselector-field2');
                    var store2 = item2.store;
                    item2.bindStore(store2);
                },
                failure: function (response, opts) {


                }

            });
        },
        resetPassword: function (id) {
            Ext.Ajax.request({
                url: "resetPassword?id=" + id,
                method: "GET",
                headers: {
                    'my-header': 'foo'
                },
                success: function (response, request) {
                    //checkExpiredResponse(response, request);
                    jsonData = Ext.decode(response.responseText);
                    if (jsonData.success == true) {
                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="user.reset"/>', function () {
                        });
                        userStore.load();
                    } else {
                        if (jsonData.success == false) {
                            Ext.MessageBox.alert('<fmt:message key="message.status"/>', jsonData.msg, function () {
                            });
                        }
                    }
                },
                failure: function (response, opts) {


                }

            });
        }
    };
</script>
