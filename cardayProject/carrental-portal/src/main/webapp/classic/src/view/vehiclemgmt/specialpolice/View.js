Ext.define('Admin.view.vehiclemgmt.specialpolice.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'specialPolice',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
    ],
    controller: {
        xclass: 'Admin.view.vehiclemgmt.specialpolice.ViewController'
    },

    width: 400,
    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.specialpolice.ViewModel'
    },

    items: [
        {
            xtype: 'tabpanel',
            activeTab:0,
            bodyStyle: 'padding: 5px;',
            
            items:[
            {
                title: '未涉密',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },                
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadforNoSecret',
                    activate: 'onActivateforNoSecret',
                },
                items: [
                    {
                        xclass: 'Admin.view.vehiclemgmt.specialpolice.SearchFormNoSecret',
                        margin: '0 0 10 0'
                    },
                    {
                        xclass: 'Admin.view.vehiclemgmt.specialpolice.GridVehicleNoSecret',
                        frame: true
                    }
                ]           
            },
            {
                title: '已涉密',
                border: false,
                autoScroll: true,
                layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                bodyPadding: 20,
                listeners:{
                    afterrender: 'onBeforeLoadforSecret',
                    activate: 'onActivateforSecret',
                },
                items: [
                    {
                        xclass: 'Admin.view.vehiclemgmt.specialpolice.SearchFormSecret',
                        margin: '0 0 10 0'
                    }, 
                    {
                        xclass: 'Admin.view.vehiclemgmt.specialpolice.GridVehicleSecret',
                        frame: true
                    }
                ] 
            }]
         }],
    initComponent: function() {
        this.callParent();
    }
});
