Ext.define('Admin.view.carnumbermgmt.carnumberapprove.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'carNumberApprove',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.carnumbermgmt.carnumberapprove.SearchForm',
        'Admin.view.carnumbermgmt.carnumberapprove.GridApply'
    ],
    controller: {
        xclass: 'Admin.view.carnumbermgmt.carnumberapprove.ViewController'
    },

    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },
    
    viewModel: {
        xclass: 'Admin.view.carnumbermgmt.carnumberapprove.ViewModel'
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
//            	{
//                    xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormApply',
//                }, 
                {
                    xclass: 'Admin.view.carnumbermgmt.carnumberapprove.GridApply',
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
//                    	{
//                            xclass: 'Admin.view.carnumbermgmt.specificcarapply.SearchFormApply',
//                        }, 
                {
                    xclass: 'Admin.view.carnumbermgmt.carnumberapprove.GridAudited',
                    frame: true
                }
            ] 
        }]
     }],
    initComponent: function() {
        this.callParent();
    }
});
