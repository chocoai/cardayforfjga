Ext.define('Admin.view.orgmgmt.enterinfoaudit.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'EnterinfoAudit',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.enterinfoaudit.SearchForm',
        'Admin.view.orgmgmt.enterinfoaudit.Grid'
    ],
    viewModel : {
		type : 'auditModel' 
	},
  
    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfoaudit.ViewController'
    },
    listeners:{
        afterrender: 'onView'
    },
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
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
