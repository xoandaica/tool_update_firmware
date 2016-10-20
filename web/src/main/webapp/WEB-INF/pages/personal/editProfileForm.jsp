<%-- 
    Document   : policyMgtForm
    Created on : Jun 5, 2015, 3:31:39 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var userProfileStore = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: ['id', 'username', 'firstName', 'lastName', 'email', 'phoneNumber',
            'department', 'password', 'confirmPassword', 'roles', 'version', 'description'],
        proxy: {
            type: 'ajax',
            url: 'loadCurrentUserProfile',
            reader: {
                root: 'currentUserProfile',
                type: 'json'
            }
        }
    });
    userProfileStore.load({
        params: {
            /* parameters to pass*/
        },
        callback: function(records, operation, success) {
            /* perform operations on the records*/
            var item = records[0].data;
            editProfileForm_Id.setValue(item.id);
            editProfileForm_Version.setValue(item.version);
            editProfileForm_UserName.setValue(item.username);
            editProfileForm_FirstName.setValue(item.firstName);
            editProfileForm_LastName.setValue(item.lastName);
            editProfileForm_Password.setValue(item.password);
            editProfileForm_RetypePassword.setValue(item.confirmPassword);
            editProfileForm_Email.setValue(item.email);
            editProfileForm_Phone.setValue(item.phoneNumber);
            editProfileForm_Department.setValue(item.department);
            editProfileForm_Description.setValue(item.description);
            editProfileForm_Roles.setValue(item.roles);
        }
    });
    var editProfileForm_Id = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Id',
        width: 550,
        id: 'user.id',
        name: 'user.id',
        labelWidth: 100,
        padding: 20,
        hidden: true,
        labelAlign: 'left',
        anchor: '99%'
    });
    var editProfileForm_Version = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Version',
        name: 'user.version',
        hidden: true
    });

    var editProfileForm_UserName = Ext.create('Ext.form.TextField', {
        fieldLabel: '<fmt:message key="user.username"/>',
        width: 550,
        labelWidth: 100,
        padding: 20,
        readOnly: true,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.username',
        id: 'user.username',
        allowBlank: false,
        blankText: '<fmt:message key="message.requiredField"/>',
        vtype: 'alphanum',
        maxLength: 50,
        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 50,
    });
    var editProfileForm_FirstName = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.firstName',
        fieldLabel: '<fmt:message key="user.firstName"/>',
        labelWidth: 100,
        allowBlank: false,
        blankText: '<fmt:message key="message.requiredField"/>',
        maxLength: 50,
        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 50
    });
    var editProfileForm_LastName = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.lastName',
        fieldLabel: '<fmt:message key="user.lastName"/>',
        labelWidth: 100,
        allowBlank: false,
        blankText: '<fmt:message key="message.requiredField"/>',
        maxLength: 50,
        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 50
    });
    var editProfileForm_Password = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        labelWidth: 100,
        anchor: '99%',
        name: 'user.password',
        fieldLabel: '<fmt:message key="user.password"/>',
        inputType: 'password',
        allowBlank: false,
        blankText: '<fmt:message key="message.requiredField"/>',
        minLengthText: '<fmt:message key="message.minLengh"/>' + 5,
        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 255
    });
    var editProfileForm_RetypePassword = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        labelWidth: 100,
        anchor: '99%',
        xtype: 'textfield',
        name: 'user.confirmPassword',
        maxLength: 200,
        fieldLabel: '<fmt:message key="user.confirmPassword"/>',
        inputType: 'password',
        validator: function(value) {
            var fieldValues = editProfileForm_Panel.getForm().getFieldValues();
            var password = fieldValues["user.password"];
            return (value === password) ? true
                    : '<fmt:message key="management.user.notmatch"/>';
        }
    });
    var editProfileForm_Email = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.email',
        id: 'user.email',
        readOnly: true,
        fieldLabel: '<fmt:message key="user.email"/>',
        labelWidth: 100,
        allowBlank: false,
        blankText: '<fmt:message key="message.requiredField"/>',
        regex: /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/,
        regexText: '<fmt:message key="message.invalidEmail"/>',
        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 255
    });
    var editProfileForm_Phone = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.phoneNumber',
        fieldLabel: '<fmt:message key="user.phoneNumber"/>',
        labelWidth: 100,
        allowBlank: true,
        regex: /^\+{0,1}[0-9]{8,13}$/,
        regexText: '<fmt:message key="message.invalidPhone"/>'
    });
    var editProfileForm_Department = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.department',
        fieldLabel: '<fmt:message key="user.department"/>',
        labelWidth: 100,
        allowBlank: true,
        maxLength: 255,
        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 255
    });
    var editProfileForm_Description = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        xtype: 'textarea',
        name: 'user.description',
        fieldLabel: '<fmt:message key="user.description"/>',
        labelWidth: 100,
        allowBlank: true
    });
    var editProfileForm_Roles = Ext.create('Ext.form.TextField', {
        width: 550,
        padding: 20,
        labelAlign: 'left',
        anchor: '99%',
        name: 'user.availableRoles',
        fieldLabel: '<fmt:message key="user.availableRoles"/>',
        labelWidth: 100,
        readOnly: true,
        allowBlank: true
    });
    var saveBnt = Ext.create('Ext.Button', {
        margin: '20 0 0 470',
        width: 100,
        text: '<fmt:message key="button.save"/>',
        handler: function() {
            var form = editProfileForm_Panel.getForm();
            if (form.isValid()) {
                saveEditProfile(editProfileForm_Id.getValue(),
                        editProfileForm_FirstName.getValue(),
                        editProfileForm_LastName.getValue(),
                        editProfileForm_Password.getValue(),
                        editProfileForm_Phone.getValue(),
                        editProfileForm_Department.getValue(),
                        editProfileForm_Description.getValue()
                        );
            } else {
                Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.validate.form.requiredField"/>');
            }
        }
    });
    var editProfileForm_Panel = new Ext.form.Panel({
        id: 'editProfileForm_PanelId',
        xtype: 'form',
        frame: true,
        layout: 'column',
        columnWidth: 1,
        items: [
            {
                xtype: 'container',
                columnWidth: 0.5,
                layout: 'vbox',
                items: [
                    editProfileForm_UserName,
                    editProfileForm_FirstName,
                    editProfileForm_LastName,
                    editProfileForm_Password,
                    editProfileForm_RetypePassword
                ]
            },
            {
                xtype: 'container',
                columnWidth: 0.5,
                layout: 'vbox',
                items: [
                    editProfileForm_Email,
                    editProfileForm_Phone,
                    editProfileForm_Department,
                    editProfileForm_Description,
                    editProfileForm_Roles,
                    saveBnt
                ]
            }
        ]
    });
</script>