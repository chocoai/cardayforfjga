Ext.define('Admin.model.enterinfo.RentalInfo', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'organization/findRelatedRentCompany',
           actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
//        ,
//
//        listeners: {
//            exception: function(proxy, response, operation) {
//                Ext.MessageBox.alert("消息提示","操作失败!");
//            }
//        }
    },
    autoLoad: 'false',
});