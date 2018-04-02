Ext.define('Admin.view.ordermgmt.orderrecreate.OrderViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.orderviewmodel',
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	ordersResults: {
            model: 'Admin.view.ordermgmt.orderrecreate.OrderDataModel',
            pageSize: 20,
            autoLoad: false,
            //remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'orderTimeF',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
