/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.annualInspection.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'annualInspection',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    id:'annualInspection',
    controller: {
        xclass: 'Admin.view.vehiclemgmt.annualInspection.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.annualInspection.ViewModel'
    },
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
        xclass: 'Admin.view.vehiclemgmt.annualInspection.SearchForm',
    }, {
        xclass: 'Admin.view.vehiclemgmt.annualInspection.Grid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
