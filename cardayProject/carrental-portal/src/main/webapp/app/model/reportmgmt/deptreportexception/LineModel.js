Ext.define('Admin.model.reportmgmt.deptreportexception.LineModel', {
    fields: [],
    extend: 'Ext.data.Model',
    proxy: { //在base.js 里面有其他字段定义
        type: 'ajax',
        actionMethods: {
            read: 'POST',
            create: 'POST',
            update: 'POST',
            destroy: 'POST'
        },
        api: {
        	read: 'vehicleAlert/statAlertByType',
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'status',
        },
        writer: {
            type: 'json',
            writeAllFields: true,
            encode: true,
            rootProperty: 'data.dataList'
        }
//        ,
//        listeners: {
//            exception: function(proxy, response, operation) {
//                Ext.MessageBox.alert("消息提示","操作失败!");
//            }
//        }
    }
});
