/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportnotused.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'reportnotused',
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
  controller: {
        xclass: 'Admin.view.reportmgmt.reportnotused.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.reportmgmt.reportnotused.ViewModel'
    },
    listeners:{
        afterrender: 'onSearchClick',
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
    {
        xclass: 'Admin.view.reportmgmt.reportnotused.SearchForm',
    },
    {
        xclass: 'Admin.view.reportmgmt.reportnotused.NoUsedDrivingGrid',
        frame: true
    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
