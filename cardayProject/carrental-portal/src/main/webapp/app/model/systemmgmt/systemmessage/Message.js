Ext.define('Admin.model.systemmgmt.systemmessage.Message', {
    fields: [],
    extend: 'Ext.data.Model',
    proxy:{
        type: 'ajax',
        url: 'sysMessage/list',
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
        direction: 'DESC',
        property: 'id'
    }
//    ,
//     listeners: {
//        exception: function(proxy, response, operation) {
//            Ext.MessageBox.alert("消息提示","操作失败!");
//        }
//	  }
});