<%@page import="vn.vnpttech.ssdc.nms.webapp.util.ResourceBundleUtils"%>
<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
         contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
     <c:set var="context2" value="${pageContext.request.contextPath}" />
    Ext.Loader.setConfig({
        enabled: true
    });
    Ext.Loader.setPath('Ext.ux', '${context2}/scripts/extjs/ux');
    Ext.require([
        'Ext.grid.*',
        'Ext.data.*',
        'Ext.ux.form.MultiSelect',
        'Ext.ux.form.ItemSelector',
        'Ext.ux.ajax.JsonSimlet',
        'Ext.ux.ajax.SimManager',
        'Ext.ux.grid.FiltersFeature',
        'Ext.toolbar.Paging',
        'Ext.ModelManager',
        'Ext.tip.QuickTipManager',
        'Ext.util.*',
        'Ext.state.*'
    ]);


    Ext.onReady(function () {
        Ext.QuickTips.init();
        var deviceList = '';
        var currentDate = Ext.create('Ext.form.field.Date', {
            fieldLabel: 'Report Time:',
            name: 'currentDate',
            id: 'currentDate',
            anchor: '99%',
            format: 'Y-m-d h:i:s',
            padding: '5 15 5 15',
        });
        var currentDateModuleLog = Ext.create('Ext.form.field.Date', {
            fieldLabel: 'Report Time:',
            name: 'currentDateModuleLog',
            id: 'currentDateModuleLog',
            anchor: '99%',
            format: 'Y-m-d h:i:s',
            padding: '5 15 5 15',
            readOnly: true,
        });

        var comboStartTime = Ext.create('Ext.ux.form.DateTimeField', {
            fieldLabel: 'Start Time:',
            name: 'startTime',
            id: 'startTime',
            anchor: '99%',
            format: 'Y-m-d',
            padding: '5 15 5 15',
            allowBlank: false,
            //maxValue: Ext.Date.add(new Date(), Ext.Date.DAY, -1),
            //minValue: Ext.Date.add(new Date(), Ext.Date.DAY, -30)
        });
        var comboEndTime = Ext.create('Ext.ux.form.DateTimeField', {
            fieldLabel: 'End Time:',
            name: 'endTime',
            id: 'endTime',
            anchor: '99%',
            format: 'Y-m-d',
            padding: '5 15 5 15',
            allowBlank: false,
          //  maxValue: new Date(),
        });
        
        

        var newCreateReportDialogModuleLog = Ext.create('Ext.window.Window', {
            closeAction: 'hide',
            title: 'Export REPORT',
            id: 'createReportIdModuleLog',
            autoEl: 'form',
            width: 600,
            constrainHeader: true,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            items: [{
                    xtype: 'textfield',
                    fieldLabel: 'Report Id',
                    padding: '5 15 5 15',
                    hidden: true,
                    readOnly: 'true',
                    name: 'idModuleLog'
                }, {
                    xtype: 'textfield',
                    readOnly: 'true',
                    fieldLabel: 'Report Name',
                    padding: '5 15 5 15',
                    name: 'nameModuleLog'
                }, {
                    xtype: 'textfield',
                    name: 'urlModuleLog',
                    readOnly: 'true',
                    padding: '5 15 5 15',
                    fieldLabel: 'Links',
                    hidden: true,
                },
                comboStartTime,
                comboEndTime,
                currentDateModuleLog,
            ],
            buttons: [{
                    xtype: 'component',
                    id: 'formErrorState3',
                    baseCls: 'form-error-state',
                    flex: 1,
                    validText: 'Form is valid',
                    invalidText: 'Form has errors',
                    tipTpl: Ext.create('Ext.XTemplate', '<ul><tpl for="."><li><span class="field-name">{name}</span>: <span class="error">{error}</span></li></tpl></ul>'),
                    getTip: function () {
                        var tip = this.tip;
                        if (!tip) {
                            tip = this.tip = Ext.widget('tooltip', {
                                target: this.el,
                                title: 'Error Details:',
                                autoHide: false,
                                anchor: 'top',
                                mouseOffset: [-11, -2],
                                closable: true,
                                constrainPosition: false,
                                cls: 'errors-tip'
                            });
                            tip.show();
                        }
                        return tip;
                    },
                    setErrors: function (errors) {
                        var me = this,
                                baseCls = me.baseCls,
                                tip = me.getTip();

                        errors = Ext.Array.from(errors);

                        // Update CSS class and tooltip content
                        if (errors.length) {
                            me.addCls(baseCls + '-invalid');
                            me.removeCls(baseCls + '-valid');
                            me.update(me.invalidText);
                            tip.setDisabled(false);
                            tip.update(me.tipTpl.apply(errors));
                        } else {
                            me.addCls(baseCls + '-valid');
                            me.removeCls(baseCls + '-invalid');
                            me.update(me.validText);
                            tip.setDisabled(true);
                            tip.hide();
                        }
                    }
                }, {
                    text: 'Export',
                    type: 'submit',
                    handler: function () {
                        var startTime = newCreateReportDialogModuleLog.down('[name=startTime]').getRawValue();
                        var endTime = newCreateReportDialogModuleLog.down('[name=endTime]').getRawValue();

                        var url = newCreateReportDialogModuleLog.down('[name=urlModuleLog]').getValue();

                        var currentDateModuleLog = newCreateReportDialogModuleLog.down('[name=currentDateModuleLog]').getRawValue();

                        if (!newCreateReportDialogModuleLog.down('[name=startTime]').isValid( )) {
                            Ext.MessageBox.alert('Error', 'Please select StartTime!', function () {});
                            return;
                        }
                        if (!newCreateReportDialogModuleLog.down('[name=endTime]').isValid( )) {
                            Ext.MessageBox.alert('Error', 'Please select EndTime!', function () {});
                            return;
                        }

                        Ext.Ajax.request({
                            url: "getReportTime",
                            method: "GET",
                            headers: {
                                'my-header': 'foo'
                            },
                            params: {
                                urlModule: url,
                                startTime: startTime,
                                endTime: endTime,
                                currentDateModuleLog: currentDateModuleLog
                            },
                            success: function (result, request) {
                                jsonData = Ext.decode(result.responseText);
                                var createReportUrl = jsonData.weekList;
                                openModalPopupWindow(createReportUrl);
                                newCreateReportDialogModuleLog.close();
                            },
                            failure: function (response, opts) {
                            },
                        });
                    }
                }, {
                    text: 'Reset',
                    handler: function () {

                        newCreateReportDialogModuleLog.down('[name=startTime]').setValue('');
                        newCreateReportDialogModuleLog.down('[name=endTime]').setValue('');

                    }
                }, {
                    text: 'Cancel',
                    handler: function () {
                        newCreateReportDialogModuleLog.hide();
                    }
                }]
        });



        var currentDateActivityLog = Ext.create('Ext.form.field.Date', {
            fieldLabel: 'Report Time:',
            name: 'currentDateActivityLog',
            id: 'currentDateActivityLog',
            anchor: '99%',
            format: 'Y-m-d h:i',
            padding: '5 15 5 15',
            readOnly: true,
        });

        var comboStartTimeActivityLog = Ext.create('Ext.form.field.Date', {
            fieldLabel: 'Start Time:',
            name: 'startTimeActivityLog',
            id: 'startTimeActivityLog',
            anchor: '99%',
            format: 'Y-m-d H:i',
            padding: '5 15 5 15',
            allowBlank: false,
        });
        var comboEndTimeActivityLog = Ext.create('Ext.form.field.Date', {
            fieldLabel: 'End Time:',
            name: 'endTimeActivityLog',
            id: 'endTimeActivityLog',
            anchor: '99%',
            format: 'Y-m-d H:i',
            padding: '5 15 5 15',
            allowBlank: false,
        });

        var newCreateReportDialogActivityLog = Ext.create('Ext.window.Window', {
            closeAction: 'hide',
            title: 'Export REPORT',
            id: 'createReportIdActivityLog',
            autoEl: 'form',
            width: 600,
            constrainHeader: true,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            items: [{
                    xtype: 'textfield',
                    fieldLabel: 'Report Id',
                    padding: '5 15 5 15',
                    hidden: true,
                    readOnly: 'true',
                    name: 'idActivityLog'
                }, {
                    xtype: 'textfield',
                    readOnly: 'true',
                    fieldLabel: 'Report Name',
                    padding: '5 15 5 15',
                    name: 'nameActivityLog'
                }, {
                    xtype: 'textfield',
                    name: 'urlActivityLog',
                    readOnly: 'true',
                    padding: '5 15 5 15',
                    fieldLabel: 'Links',
                },
                comboStartTimeActivityLog,
                comboEndTimeActivityLog,
                currentDateActivityLog

            ],
            buttons: [{
                    xtype: 'component',
                    id: 'formErrorState3',
                    baseCls: 'form-error-state',
                    flex: 1,
                    validText: 'Form is valid',
                    invalidText: 'Form has errors',
                    tipTpl: Ext.create('Ext.XTemplate', '<ul><tpl for="."><li><span class="field-name">{name}</span>: <span class="error">{error}</span></li></tpl></ul>'),
                    getTip: function () {
                        var tip = this.tip;
                        if (!tip) {
                            tip = this.tip = Ext.widget('tooltip', {
                                target: this.el,
                                title: 'Error Details:',
                                autoHide: false,
                                anchor: 'top',
                                mouseOffset: [-11, -2],
                                closable: true,
                                constrainPosition: false,
                                cls: 'errors-tip'
                            });
                            tip.show();
                        }
                        return tip;
                    },
                    setErrors: function (errors) {
                        var me = this,
                                baseCls = me.baseCls,
                                tip = me.getTip();

                        errors = Ext.Array.from(errors);
                        if (errors.length) {
                            me.addCls(baseCls + '-invalid');
                            me.removeCls(baseCls + '-valid');
                            me.update(me.invalidText);
                            tip.setDisabled(false);
                            tip.update(me.tipTpl.apply(errors));
                        } else {
                            me.addCls(baseCls + '-valid');
                            me.removeCls(baseCls + '-invalid');
                            me.update(me.validText);
                            tip.setDisabled(true);
                            tip.hide();
                        }
                    }
                }, {
                    text: 'Export',
                    type: 'submit',
                    handler: function () {
                        // khanh
                        var startTimeActivityLog = newCreateReportDialogActivityLog.down('[name=startTimeActivityLog]').getRawValue();
                        var endTimeActivityLog = newCreateReportDialogActivityLog.down('[name=endTimeActivityLog]').getRawValue();

                        var hostStrActivityLog = '';
                        var urlActivityLog = newCreateReportDialogActivityLog.down('[name=urlActivityLog]').getValue();

                        var currentDateActivityLog = newCreateReportDialogActivityLog.down('[name=currentDateActivityLog]').getRawValue();

                        if (!newCreateReportDialogActivityLog.down('[name=startTimeActivityLog]').isValid( )) {
                            Ext.MessageBox.alert('Error', 'Please select StartTime!', function () {});
                            return;
                        }
                        if (!newCreateReportDialogActivityLog.down('[name=endTimeActivityLog]').isValid( )) {
                            Ext.MessageBox.alert('Error', 'Please select EndTime!', function () {});
                            return;
                        }

                        Ext.Ajax.request({
                            url: "getReportTime",
                            method: "GET",
                            headers: {
                                'my-header': 'foo'
                            },
                            params: {
                                module: 'activity',
                                hostActivity: hostStrActivityLog,
                                urlActivity: urlActivityLog,
                                startTimeActivity: startTimeActivityLog,
                                endTimeActivity: endTimeActivityLog,
                                currentDateActivityLog: currentDateActivityLog
                            },
                            success: function (result, request) {
                                jsonData = Ext.decode(result.responseText);
                                var createReportUrl = jsonData.weekList;
                                openModalPopupWindow(createReportUrl);
                                newCreateReportDialogActivityLog.close();
                            },
                            failure: function (response, opts) {
                            },
                        });
                    }
                }, {
                    text: 'Reset',
                    handler: function () {

                        newCreateReportDialogActivityLog.down('[name=startTimeActivityLog]').setValue('');
                        newCreateReportDialogActivityLog.down('[name=endTimeActivityLog]').setValue('');

                    }
                }, {
                    text: 'Cancel',
                    handler: function () {
                        newCreateReportDialogActivityLog.hide();
                    }
                }]
        });





        // Week picker
        var weekStore = Ext.create('Ext.data.Store', {
            fields: ['weekID', 'weekName']
        });
        for (var i = 1; i < 54; i++) {
            record = {'weekID': i, 'weekName': 'Week ' + i}
            weekStore.add(record);
        }
        ;
        var comboWeek = Ext.create('Ext.ux.form.field.BoxSelect', {
            fieldLabel: 'Week',
            id: 'week',
            name: 'week',
            padding: '5 15 5 15',
            anchor: '99%',
            emptyText: "--Please Select--",
            editable: false,
            multiSelect: false,
            allowBlank: false,
            displayField: 'weekName',
            valueField: 'weekID',
            store: weekStore,
            queryMode: 'local',
            value: Ext.Date.getWeekOfYear(new Date())
        });
        // Year picker
        var yearStore = Ext.create('Ext.data.Store', {
            fields: ['yearID', 'yearName']
        });
        for (var i = 2010; i < 2031; i++) {
            record = {'yearID': i, 'yearName': 'Year ' + i}
            yearStore.add(record);
        }
        ;
        var comboYear = Ext.create('Ext.ux.form.field.BoxSelect', {
            fieldLabel: 'Year',
            id: 'year',
            name: 'year',
            anchor: '99%',
            editable: false,
            padding: '5 15 5 15',
            multiSelect: false,
            displayField: 'yearName',
            allowBlank: false,
            valueField: 'yearID',
            store: yearStore,
            queryMode: 'local',
            value: new Date().getFullYear()
        });

        // create the data store
        var store = Ext.create('Ext.data.ArrayStore', {
            pageSize: 20,
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'dataReportGrid',
                reader: {
                    root: 'reportList',
                    totalProperty: 'reportCount',
                    type: 'json'
                }
            },
            fields: ['id', 'description', 'name', 'url']
        });


        // popup window create report
        var newCreateReportDialog = Ext.create('Ext.window.Window', {
            closeAction: 'hide',
            title: 'Export REPORT',
            id: 'createReportId',
            autoEl: 'form',
            width: 600,
            constrainHeader: true,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            items: [{
                    xtype: 'textfield',
                    fieldLabel: 'Report Id',
                    padding: '5 15 5 15',
                    hidden: true,
                    readOnly: 'true',
                    name: 'id'
                }, {
                    xtype: 'textfield',
                    readOnly: 'true',
                    fieldLabel: 'Report Name',
                    padding: '5 15 5 15',
                    name: 'name'
                }, {
                    xtype: 'textfield',
                    name: 'url',
                    readOnly: 'true',
                    padding: '5 15 5 15',
                    fieldLabel: 'Links',
                }, {
                    xtype: 'hiddenfield',
                    name: 'params',
                    fieldLabel: 'Parameters List',
                    readOnly: 'true',
                }, {
                    xtype: 'hiddenfield',
                    name: 'provinces',
                    fieldLabel: 'Province Id List',
                    readOnly: 'true',
                    padding: '5 15 5 15',
                },
                {
                    xtype: 'hiddenfield',
                    name: 'nodeIds',
                    fieldLabel: 'Node Id List',
                    padding: '5 15 5 15',
                },
                comboWeek,
                comboYear,
                currentDate

            ],
            buttons: [{
                    xtype: 'component',
                    id: 'formErrorState3',
                    baseCls: 'form-error-state',
                    flex: 1,
                    validText: 'Form is valid',
                    invalidText: 'Form has errors',
                    tipTpl: Ext.create('Ext.XTemplate', '<ul><tpl for="."><li><span class="field-name">{name}</span>: <span class="error">{error}</span></li></tpl></ul>'),
                    getTip: function () {
                        var tip = this.tip;
                        if (!tip) {
                            tip = this.tip = Ext.widget('tooltip', {
                                target: this.el,
                                title: 'Error Details:',
                                autoHide: false,
                                anchor: 'top',
                                mouseOffset: [-11, -2],
                                closable: true,
                                constrainPosition: false,
                                cls: 'errors-tip'
                            });
                            tip.show();
                        }
                        return tip;
                    },
                    setErrors: function (errors) {
                        var me = this,
                                baseCls = me.baseCls,
                                tip = me.getTip();

                        errors = Ext.Array.from(errors);

                        // Update CSS class and tooltip content
                        if (errors.length) {
                            me.addCls(baseCls + '-invalid');
                            me.removeCls(baseCls + '-valid');
                            me.update(me.invalidText);
                            tip.setDisabled(false);
                            tip.update(me.tipTpl.apply(errors));
                        } else {
                            me.addCls(baseCls + '-valid');
                            me.removeCls(baseCls + '-invalid');
                            me.update(me.validText);
                            tip.setDisabled(true);
                            tip.hide();
                        }
                    }
                }, {
                    text: 'Export',
                    type: 'submit',
                    handler: function () {
                        var year = newCreateReportDialog.down('[name=year]').getValue();
                        var week = newCreateReportDialog.down('[name=week]').getValue();
                        var hostStr = '';
                        var url = newCreateReportDialog.down('[name=url]').getValue();
                        var areaID = Ext.getCmp('areaid').getValue();

                        var reportTime = newCreateReportDialog.down('[name=currentDate]').getRawValue();


                        if (!newCreateReportDialog.down('[name=year]').isValid( )) {
                            Ext.MessageBox.alert('Error', 'Please select year!', function () {});
                            return;
                        }
                        if (!newCreateReportDialog.down('[name=week]').isValid( )) {
                            Ext.MessageBox.alert('Error', 'Please select week!', function () {});
                            return;
                        }

                        Ext.Ajax.request({
                            url: "getReportTime",
                            method: "GET",
                            headers: {
                                'my-header': 'foo'
                            },
                            params: {
                                hostStr: hostStr,
                                url: url,
                                areaID: areaID,
                                reportTime: reportTime,
                                device: "\"" + deviceList + "\"",
                                week: week,
                                year: year,
                            },
                            success: function (result, request) {
                                jsonData = Ext.decode(result.responseText);
                                var createReportUrl = jsonData.weekList;
                                openModalPopupWindow(createReportUrl);
                                newCreateReportDialog.close();
                                deviceList = '';
                            },
                            failure: function (response, opts) {
                            },
                        });
                    }
                }, {
                    text: 'Reset',
                    handler: function () {
                        newCreateReportDialog.down('[name=provinces]').setValue('');
                        newCreateReportDialog.down('[name=nodeIds]').setValue('');
                        comboWeek.setValue(null);
                        newCreateReportDialog.down('[name=startDate]').setValue('');
                        newCreateReportDialog.down('[name=endDate]').setValue(new Date());

                    }
                }, {
                    text: 'Cancel',
                    handler: function () {
                        newCreateReportDialog.hide();
                        createReportParamsStore.removeAll();
                        deviceList = '';
                    }
                }]
        });

        // create the Grid
        var grid = Ext.create('Ext.grid.Panel', {
            store: store,
            collapsible: false,
            columnLines: true,
            columns: [
                Ext.create('Ext.grid.RowNumberer', {
                    header: 'No',
                    width: 50,
                    renderer: function (v, p, record, rowIndex) {
                        if (this.rowspan) {
                            p.cellAttr = 'rowspan="' + this.rowspan + '"';
                        }
                        return rowIndex + 1;
                    }
                }),
                {
                    text: 'Report ID',
                    width: 60,
                    sortable: true,
                    filter: true,
                    hidden: true,
                    dataIndex: 'id'
                }, {
                    text: 'Report Name',
                    sortable: true,
                    filter: true,
                    width: 300,
                    dataIndex: 'name'
                }, {
                    text: 'Report Description',
                    flex: 1,
                    sortable: true,
                    filter: true,
                    dataIndex: 'description'
                }, {
                    text: 'Links',
                    flex: 1,
                    sortable: true,
                    filter: true,
                    hidden: true,
                    dataIndex: 'url'
                }, {
                    text: 'Export',
                    xtype: 'actioncolumn',
                    flex: 1,
                    items: [{
                            icon: '<c:url value="/styles/icons/fam/rss_go.png"/>', // Use a URL in the icon config
                            text: 'Export',
                            tooltip: 'Export Report',
                            centered: true,
                            handler: function (grid, rowIndex, colIndex) {

                                var rec = store.getAt(rowIndex);
                                newCreateReportDialogModuleLog.down('[name=nameModuleLog]').setValue(rec.get('name'));
                                newCreateReportDialogModuleLog.down('[name=urlModuleLog]').setValue(rec.get('url'));
                                newCreateReportDialogModuleLog.down('[name=currentDateModuleLog]').setValue(new Date());
                                newCreateReportDialogModuleLog.down('[name=startTime]').setValue('');
                                newCreateReportDialogModuleLog.down('[name=endTime]').setValue('');
                                newCreateReportDialogModuleLog.show();


                            }
                        }]
                }],
            // paging bar on the bottom
            bbar: Ext.create('Ext.PagingToolbar', {
                store: store,
                displayInfo: true,
                displayMsg: 'Displaying topics {0} - {1} of {2}',
                emptyMsg: "No topics to display"
            }),
            height: 900,
            width: "90%",
            title: 'List of Reports',
            id: 'gridDetailIdGrid',
        });
        var form = Ext.create('Ext.form.Panel', {
            id: 'searchReportFormId',
            autoHeight: true,
            frame: true,
            width: '100%',
            bodyPadding: 0,
            layout: {
                pack: 'left'
            },
            defaults: {
                anchor: '100%',
                labelWidth: 100
            },
            items: [grid]
        });
        form.render("formReportIdRender");
    });
</script>
