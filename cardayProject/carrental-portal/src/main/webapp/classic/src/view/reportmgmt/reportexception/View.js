/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportexception.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'reportexception',
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.reportmgmt.reportexception.ViewController',
    ],
    
    controller: {
        xclass: 'Admin.view.reportmgmt.reportexception.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.reportmgmt.reportexception.ViewModel'
    },
    listeners:{
        afterrender: 'onSearchClick',
    },
    id: 'tbdc_2',
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
    {
        xclass: 'Admin.view.reportmgmt.reportexception.SearchForm',
    },
    {
        margin: '40 0 0 0',
    	layout: {
            type: 'vbox',
            pack: 'start',
            align: 'stretch'
        },
        items: [
			{
				xtype: 'container',
//			    flex: 1,
				height:500,
			    layout: {
			        type: 'hbox',
			        pack: 'start',
			        align: 'stretch'
			    },
			    margin: '0 0 3 0',
			    defaults: {
			        flex: 1,
			        //frame: true,
			    },
			    items: [{
			        xclass: 'Admin.view.reportmgmt.reportexception.OverspeedAlarm',
			    },{
                    html: 'heheheheh',
			    	xclass: 'Admin.view.reportmgmt.reportexception.AverageOverspeedAlarm',
			    }]
			},
			{
		    	xtype: 'container',
//		        flex: 1,
		    	height:500,
		        layout: {
		            type: 'hbox',
		            pack: 'start',
		            align: 'stretch'
		        },
		        margin: '0 0 3 0',
		        defaults: {
		            flex: 1,
		            //frame: true,
		        },
		        items: [{
		        	xclass: 'Admin.view.reportmgmt.reportexception.EnterAlarm',
		        },{
		        	xclass: 'Admin.view.reportmgmt.reportexception.AverageEnterAlarm',
		        }]
		    },
            {
                xtype: 'container',
//                flex: 1,
                height:500,
                layout: {
                    type: 'hbox',
                    pack: 'start',
                    align: 'stretch'
                },
                margin: '0 0 3 0',
                defaults: {
                    flex: 1,
                    //frame: true,
                },
                items: [{
                    xclass: 'Admin.view.reportmgmt.reportexception.OverborderAlarm',
                },{
                    xclass: 'Admin.view.reportmgmt.reportexception.AverageOverborderAlarm',
                }]
            },
            {
                xtype: 'container',
//                flex: 1,
                height:500,
                layout: {
                    type: 'hbox',
                    pack: 'start',
                    align: 'stretch'
                },
                margin: '0 0 3 0',
                defaults: {
                    flex: 1,
                    //frame: true,
                },
                items: [{
                    xclass: 'Admin.view.reportmgmt.reportexception.OverborderMileage',
                },{
                    xclass: 'Admin.view.reportmgmt.reportexception.AverageOverborderMileage',
                }]
            }
        ]
    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
