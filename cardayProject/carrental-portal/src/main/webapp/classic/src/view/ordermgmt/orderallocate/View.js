/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'orderAllocate',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 0,
//    defaults: {
//        frame: true,
//        collapsible: true,
//        margin: '0 0 3 0'
//    },
    items: [
   {
       xtype: 'tabpanel',
       activeTab:0,
       bodyStyle: 'padding: 5px;',
       
       items:[{
    	  title: '待调度订单',
    	  header:false,
    	  xclass: 'Admin.view.ordermgmt.orderallocate.AllocatingCarView',
    	  viewModel: {
    	    	xclass: 'Admin.view.ordermgmt.orderallocate.AllocateModel'
    	    },
    	  border:false,
    	  listeners : {
	 		  beforeshow: function() {
	 			this.getViewModel().getStore("allocatedCarReport").load();
	 		  }
	 	  }
       },{
    	  title: '已调度订单',
     	  header:false,
     	  xclass: 'Admin.view.ordermgmt.orderallocate.AllocatedCarView',
     	  viewModel: {
         	xclass: 'Admin.view.ordermgmt.orderallocate.AllocateModel'
          },
     	  border:false,
     	  listeners : {
     		  beforeshow: function() {
	 			this.getViewModel().getStore("allocatedCarReport").load();
	 		  }
	 	  }
       }
       ]
    }],
    initComponent: function() {
        this.callParent();
    }
});
