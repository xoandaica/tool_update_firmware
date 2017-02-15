<%-- 
    Document   : areaMgtForm
    Created on : May 15, 2015, 11:10:30 AM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var areaMgtForm_TreeStore = Ext.create('Ext.data.TreeStore', {
        fields: ['id', 'text', 'type', 'description', 'parent'],
        proxy: {
            type: 'ajax',
            url: 'loadArea',
            reader: {
                type: 'json',
                root: 'root'
            }
        },
        autoLoad: false
    });
    var areaMgtForm_Panel = Ext.create('Ext.tree.Panel', {
        useArrows: true,
        rootVisible: false,
        singleExpand: true,
        autoHeight: true,
        border: false,
        autoWidth: true,
        autoSizeColumns: true,
        columnLines: true,
        title: '<fmt:message key="management.area.treePanel.label"/>',
        store: areaMgtForm_TreeStore,
        tbar: [
            {//add 
                text: '<fmt:message key="management.area.treePanel.tbar.text"/>',
                icon: iconForder + 'add.png',
                handler: function () {
                    actionType = 1; //create
                    areaId.setValue('-1');
                    areaName.setValue('');
                    areaName.setReadOnly(false);
                    areaType.setValue(1);
                    areaParent.setValue(null);
                    areaParent.setDisabled(true);
                    areaDescription.setValue('');
                    areaMgtPopupForm.setTitle('<fmt:message key="management.area.popupPanel.title.create"/>');
                    areaMgtPopupForm.show();
                }
            }
        ], //end of tbar
        columns: [
            {
                xtype: 'actioncolumn',
                text: '<fmt:message key="management.area.treePanel.columns.action"/>',
                width: 50,
                align: 'center',
                items: [{//edit
                        icon: iconForder + 'edit.png', // Use a URL in the icon config			                
                        tooltip: '<fmt:message key="button.edit"/>',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            if (rec.get('id') < 0) {
                                areaMgtPopupForm.hide();
                                Ext.MessageBox.alert('<fmt:message key="message.warning"/>', '<fmt:message key="management.area.message.edit.warning"/>', function () {
                                });
                                return;
                            }
                            actionType = 2; //edit
                            areaId.setValue(rec.get('id'));
                            areaName.setValue(rec.get('text'));
                            areaName.setReadOnly(true);
                            areaParent.setValue(rec.get('parent'));
                            areaType.setValue(rec.get('type'));
                            areaDescription.setValue(rec.get('description'));
                            areaMgtPopupForm.setTitle('<fmt:message key="management.area.popupPanel.title.edit"/>');
                            areaMgtPopupForm.show();
                        }
                    }, {//delete
                        icon: iconForder + 'delete.gif', // Use a URL in the icon config			                
                        tooltip: '<fmt:message key="button.delete"/>',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            if (rec.get('id') < 0) {
                                Ext.MessageBox.alert('<fmt:message key="message.warning"/>', '<fmt:message key="management.area.message.delete.warning"/>', function () {
                                });
                                return;
                            }
                            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="management.area.message.delete.confirm"/>', function (btn) {
                                if (btn == 'yes') {
                                    deleteArea(rec.get('id'));
                                }
                            });
                        }
                    }]
            },
            {text: 'Id', dataIndex: 'id', hidden: true},
            {
                xtype: 'treecolumn', //this is so we know which column will show the tree
                text: '<fmt:message key="management.area.treePanel.columns.text"/>',
                flex: 1,
                dataIndex: 'text'
            },
            {text: '<fmt:message key="management.area.treePanel.columns.type"/>', dataIndex: 'type', flex: 1,
                renderer: function (value, meta, record) {
                    if ((value == '0') || (value == 0)) {
                        return '<fmt:message key="management.device.gridPanel.columns.country"/>';
                    } else if ((value == '1') || (value == 1))
                    {
                        return '<fmt:message key="management.device.gridPanel.columns.province"/>';
                    } else if ((value == '2') || (value == 2))
                    {
                        return '<fmt:message key="management.device.gridPanel.columns.district"/>';
                    }
                }
            },
            {text: '<fmt:message key="management.area.treePanel.columns.description"/>', dataIndex: 'description', flex: 1},
            {text: '<fmt:message key="management.area.treePanel.columns.parent"/>', dataIndex: 'parent', hidden: true}
        ]
    });

</script>
