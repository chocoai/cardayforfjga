Ext.define('Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.enterInfoModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],
/*	data: {
            status: window.sessionStorage.getItem('userType')

        },*/
    stores: {
        usersResults: {
            type: 'enterinfousers',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeload'
            }
        },

        auditInfoStore: {
            model: 'Admin.model.enterinfo.AuditInfo',
            autoLoad: false,
            remoteFilter: true,
/*            listeners: {
                load: function(store,records,successful,operation,eOpts){
                     console.log('Load auditInfoStore data');
                     if(records.length == 0){
                        Ext.MessageBox.alert("消息提示", "无审核信息!");
                     }
                }
            }*/
        },

        vehsResourcesStore: {
            model: 'Admin.model.enterinfo.VehsResourcesInfo',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeloadVehsResources'
            }
        },

        rentalInfoStore: {
            model: 'Admin.model.enterinfo.RentalInfo',
            autoLoad: false,
            remoteFilter: true,
        },

        payHistoryStore: {
            model: 'Admin.model.enterinfo.PayHistory',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 5,
            sorters: [{
                property: 'operateTime',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeloadPayHistory'
            }
        },
    }
});
