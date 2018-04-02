Ext.define('Admin.view.syncUsermgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'syncUsermgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.syncUsermgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.orgmgmt.syncUsermgmt.ViewController'
    },
    viewModel : {
		type : 'syncusermodel' 
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
    items: [
   			{
        	xclass: 'Admin.view.orgmgmt.syncUsermgmt.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
