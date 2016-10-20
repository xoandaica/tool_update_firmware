<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

    Ext.Loader.setConfig({
        enabled: true
    });
    Ext.Loader.setPath('Ext.ux', '/scripts/extjs/ux');
    Ext.require(['Ext.grid.*', 'Ext.data.*', 'Ext.util.*', 'Ext.state.*',
        'Ext.ux.CheckColumn', 'Ext.ux.grid.FiltersFeature', 'Ext.ux.form.field.BoxSelect'

    ]);
    storeForwadingRouting = Ext.create('Ext.data.Store', {
        storeId: 'storeForwadingRouting',
        fields: ['DestIP', 'Gateway', 'Interface', 'SubnetMask', 'path']
    });

    storeForwadingDynamicDNS = Ext.create('Ext.data.Store', {
        storeId: 'storeForwadingDynamicDNS',
        fields: ['hostName', 'userName', 'service', 'interface', 'path', 'password']
    });

    storeForwadingInterface = Ext.create('Ext.data.Store', {
        storeId: 'storeForwadingInterface',
        fields: ['type', 'interface']
    });

    storeDefaultGateway1 = Ext.create('Ext.data.Store', {
        storeId: 'storeDefaultGateway1',
        fields: ['value', 'display']
    });

    storeDefaultGateway = Ext.create('Ext.data.Store', {
        storeId: 'storeDefaultGateway',
        fields: ['type', 'interface']
    });

    storeDNSServer = Ext.create('Ext.data.Store', {
        storeId: 'storeDNSServer',
        fields: ['type', 'interface']
    });

    storeDNSServer1 = Ext.create('Ext.data.Store', {
        storeId: 'storeDNSServer1',
        fields: ['value', 'display']
    });

    storeForwadingInterfaceRouting = Ext.create('Ext.data.Store', {
        storeId: 'storeForwadingInterfaceRouting',
        fields: ['type', 'interface']
    });


    var destinationF = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: 'Destination',
        labelClsExtra: 'label-bold',
        name: 'Destination',
        id: 'destinationF',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        vtype: 'IPAddress'
    });

    var subnetMaskF = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: 'Subnet Mask',
        labelClsExtra: 'label-bold',
        name: 'Subnet Mask',
        id: 'subnetMaskF',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        vtype: 'IPAddress'
    });

    var gatewayF = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: 'GateWay',
        labelClsExtra: 'label-bold',
        name: 'Gateway',
        id: 'gatewayF',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        vtype: 'IPAddress'
    });
    var defautGateWayF = Ext.create('Ext.form.ComboBox', {
        id: 'defautGateWayF',
        fieldLabel: 'Default Gateway',
        store: storeDefaultGateway1,
        queryMode: 'local',
        labelAlign: 'top',
        displayField: 'display',
        valueField: 'value',
        editable: false,
        anchor: '99%',
        labelClsExtra: 'label-bold',
        emptyText: "--Please Select--"
    });
    var defautDNSF = Ext.create('Ext.form.ComboBox', {
        id: 'defautDNSF',
        fieldLabel: 'Default DNS',
        store: storeDNSServer1,
        queryMode: 'local',
        labelAlign: 'top',
        displayField: 'display',
        valueField: 'value',
        editable: false,
        anchor: '99%',
        labelClsExtra: 'label-bold',
        emptyText: "--Please Select--"
    });

    var primaryDNS = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        id: 'primaryDNS',
        fieldLabel: 'Primary DNS Server',
        labelAlign: 'top',
        anchor: '99%',
        labelClsExtra: 'label-bold',
        hidden: true,
        vtype: 'IPAddress'
    });

    var secondaryDNS = Ext.create('Ext.form.field.Text', {
        id: 'secondaryDNS',
        fieldLabel: 'Secondary DNS Server',
        labelAlign: 'top',
        anchor: '99%',
        labelClsExtra: 'label-bold',
        hidden: true,
        vtype: 'IPAddress'
    });

    var intefaceComboF = Ext.create('Ext.form.ComboBox', {
        id: 'intefaceComboF',
        fieldLabel: 'Interface',
        store: storeForwadingInterface,
        queryMode: 'local',
        labelAlign: 'top',
        name: 'Interface',
        valueField: 'interface',
        editable: false,
        allowBlank: false,
        anchor: '99%',
        labelClsExtra: 'label-bold',
        emptyText: "--Please Select--",
        tpl: Ext.create('Ext.XTemplate',
                '<tpl for=".">',
                '<div class="x-boundlist-item">{type} / {interface}</div>',
                '</tpl>'
                ),
        displayTpl: Ext.create('Ext.XTemplate',
                '<tpl for=".">',
                '{type} / {interface}',
                '</tpl>'
                ),
        listeners: {
            change: function (combobox, newValue, oldValue) {
                var index = storeForwadingInterface.find('interface', newValue);
                if (index != -1) {
                    var record = storeForwadingInterface.getAt(index);
                    gatewayF.enable();
                }
            }

        },
    });

    var intefaceComboRouting = Ext.create('Ext.form.ComboBox', {
        id: 'intefaceComboRouting',
        fieldLabel: 'Interface',
        store: storeForwadingInterfaceRouting,
        queryMode: 'local',
        labelAlign: 'top',
        name: 'Interface',
        valueField: 'interface',
        editable: false,
        allowBlank: false,
        anchor: '99%',
        labelClsExtra: 'label-bold',
        emptyText: "--Please Select--",
        tpl: Ext.create('Ext.XTemplate',
                '<tpl for=".">',
                '<div class="x-boundlist-item">{type} / {interface}</div>',
                '</tpl>'
                ),
        displayTpl: Ext.create('Ext.XTemplate',
                '<tpl for=".">',
                '{type} / {interface}',
                '</tpl>'
                ),
        listeners: {
            change: function (combobox, newValue, oldValue) {
                var index = storeForwadingInterfaceRouting.find('interface', newValue);
                if (index != -1) {
                    var record = storeForwadingInterfaceRouting.getAt(index);
                    gatewayF.enable();
                }
            }

        },
    });


    var forwardingForm = Ext.widget({
        xtype: 'form',
        layout: 'form',
        collapsible: true,
        autoScroll: true,
        frame: true,
        title: 'Default Setting',
        bodyPadding: '5 5 0',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 150
        },
        items: [{
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                items: [
                    {
                        xtype: 'container',
                        columnWidth: 1,
                        layout: 'anchor',
                        items: [defautGateWayF]
                    }
                ]
            }],
        buttons: [{
                id: 'btnSaveForwarding',
                text: 'Save',
                handler: function () {
                    if (serial.dom.value == "") {
                        ExtWnms.alertError("Please select a device!");
                        return;
                    }
                    if (defautGateWayF.getValue() == null) {
                        ExtWnms.alertError("Please select value for default gateway!");
                        return;
                    }
                    editDefaultSetting(defautDNSF.getValue(), defautGateWayF.getValue());
                }
            }]

    });

    var winAddStaticRouting;
    var forwardingGrid = Ext.create('Ext.grid.Panel', {
        id: 'forwardingGrid',
        title: ' Static Route (A maximum 32 entries can be configured)',
        store: storeForwadingRouting,
        columns: [
            {text: 'Destination IP', dataIndex: 'DestIP', flex: 1},
            {text: 'Subnet Mask', dataIndex: 'SubnetMask', flex: 1},
            {text: 'Gateway', dataIndex: 'Gateway', flex: 1},
            {text: 'Interface', dataIndex: 'Interface', flex: 1},
            {
                xtype: 'actioncolumn',
                width: 30,
                icon: '/styles/icons/fam/delete.gif"', // Use a URL in the icon config
                tooltip: 'Delete',
                handler: function (grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    var msg = 'Are you sure to delete this item?';
                    deleteObject(rec, msg, storeForwadingRouting, forwardingGrid);
                }
            }
        ],
        tbar: [
            {//delete sublist
                text: 'Add Static Route',
                iconCls: 'icon-add',
                handler: function () {
                    if (!winAddStaticRouting) {
                        winAddStaticRouting = Ext.create('widget.window', {
                            title: 'Add Static Route',
                            header: {
                                titlePosition: 2,
                                titleAlign: 'center'
                            },
                            closeAction: 'hide',
                            width: 300,
                            minWidth: 250,
                            height: 300,
                            items: [{
                                    xtype: 'form',
                                    layout: 'form',
                                    id: 'addLanLeaseForm',
                                    frame: true,
                                    bodyPadding: '5 5 0',
                                    items: [{
                                            xtype: 'container',
                                            anchor: '100%',
                                            layout: 'column',
                                            items: [{
                                                    xtype: 'container',
                                                    columnWidth: 1,
                                                    layout: 'anchor',
                                                    items: [destinationF, subnetMaskF, intefaceComboRouting, gatewayF]
                                                }]
                                        }, ],
                                    buttons: [{
                                            id: 'btnStaticRoute',
                                            text: 'Add',
                                            handler: function () {
                                                //validate
                                                if (serial.dom.value == "") {
                                                    ExtWnms.alertError("Please select a device!");
                                                    return;
                                                }
                                                var myValidator = new Array(destinationF, subnetMaskF, intefaceComboRouting, gatewayF);
                                                for (var i = 0; i < myValidator.length; i++) {
                                                    ExtWnms.validateTextInput(myValidator[i]);
                                                    if (myValidator[i].hasActiveError()) {
                                                        ExtWnms.alertError(myValidator[i].getActiveError());
                                                        return;
                                                    }
                                                }
                                                var index = storeForwadingInterfaceRouting.find('interface', intefaceComboRouting.getValue());
                                                if (index != -1) {
                                                    var record = storeForwadingInterfaceRouting.getAt(index);
                                                    if (destinationF.getValue() === "0.0.0.0") {
                                                        ExtWnms.alertError("Invalid Destination Ip!");
                                                        return;
                                                    }
                                                    var found = storeForwadingInterfaceRouting.findRecord('DestIP', destinationF.getValue());
                                                    if (found) {
                                                        ExtWnms.alertError("Destination Ip existed!");
                                                        return;
                                                    }
                                                    var gatewayCheck = gatewayF.getValue();
                                                    gatewayCheck = gatewayCheck.slice(6);
                                                    if (gatewayCheck === "0") {
                                                        ExtWnms.alertError("Invalid Gateway!");
                                                        return;
                                                    }
                                                    addStaticRoute(destinationF.getValue(), subnetMaskF.getValue(), gatewayF.getValue(), intefaceComboRouting.getValue(), record.get('type'));
                                                   
                                                } else {
                                                    ExtWnms.alertError("Please select a interface");
                                                    return;
                                                }
                                            }
                                        }]
                                }]
                        });
                    }
                    if (winAddStaticRouting.isVisible()) {
                        winAddStaticRouting.hide(this, function () {
                        });
                    } else {
                        destinationF.setValue('');
                        destinationF.reset();
                        subnetMaskF.setValue('');
                        subnetMaskF.reset();
                        intefaceComboRouting.setValue();
                        intefaceComboRouting.reset();
                        gatewayF.setValue('');
                        gatewayF.reset();
                        winAddStaticRouting.show(this, function () {
                        });
                    }
                }

            }
        ]
    });


    var dnsForm = Ext.widget({
        xtype: 'form',
        layout: 'form',
        collapsible: true,
        id: 'dnsForm',
        autoScroll: true,
        frame: true,
        title: 'DNS Server',
        bodyPadding: '5 5 0',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 150
        },
        items: [{
                xtype: 'radiogroup',
                id: 'radioDNS',
                columns: 1,
                items: [
                    {
                        boxLabel: 'Select DNS Server Interface from available WAN interfaces',
                        name: 'addDNS',
                        inputValue: '0',
                        checked: true
                    }, {
                        boxLabel: 'Use the following Static DNS IP Address',
                        name: 'addDNS',
                        inputValue: '1'
                    }
                ],
                listeners: {
                    change: function (radio, newValue, oldValue) {
                        if (newValue['addDNS'] == '1') {
                            defautDNSF.setVisible(false);
                            primaryDNS.setVisible(true);
                            secondaryDNS.setVisible(true);
                        } else {
                            defautDNSF.setVisible(true);
                            primaryDNS.setVisible(false);
                            secondaryDNS.setVisible(false);
                        }
                    }

                }
            }, {
                xtype: 'container',
                layout: 'anchor',
                items: [defautDNSF, primaryDNS, secondaryDNS]}
        ],
        buttons: [{
                id: 'btnSaveDNSServer',
                text: 'Save',
                handler: function () {

                    if (serial.dom.value == "") {
                        ExtWnms.alertError("Please select a device!");
                        return;
                    }

                    if (Ext.getCmp('radioDNS').getValue().addDNS == '1') {
                        if (primaryDNS.getValue() == '' && secondaryDNS.getValue() == '') {
                            ExtWnms.alertError("Primary DNS Server/Secondary DNS Server is not valid !");
                            return;
                        }
                        editDNSServer(primaryDNS.getValue(), secondaryDNS.getValue())
                    } else {
                        if (defautDNSF.getValue() == '') {
                            ExtWnms.alertError("You have to choose a WAN interface as a DNS Server OR enter static DNS IP addresses !");
                            return;
                        }
                        editDefaultSetting(defautDNSF.getValue(), defautGateWayF.getValue());
                    }

                }
            }]

    });

    var hostName = Ext.create('Ext.form.field.Text', {
        id: 'hostNameId',
        fieldLabel: 'Hostname',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        labelClsExtra: 'label-bold'
    });

    var userNameDDNS = Ext.create('Ext.form.field.Text', {
        id: 'userNameDDNSId',
        fieldLabel: 'Username',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        labelClsExtra: 'label-bold'
    });

    var passWordDDNS = Ext.create('Ext.form.field.Text', {
        id: 'passWordDDNSId',
        fieldLabel: 'Password',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        labelClsExtra: 'label-bold'
    });

    var ddnsProviderStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name'],
        data: [
            {'id': 'noip', 'name': 'NO-IP'},
            {'id': 'dyndns', 'name': 'DynDNS.org'},
            {'id': 'tzo', 'name': 'TZO'},
        ],
    });

    var ddnsProvider = Ext.create('Ext.form.ComboBox', {
        id: 'ddnsProviderId',
        fieldLabel: 'D-DNS Provicer',
        store: ddnsProviderStore,
        queryMode: 'local',
        labelAlign: 'top',
        name: 'ddnsProvider',
        displayField: 'name',
        valueField: 'id',
        value: 'noip',
        editable: false,
        allowBlank: false,
        anchor: '99%',
        labelClsExtra: 'label-bold',
        emptyText: "--Please Select--",
        listeners: {
            change: function (combobox, newValue, oldValue) {
                if (newValue == '3') {
                    userNameDDNS.setFieldLabel('Email');
                    passWordDDNS.setFieldLabel('Key');
                } else {
                    userNameDDNS.setFieldLabel('Username');
                    passWordDDNS.setFieldLabel('Password');
                }
            }
        }
    });

    var winAddDynamicDNS;
    var dnsGrid = Ext.create('Ext.grid.Panel', {
        id: 'dnsGrid',
        title: ' Dynamic DNS',
        store: storeForwadingDynamicDNS,
        columns: [
            {text: 'Hostname', dataIndex: 'hostName', flex: 1},
            {text: 'Username', dataIndex: 'userName', flex: 1},
            {text: 'Service', dataIndex: 'service', flex: 1},
            {text: 'Interface', dataIndex: 'interface', flex: 1},
            {text: 'Password', dataIndex: 'password', flex: 1},
            {
                xtype: 'actioncolumn',
                width: 30,
                icon: '/styles/icons/fam/delete.gif"', // Use a URL in the icon config
                tooltip: 'Delete',
                handler: function (grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    var msg = 'Are you sure to delete this item?';
                    deleteObject(rec, msg, storeForwadingDynamicDNS, dnsGrid);
                }
            }
        ],
        tbar: [
            {//delete sublist
                text: 'Add Dynamic DNS',
                iconCls: 'icon-add',
                handler: function () {
                    if (!winAddDynamicDNS) {
                        winAddDynamicDNS = Ext.create('widget.window', {
                            title: 'Add Dynamic DNS',
                            header: {
                                titlePosition: 2,
                                titleAlign: 'center'
                            },
                            closeAction: 'hide',
                            width: 300,
                            minWidth: 250,
                            items: [{
                                    xtype: 'form',
                                    layout: 'form',
                                    id: 'addDynamicDNSForm',
                                    frame: true,
                                    bodyPadding: '5 5 0',
                                    items: [{
                                            xtype: 'container',
                                            anchor: '100%',
                                            layout: 'column',
                                            items: [{
                                                    xtype: 'container',
                                                    columnWidth: 1,
                                                    layout: 'anchor',
                                                    items: [ddnsProvider, hostName, intefaceComboF, userNameDDNS, passWordDDNS]
                                                }]
                                        }, ],
                                    buttons: [{
                                            id: 'btnDynamicDNS',
                                            text: 'Add',
                                            handler: function () {
                                                //validate
                                                if (serial.dom.value == "") {
                                                    ExtWnms.alertError("Please select a device!");
                                                    return;
                                                }

                                                var myValidator = new Array(ddnsProvider, hostName, intefaceComboF, userNameDDNS, passWordDDNS);
                                                for (var i = 0; i < myValidator.length; i++) {
                                                    ExtWnms.validateTextInput(myValidator[i]);
                                                    if (myValidator[i].hasActiveError()) {
                                                        ExtWnms.alertError(myValidator[i].getActiveError());
                                                        return;
                                                    }
                                                }

                                                var index = storeForwadingDynamicDNS.find('hostName', hostName.getValue());
                                                if (index == -1) {
                                                    addDynamicDNS(ddnsProvider.getValue(), hostName.getValue(), intefaceComboF.getValue(), userNameDDNS.getValue(), passWordDDNS.getValue());
                                                } else {
                                                    ExtWnms.alertError("Hostname already exists");
                                                    return;
                                                }
                                            }
                                        }]
                                }]
                        });
                    }
                    if (winAddDynamicDNS.isVisible()) {
                        winAddDynamicDNS.hide(this, function () {
                        });
                    } else {
                        ddnsProvider.setValue('');
                        ddnsProvider.reset();
                        hostName.setValue('');
                        hostName.reset();
                        intefaceComboF.setValue();
                        intefaceComboF.reset();
                        userNameDDNS.setValue('');
                        userNameDDNS.reset();
                        passWordDDNS.setValue('');
                        passWordDDNS.reset();
                        winAddDynamicDNS.show(this, function () {
                        });
                    }
                }

            },
        ], //end of tbar


    });

    var forwardingFormPanel = Ext.widget({
        xtype: 'form',
        layout: 'column',
        id: 'forwardingFormPanelId',
        frame: true,
        border: true,
        items: [
            {
                xtype: 'container',
                columnWidth: .5,
                layout: 'fit',
                items: [forwardingGrid, forwardingForm]
            },
            {
                xtype: 'container',
                columnWidth: .5,
                layout: 'fit',
                items: [dnsGrid, dnsForm]
            }
        ]
    });

    function createPopUpAddStaticRoute() {

    }
    function addStaticRoute(desIp, mask, gateway, inter, type) {
        myMask.show();
        Ext.Ajax.request({
            url: 'getResultAddStaticRoute',
            timeout: 20000,
            params: {
                'ipAddress': ipAddress.dom.value,
                'serial': serial.dom.value,
                'desIp': desIp,
                'gateway': gateway,
                'subnetMark': mask,
                'interface': inter,
                'type': type
            },
            success: function (response) {
                myMask.hide();
                if (response.responseText === "ipError") {
                    ExtWnms.alertError("Invalid Destination Ip or Subnet Mask!");
                    return;
                }
                if (response.responseText === "error") {
                    ExtWnms.alertError("Error! Please try again");
                    return;
                }
                
                winAddStaticRouting.hide();
                 
                getForwardInit('add');

            },
            failure: function (response) {
                myMask.hide();
                ExtWnms.alertError("Error! Please try again!");
                return;
            },
        });
    }

    function addDynamicDNS(ddnsProvider, hostName, inter, user, passWord) {
        myMask.show();
        Ext.Ajax.request({
            url: 'getResultAddDynamicDNS',
            timeout: 20000,
            params: {
                'ipAddress': ipAddress.dom.value,
                'serial': serial.dom.value,
                'ddnsProvider': ddnsProvider,
                'hostName': hostName,
                'interface': inter,
                'user': user,
                'pass': passWord
            },
            success: function (response) {
                myMask.hide();
                if (response.responseText == "error") {
                    ExtWnms.alertError("Error!");
                    return;
                }
                winAddDynamicDNS.hide();
                getForwardInit('add');
               
            },
            failure: function (response) {
                myMask.hide();
                ExtWnms.alertError("Error! Please try again!");
                return;
            },
        });
    }

    function editDNSServer(primaryDNS, secondaryDNS) {
        myMask.show();
        Ext.Ajax.request({
            url: 'getResultDNSServer',
            timeout: 20000,
            params: {
                'ipAddress': ipAddress.dom.value,
                'serial': serial.dom.value,
                'primaryDNS': primaryDNS,
                'secondaryDNS': secondaryDNS
            },
            success: function (response) {
                myMask.hide();
                if (response.responseText == "error") {
                    ExtWnms.alertError("Error!");
                    return;
                }
                ExtWnms.alertSuccess("Change DNS Server successfully! Please reboot the device to update this new configuration.!");

            },
            failure: function (response) {
                myMask.hide();
                ExtWnms.alertError("Error! Please try again!");
                return;
            },
        });
    }

    function editDefaultSetting(dns, gateway) {
        myMask.show();
        Ext.Ajax.request({
            url: 'getResultSaveDefaultForward',
            timeout: 20000,
            params: {
                'ipAddress': ipAddress.dom.value,
                'serial': serial.dom.value,
                'dns': dns,
                'gateway': gateway
            },
            success: function (response) {
                myMask.hide();
                if (response.responseText == "error") {
                    ExtWnms.alertError("Error!");
                    return;
                }
                ExtWnms.alertSuccess("Change default setting successfully! Please reboot the device to update this new configuration.!");

            },
            failure: function (response) {
                myMask.hide();
                ExtWnms.alertError("Error! Please try again!");
                return;
            },
        });
    }


</script>