/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.outMarkerAlarm.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'outMarkerMgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.alertMgmt.outMarkerAlarm.SearchForm'
    ],
    controller: {
        xclass: 'Admin.view.alertMgmt.outMarkerAlarm.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.alertMgmt.outMarkerAlarm.ViewModel'
    },
    listeners:{
        afterrender: 'onSearchClick'
    },
    
    scrollable: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
        xclass: 'Admin.view.alertMgmt.outMarkerAlarm.SearchForm'
    }, {
        xclass: 'Admin.view.alertMgmt.outMarkerAlarm.OutMarkerVehicleGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
