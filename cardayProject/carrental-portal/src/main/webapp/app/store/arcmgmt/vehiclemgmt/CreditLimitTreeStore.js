Ext.define('Admin.store.arcmgmt.vehiclemgmt.CreditLimitTreeStore', {
    extend: 'Ext.data.TreeStore',

    storeId: 'CreditLimitTreeStore',

    fields: [
        "text", "limitedCredit", "availableCredit"
    ],
	autoLoad : true,
	proxy: { 
	  		type: 'ajax', 	  		
	  		//url: 'app/data/arcmgmtinfo/CreditLimitTree.json',
			rootProperty: 'children' ,
			actionMethods : 'GET',
	},

    listeners:{    
	    beforeload: function( store, operation, eOpts ){
	    	var deptId;
	    	if(Ext.getCmp("deptMgmtTreeList") == undefined){
		    	Ext.Ajax.request({
//	                url: '/carrental-portal/user/loadCurrentUser',
	                url: './user/loadCurrentUser',
	                method: 'POST',
	                async: false,
	                success: function(response) {
	                    var resp = Ext.decode(response.responseText);
	                    deptId = resp.data.organizationId;
	                },
	                failure: function(response) {
	                    Ext.Msg.alert('消息提示', '无法获取部门ID！');
	                    return;
	                }
	            });
	    		
	    	}else{
	    		var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
		        if(selection.length == 0){
		            return;
		        }
		        var select = selection[0].getData();
		        deptId = select.id;
	    	}
	        store.proxy.url = 'department/findTreeCreditByOrgId/'+ deptId;
	    },
	}
});
