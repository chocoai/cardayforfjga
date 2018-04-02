Ext.define('Admin.store.enterinfomgmt.Users', {
    extend: 'Ext.data.Store',

    alias: 'store.enterinfousers',

    model: 'Admin.model.enterinfo.User',

    proxy:{
    	type: 'ajax',
        url: 'organization/list',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data.resultList',
             successProperty: 'status',
             totalProperty:'data.totalRows'
        }
    },
    autoLoad: 'false',

    sorters: {
    	direction:'DESC',
        property: 'id'
    }
//    , 
//    listeners: {
//	    exception: function(proxy, response, operation) {
//	        Ext.MessageBox.alert("消息提示","操作失败!");
//	    }
//    }
});
