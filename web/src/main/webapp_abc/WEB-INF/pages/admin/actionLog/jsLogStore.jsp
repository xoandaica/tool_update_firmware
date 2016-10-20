<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

/******* STORES ********/

var actionLogStore = Ext.create('Ext.data.JsonStore', {
    id: 'actionLogStore',
    autoLoad: false,
    model: 'ActionLogModel',
    fields: ['id', 'username', 'actionTime', 'actionType', 'actionObject', 'description'],
    pageSize: 20,
    proxy: {
        type: 'ajax',
        actionMethods   : 'POST',
        url: 'actionLogList',
        startParam: "searchCriteria.start",
        limitParam: "searchCriteria.limit",
        sortParam: "searchCriteria.sort",
        directionParam: "searchCriteria.order",
        pageParam: "searchCriteria.page",
        reader: {
            type: 'json',
            root: 'result.list',
            id: 'No',
            totalProperty: 'result.totalCount'
        },
    },
    listeners: {
        'beforeload': function(store, options) {
        	var params = ActionLogComponents.searchForm.getForm().getFieldValues();
        	store.proxy.extraParams = params; 
        },
        load: function(store, records, success) {
        }
    }

});
   
</script>
