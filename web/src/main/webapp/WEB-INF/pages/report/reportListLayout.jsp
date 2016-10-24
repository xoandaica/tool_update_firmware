<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<script type="text/javascript">
    Ext.require(['Ext.data.*']);
    Ext.onReady(function () {
        Ext.create('Ext.Viewport', {
            layout: 'border',
            title: 'Ext Layout Browser',
            hideCollapseTool: true,
            titleCollapse: true,
            collapsible: true,
            defaults: {
                collapsible: true,
                split: true
            },
            items: [
                Ext.create('Ext.tab.Panel',
                        {
                            region: 'center', // a center region is ALWAYS required for border layout
                            deferredRender: false,
                            width: "100%",
                            height: "100%",
                            margins: '43 0 30 0',
                            activeTab: 0, // first tab initially active
                            items: [
                                {
                                    contentEl: 'tabs-1',
                                    title: 'Report Management',
                                    id: 'tabs1Id',
                                    closable: false
                                },
                            ],
                        })
            ],
            listeners: {
                resize: function (width, height, oldWidth, oldHeight, eOpts) {
                    setTimeout(function () {
                    }, 1);
                },
            }
        });
    });
</script>


