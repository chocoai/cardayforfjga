Ext.define('Admin.view.ordermgmt.orderallocate.DriverViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.driverviewmodel',
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	driversResults: {
            model: 'Admin.view.ordermgmt.orderallocate.DriverDataModel',
            pageSize: 20,
            autoLoad: false,
            //remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'ASC'
            }],
            listeners: {
                //beforeload: 'onBeforeLoadDriver'
            	load: 'checkAvialiableDriver',
            }
        },
    }
});
