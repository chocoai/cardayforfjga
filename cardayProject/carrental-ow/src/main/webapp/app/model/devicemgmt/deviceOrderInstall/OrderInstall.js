Ext.define('Admin.model.devicemgmt.deviceOrderInstall.OrderInstall', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
        type: 'ajax',
        //url: 'app/data/devicemgmt/deviceOrderInstall/OrderInstallList.json',
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
});