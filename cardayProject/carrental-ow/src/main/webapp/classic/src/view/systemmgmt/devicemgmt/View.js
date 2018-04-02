/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.devicemgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'devicemgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.systemmgmt.devicemgmt.SearchForm',
    ],
    
    controller: {
        xclass: 'Admin.view.systemmgmt.devicemgmt.ViewController'
    },
    viewModel: {
    	xclass: 'Admin.view.systemmgmt.devicemgmt.DeviceModel'
    },
    
    listeners:{
    	afterrender: 'onSearchClick'
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
//    defaults: {
//        frame: true,
//        collapsible: true,
//        margin: '0 0 3 0'
//    },
    items: [{
        xclass: 'Admin.view.systemmgmt.devicemgmt.SearchForm',
    }, {
        xclass: 'Admin.view.systemmgmt.devicemgmt.GridDevice',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
