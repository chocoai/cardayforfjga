/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.stationmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'stationmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.vehiclemgmt.stationmgmt.SearchForm',
    ],
    id:"stationmgmt",
    controller: {
        xclass: 'Admin.view.vehiclemgmt.stationmgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.stationmgmt.StationModel'
    },
    listeners:{
        afterrender: 'searchByStationName'
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
        xclass: 'Admin.view.vehiclemgmt.stationmgmt.SearchForm',
    }, {
        xclass: 'Admin.view.vehiclemgmt.stationmgmt.GridStation',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
