/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'mantainance',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    id:'mantainance',
    controller: {
        xclass: 'Admin.view.vehiclemgmt.mantainance.ViewController'
    },
    /*viewModel: {
        xclass: 'Admin.view.vehiclemgmt.mantainance.ViewModel'
    },*/
    listeners:{
//        afterrender: 'searchByGeofenceName'
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
        xclass: 'Admin.view.vehiclemgmt.mantainance.SearchForm',
    }, {
        xclass: 'Admin.view.vehiclemgmt.mantainance.Grid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
