Ext.define('Admin.store.NavigationVehicleMonitoringTree', {
    extend: 'Ext.data.TreeStore',

    storeId: 'NavigationVehicleMonitoringTree',

    fields: [{
        name: 'text'
    }],
	autoLoad : true,
	proxy: { 
	  		type: 'ajax',
	  		actionMethods: {
	            create : 'POST',
	            read   : 'POST', // by default GET
	            update : 'POST',
	            destroy: 'POST'
	        },
	  	//	url: 'vehicle/listVehicleTree',
	  		url: 'vehicle/listVehMoniStatusTreeData',
	  		
	  	//	url: 'app/data/arcmgmtinfo/vehicleMonitoring.json',
			rootProperty: 'children' ,
			cb:null
	},
	root:{
		expanded:true,
        viewType: 'vehicleMonitoringMain',
        routeId: 'vehicleMonitoringMain',
	},
	listeners: {
		load: function(store, records, successful, operation, node, eOpts ){
			var tree = Ext.getCmp("navigationVehicleMonitoringTree");
			console.log("选中监控首页第一个节点");
			if(records && records.length > 0){
				var first = records[0];
				if(tree != null){
					tree.setSelection(first);
				}
				//如果有搜索内容则展开所有搜索结果
				var searchText = $("#searchVehicleText-inputEl").val();
				if(searchText && tree){
					tree.getStore().getRoot().cascadeBy(function() {
						this.expand();
		            }, null);
				}
			}
			var cb = this.proxy.cb;
			cb && typeof(cb) == "function" && cb();
		},
		beforeload: function(){
		}
	}
});
