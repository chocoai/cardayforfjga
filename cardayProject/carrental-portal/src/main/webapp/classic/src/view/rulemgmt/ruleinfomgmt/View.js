/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.rulemgmt.ruleinfomgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'ruleinfomgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.rulemgmt.ruleinfomgmt.SearchForm',
    ],
    id:"ruleinfomgmt",
    controller: {
        xclass: 'Admin.view.rulemgmt.ruleinfomgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.rulemgmt.ruleinfomgmt.ViewModel'
    },
    listeners:{
        afterrender: 'afterrenderRuleInfo'
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
        xclass: 'Admin.view.rulemgmt.ruleinfomgmt.SearchForm',
    }, {
        xclass: 'Admin.view.rulemgmt.ruleinfomgmt.GridRuleInfo',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
