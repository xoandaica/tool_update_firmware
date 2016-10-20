<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

/******* STORES ********/
var userStore = Ext.create('Ext.data.JsonStore', {
    id: 'userStore',
    autoLoad: false,
    model: 'UserModel',
    fields: ['id', 'username', 'firstName', 'lastName', 'email', 'phoneNumber', 
             'department', 'password', 'confirmPassword', 'roles', 'version', 'description'],
    pageSize: 20,
    proxy: {
        type: 'ajax',
        actionMethods   : 'POST',
        url: 'userList',
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
        	/* var params = RadioComponents.searchCriteriaForm.getForm().getFieldValues();
        	store.proxy.extraParams = params; */
        	
        	var params = UserComponents.searchForm.getForm().getFieldValues();
        	store.proxy.extraParams = params;
        },
        load: function(store, records, success) {
		
        }
    }

});

var statusOptions =  [ {
	label : "---<fmt:message key="common.all"/>---",
	value : '',
},{
	label : "<fmt:message key="user.enabled"/>",
	value : '1',
}, {
	label : "<fmt:message key="user.accountLocked"/>",
	value : '0',
} ];

var statusStore = Ext.create('Ext.data.JsonStore', {
	fields : [ 'label', 'value' ]
});

statusStore.loadData(statusOptions);

var roleStore = Ext.create('Ext.data.ArrayStore', {
    fields: ['id','name'],
  
    sortInfo: {
        field: 'name',
        direction: 'ASC'
    }
});
   
</script>
