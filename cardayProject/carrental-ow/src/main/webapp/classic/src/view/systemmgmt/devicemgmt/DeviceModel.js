Ext.define('Admin.view.systemmgmt.devicemgmt.DeviceModel', {
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
//            type: 'DeviceMgmtuser'   //app/store/systemmgmt/DeviceMgmt/Users.js
//        }
//    }
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	devicesResults: {
            model: 'Admin.model.systemmgmt.devicemgmt.Device',
            pageSize: 10,
            autoLoad: false,
            //remoteSort: false,
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
