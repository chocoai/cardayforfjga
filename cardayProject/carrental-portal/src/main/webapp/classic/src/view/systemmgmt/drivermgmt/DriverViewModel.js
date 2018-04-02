Ext.define('Admin.view.systemmgmt.drivermgmt.DriverViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.driverviewmodel',
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	driversResults: {
            model: 'Admin.view.systemmgmt.drivermgmt.DriverDataModel',
            pageSize: 20,
            autoLoad: false,
            //remoteSort: false,
            remoteFilter: true,
            /*sorters: [{
                property: 'id',
                direction: 'DESC'
            }],*/
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
