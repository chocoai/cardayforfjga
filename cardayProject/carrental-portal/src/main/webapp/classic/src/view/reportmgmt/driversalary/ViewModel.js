Ext.define('Admin.view.reportmgmt.driversalary.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.driversalarymodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	driverSalaryGridStore: {
    		model: 'Admin.model.reportmgmt.driversalary.DriverSalary',
            autoLoad: false,
//            pageSize: 10,
            remoteFilter: true,
            sorters: [{
                property: 'name',
                direction: 'ASC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
