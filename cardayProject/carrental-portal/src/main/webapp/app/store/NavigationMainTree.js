Ext.define('Admin.store.NavigationMainTree', {
    extend: 'Ext.data.TreeStore',

    storeId: 'NavigationMainTree',

    fields: [{
        name: 'text'
    }],
	autoLoad : false,
	proxy: { 
	  		type: 'ajax', 
	  	//	url: 'vehicle/listVehicleTree',
	  		url: 'vehicle/listVehicleStatusTree',
	  		
	  	//	url: 'app/data/arcmgmtinfo/test.json',
			rootProperty: 'children' 
	},
	root:{
		expanded:true,
        viewType: 'admindashboard',
        routeId: 'dashboard',
	},
	listeners: {
		load: function(store, records, successful, operation, node, eOpts ){
			var tree = Ext.getCmp("navigationMainPageTree");
			console.log("选中第一个节点");
			if(records && records.length > 0){
				var first = records[0];
				if(tree != null){
					tree.setSelection(first);
				}
			}
		}
	}
});
