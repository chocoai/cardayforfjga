/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderaudit.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'orderaudit',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },
    
    items: [
    {
        xtype: 'tabpanel',
        activeTab:0,
        bodyStyle: 'padding: 5px;',
        
        items:[
        {
		 	 title: '待审核订单',
		 	 header:false,
		 	 border:false,
		 	 xclass: 'Admin.view.ordermgmt.orderaudit.AuditView',
		 	 viewModel: {
		         xclass: 'Admin.view.ordermgmt.orderaudit.OrderViewModel'
		     },
		 	  border:false,
		 	  listeners : {
		 		  beforeshow: function() {
		 			this.getViewModel().getStore("ordersResults").load();
		 		  }
		 	  }
        },
        {
	     	  title: '已审核订单',
	      	  header:false,
	      	  xclass: 'Admin.view.ordermgmt.orderaudit.AuditedView',
	      	  viewModel: {
		         xclass: 'Admin.view.ordermgmt.orderaudit.OrderViewModel'
		     },
	      	  border:false,
	      	  listeners : {
	      		  beforeshow: function() {
		 			this.getViewModel().getStore("ordersResults").load();
		 		  }
		 	  }
        }
        ]
     }],
    
    initComponent: function() {
        this.callParent();
    }
});
