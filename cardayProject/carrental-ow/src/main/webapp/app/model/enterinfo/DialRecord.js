Ext.define('Admin.model.enterinfo.DialRecord', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
        type: 'ajax',
        url: 'dialcenter/list',
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