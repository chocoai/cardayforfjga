Ext.define('Admin.view.rulemgmt.locationmgmt.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.locationmodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	locationResults: {
    		model: 'Admin.model.rulemgmt.locationmgmt.Location',
            autoLoad: false,
            pageSize: 10,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
