Ext.define('Admin.view.reportmgmt.vehicleusereport.VehicleDataModel', {
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
            read: 'usage/report/getVehiclePropertyData',

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
