Ext.define('Admin.view.systemmgmt.rolemgmt.PrivilegeTree', {
	extend:'Ext.tree.Panel', 
	requires: [
	           'Admin.view.systemmgmt.rolemgmt.ViewController',   
	],
	
	reference: 'privilegetree',
	id: 'privilegetree_id',
	xtype: 'privilegetree', 
	checkPropagation: 'none',
//    store: new Ext.data.TreeStore({
//    	alias: 'store.resourceStore',
//    	autoLoad : true,
//		proxy: {
//		   type: 'ajax',
//		   actionMethods: {
//		      read: 'GET',
//		      create: 'GET',
//		      update: 'GET',
//		      destroy: 'GET'
//		    },
//		    //url: 'app/data/systemmgmt/rolemgmt/privilege1.json',
//		   url: 'resource',
//	      	reader: {
//		             type: 'json',
//		             rootProperty: 'children',
//		             successProperty: 'success'
//		         }
//		  },
//		  
//		  sorters: [{
//		      property: 'leaf',
//		      direction: 'ASC'
//		  }, {
//		      property: 'text',
//		      direction: 'DESC'
//		  }]
//	}),
	rootVisible: false,
    useArrows: true,
    frame: true,
    //title: '部门',
    width: 280,
    height: 300,
    bufferedRenderer: false,
    animate: true,
    
    
    listeners: {
       //beforecheckchange: 'onBeforeCheckChange'
    },
});