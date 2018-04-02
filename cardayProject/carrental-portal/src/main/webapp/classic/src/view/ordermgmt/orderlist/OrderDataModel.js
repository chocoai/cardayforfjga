Ext.define('Admin.view.ordermgmt.orderlist.OrderDataModel', {
    fields: [],
    extend: 'Ext.data.Model',
    // requires: ['Ext.data.proxy.Rest',],
    //fields: ['IMEI', 'SIM_NO','STATUS','CREATE_TIME','UPDATE_TIME','DEVICE_NAME','FW_VER','HW_VER'],
    //fields: ['imei'],
    proxy: { //在base.js 里面有其他字段定义
        type: 'ajax',
        actionMethods: {
            read: 'POST',
            create: 'POST',
            update: 'POST',
            destroy: 'POST'
        },
        api: {
            // read: '/concar-portal/web/vehicle/loadVehicle',
            read: 'order/admin/list',
            //read: 'classic/src/view/ordermgmt/orderlist/order.json',
            // create: '/etruck-portal/web/vehicle/add',
            // update: '/etruck-portal/web/vehicle/update',
            // destroy: '/etruck-portal/web/vehicle/delete'
        },
        reader: {
            type: 'json',
            rootProperty: 'data.resultList',
            successProperty: 'status',
            totalProperty: 'data.totalRows'
        },
        writer: {
            type: 'json',
            writeAllFields: true,
            encode: true,
            rootProperty: 'data.resultList'
        }
//        ,
//        listeners: {
//            exception: function(proxy, response, operation) {
//                Ext.MessageBox.alert("消息提示","操作失败!");
//            }
//        }
    }
});
