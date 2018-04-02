Ext.define('Admin.model.vehiclemgmt.authorizedmgmt.authorizedApplyResults', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
        type: 'ajax',
        url: 'vehicle/authorized/list',
       // url: 'app/data/vehiclemgmt/authorizedmgmt/AuthorizedApplyRefuseResults.json',
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
        direction:'DESC',
        property: 'id'
    }
});
