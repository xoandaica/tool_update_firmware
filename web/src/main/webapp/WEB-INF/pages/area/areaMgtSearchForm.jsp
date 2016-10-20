<%-- 
    Document   : areaMgtSearchForm
    Created on : May 19, 2015, 5:31:28 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var province_Store = Ext.create('Ext.data.Store', {
        fields: ['id', 'name'],
        proxy: {
            type: 'ajax',
            url: 'loadAllProvince',
            reader: {
                root: 'provinceList',
                type: 'json'
            }
        },
        autoLoad: true
    });
    var areaName_Search = Ext.create('Ext.form.TextField', {
        labelWidth: 150,
        xtype: 'textfield',
        fieldLabel: '<fmt:message key="management.area.searchPanel.nameSearch"/>',
        labelAlign: 'top',
        anchor: '99%'
    });
//            NMS_STB.createTextField('<fmt:message key="management.area.searchPanel.nameSearch"/>', 300, 'top', false);
    var province_Search = Ext.create('Ext.ux.form.field.BoxSelect', {
        fieldLabel: '<fmt:message key="management.area.searchPanel.provinceSearch"/>',
        labelAlign: 'top',
        anchor: '99%',
        editable: true,
        multiSelect: false,
        displayField: 'name',
        valueField: 'id',
        labelWidth: 150,
        store: province_Store,
        queryMode: 'local'
    });
//            NMS_STB.createBoxSelectedWithoutListener('<fmt:message key="management.area.searchPanel.provinceSearch"/>', 300, 'top', province_Store, 'id', 'name', false, false);
    var searchForm_ResetBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.reset"/>',
        handler: function () {
            areaName_Search.setValue('');
            province_Search.setValue(null);
            searchArea(areaName_Search.getValue(), province_Search.getValue());
        }
    });
    var searchForm_SearchBnt = Ext.create('Ext.Button', {
        text: '<fmt:message key="button.search"/>',
        handler: function () {
            searchArea(areaName_Search.getValue(), province_Search.getValue());
        }
    });
    var areaMgtSearch_Form = Ext.widget({
        xtype: 'form',
        layout: 'form',
        collapsible: true,
        id: 'filterForm',
        autoScroll: true,
        frame: true,
        bodyPadding: '5 5 0',
        height: 150,
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 75
        },
        items: [{
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                items: [{
                        xtype: 'container',
                        columnWidth: .5,
                        layout: 'anchor',
                        items: [areaName_Search]
                    }, {
                        xtype: 'container',
                        columnWidth: .5,
                        layout: 'anchor',
                        items: [province_Search]
                    }
                ]
            }],
//        xtype: 'form',
//        layout: 'hbox',
//        collapsible: true,
//        autoScroll: true,
//        frame: true,
        title: '<fmt:message key="management.area.searchPanel.title"/>',
//        autoHeight: true,
//        autoWidth: true,
//        items: [areaName_Search, province_Search],
        buttons: [searchForm_SearchBnt, searchForm_ResetBnt]
    });
</script>