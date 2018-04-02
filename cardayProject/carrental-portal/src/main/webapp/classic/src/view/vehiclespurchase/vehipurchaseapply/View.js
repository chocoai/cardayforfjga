Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehiPurchaseApply',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
    ],
    controller: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewController'
    },

    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    viewModel: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewModel'
    },

    items: [
        {
            xtype: 'tabpanel',
            activeTab:0,
            bodyStyle: 'padding: 5px;',
            
            items:[
            {
                title: '申请中',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadApply',
                },
                items: [
                    {
                        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.SearchFormApply',
                    },
                    {
                        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseApply',
                        frame: true
                    }
                ]           
            },
            {
                title: '已驳回',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadRefuse',
                },
                items: [
/*                    {
                        xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormRefuse',
                    }, */
                    {
                        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseRefuse',
                        frame: true
                    }
                ] 
            },
            {
                title: '审核通过',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadAudited',
                },
                items: [
/*                    {
                        xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormAudited',
                    }, */
                    {
                        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseAudited',
                        frame: true
                    }
                ] 
            }]
         }],
    initComponent: function() {
        this.callParent();
    }
});
