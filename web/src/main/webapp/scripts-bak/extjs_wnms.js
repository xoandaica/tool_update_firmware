//Bind time with date selected in the picker

/**
 * Override Ext.data.Connection to check expired connection
 */
Ext.util.Observable.observe(Ext.data.Connection, {
    requestcomplete: function(conn, response, options) {
        // Do stuff on success
        //console.log('requestcomplete');

        if (response.getAllResponseHeaders !== undefined) {
            var header = response.getAllResponseHeaders();
            var contentType = header['content-type'];
            if (contentType.indexOf("text/html") > -1 && response.status == 200) {
                console.log('session expired');
                Ext.MessageBox.alert('Thông báo', 'Tài khoản người dùng của bạn bị hết hạn đăng nhập. Xin vui lòng đăng nhập để sử dụng tiếp', function() {
                });
                location.reload();
            }
        }
    },
    requestexception: function(conn, response, options) {
        // Do stuff on failure
        //console.log('requestexception');
    }
});

Ext.apply(Ext.form.field.VTypes, {
    IPAddress: function(v) {
        if (/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(v)) {
            var temp = v.split('.');
            for (i = 0; i < temp.length; i++) {
                if (parseInt(temp[i]) > 255) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    },
    IPAddressText: 'Must be a numeric IP address X.X.X.X and X < 256',
    IPAddressMask: /[\d\.]/i
});
Ext.apply(Ext.form.field.VTypes, {
    MacAddress: function(v) {
        return /^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$/.test(v);
    },
    MacAddressText: 'Must be a numeric MAC',
    MacAddressMask: /[\d\.]/i
});

function openPopupWindow(page, IEWidth, IEHeight, NNWidth, NNHeight) {
    var IEWidth = "100%";
    var IEHeight = "100%";

    var myWin = window
            .open(
                    page,
                    "MainWin",
                    "width="
                    + IEWidth
                    + ",height="
                    + IEHeight
                    + ",screenX="
                    + IEWidth
                    + ",screenY="
                    + IEHeight
                    + ", scrollbars=yes,toolbar=0,status=1,menubar=0,resizable=0,titlebar=no");

    myWin.focus();

}
var form = null;
Ext.require(['Ext.ux.form.field.BoxSelect'

]);
function openModalPopupWindow(url, width, height) {
    if (!width) {
        width = window.screen.width;
    }
    if (!height) {
        height = window.screen.height;
    }
    // alert(width + '' + height)
    if (window.showModalDialog) {
        myMask.show();
        window.showModalDialog(url, '_blank', 'dialogWidth:' + width
                + ';dialogHeight:' + height);
        myMask.hide();
    } else {
        popUpObj = window.open(url, "ModalPopUp", "toolbar=no,"
                + "scrollbars=yes," + "location=yes," + "statusbar=no,"
                + "menubar=no," + "resizable=0," + "width=" + width + ",",
                "height=" + height + ",", "left = 490," + "top=300");
        popUpObj.focus();
    }

}
ExtWnms = {};
var myMask = '';
Ext
        .onReady(function() {

            Ext.form.VTypes["hostnameVal1"] = /^[a-zA-Z][-.a-zA-Z0-9]{0,254}$/;
            Ext.form.VTypes["hostnameVal2"] = /^[a-zA-Z]([-a-zA-Z0-9]{0,61}[a-zA-Z0-9]){0,1}([.][a-zA-Z]([-a-zA-Z0-9]{0,61}[a-zA-Z0-9]){0,1}){0,}$/;
            Ext.form.VTypes["ipVal"] = /^([1-9][0-9]{0,1}|1[013-9][0-9]|12[0-689]|2[01][0-9]|22[0-3])([.]([1-9]{0,1}[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])){2}[.]([1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-4])$/;
            Ext.form.VTypes["netmaskVal"] = /^(128|192|224|24[08]|25[245].0.0.0)|(255.(0|128|192|224|24[08]|25[245]).0.0)|(255.255.(0|128|192|224|24[08]|25[245]).0)|(255.255.255.(0|128|192|224|24[08]|252))$/;
            Ext.form.VTypes["portVal"] = /^(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/;
            Ext.form.VTypes["multicastVal"] = /^((22[5-9]|23[0-9])([.](0|[1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])){3})|(224[.]([1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.](0|[1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])){2})|(224[.]0[.]([1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.](0|[1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])))$/;
            Ext.form.VTypes["usernameVal"] = /^[a-zA-Z][-_.a-zA-Z0-9]{0,30}$/;
            Ext.form.VTypes["passwordVal1"] = /^.{6,31}$/;
            Ext.form.VTypes["passwordVal2"] = /[^a-zA-Z].*[^a-zA-Z]/;
            Ext.form.VTypes["hostname"] = function(v) {
                if (!Ext.form.VTypes["hostnameVal1"].test(v)) {
                    Ext.form.VTypes["hostnameText"] = "Must begin with a letter and not exceed 255 characters"
                    return false;
                }
                Ext.form.VTypes["hostnameText"] = "L[.L][.L][.L][...] where L begins with a letter, ends with a letter or number, and does not exceed 63 characters";
                return Ext.form.VTypes["hostnameVal2"].test(v);
            }
            Ext.form.VTypes["hostnameText"] = "Invalid Hostname"
            Ext.form.VTypes["hostnameMask"] = /[-.a-zA-Z0-9]/;
            Ext.form.VTypes["ip"] = function(v) {
                return Ext.form.VTypes["ipVal"].test(v);
            }
            Ext.form.VTypes["ipText"] = "1.0.0.1 - 223.255.255.254 excluding 127.x.x.x"
            Ext.form.VTypes["ipMask"] = /[.0-9]/;
            Ext.form.VTypes["netmask"] = function(v) {
                return Ext.form.VTypes["netmaskVal"].test(v);
            }
            Ext.form.VTypes["netmaskText"] = "128.0.0.0 - 255.255.255.252"
            Ext.form.VTypes["netmaskMask"] = /[.0-9]/;
            Ext.form.VTypes["port"] = function(v) {
                return Ext.form.VTypes["portVal"].test(v);
            }
            Ext.form.VTypes["portText"] = "0 - 65535";
            Ext.form.VTypes["portMask"] = /[0-9]/;
            Ext.form.VTypes["multicast"] = function(v) {
                return Ext.form.VTypes["multicastVal"].test(v);
            }
            Ext.form.VTypes["multicastText"] = "224.0.1.0 - 239.255.255.255";
            Ext.form.VTypes["multicastMask"] = /[.0-9]/;
            Ext.form.VTypes["username"] = function(v) {
                return Ext.form.VTypes["usernameVal"].test(v);
            }
            Ext.form.VTypes["usernameText"] = "Username must begin with a letter and cannot exceed 255 characters"
            Ext.form.VTypes["usernameMask"] = /[-_.a-zA-Z0-9]/;
            Ext.form.VTypes["password"] = function(v) {
                if (!Ext.form.VTypes["passwordVal1"].test(v)) {
                    Ext.form.VTypes["passwordText"] = "Password length must be 6 to 31 characters long";
                    return false;
                }
                Ext.form.VTypes["passwordText"] = "Password must include atleast 2 numbers or symbols";
                return Ext.form.VTypes["passwordVal2"].test(v);
            }
            Ext.form.VTypes["passwordText"] = "Invalid Password"
            Ext.form.VTypes["passwordMask"] = /./;
            Ext.apply(Ext.form.VTypes, {
                usPhone: function(v) {
                    return /[0-9]$/.test(v);
                },
                usPhoneText: 'Please enter a valid Phone number',
                usPhoneMask: /[\d\s\(\)\-]/i
            });
            Ext.apply(Ext.form.VTypes, {
                numberIp: function(v) {
                    return /[.0-9]$/.test(v);
                },
                numberIpText: 'Please enter number and .',
                numberIpMask: /[.0-9]/
            });
            myMask = new Ext.LoadMask(Ext.getBody(), {
                msg: "Please wait..."
            });
            var colors = ['url(#v-1)', 'url(#v-2)', 'url(#v-3)', 'url(#v-4)',
                'url(#v-5)'];

            var baseColor = '#eee';
            // var colors = ['rgb(47, 162, 223)',
            // 'rgb(60, 133, 46)',
            // 'rgb(234, 102, 17)',
            // 'rgb(154, 176, 213)',
            // 'rgb(186, 10, 25)',
            // 'rgb(40, 40, 40)'];
            var colors = ["#94ae0a", "#115fa6", 'rgb(47, 162, 223)',
                'rgb(60, 133, 46)', 'rgb(234, 102, 17)',
                'rgb(154, 176, 213)', 'rgb(186, 10, 25)', 'rgb(40, 40, 40)'];
            //	
            // "#94ae0a", "#115fa6","#a61120", "#ff8809", "#ffd13e", "#a61187",
            // "#24ad9a", "#7c7474", "#a66111"

            Ext.define('Ext.chart.theme.Fancy', {
                extend: 'Ext.chart.theme.Base',
                constructor: function(config) {
                    this.callParent([Ext.apply({
                            colors: colors,
                            axisTitleLeft: {
                                font: 'bold 11px Arial'
                            },
                            axisTitleBottom: {
                                font: 'bold 11px Arial'
                            },
                        }, config)]);
                }
            });

        });
var xyx = null;
ExtWnms.getTools = function() {
    return [/*
     * { xtype : 'tool', type : 'gear', handler : function(e,
     * target, panelHeader, tool) { // alert(panelHeader.child())
     * var abc = panelHeader.child(2); // // // // if (xyx){ //
     * xyx.show(); // } else { // xyx=
     * ExtWnms.createExtWindow('testid',panelHeader.child()); //
     * xyx.show(); // } // // var portlet = panelHeader.ownerCt; //
     * portlet.setLoading('Loading...'); // Ext.defer(function() { //
     * portlet.setLoading(false); // }, 2000); } }
     */
        /*
         * ,{ xtype: 'tool', type: 'save', handler: function(e, target, panelHeader,
         * tool){ Ext.MessageBox.confirm('Confirm Download', 'Would you like to
         * download the chart as an image?', function(choice){ if(choice == 'yes'){
         * chart.save({ type: 'image/png' }); } }); } }
         */

    ];
};

ExtWnms.createExtChartTopBar = function(id, titleAxes, xField, yField, store1,
        unit, urlStr, fieldPopup) {
    if (!unit) {
        unit = '';
    }

    if (!fieldPopup) {
        fieldPopup = 'id';
    }
    var chartDownload = Ext.create('Ext.chart.Chart',
            {
                id: id,
                xtype: 'chart',
                theme: "Fancy",
                style: 'background:#fff',
                animate: true,
                shadow: true,
                store: store1,
                legend: {
                    position: 'bottom',
                    visible: false
                },
                axes: [
                    {
                        type: 'Numeric',
                        position: 'bottom',
                        fields: [yField],
                        minimum: 0,
                        // majorTickSteps: 5,
                        step: 1,
                        // minorTickSteps: 2,
                        decimals: 0,
                        alwaysShowZero: false,
                        label: {
                            renderer: function(v) {
                                // v = v + '';
                                // if (v.indexOf(".")){
                                // return '';
                                // } else {
                                // return v;
                                // }
                                // return v;
                                return v.toFixed(0);
                            }
                        },
                        grid: true,
                        title: titleAxes
                    },
                    {
                        type: 'Category',
                        position: 'left',
                        label: {
                            renderer: function(v) {
                                var abc = v;
                                if (v && v != null && v != 'null'
                                        && v.length > 10) {
                                    abc = v.substring(0, 10) + '...';
                                }

                                return abc;
                            }
                        },
                        tips: {
                            trackMouse: true,
                            width: 300,
                            // height : 28,
                            renderer: function(storeItem, item) {
                                this.setTitle(storeItem.get(xField)
                                        + ': '
                                        + Ext.util.Format.number(
                                                parseInt(storeItem
                                                        .get(yField)),
                                                '0,000/i') + unit);
                            }
                        },
                        fields: [xField]
                    }],
                series: [{
                        type: 'bar',
                        axis: 'bottom',
                        xField: xField,
                        grid: true,
                        listeners: {
                            'itemdblclick': function(item) {
                                console.log("hello");
                                if (urlStr) {
                                    console.log(fieldPopup);
                                    if (item.storeItem.data[fieldPopup]) {
                                        console.log("hello3");
                                        openModalPopupWindow(urlStr
                                                + item.storeItem.data[fieldPopup]);
                                    }
                                }

                            }
                        },
                        tips: {
                            trackMouse: true,
                            width: '20%',
                            // height : '10%',
                            renderer: function(storeItem, item) {

                                var abc = storeItem.get(xField)
                                        + ': '
                                        + Ext.util.Format.number(parseInt(storeItem
                                                .get(yField)), '0,000/i') + ' '
                                        + unit;

                                this.setTitle('<table><tr><td>' + abc
                                        + '</td></tr><table>');
                            }
                        },
                        label: {
                            display: 'insideEnd',
                            field: yField,
                            // renderer: Ext.util.Format.numberRenderer('0'),
                            renderer: Ext.util.Format.numberRenderer('0,0'),
                            orientation: 'horizontal',
                            color: '#333',
                            'text-anchor': 'middle'
                        },
                        yField: [yField]
                    }]
            });
    return chartDownload;
};
ExtWnms.createExtChartTopBar2 = function(id, titleAxes, xField, yField,
        yField2, store1, unit, urlStr, fieldPopup, titleSxis) {
    if (!unit) {
        unit = '';
    }

    if (!fieldPopup) {
        fieldPopup = 'id';
    }
    var chartDownload = Ext.create('Ext.chart.Chart',
            {
                id: id,
                xtype: 'chart',
                theme: "Fancy",
                style: 'background:#fff',
                animate: true,
                shadow: true,
                store: store1,
                legend: {
                    position: 'bottom',
                    labelFont: '8px Helvetica, sans-serif'
                },
                axes: [
                    {
                        type: 'Numeric',
                        position: 'bottom',
                        fields: [yField, yField2],
                        minimum: 0,
                        label: {
                            renderer: Ext.util.Format
                                    .numberRenderer('0,0')
                        },
                        grid: true,
                        title: titleAxes
                    },
                    {
                        type: 'Category',
                        position: 'left',
                        label: {
                            renderer: function(v) {
                                var abc = v;
                                if (v && v != null && v != 'null'
                                        && v.length > 10) {
                                    abc = v.substring(0, 10) + '...';
                                }

                                return abc;
                            }
                        },
                        tips: {
                            trackMouse: true,
                            width: 300,
                            // height : 28,
                            renderer: function(storeItem, item) {
                                this.setTitle(storeItem.get(xField)
                                        + ': '
                                        + Ext.util.Format.number(
                                                parseInt(storeItem
                                                        .get(yField)),
                                                '0,000/i') + unit);
                            }
                        },
                        fields: [xField]
                    }],
                series: [{
                        type: 'bar',
                        axis: 'bottom',
                        xField: xField,
                        grid: true,
                        listeners: {
                            'itemdblclick': function(item) {
                                // console.log("hello");
                                if (urlStr) {
                                    console.log(fieldPopup);
                                    if (item.storeItem.data[fieldPopup]) {
                                        // console.log("hello3");
                                        openModalPopupWindow(urlStr
                                                + item.storeItem.data[fieldPopup]);
                                    }
                                }

                            }
                        },
                        tips: {
                            trackMouse: true,
                            width: '20%',
                            // height : '10%',
                            renderer: function(storeItem, item) {
                                console.log(item)

                                var abc = storeItem.get(xField)
                                        + ': '
                                        + Ext.util.Format.number(parseInt(storeItem
                                                .get(item.yField)), '0,000/i')
                                        + ' ' + unit;

                                this.setTitle('<table><tr><td>' + abc
                                        + '</td></tr><table>');
                            }
                        },
                        label: {
                            display: 'insideEnd',
                            field: [yField, yField2],
                            // renderer: Ext.util.Format.numberRenderer('0'),
                            renderer: Ext.util.Format.numberRenderer('0,0'),
                            orientation: 'horizontal',
                            color: '#333',
                            'text-anchor': 'middle'
                        },
                        yField: [yField, yField2],
                        title: titleSxis
                    }]
            });
    return chartDownload;
};

ExtWnms.createExtChartDynamicLine = function(id, title, xtitle, ytitle, xField,
        yField, store, series) {

    chart = Ext.create('Ext.chart.Chart', {
        xtype: 'chart',
        style: 'background:#fff',
        id: id,
        store: store,
        shadow: false,
        theme: "Fancy",
        animate: true,
        axisLabelLeft: {
            fill: '#000',
            font: '8px Arial'
        },
        axisTitleLeft: {
            fill: '#000',
            font: '8px Arial'
        },
        axes: [{
                type: 'Numeric',
                grid: true,
                minimum: 0,
                maximum: 100,
                position: 'left',
                fields: ['label'],
                title: xtitle,
                grid : {
                    odd: {
                        fill: '#dedede',
                        stroke: '#ddd',
                        'stroke-width': 0.5
                    }
                }
            }, {
                type: 'Time',
                position: 'bottom',
                fields: 'value',
                title: ytitle,
                dateFormat: 'M d',
                // groupBy: 'year,month,day',
                // aggregateOp: 'sum',

                // constrain: true,
                // fromDate: new Date(2011, 1, 1),
                // toDate: new Date(2011, 1, 7),
                grid: true
            }],
        series: series
    });
    return chart;
};
ExtWnms.createTextFielReadOnly = function(textDisplay, name, id) {
    var label = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: textDisplay,
        allowNegative: false,
        labelClsExtra: 'label-bold',
        name: name,
        id: id,
        readOnly: true,
        labelAlign: 'top',
        anchor: '99%',
    });
    return label;
};
ExtWnms.createTextFielReadOnlyNoAnchor = function(textDisplay, name, id) {
    var label = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: textDisplay,
        labelClsExtra: 'label-bold',
        name: name,
        margin: '0 0 0 10',
        id: id,
        readOnly: true,
        labelAlign: 'left',
        anchor: '40%',
    });
    return label;
};

ExtWnms.createExtChartLine = function(id, title, xtitle, ytitle, xField,
        yField, store, series, axes, showLengend) {
    if (showLengend != undefined) {
        showLengend = showLengend;
    } else {
        showLengend = true;
    }

    var chart = Ext.create('Ext.chart.Chart', {
        xtype: 'chart',
        style: 'background:#fff',
        animate: true,
        store: store,
        id: id,
        shadow: true,
        theme: 'Fancy',
        legend: {
            visible: showLengend,
            position: 'bottom',
            labelFont: '8px Helvetica, sans-serif'
        },
        axes: axes,
        series: series
    });
    return chart;
};
ExtWnms.alertError = function(msgr) {
    // var msgr = '<fmt:message key="redFields.invalid"/>';
    Ext.MessageBox.show({
       title: 'Warning',
        msg: msgr,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.WARNING
    });
    return;
};
ExtWnms.alertSuccess = function(msgr) {
    // var msgr = '<fmt:message key="redFields.invalid"/>';
    Ext.MessageBox.show({
        title: 'Success',
        msg: msgr,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.SUCESS
    });
    return;
};
ExtWnms.validateTextInput = function(vpiText) {
    var erors = vpiText.getErrors(vpiText.getValue());
    if (erors.length > 0) {
        Ext.each(erors, function(value) {
            if (value !== undefined) {
                vpiText.setActiveError(vpiText.getName() + " error: " + value);
                return false;
            }
        });
    }
    return true;
};

ExtWnms.createTextFieldNumberValidate = function(textDisplay, name, id, min,
        max) {
    var label = Ext.create('Ext.form.field.Text', {
        xtype: 'numberfield',
        fieldLabel: textDisplay,
        allowNegative: false,
        labelClsExtra: 'label-bold',
        name: name,
        id: id,
        allowBlank: false,
        labelAlign: 'top',
        anchor: '99%',
        validator: function(val) {
            if (!Ext.isEmpty(val)) {
                if (!Ext.isNumeric(val)) {
                    return "Must be a number";
                } else if (parseInt(val) > max) {
                    return textDisplay + " must less than " + max;
                } else if (parseInt(val) < min) {
                    return textDisplay + " must greater than " + min;
                } else
                    return true;
            }
        }
    });
    return label;
};
ExtWnms.createTextFiel = function(textDisplay, name, id) {
    var label = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: textDisplay,
        allowNegative: false,
        labelClsExtra: 'label-bold',
        name: name,
        id: id,
        labelAlign: 'top',
        anchor: '99%',
    });
    return label;
};
ExtWnms.createTextFielMax = function(textDisplay, name, id, length) {
    var label = Ext.create('Ext.form.field.Text', {
        xtype: 'textfield',
        fieldLabel: textDisplay,
        allowNegative: false,
        labelClsExtra: 'label-bold',
        name: name,
        id: id,
        labelAlign: 'top',
        anchor: '99%',
        maxLength: length
    });
    return label;
};

ExtWnms.createExtChartPie = function(id, title, xField, yField, storePie,
        donut, legendPosition) {
    if (!legendPosition) {
        legendPosition = 'bottom';
    }
    if (!donut) {
        donut = false;
    }
    chart = Ext.create('Ext.chart.Chart', {
        xtype: 'chart',
        id: id,
        animate: true,
        store: storePie,
        shadow: true,
        legend: {
            position: legendPosition
        },
        insetPadding: 30,
        theme: 'Base:gradients',
        series: [{
                type: 'pie',
                /*
                 * colorSet: [ 'red', 'blue' ],
                 */
                field: yField,
                showInLegend: true,
                donut: donut,
                tips: {
                    trackMouse: true,
                    width: 200,
                    // height : 28,
                    renderer: function(storeItem, item) {
                        // calculate percentage.
                        var total = 0;
                        storePie.each(function(rec) {
                            total += rec.get(yField);
                        });
                        this.setTitle(storeItem.get(xField)
                                + ': '
                                + Math.round(storeItem.get(yField) / total * 100)
                                + '%'
                                + '  : '
                                + Ext.util.Format.number(parseInt(storeItem
                                        .get(yField)), '0,000/i'));
                    }
                },
                /*
                 * listeners : { 'itemclick' : function(item) { alert("View detail
                 * of " + item.storeItem.get(xField)) } },
                 */

                highlight: {
                    segment: {
                        margin: 20
                    }
                },
                label: {
                    visible: false,
                    field: xField,
                    display: 'rotate',
                    contrast: true,
                    font: '8px Arial',
                },
            }]
    });
    return chart;
};

ExtWnms.createExtChartPieStatistic = function(id, title, xField, yField,
        storePie, donut, legendPosition, type) {
    if (!legendPosition) {
        legendPosition = 'bottom';
    }
    if (!donut) {
        donut = false;
    }
    chart = Ext.create('Ext.chart.Chart', {
        xtype: 'chart',
        id: id,
        animate: true,
        store: storePie,
        shadow: true,
        legend: {
            position: legendPosition
        },
        insetPadding: 30,
        theme: 'Base:gradients',
        series: [{
                type: 'pie',
                colorSet: ["#CC0200", "#FF7F00", "yellow", "#7790F7", "#00f"],
                field: yField,
                showInLegend: true,
                donut: donut,
                tips: {
                    trackMouse: true,
                    width: 140,
                    // height : 28,
                    renderer: function(storeItem, item) {
                        // calculate percentage.
                        var total = 0;
                        storePie.each(function(rec) {
                            total += rec.get(yField);
                        });
                        this.setTitle(storeItem.get(xField)
                                + ': '
                                + Math.round(storeItem.get(yField) / total * 100)
                                + '%'
                                + '  : '
                                + Ext.util.Format.number(parseInt(storeItem
                                        .get(yField)), '0,000/i'));
                    }
                },
                highlight: {
                    segment: {
                        margin: 20
                    }
                },
                label: {
                    visible: false,
                    field: xField,
                    display: 'rotate',
                    contrast: true,
                    font: '8px Arial',
                },
            }]
    });
    return chart;
};

ExtWnms.createExtChartPieStatus = function(id, title, xField, yField, storePie,
        donut, legendPosition, type) {
    if (!legendPosition) {
        legendPosition = 'bottom';
    }
    if (!donut) {
        donut = false;
    }
    chart = Ext.create('Ext.chart.Chart', {
        xtype: 'chart',
        id: id,
        animate: true,
        store: storePie,
        shadow: true,
        legend: {
            position: legendPosition
        },
        insetPadding: 30,
        theme: 'Base:gradients',
        series: [{
                type: 'pie',
                colorSet: ["#356AA0", "#CC0200", "#00f"],
                field: yField,
                showInLegend: true,
                donut: donut,
                tips: {
                    trackMouse: true,
                    width: 140,
                    // height : 28,
                    renderer: function(storeItem, item) {
                        // calculate percentage.
                        var total = 0;
                        storePie.each(function(rec) {
                            if (rec.get(yField))
                                total += parseInt(rec.get(yField));
                        });
                        this.setTitle(storeItem.get(xField)
                                + ': '
                                + Math.round(storeItem.get(yField) / total * 100)
                                + '%'
                                + '  : '
                                + Ext.util.Format.number(parseInt(storeItem
                                        .get(yField)), '0,000/i'));
                    }
                },
                highlight: {
                    segment: {
                        margin: 20
                    }
                },
                label: {
                    visible: false,
                    field: xField,
                    display: 'rotate',
                    contrast: true,
                    font: '8px Arial',
                },
            }]
    });
    return chart;
};
ExtWnms.arrayize = function(theForm) {
    var arrComp = document.getElementById(theForm).findBy(
            function(c, o) {
                if (c.xtype == 'editorgrid' || c.xtype == 'myeditorgrid'
                        || c.xtype == 'checkbox' || c.xtype == 'combo'
                        || c.xtype == 'datefield' || c.xtype == 'field'
                        || c.xtype == 'fieldset' || c.xtype == 'hidden'
                        || c.xtype == 'htmleditor' || c.xtype == 'numberfield'
                        || c.xtype == 'radio' || c.xtype == 'textarea'
                        || c.xtype == 'textfield' || c.xtype == 'timefield'
                        || c.xtype == 'trigger' || c.xtype == 'label') {
                    return true;
                } else {
                    return false;
                }
            });
    theForm.arrComp = arrComp;
    return arrComp;
};

function convertForm(formId) {
    alert(Ext.getCmp(formId));
}
// -----------------------------------------------------------------------
ExtWnms.constructParam = function(theForm) {
    var FormValue = {};
    var arrComp = ExtWnms.arrayize(theForm);
    var strvalidate = '';
    for (var i = 0; i < arrComp.length; i++) {
        if (!arrComp[i].dontSend) {
            var binding = (arrComp[i].binding ? arrComp[i].binding + "." : "")
                    + arrComp[i].name;
            if ((arrComp[i].xtype == 'checkbox')) {
                if (arrComp[i].validValue) {
                    var validValue = arrComp[i].validValue;
                    if ((arrComp[i].checked))
                        FormValue[binding] = validValue.check;
                    else
                        FormValue[binding] = validValue.uncheck;
                } else {
                    FormValue[binding] = arrComp[i].getValue();
                }
            } else if ((arrComp[i].xtype == 'radio')) {
                // budi add valid value for radio button
                if (arrComp[i].checked) {

                    if (arrComp[i].validValue) {
                        var validValue = arrComp[i].validValue;
                        if ((arrComp[i].checked))
                            FormValue[binding] = validValue.check;
                    } else {
                        FormValue[binding] = arrComp[i].getValue();
                    }
                }
                // FormValue[binding]=arrComp[i].getGroupValue();
            } else if (arrComp[i].xtype == 'fieldset') {
                // do
                // nothigFormValue[arrComp[i].name]=arrComp[i].getGroupValue();
            } else if (arrComp[i].xtype == 'combo') {
                FormValue[binding] = arrComp[i].getValue();
                if (doValidation) {
                    arrComp[i].validate();
                    // minhvd coded under cuongnh3's instruction
                    if (!arrComp[i].isValid(false)
                            && arrComp[i].getRawValue().trim() == '') {
                        strvalidate += 'Please fill in '
                                + arrComp[i].fieldLabel + '<br>';
                    } else if (!arrComp[i].isValid(false)) {
                        strvalidate += '[' + arrComp[i].fieldLabel + '] '
                                + arrComp[i].invalidText + '<br>';
                    }
                }
            } else if (arrComp[i].xtype == 'datefield') {
                //				
            } else {

            }
        }
    }
    if (strvalidate != '') {
        global.curValid = false;
        Ext.Msg.alert('Invalid', strvalidate);
    }
    return {
        formValue: FormValue,
        valid: strvalidate == ''
    };
};

ExtWnms.createExtChartColumn = function(id, title, xtitle, ytitle, xField,
        yField, storePie) {

    // var colors = [ 'url(#v-1)', 'url(#v-2)', 'url(#v-3)', 'url(#v-4)',
    // 'url(#v-5)' ];
    var colors = ['#FF000B', '#FF7F00', 'FFDB00', '#4464BB', 'green'];
    var baseColor = '#eee';
    var chartavb = Ext.create('Ext.chart.Chart', {
        id: id,
        xtype: 'chart',
        theme: 'Fancy',
        animate: {
            easing: 'bounceOut',
            duration: 750
        },
        store: storePie,
        // gradients : [ {
        // 'id' : 'v-1',
        // 'angle' : 0,
        //
        // stops : {
        // 0 : {
        // color : 'red'
        // },
        // 100 : {
        // color : 'red'
        // }
        // }
        // }, {
        // 'id' : 'v-2',
        // 'angle' : 0,
        // stops : {
        // 0 : {
        // color : 'orange'
        // },
        // 100 : {
        // color : 'orange'
        // }
        // }
        // }, {
        // 'id' : 'v-3',
        // 'angle' : 0,
        // stops : {
        // 0 : {
        // color : 'yellow'
        // },
        // 100 : {
        // color : 'yellow'
        // }
        // }
        // }, {
        // 'id' : 'v-4',
        // 'angle' : 0,
        // stops : {
        // 0 : {
        // color : 'rgb(212, 40, 40)'
        // },
        // 100 : {
        // color : 'rgb(117, 14, 14)'
        // }
        // }
        // } ],
        axes: [{
                type: 'Numeric',
                position: 'left',
                fields: [yField],
                minimum: 0,
                // maximum: 100,
                label: {
                    renderer: Ext.util.Format.numberRenderer('0,0')
                            // renderer: function (v) {
                            // return v.toFixed(0);
                            // }

                },
                title: xtitle,
                grid: {
                    odd: {
                        stroke: '#555'
                    },
                    even: {
                        stroke: '#555'
                    }
                }
            }, {
                type: 'Category',
                position: 'bottom',
                fields: [xField],
                title: ytitle
            }],
        series: [{
                type: 'column',
                axis: 'left',
                highlight: true,
                label: {
                    display: 'insideEnd',
                    'text-anchor': 'middle',
                    field: yField,
                    orientation: 'horizontal',
                    fill: '#fffaa',
                    font: '15px Arial'
                },
                renderer: function(sprite, storeItem, barAttr, i, store) {
                    barAttr.fill = colors[i % colors.length];
                    return barAttr;
                },
                style: {
                    opacity: 0.95
                },
                xField: xField,
                yField: yField
            }]
    });

    return chartavb;
};

ExtWnms.createExtPanel = function(id, renderTo, title, width, height, items,
        collapsible, tools, tBar) {
    var win = Ext.create('Ext.Panel', {
        width: width,
        height: height,
        minHeight: 200,
        minWidth: 200,
        hidden: false,
        titleCollapse: true,
        // hideCollapseTool:true,
        collapsible: collapsible,
        maximizable: true,
        id: id,
        title: title,
        renderTo: renderTo,
        layout: 'fit',
        tools: tools,
        // tbar: tBar,
        items: items

    });
    return win;
};
function trim(s) {
    return Ext.util.Format.trim(s);
}

ExtWnms.createExtCombobox = function(id, name, store, fieldLabel, displayField,
        valueField, editable, emptyText, renderTo) {
    var scombox2 = Ext.create('Ext.form.field.ComboBox', {
        id: id,
        hiddenName: name,
        selectOnFocus: true,
        typeAhead: true,
        fieldLabel: fieldLabel,
        labelWidth: 20,
        store: store,
        editable: editable,
        queryMode: 'local',
        labelAlign: 'top',
        displayField: displayField,
        valueField: valueField,
        // margin: '0 0 0 0',
        emptyText: emptyText,
        triggerAction: 'all',
        renderTo: renderTo
    });

    return scombox2;

};

ExtWnms.betterFormGetValues = function(formPanel) {
    var values = '';
    if (formPanel && formPanel != null) {
        var form_items = formPanel.getForm().getFields();

        // Iterate through all the form items and locate the xtype "combo"
        // if (form_items)
        // form_items.each(function(field)
        // {
        // if(field.isXType("combo"))
        // {
        // //Set the "Raw" element value to the value returned by the normal
        // method (key if present)
        // field.setRawValue(field.getValue());
        // }
        // });

        values = "&" + formPanel.getForm().getValues(true); // Standard
        // serialisation call
        // if (form_items)
        // form_items.each(function(field)
        // {
        // if(field.isXType("combo"))
        // {
        // //Returns the value back to their previous state
        // field.setValue(field.getRawValue());
        // }
        // });
    }

    return values;
}
ExtWnms.createExtComboboxNoRender = function(id, name, store, fieldLabel,
        displayField, valueField, editable, emptyText, renderTo, multiselect,
        valueDefault, width) {
    if (!width) {
        width = 150;
    }
    var scombox2 = null;
    if (multiselect) {
        scombox2 = Ext.create('Ext.ux.form.field.BoxSelect', {
            id: id,
            hiddenName: name,
            name: name,
            xtype: 'combo',
            fieldLabel: fieldLabel,
            // labelWidth: 20,
            labelAlign: 'top',
            store: store,
            editable: true,
            queryMode: 'local',
            multiSelect: true,
            // value:'',
            labelAlign : 'top',
                    // labelAlign:'top',
                    displayField: displayField,
            valueField: valueField,
            margin: '0 0 0 5',
            emptyText: emptyText,
            // typeAhead:false,
            triggerAction: 'all',
            width: 150,
            mode: 'local',
            forceSelection: true,
            listeners: {
                boxready: function() {

                    var record = this.getStore().getAt(0);
                    if (record) {
                        this.setValue(record.get(this.valueField), true);
                        // alert(record.get(this.valueField))
                        // fire the select event ( Event in extjs )
                        this.fireEvent('select', this);
                    }

                }
            }
        });

    } else {
        scombox2 = Ext.create('Ext.form.field.ComboBox', {
            id: id,
            hiddenName: name,
            name: name,
            xtype: 'combo',
            fieldLabel: fieldLabel,
            // labelWidth: 20,
            labelAlign: 'top',
            store: store,
            editable: true,
            queryMode: 'local',
            // multiSelect : true,
            // value:'',
            labelAlign : 'top',
                    // labelAlign:'top',
                    displayField: displayField,
            valueField: valueField,
            margin: '0 0 0 5',
            emptyText: emptyText,
            // typeAhead:false,
            triggerAction: 'all',
            width: width,
            mode: 'local'/*
             * , forceSelection : true
             *//*
              * , listeners : { boxready :
              * function() { if
              * (valueDefault) {
              * this.setValue(valueDefault); }
              * else { // var record =
              * this.getStore().getAt(0); //
              * if (record) { //
              * this.setValue(record.get(this.valueField),
              * true); // //
              * alert(record.get(this.valueField)) // //
              * fire the select event ( Event
              * in extjs ) //
              * this.fireEvent('select',
              * this); // } }
              *  } }
              */
        });

        scombox2.getStore().on("load", function() {
            var record = scombox2.getStore().getAt(0);
            if (valueDefault) {
                scombox2.setValue(valueDefault);
            } else {
                if (record) {
                    scombox2.setValue(record.get(valueField), true);
                    // alert(record.get(this.valueField))
                    // fire the select event ( Event in extjs )
                    scombox2.fireEvent('select', scombox2);
                }
            }
        })

    }

    return scombox2;

};
ExtWnms.createExtComboboxNoRenderLabel = function(id, name, store, fieldLabel,
        displayField, valueField, editable, emptyText, renderTo) {
    var scombox2 = Ext.create('Ext.form.field.ComboBox', {
        id: id,
        hiddenName: name,
        xtype: 'combo',
        fieldLabel: fieldLabel,
        // labelWidth: 20,
        // labelAlign: 'top',
        store: store,
        editable: editable,
        queryMode: 'local',
        // labelAlign : 'top',
        // labelAlign:'top',
        displayField: displayField,
        valueField: valueField,
        // margin: '0 0 0 5',
        emptyText: emptyText,
        triggerAction: 'all',
        width: 150,
        mode: 'local',
        forceSelection: true
    });

    return scombox2;

};

ExtWnms.createSeriesLineGrid = function(xField, yField, titleLine, unit, type) {
    if (!type) {
        type = 'line';
    }

    if (!unit) {
        unit = ' ';
    }
    var scombox2 = {
        type: type,
        highlight: {
            size: 7,
            radius: 7
        },
        style: {
            colors: ['red', '#555']
                    // should override colorArrayStyle
        },
        tips: {
            trackMouse: true,
            width: 200,
            // height : 28,
            renderer: function(storeItem, item) {
                this.setTitle(titleLine
                        + ':  '
                        + storeItem.get(xField)
                        + ' - '
                        + Ext.util.Format.number(
                                parseInt(storeItem.get(yField)), '0,000/i')
                        + ' ' + unit);
            }
        },
        axis: 'left',
        smooth: true,
        xField: xField,
        yField: yField,
        title: titleLine,
        markerConfig: {
            type: 'circle',
            size: 4,
            radius: 4,
            'stroke-width': 0
        }
    };

    return scombox2;

};
// longdq
ExtWnms.StringTrim = function(str) {
    // console.log("1"+str + "end");
    if (str == null)
        return null;
    str = str + "";
    var start = 0;
    var end = str.length;
    while (start < str.length && str.charAt(start) == ' ')
        start++;
    while (end > 0 && str.charAt(end - 1) == ' ')
        end--;
    str = str.substr(start, end - start);
    // console.log("2"+str + "end");
    return str;
}
ExtWnms.isNotEmpty = function(val) {
    if (val != null && ExtWnms.StringTrim(val) != "")
        return true
    else
        return false;

}
// ExtWnms.CheckSession = function () {
// var session = '<%=Session["admin"] != null%>';
// //session = '<%=Session["username"]%>';
// alert(session);
// if (session == false) {
// alert("Your Session has expired");
// // window.location = "login.jsp";
// }
// else {
// alert(session);
// }
// }

ExtWnms.validateComboBox = function(combo) {

    var returnVal = true;

    if (combo.getRawValue() != null
            && ExtWnms.StringTrim(combo.getRawValue()) != "") {
        var val = combo.getRawValue().toString();
        var store = combo.getStore();
        if (store.isFiltered())
            store.clearFilter();
        // console.log(val);
        var a = val.split(",");
        var valSet = new Array();
        j = 0;
        // console.log(a);
        alert(rawVal);
        for (var i = 0; i < a.length; i++) {
            var rawVal = ExtWnms.StringTrim(a[i]);
            var index = store.findExact("name", rawVal);
            if (index == -1) {
                combo.setActiveError("Invalid value.");
                returnVal = false;
            } else {
                var rec = store.getAt(index);
                valSet[j] = parseInt(rec.get('id'));
                j++;
            }

        }
        // combo.setValue(valSet);
        return returnVal;
        // console.log(find);
        // // console.log(combo.hasActiveError());

    }

};

ExtWnms.validateComboBoxSingle = function(combo) {

    var returnVal = true;

    if (combo.getRawValue() != null
            && ExtWnms.StringTrim(combo.getRawValue()) != "") {
        var val = combo.getRawValue().toString();
        var store = combo.getStore();
        if (store.isFiltered())
            store.clearFilter();
        // console.log(val);

        var valSet = new Array();

        var rawVal = ExtWnms.StringTrim(val);
        var index = store.findExact("name", rawVal);
        if (index == -1) {
            combo.setActiveError("Invalid value.");
            returnVal = false;
        } else {
            var rec = store.getAt(index);
            valSet[j] = parseInt(rec.get('id'));
            j++;

        }
        combo.setValue(valSet);
        return returnVal;
        // console.log(find);
        // // console.log(combo.hasActiveError());

    }

};
ExtWnms.createExtWindow = function(id, items) {
    win = Ext.create('widget.window', {
        title: 'Layout Window',
        closable: true,
        autoScroll: true,
        id: id,
        closeAction: 'hide',
        width: '80%',
        constrainHeader: true,
        minWidth: 350,
        height: 550,
        layout: {
            type: 'border',
            padding: 5
        },
        items: [items]

    });
    return win;
};

ExtWnms.createExtPanelNoRender = function(id, renderTo, title, width, height,
        items, collapsible, tools, hidetitle) {
    if (!hidetitle) {
        hidetitle = false;
    }
    var win = Ext.create('Ext.Panel', {
        width: width,
        height: height,
        minHeight: 200,
        minWidth: 200,
        hidden: false,
        hideTitle: hidetitle,
        titleCollapse: true,
        // hideCollapseTool:true,
        collapsible: collapsible,
        maximizable: true,
        id: id,
        title: title,
        // renderTo : renderTo,
        layout: 'fit',
        tools: tools,
        // tbar: tBar,
        items: items

    });
    return win;
};
ExtWnms.compareTwoDate = function(value1, value2, dateFormat) {
    if (!dateFormat) {
        dateFormat = 'd-m-Y H:i'
    }

    if (value1 && value2) {

        if (value1 > value2) {

            return -1
        } else if (value1 == value2) {

            return 0
        } else {

            return 1;
        }
    }

    return 1;

}

ExtWnms.bindTimeWithDate = function(dtpIssueDate, date) {
    try {
        var currentTime = new Date()

        date.setHours(currentTime.getHours());

        date.setMinutes(currentTime.getMinutes());

        dtpIssueDate.setValue(date);
    } catch (e) {
        console.log(e.message);
    }
}
