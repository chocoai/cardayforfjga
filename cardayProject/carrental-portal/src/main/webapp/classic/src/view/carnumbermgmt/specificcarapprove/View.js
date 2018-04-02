Ext.define('Admin.view.carnumbermgmt.specificcarapprove.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'specificCarApprove',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.carnumbermgmt.specificcarapprove.SearchForm',
        'Admin.view.carnumbermgmt.specificcarapprove.GridSpecificApply'
    ],
    controller: {
        xclass: 'Admin.view.carnumbermgmt.specificcarapprove.ViewController'
    },

    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    viewModel: {
        xclass: 'Admin.view.carnumbermgmt.specificcarapprove.ViewModel'
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
                    afterrender: 'onBeforeLoadApply',
                },
                items: [
//                	{
//                        xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormApply',
//                    }, 
                    {
                        xclass: 'Admin.view.carnumbermgmt.specificcarapprove.GridSpecificApply',
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
                    afterrender: 'onBeforeLoadAudited',
                },
                items: [
//                	{
//                        xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormApply',
//                    }, 
                    {
                        xclass: 'Admin.view.carnumbermgmt.specificcarapprove.GridSpecificAudited',
                        frame: true
                    }
                ] 
            }]
         }],
    
    
    
    
    initComponent: function() {
        this.callParent();
    }
});
