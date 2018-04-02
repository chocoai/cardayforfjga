Ext.define('Admin.view.carnumbermgmt.carnumberapply.ApplyResults', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
        type: 'ajax',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        api: {
            read: 'app/data/carnumbermgmt/carnumberapply/applyResults.json',
            //read: 'classic/src/view/ordermgmt/orderlist/order.json',
            // create: '/etruck-portal/web/vehicle/add',
            // update: '/etruck-portal/web/vehicle/update',
            // destroy: '/etruck-portal/web/vehicle/delete'
        },
        reader: {
             type: 'json',
             rootProperty: 'data.resultList',
             successProperty: 'status',
             totalProperty:'data.totalRows'
        },
        writer: {
            type: 'json',
            writeAllFields: true,
            encode: true,
            rootProperty: 'data.resultList'
        }
    },
    autoLoad: 'false',

    sorters: {
        direction:'DESC',
        property: 'id'
    }
});
