Ext.define('Admin.view.systemmgmt.rolemgmt.RoleModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.rolemodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	rolesResults: {
            model: 'Admin.model.systemmgmt.rolemgmt.Role',
            pageSize: 20,
            autoLoad: false,
            remoteSort: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad',
                load: 'loadforRole',
            }
        },
    }
});
