/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportdriving.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'reportdriving',
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    controller: {
        xclass: 'Admin.view.reportmgmt.reportdriving.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.reportmgmt.reportdriving.ViewModel'
    },
/*    listeners:{
        afterrender: 'onSearchClick',
    },*/
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
    {
        xclass: 'Admin.view.reportmgmt.reportdriving.SearchForm',
    },
    {
        xclass: 'Admin.view.reportmgmt.reportdriving.DrivingMainGrid',
        frame: true
    },
    {
        xclass: 'Admin.view.reportmgmt.reportdriving.DrivingGrid',
        frame: true
    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
