/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.rulemgmt.locationmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'locationmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.rulemgmt.locationmgmt.SearchForm',
    ],
    id:"locationmgmt",
    controller: {
        xclass: 'Admin.view.rulemgmt.locationmgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.rulemgmt.locationmgmt.ViewModel'
    },
    listeners:{
        afterrender: 'searchByLocationName'
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
        xclass: 'Admin.view.rulemgmt.locationmgmt.SearchForm',
    }, {
        xclass: 'Admin.view.rulemgmt.locationmgmt.GridLocation',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
