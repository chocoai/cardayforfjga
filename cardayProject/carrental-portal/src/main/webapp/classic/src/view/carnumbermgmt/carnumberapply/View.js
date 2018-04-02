Ext.define('Admin.view.carnumbermgmt.carnumberapply.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'carNumberApply',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.carnumbermgmt.carnumberapply.SearchFormApply',
        'Admin.view.carnumbermgmt.carnumberapply.GridApply'
    ],
    controller: {
        xclass: 'Admin.view.carnumbermgmt.carnumberapply.ViewController'
    },
    
    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    viewModel: {
        xclass: 'Admin.view.carnumbermgmt.carnumberapply.ViewModel'
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
                        xclass: 'Admin.view.carnumbermgmt.carnumberapply.SearchFormApply',
                    }, 
                    {
                        xclass: 'Admin.view.carnumbermgmt.carnumberapply.GridApply',
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
//                	{
//                        xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormApply',
//                    }, 
                    {
                        xclass: 'Admin.view.carnumbermgmt.carnumberapply.GridRefuse',
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
//                	{
//                        xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormApply',
//                    }, 
                    {
                        xclass: 'Admin.view.carnumbermgmt.carnumberapply.GridAudited',
                        frame: true
                    }
                ] 
            }]
         }],
         
    initComponent: function() {
        this.callParent();
    }
});
