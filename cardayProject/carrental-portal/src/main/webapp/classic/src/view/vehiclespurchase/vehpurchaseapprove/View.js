Ext.define('Admin.view.vehiclespurchase.vehpurchaseapprove.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehPurchaseApprove',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
    ],
    controller: {
        xclass: 'Admin.view.vehiclespurchase.vehpurchaseapprove.ViewController'
    },
    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    viewModel: {
        xclass: 'Admin.view.vehiclespurchase.vehpurchaseapprove.ViewModel'
    },

    items: [
        {
            xtype: 'tabpanel',
            activeTab:0,
            bodyStyle: 'padding: 5px;',
            
            items:[
            {
                title: '待审核',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadApproving',
                },
                items: [
/*                    {
                        xclass: 'Admin.view.vehiclespurchase.vehpurchaseapprove.SearchFormApply',
                    }, */
                    {
                        xclass: 'Admin.view.vehiclespurchase.vehpurchaseapprove.GridVehPurchaseApproving',
                        frame: true
                    }
                ]           
            },
            {
                title: '已审核',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadApproved',
                },
                items: [
/*                    {
                        xclass: 'Admin.view.carnumbermgmt.vehpurchaseapprove.SearchFormRefuse',
                    }, */
                    {
                        xclass: 'Admin.view.vehiclespurchase.vehpurchaseapprove.GridVehPurchaseApproved',
                        frame: true
                    }
                ] 
            }]
         }],
    initComponent: function() {
        this.callParent();
    }
});
