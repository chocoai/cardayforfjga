Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'authorizedmgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
    ],
    controller: {
        xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.ViewController'
    },

    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.ViewModel'
    },
    listeners:{
    //	afterrender: 'onSearchClick'
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
							xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.SearchFormApply',
							 margin: '0 0 20 0'
							 
						},
						{
							xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.GridAuthorizedApply',
							frame:true
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
                      /*{
							xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.SearchForm',
							 margin: '0 0 20 0'
							 
						},*/
						{
							xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.GridAuthorizedRefuse',
							frame:true
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
                    afterrender: 'onSearchClick',
                },
                items: [
                        {
							xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.SearchForm',
							 margin: '0 0 20 0'
							 
						},
						{
							xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.Grid',
							frame:true
						}
                ] 
            }]
         }],
    initComponent: function() {
        this.callParent();
    }
});
