Ext.define('Admin.view.ordermgmt.orderavoidapprove.OrderAvoidApproveModel', {
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
            read: 'order/admin/list',
            //read: 'classic/src/view/ordermgmt/orderavoidapprove/order.json',
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
    }
});
