Ext.define('Admin.model.reportmgmt.reportdriving.Driving', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'usage/report/getDrivingDetailedReport',
        //url: 'app/data/arcmgmtinfo/reportdriving.json',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
    },
    autoLoad: 'false',
});