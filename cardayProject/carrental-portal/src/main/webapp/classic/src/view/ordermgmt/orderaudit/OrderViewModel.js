Ext.define('Admin.view.ordermgmt.orderaudit.OrderViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.orderviewmodel',
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	ordersResults: {
            model: 'Admin.view.ordermgmt.orderaudit.OrderDataModel',
            pageSize: 20,
            autoLoad: false,
            //remoteSort: false,
            remoteFilter: true,
            sorters: [{
            	property: 'orderTimeF',
            	direction: 'ASC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
