Ext.define('Admin.view.dashboard.Dashboard', {
    extend: 'Ext.container.Container',
    //xtype: 'admindashboard',

    requires: [
        'Ext.ux.layout.ResponsiveColumn'
    ],

    controller: 'dashboard',
    viewModel: {
        type: 'dashboard'
    },

    //layout: 'responsivecolumn',
    layout: 'absolute',
    listeners: {
        hide: 'onHideView'
    },
    style: 'background-color:#006083',
 	html:'<div style="font:40px arial;color:white;text-align: center;padding: 150px;">欢迎使用企业客户用车管理平台</div>'
});
