Ext.define('Admin.view.systemmgmt.usermgmt.UserModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.usermodel',

//    requires: [
//        'Ext.data.Store',
//        'Ext.data.proxy.Memory',
//        'Ext.data.field.Integer',
//        'Ext.data.field.String',
//        'Ext.data.field.Date',
//        'Ext.data.field.Boolean',
//        'Ext.data.reader.Json'
//    ],
//
//    stores: {
//        usersResults: {
//            type: 'usermgmtuser'   //app/store/systemmgmt/usermgmt/Users.js
//        }
//    }
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	usersResults: {
            model: 'Admin.model.systemmgmt.usermgmt.User',
            pageSize: 20,
            autoLoad: false,
            remoteSort: false,
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
