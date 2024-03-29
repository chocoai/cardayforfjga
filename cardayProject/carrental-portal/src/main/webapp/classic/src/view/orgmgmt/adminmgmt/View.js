/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.adminmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'adminmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.orgmgmt.adminmgmt.SearchForm',
        'Admin.view.orgmgmt.adminmgmt.Grid'
//        'Web.view.module.license_obdmgmt.obdconfig.ViewController',
//        'Web.view.module.license_obdmgmt.obdconfig.ViewModel',
//        //'Web.view.module.license_obdmgmt.obdconfig.GridSummary',
//        'Web.view.module.license_obdmgmt.obdconfig.ConfigModel',
    ],
    
    controller: {
        xclass: 'Admin.view.orgmgmt.adminmgmt.ViewController'
    },
//    viewModel: {
//        xclass: 'Web.view.module.license_obdmgmt.obdconfig.ViewModel'
//    },
//    listeners:{
//        afterrender: 'onSearchClick',
//    },
    
    autoScroll: true,
    height: 700,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 0,
    defaults: {
        frame: true,
        collapsible: true,
        margin: '0 0 3 0'
    },
    items: [{
    	 xclass: 'Admin.view.orgmgmt.adminmgmt.SearchForm',
         margins: '5 0 0 0'
    },
   {
        xclass: 'Admin.view.orgmgmt.adminmgmt.Grid',
        margins: '5 0 0 0'
    }],
    initComponent: function() {
        this.callParent();
    }
});
