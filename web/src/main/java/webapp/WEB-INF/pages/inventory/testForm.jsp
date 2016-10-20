<%-- 
    Document   : testForm
    Created on : May 24, 2015, 5:34:51 PM
    Author     : Dell
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    var testForm_Store = Ext.create('Ext.data.Store', {
        storeId: 'simpsonsStore',
        fields: ['name', 'email', 'phone'],
        data: {'items': [
                {'name': 'Lisa', "email": "lisa@simpsons.com", "phone": "555-111-1224"},
                {'name': 'Bart', "email": "bart@simpsons.com", "phone": "555-222-1234"},
                {'name': 'Homer', "email": "homer@simpsons.com", "phone": "555-222-1244"},
                {'name': 'Marge', "email": "marge@simpsons.com", "phone": "555-222-1254"}
            ]},
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                root: 'items'
            }
        }
    });

    var testForm_Panel = Ext.create('Ext.grid.Panel', {
        title: '<fmt:message key="inventory.testFormPanel"/>',
        store: testForm_Store,
        columns: [
            {text: 'Name', dataIndex: 'name'},
            {text: 'Email', dataIndex: 'email', flex: 1},
            {text: 'Phone', dataIndex: 'phone'}
        ],
        height: 200,
    });




    var chartDataStore = Ext.create("Ext.data.ArrayStore", {
        storeId: "chartData",
        fields: [
            {name: "year", type: "integer"},
            "country1",
            {name: "value1", type: "integer"},
            "country2",
            {name: "value2", type: "integer"}
        ],
        data: [
            [1997, "USA", 66, "Canada", 53],
            [1998, "USA", 81, "Canada", 67],
            [1999, "USA", 83, "Canada", 46],
            [2000, "USA", 61, "Canada", 45],
            [2001, "USA", 67, "Canada", 53],
            [2002, "USA", 68, "Canada", 43]
        ]

    });

    var testForm_Chart = Ext.create("Ext.chart.Chart", {
        height: 400,
        width: 400,
        hidden: false,
        title: "Example working chart",
        layout: "fit",
        style: "background:#fff",
        animate: true,
        store: chartDataStore,
        shadow: true,
        theme: "Category1",
        legend: {
            position: "bottom"
        },
        axes: [{
                type: "Numeric",
                minimum: 0,
                position: "left",
                fields: ["value1", "value2"],
                title: "Value",
                minorTickSteps: 1,
                grid: {
                    odd: {
                        opacity: 1,
                        fill: "#ddd",
                        stroke: "#bbb",
                        "stroke-width": 0.5
                    }
                }
            }, {
                type: "Category",
                position: "bottom",
                fields: ["year"],
                title: "Year"
            }],
        series: [{
                type: "line",
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: "left",
                smooth: true,
                xField: "year",
                yField: "value1",
                title: "USA",
                markerConfig: {
                    type: "cross",
                    size: 4,
                    radius: 4,
                    "stroke-width": 0
                },
            }, {
                type: "line",
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: "left",
                smooth: true,
                xField: "year",
                yField: "value2",
                title: "Canada",
                markerConfig: {
                    type: "circle",
                    size: 4,
                    radius: 4,
                    "stroke-width": 0
                }
            }]

    });
    var testForm_ResetBnt = Ext.create('Ext.Button', {
        text: 'Reset',
        handler: function () {
        }
    });
    var testForm_SearchBnt = Ext.create('Ext.Button', {
        text: 'Search',
        handler: function () {
        }
    });
    var testForm_StartTime = Ext.create('Ext.ux.form.DateTimeField', {
        labelWidth: 100,
        width: 250,
        labelAlign: 'right',
        fieldLabel: 'From Time',
        format: 'Y-m-d',
        hidden: false
    });
    var testForm_Widget = Ext.widget({
        xtype: 'form',
        layout: 'vbox',
        collapsible: true,
        autoScroll: true,
        frame: true,
        title: 'Search Criteria',
        height: 180,
        items: [
            testForm_StartTime
        ],
        buttons: [testForm_ResetBnt, testForm_SearchBnt]
    });
</script>
