Ext.define('Admin.view.dialrecordmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'dialRecordMgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.dialrecordmgmt.SearchForm',
        'Admin.view.orgmgmt.dialrecordmgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.orgmgmt.dialrecordmgmt.ViewController'
    },
    viewModel : {
		type : 'dialRecordModel' 
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
	    	xclass: 'Admin.view.orgmgmt.dialrecordmgmt.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.dialrecordmgmt.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
