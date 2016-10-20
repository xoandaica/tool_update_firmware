<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var UserComponents = {
        build: function () {
            UserComponents.searchStatusCombo = ExtWnms.createExtComboboxNoRender(
                    'searchCriteria.status', 'searchCriteria.status',
                    statusStore, '<fmt:message key="user.status"/>', 'label', 'value', false,
                    '---<fmt:message key="common.all"/>---', '', false, null, '24%');

            UserComponents.searchForm = new Ext.FormPanel({
                labelWidth: 130,
                id: 'formSearchId',
                title: '<fmt:message key="common.tab.search"/>',
                frame: true,
                border: false,
                width: '100%',
                autoHeight: true,
                defaultType: 'textfield',
                monitorValid: true,
                items: [
                    {
                        xtype: 'container',
                        anchor: '100%',
                        columnWidth: 1,
                        layout: 'column',
                        items: [
                            {
                                xtype: 'textfield',
                                id: 'username',
                                columnWidth: .25,
                                labelAlign: 'top',
                                layout: 'anchor',
                                margin: '0 0 0 5',
                                fieldLabel: '<fmt:message key="user.username"/>',
                                name: 'searchCriteria.username'
                            },
                            {
                                xtype: 'textfield',
                                id: 'firstName',
                                columnWidth: .25,
                                labelAlign: 'top',
                                layout: 'anchor',
                                margin: '0 0 0 5',
                                fieldLabel: '<fmt:message key="user.firstName"/>',
                                name: 'searchCriteria.firstName'
                            },
                            {
                                xtype: 'textfield',
                                id: 'lastName',
                                columnWidth: .25,
                                labelAlign: 'top',
                                layout: 'anchor',
                                margin: '0 0 0 5',
                                fieldLabel: '<fmt:message key="user.lastName"/>',
                                name: 'searchCriteria.lastName'
                            },
                            {
                                xtype: 'textfield',
                                id: 'description',
                                columnWidth: .25,
                                labelAlign: 'top',
                                layout: 'anchor',
                                margin: '0 0 0 5',
                                fieldLabel: '<fmt:message key="user.department"/>',
                                name: 'searchCriteria.department'
                            },
                        ]

                    }
                ],
                buttons: [{
                        text: '<fmt:message key="button.search"/>',
                        id: 'save',
                        handler: function () {
                            UserController.loadUserStore(1);
                        }
                    },
                    {
                        text: '<fmt:message key="button.reset"/>',
                        id: 'reset',
                        handler: function () {
                            this.up('form').getForm().reset();
                        }
                    },
                ]
            });

            var sm = Ext.create('Ext.selection.CheckboxModel', {
                checkOnly: true
            });

            UserComponents.grid = Ext.create('Ext.grid.Panel',
                    {
                        //renderTo: formIdRender,
                        store: userStore,
                        collapsible: false,
                        scroll: true,
                        selModel: sm,
                        columnLines: true,
                        border: false,
                        tbar: [
                            {
                                text: '<fmt:message key="button.add"/>',
                                icon: '/styles/icons/fam/add.png',
                                handler: function () {
                                    UserComponents.createOrUpdateForm.getForm().reset();
                                    Ext.getCmp('user.username').setReadOnly(false);
                                    Ext.getCmp('user.email').setReadOnly(false);
                                    UserComponents.createOrUpdateWindow.show();
                                }
                            },
                            {
                                text: '<fmt:message key="button.delete"/>',
                                icon: '/styles/icons/fam/delete.gif',
                                handler: function () {
                                    var selected = UserComponents.grid.getView().getSelectionModel().getSelection();
                                    var ids = [];
                                    var isCheck = false;
                                    var valid = true;
                                    var invalidUser = '';
                                    Ext.each(selected, function (item) {
                                        isCheck = true;
                                        //arrayList =arrayList+ '&deleteIds='+(item.get('id'));
                                        ids.push(item.get('id'));

                                        if (item.get('username') == 'admin') {
                                            valid = false;
                                            invalidUser = item.get('username');
                                        }
                                    });

                                    if (valid == false) {
                                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.user.cannotDelete"/>' + ' ' + invalidUser, function () {
                                        });
                                    } else if (!isCheck) {
                                        Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.delete.select"/>');
                                    } else {
                                        Ext.Msg.confirm('<fmt:message key="message.confirm"/>', '<fmt:message key="message.delete.confirm"/>', function (e) {
                                            if (e == 'yes') {
                                                UserController.deleteItems(ids);
                                            }
                                        });

                                    }

                                }
                            }
                        ],
                        columns: [
                            Ext.create('Ext.grid.RowNumberer', {
                                header: 'No.',
                                width: 30,
                                align: 'center',
                                renderer: function (v, p, record, rowIndex) {
                                    if (this.rowspan) {
                                        p.cellAttr = 'rowspan="' + this.rowspan + '"';
                                    }
                                    return rowIndex + 1;
                                }
                            }),
                            {
                                xtype: 'actioncolumn',
                                width: 60,
                                items: [
                                    {
                                        icon: "/styles/icons/fam/delete.gif",
                                        tooltip: '<fmt:message key="button.delete"/>',
                                        handler: function (grid, rowIndex, colIndex) {
                                            var rec = grid.getStore().getAt(rowIndex);
                                            if (rec.get('username') == 'admin') {
                                                Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.user.cannotDelete"/>' + ' ' + rec.get('username'), function () {
                                                });
                                            } else {
                                                Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', "<fmt:message key="message.delete.confirm"/>", function (btn) {
                                                    if (btn == 'yes') {
                                                        var ids = [];
                                                        ids.push(rec.get('id'));
                                                        UserController.deleteItems(ids);
                                                    }
                                                });
                                            }
                                        }
                                    },
                                    {
                                        icon: "/styles/icons/fam/edit_16x16.png",
                                        tooltip: '<fmt:message key="button.edit"/>',
                                        handler: function (grid, rowIndex, colIndex) {
                                            var rec = grid.getStore().getAt(rowIndex);
                                            if (rec.get('username') == 'admin') {
                                                Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="management.user.cannotEdit"/>' + ' ' + rec.get('username'), function () {
                                                });
                                                return;
                                            }
                                            UserComponents.showForm(rowIndex);
                                        }
                                    },
                                    {
                                        icon: "/styles/icons/fam/application_view_list.png",
                                        tooltip: '<fmt:message key="management.actionlog.detail"/>',
                                        handler: function (grid, rowIndex, colIndex) {
                                            UserComponents.showActionLog(rowIndex);
                                        }
                                    }
                                ]
                            },
                            {
                                text: '<fmt:message key="user.username"/>',
                                flex: 1,
                                sortable: true,
                                filter: true,
                                dataIndex: 'username'
                            },
                            {
                                // 				text : 'Equipment Name',
                                flex: 1,
                                sortable: true,
                                filter: true,
                                hidden: true,
                                dataIndex: 'id'
                            },
                            {
                                text: '<fmt:message key="user.firstName"/>',
                                flex: 1,
                                sortable: true,
                                filter: true,
                                dataIndex: 'firstName'
                            },
                            {
                                text: '<fmt:message key="user.lastName"/>',
                                flex: 1,
                                sortable: true,
                                filter: true,
                                dataIndex: 'lastName'
                            },
                            /* {
                             text : 'Status',
                             flex : 1,
                             sortable : true,
                             filter : true,
                             renderer : function(v) {
                             return v == true ? "<fmt:message key="user.enabled"/>" : "<fmt:message key="user.accountLocked"/>"; 
                             },
                             dataIndex : 'enabled'
                             }, */
                            {
                                text: '<fmt:message key="user.email"/>',
                                flex: 1,
                                sortable: false,
                                filter: true,
                                dataIndex: 'email'
                            }, {
                                text: '<fmt:message key="user.phoneNumber"/>',
                                flex: 1,
                                sortable: false,
                                filter: true,
                                dataIndex: 'phoneNumber'
                            }, {
                                text: '<fmt:message key="user.department"/>',
                                flex: 1,
                                sortable: false,
                                filter: true,
                                dataIndex: 'department'
                            },
                            {
                                text: '<fmt:message key="user.description"/>',
                                flex: 1,
                                sortable: false,
                                filter: true,
                                dataIndex: 'description'
                            },
                            {
                                //dataIndex: 'policyStatus',
                                xtype: 'componentcolumn',
                                width: 200,
                                dataIndex: 'id',
                                renderer: function (value) {
                                    return {
                                        xtype: 'button',
                                        text: '<fmt:message key="management.user.reset"/>',
                                        tooltip: '<fmt:message key="management.user.resetTooltip"/>',
                                        handler: function () {
                                            //Code to here
                                            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', "<fmt:message key="management.user.resetConfirm"/>", function (btn) {
                                                if (btn == 'yes') {
                                                    UserController.resetPassword(value);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            /* {
                             xtype: 'actioncolumn',
                             text: '',
                             width: 60,
                             items: [{
                             icon: '/styles/icons/fam/edit_16x16.png',
                             tooltip: '<fmt:message key="management.user.resetTooltip"/>',
                             handler: function(grid, rowIndex, colIndex) {
                             var rec = UserComponents.grid.getStore().getAt(rowIndex);
                             //Code to here
                             
                             Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', "<fmt:message key="management.user.resetConfirm"/>", function(btn) {
                             if (btn == 'yes') {
                             UserController.resetPassword(rec.get('id'));
                             }
                             });
                             }
                             }]
                             } */
                        ],
                        listeners: {
                            'celldblclick': function (thisGrid, td, colIndex, record, tr, rowIndex, e, eOpts) {
                                UserComponents.showForm(rowIndex);
                            }
                        },
                        // paging bar on the bottom
                        bbar: Ext.create('Ext.PagingToolbar',
                                {
                                    store: userStore,
                                    displayInfo: true,
                                    displayMsg: '<fmt:message key="message.paging"/>',
                                    emptyMsg: "<fmt:message key="message.empty"/>"

                                }),
                        width: "90%",
                        title: '<fmt:message key="management.user.list"/>',
                        id: 'userGrid',
                        viewConfig: {
                            stripeRows: true
                        }
                    });


            UserComponents.panel = Ext.create('Ext.form.Panel',
                    {
                        autoHeight: true,
                        frame: true,
                        listeners: {
                        },
                        width: '100%',
                        bodyPadding: 0,
                        layout: {
                            pack: 'left'
                        },
                        defaults: {
                            anchor: '100%',
                            labelWidth: 100
                        },
                        items: [
                            UserComponents.grid
                        ]
                    });

            UserComponents.createOrUpdateForm = new Ext.form.Panel({
                id: 'createOrUpdateForm',
                xtype: 'form',
                dock: 'left',
                frame: true,
                columnWidth: 1,
                bodyPadding: 0,
                items: [
                    {
                        xtype: 'container',
                        anchor: '100%',
                        columnWidth: 1,
                        layout: 'column',
                        items: [
                            {
                                xtype: 'container',
                                columnWidth: 1,
                                layout: 'anchor',
                                items: [
                                    {
                                        xtype: 'hiddenfield',
                                        fieldLabel: 'ID',
                                        name: 'user.id'
                                    },
                                    {
                                        xtype: 'hiddenfield',
                                        fieldLabel: 'Version',
                                        name: 'user.version'
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.username',
                                        id: 'user.username',
                                        fieldLabel: '<fmt:message key="user.username"/>',
                                        labelWidth: 100,
                                        allowBlank: false,
                                        blankText: '<fmt:message key="message.requiredField"/>',
                                        vtype: 'alphanum',
                                        maxLength: 50,
                                        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 50,
                                        anchor: '99%',
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.firstName',
                                        fieldLabel: '<fmt:message key="user.firstName"/>',
                                        labelWidth: 100,
                                        allowBlank: false,
                                        blankText: '<fmt:message key="message.requiredField"/>',
                                        maxLength: 50,
                                        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 50,
                                        anchor: '99%',
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.lastName',
                                        fieldLabel: '<fmt:message key="user.lastName"/>',
                                        labelWidth: 100,
                                        allowBlank: false,
                                        blankText: '<fmt:message key="message.requiredField"/>',
                                        maxLength: 50,
                                        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 50,
                                        anchor: '99%',
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.password',
                                        maxLength: 200,
                                        fieldLabel: '<fmt:message key="user.password"/>',
                                        inputType: 'password',
                                        allowBlank: false,
                                        blankText: '<fmt:message key="message.requiredField"/>',
                                        minLength: 5,
                                        minLengthText: '<fmt:message key="message.minLengh"/>' + 5,
                                        maxLength: 255,
                                                maxLengthText: '<fmt:message key="message.maxLengh"/>' + 255,
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.confirmPassword',
                                        maxLength: 200,
                                        fieldLabel: '<fmt:message key="user.confirmPassword"/>',
                                        inputType: 'password',
                                        validator: function (value) {
                                            /* var password = _this.dialog.down('[name=password]').getValue();
                                             return (value === password) ? true
                                             : 'Passwords do not match.'; */

                                            var fieldValues = UserComponents.createOrUpdateForm.getForm().getFieldValues();

                                            var password = fieldValues["user.password"];
                                            return (value === password) ? true
                                                    : '<fmt:message key="management.user.notmatch"/>';
                                        }
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.email',
                                        id: 'user.email',
                                        fieldLabel: '<fmt:message key="user.email"/>',
                                        labelWidth: 100,
                                        allowBlank: false,
                                        blankText: '<fmt:message key="message.requiredField"/>',
                                        regex: /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/,
                                        regexText: '<fmt:message key="message.invalidEmail"/>',
                                        anchor: '99%',
                                        maxLength: 255,
                                        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 255,
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.phoneNumber',
                                        fieldLabel: '<fmt:message key="user.phoneNumber"/>',
                                        labelWidth: 100,
                                        allowBlank: true,
                                        anchor: '99%',
                                        regex: /^\+{0,1}[0-9]{8,13}$/,
                                        regexText: '<fmt:message key="message.invalidPhone"/>'
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'user.department',
                                        fieldLabel: '<fmt:message key="user.department"/>',
                                        labelWidth: 100,
                                        allowBlank: true,
                                        anchor: '99%',
                                        maxLength: 255,
                                        maxLengthText: '<fmt:message key="message.maxLengh"/>' + 255,
                                    },
                                    /* {
                                     //xtype : 'checkboxfield',
                                     xtype : 'hiddenfield',
                                     name : 'user.enabled',
                                     value : 1,
                                     //fieldLabel : '<fmt:message key="user.status"/>',
                                     //hideLabel : true,
                                     }, */
                                    {
                                        xtype: 'textarea',
                                        name: 'user.description',
                                        fieldLabel: '<fmt:message key="user.description"/>',
                                        labelWidth: 100,
                                        allowBlank: true,
                                        anchor: '99%',
                                    },
                                    {
                                        xtype: 'itemselector',
                                        name: 'userRoles',
                                        id: 'itemselector-field2',
                                        anchor: '100%',
                                        fieldLabel: '<fmt:message key="user.roles"/>',
                                        imagePath: '/scripts/extjs/ux/images/',
                                        store: roleStore,
                                        displayField: 'name',
                                        valueField: 'id',
                                        allowBlank: true,
                                        msgTarget: 'side',
                                        fromTitle: '<fmt:message key="management.user.available"/>',
                                        toTitle: '<fmt:message key="management.user.selected"/>',
                                    }
                                ]
                            }
                        ]

                    }
                ],
                buttons: [
                    {
                        text: '<fmt:message key="button.save"/>',
                        id: 'btnSave',
                        handler: function () {
                            var form = this.up('form').getForm();
                            if (form.isValid()) {
                                var fieldValues = UserComponents.createOrUpdateForm.getForm().getFieldValues();
                                UserController.save(fieldValues);
                            } else {
                                Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.validate.form.requiredField"/>');
                            }
                        }
                    },
                    {
                        text: '<fmt:message key="button.cancel"/>',
                        id: 'btnCancel',
                        handler: function () {
                            UserComponents.createOrUpdateWindow.close();
                        }
                    },
                ],
                listeners: {
                    afterRender: function (thisForm, options) {
                        /* this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                         enter: saveapFirmware
                         }); */
                    },
                }, //end of listeners

            });

            UserComponents.createOrUpdateWindow = new Ext.Window({
                frame: true,
                title: '<fmt:message key="management.user.formTitle"/>',
                width: '30%',
                minHeight: 100,
                autoHeight: true,
                modal: true,
                items: [UserComponents.createOrUpdateForm],
                closeAction: 'hide',
                listeners: {
                    'close': function (panel, eOpts) {

                    }
                }
            });

            UserComponents.actionLogWindow = new Ext.Window({
                frame: true,
                title: '<fmt:message key="management.actionlog.title"/>',
                width: '70%',
                minHeight: 200,
                autoHeight: true,
                modal: true,
                items: [ActionLogComponents.panel],
                closeAction: 'hide',
                listeners: {
                    'close': function (panel, eOpts) {

                    }
                }
            });
        },
        showForm: function (rowIndex) {
            var record = UserComponents.grid.getStore().getAt(rowIndex).getData();
            UserComponents.createOrUpdateForm.getForm().reset();
            for (var key in record) {
                if (key == 'roles')
                    continue;

                var field = UserComponents.createOrUpdateForm.down('[name=user.' + key + ']');
                field.setValue(record[key]);
            }

            var arrayRole = record.roles;

            var arr = [];
            for (var i = 0; i < arrayRole.length; i++) {
                arr.push(arrayRole[i].id);
            }

            Ext.getCmp('itemselector-field2').setValue(arr);
            Ext.getCmp('user.username').setReadOnly(true);
            Ext.getCmp('user.email').setReadOnly(true);

            UserComponents.createOrUpdateWindow.show();
        },
        showActionLog: function (rowIndex) {
            var record = UserComponents.grid.getStore().getAt(rowIndex).getData();
            UserComponents.actionLogWindow.show();

            var field = ActionLogComponents.searchForm.down('[name=searchCriteria.username]');
            field.setValue(record.username);

            Ext.getCmp('actionLog.username').setReadOnly(true)
            actionLogStore.loadPage(1, {});
        },
    }

</script>
