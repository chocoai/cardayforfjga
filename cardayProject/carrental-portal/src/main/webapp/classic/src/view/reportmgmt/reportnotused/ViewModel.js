Ext.define('Admin.view.reportmgmt.reportnotused.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.reportnotusedmodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	drivingGridStore: {
    		model: 'Admin.model.reportmgmt.reportnotused.NoUsedDriving',
            autoLoad: false,
            pageSize: 10,
            remoteFilter: true,
           /* sorters: [{
                property: 'id',
                direction: 'DESC'
            }],*/
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
