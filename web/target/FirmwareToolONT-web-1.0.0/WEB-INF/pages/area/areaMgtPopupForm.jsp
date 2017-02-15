<%-- 
    Document   : areaMgtPopupForm
    Created on : May 15, 2015, 11:45:30 AM
    Author     : Dell
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var areaId = NMS_STB.createTextField('Area Id', 550, 'left', false);
    var areaName = Ext.create('Ext.form.TextField', {
        width: 550,
        labelWidth: 150,
        padding: 10,
        fieldLabel: '<fmt:message key="management.area.treePanel.columns.text"/>',
        labelAlign: 'left',
        anchor: '99%',
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.requiredField.name"/>'
    });
    var areaType_Store = Ext.create('Ext.data.Store', {
        fields: ['value', 'display'],
        data: [
            {'value': 1, 'display': '<fmt:message key="management.device.gridPanel.columns.province"/>'},
            {'value': 2, 'display': '<fmt:message key="management.device.gridPanel.columns.district"/>'}
        ]
    });
    var areaParent = NMS_STB.createBoxSelectedWithoutListener('<fmt:message key="management.area.treePanel.columns.parent"/>', 550, 'left', province_Store, 'id', 'name', false, false);
    var areaType = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.area.treePanel.columns.type"/>',
        width: 550,
        labelWidth: 150,
        padding: 10,
        store: areaType_Store,
        valueField: 'value',
        displayField: 'display',
        multiSelect: false,
        queryMode: 'local',
        value: 1,
        allowBlank: false,
        blankText: '<fmt:message key="message.validate.form.area.areaType"/>',
        listeners: {
            'change': function (combo, newValue, oldValue, eOpts) {
                areaParent.setValue(null);
                if (newValue != null) {
                    if (newValue == 1) { //province
                        areaParent.setDisabled(true);
                    } else if (newValue == 2) { //district
                        areaParent.setDisabled(false);
                    }
                }
            }
        }
    });
    var areaDescription = NMS_STB.createTextField('<fmt:message key="management.area.treePanel.columns.description"/>', 550, 'left', false);
    var saveBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.save"/>',
        handler: function () {
            if (validateAreaMgtPopupForm() != '') {
                Ext.MessageBox.alert('Status', validateAreaMgtPopupForm(), function () {
                });
                return;
            }
            var areaParentId = -1;
            if (areaType.getValue() == 2)
                areaParentId = areaParent.getValue();
            saveArea(actionType, areaId.getValue(), areaName.getValue(), areaType.getValue(), areaParentId, areaDescription.getValue());
        }
    });
    var cancelBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.cancel"/>',
        handler: function () {
            areaMgtPopupForm.hide();

        }
    });
    var areaMgtPopupForm = Ext.create('Ext.window.Window', {
        closeAction: 'show',
        autoHeight: true,
        autoWidth: true,
        autoScroll: true,
        layout: 'vbox',
        items: [
            areaName, areaType, areaParent, areaDescription
        ],
        buttons: [saveBnt, cancelBnt]
    });
</script>
