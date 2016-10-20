<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script>
    var destinationF = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: 'Destination',
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
        name: 'Gateway',
        id: 'gatewayF',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
        vtype: 'IPAddress'
    });


    var interfaceName = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: 'Interface',
        name: 'interface',
        id: 'interface',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
    });
    var idListF = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: 'IdList',
        name: 'id',
        id: 'idF',
        labelAlign: 'top',
        anchor: '99%',
        allowBlank: false,
    });




    ///------------function------------------------------
    function addStaticRoute(desIp, mask, gateway, inter, idList) {
        myMask.show();
        Ext.Ajax.request({
            url: 'getStaticRoute',
            timeout: 120000,
            params: {
                'destIp': desIp,
                'gateway': gateway,
                'subnetMark': mask,
                'interfaceName': inter,
                'idList': idList
            },
            success: function(response) {
                myMask.hide();
                var json = Ext.decode(response.responseText);
                if (json.saveStatus === "error") {
                    ExtWnms.alertError("Error! Please try again");
                    return;
                } else if (json.saveStatus === "success") {
                    ExtWnms.alertError("Success");

                }

                winAddStaticRouting.hide();

                //   getForwardInit('add');

            },
            failure: function(response) {
                myMask.hide();
                ExtWnms.alertError("Error! Please try again!");
                return;
            },
        });
    }


    var winAddStaticRouting = Ext.create('widget.window', {
        title: 'Add Static Route',
        closeAction: 'hide',
        width: 300,
        minWidth: 250,
        height: 300,
        items: [{
                xtype: 'form',
                layout: 'form',
                id: 'addStaticForm',
                frame: true,
//                bodyPadding: '5 5 0',
                items: [{
                        xtype: 'container',
                        anchor: '100%',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: 1,
                                layout: 'anchor',
                                items: [destinationF, subnetMaskF, interfaceName, gatewayF, idListF]
                            }]
                    }, ],
                buttons: [{
                        id: 'btnStaticRoute',
                        text: 'Add Static Route',
                        handler: function() {
                            var msg = '<fmt:message key="message.staticroute.confirm"/>';
                            Ext.MessageBox.confirm('<fmt:message key="message.confirm"/>', msg, function(btn) {
                                if (btn == 'yes')
                                    addStaticRoute(destinationF.getValue(), subnetMaskF.getValue(), gatewayF.getValue(), interfaceName.getValue(), idListF.getValue());
                            });

                        }
                    }]
            }]
    });



</script>