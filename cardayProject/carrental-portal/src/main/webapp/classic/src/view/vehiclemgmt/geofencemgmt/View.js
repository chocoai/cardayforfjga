/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.geofencemgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'geofencemgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.vehiclemgmt.geofencemgmt.SearchForm',
    ],
    id:"geofencemgmt",
    controller: {
        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.ViewModel'
    },
    listeners:{
        afterrender: 'searchByGeofenceName'
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
        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.SearchForm',
    }, {
        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.GridGeofence',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
