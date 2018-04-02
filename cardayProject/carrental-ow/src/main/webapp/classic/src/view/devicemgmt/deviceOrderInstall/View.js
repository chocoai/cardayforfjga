Ext.define('Admin.view.devicemgmt.deviceOrderInstall.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'deviceOrderInstall',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.devicemgmt.deviceOrderInstall.SearchForm',
        'Admin.view.devicemgmt.deviceOrderInstall.Grid'
    ],
    controller: {
        xclass: 'Admin.view.devicemgmt.deviceOrderInstall.ViewController'
    },
    viewModel : {
		type : 'deviceOrderInstallModel' 
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
	    	xclass: 'Admin.view.devicemgmt.deviceOrderInstall.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.devicemgmt.deviceOrderInstall.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
