Ext.define('Admin.view.systemmgmt.driverAllowanceSetting.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.allowanceConfigModel',

    stores: {
    	allowanceConfigResults: {
            model: 'Admin.model.systemmgmt.driverAllowanceSetting.AllowanceConfigModel',
            remoteFilter: true,
            autoLoad: false,
        }
    }
});
