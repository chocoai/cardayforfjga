Ext.define('Admin.store.enterinfomgmt.Rents', {
    extend: 'Ext.data.Store',

    alias: 'store.rents',

    model: 'Admin.model.enterinfo.Rent',

    proxy:{
    	type: 'ajax',
        url: 'rent/list',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status'
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