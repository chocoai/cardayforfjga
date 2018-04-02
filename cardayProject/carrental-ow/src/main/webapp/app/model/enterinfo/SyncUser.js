Ext.define('Admin.model.enterinfo.SyncUser', {
    fields: [],
    extend: 'Ext.data.Model',
    
    proxy:{
        type: 'ajax',
        reader: {
             type: 'json',
             rootProperty: 'result.dataList',
             successProperty: 'resultCode',
             totalProperty:'result.totalRows'
        }
    },
    autoLoad: 'false'
});