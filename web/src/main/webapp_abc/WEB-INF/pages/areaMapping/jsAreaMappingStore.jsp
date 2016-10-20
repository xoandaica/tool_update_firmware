<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

/******* STORES ********/
var areaMappingStore = Ext.create('Ext.data.JsonStore', {
    id: 'areaMappingStore',
    autoLoad: false,
    model: 'AreaMappingModel',
    fields: ['id', 'serialNumber', 'macAddress', 'ip', 'province', 'district'],
    pageSize: 20,
    proxy: {
        type: 'ajax',
        actionMethods   : 'POST',
        url: 'areaMappingList',
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
        }
    },
    listeners: {
        'beforeload': function(store, options) {
        	var params = AreaMappingComponents.searchForm.getForm().getFieldValues();
        	store.proxy.extraParams = params; 
        },
        load: function(store, records, success) {
		
        }
    }

});

var resultStore = Ext.create('Ext.data.JsonStore', {
	id: 'resultStore',
	model: 'ResultModel',
    fields: ['serialNumber', 'macAddress', 'province', 'district'],
    pageSize: 20,
});
   
</script>
