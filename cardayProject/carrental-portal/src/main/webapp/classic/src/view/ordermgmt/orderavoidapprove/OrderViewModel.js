Ext.define('Admin.view.ordermgmt.orderavoidapprove.OrderViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.avoidapproveviewmodel',
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	ordersResults: {
            model: 'Admin.view.ordermgmt.orderavoidapprove.OrderAvoidApproveModel',
            pageSize: 10,
            autoLoad: false,
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
