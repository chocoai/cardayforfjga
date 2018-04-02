Ext.define('Admin.view.orgmgmt.enterinfoaudit.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'EnterinfoAudit',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
    ],
    viewModel : {
		type : 'auditModel' 
	},
  
    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfoaudit.ViewController'
    },
    listeners:{
        afterrender: 'onSearchClick'
    },
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
	    	 xclass: 'Admin.view.orgmgmt.enterinfoaudit.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.enterinfoaudit.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
