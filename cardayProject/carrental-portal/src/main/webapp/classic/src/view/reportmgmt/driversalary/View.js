/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.driversalary.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'driversalary',
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
  controller: {
        xclass: 'Admin.view.reportmgmt.driversalary.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.reportmgmt.driversalary.ViewModel'
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
        xclass: 'Admin.view.reportmgmt.driversalary.SearchForm',
    },
    {
        xclass: 'Admin.view.reportmgmt.driversalary.DriverSalaryGrid',
        frame: true
    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
