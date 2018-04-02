Ext.define('Admin.view.EnterpriseOrder.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'EnterpriseOrder',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date'
    ],
    // controller: {
    //     xclass: 'Admin.view.orgmgmt.dialrecordmgmt.ViewController'
    // },
    // viewModel : {
    // 	type : 'dialRecordModel'
    // },
    // listeners:{
    // afterrender: 'onSearchClick'
    // },
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
        {
            // xclass: 'Admin.view.orgmgmt.EnterpriseOrder.Grid',
            frame: false,
            html: '<iframe id="frame1" src="enterpriseOrder.html" frameborder="0" width="100%" height="800"></iframe>'
        }],
    initComponent: function() {
        this.callParent();
    }
});
