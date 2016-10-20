<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>

var AreaMappingController = {
	save: function(obj){
		
	},
	
	deleteItems: function(ids){
		myMask.show();
		
		var arrayList = '';
		for(var i = 0; i < ids.length; i ++){
			arrayList =arrayList+ '&deleteIds='+(ids[i]);            
		}
		
		
		Ext.Ajax.request({
			url : "deleteAreaMapping?" + arrayList,
			method : "GET",
			headers : {
				'my-header' : 'foo'
			},
			success : function(res, request) {
				var result = JSON.parse(res.responseText) || {success: true, msg: '<fmt:message key="message.error"/>'};
                myMask.hide();
                if (result.success) {
                	areaMappingStore.load();
                	Ext.MessageBox.alert('<fmt:message key="message.status"/>', '<fmt:message key="message.delete.success"/>', function(){});
                }
			},
			failure : function(response, opts) {
				myMask.hide();
                ExtWnms.alertError("<fmt:message key="message.error"/>");
                return false;
				
			}

		});
	},
	
};
</script>
