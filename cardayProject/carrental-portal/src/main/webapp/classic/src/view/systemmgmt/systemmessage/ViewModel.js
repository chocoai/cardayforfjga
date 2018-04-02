Ext.define('Admin.view.systemmgmt.systemmessage.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.rolemodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	messageResults: {
            model: 'Admin.model.systemmgmt.systemmessage.Message',
            pageSize: 20,
            autoLoad: false,
            remoteSort: false,
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
