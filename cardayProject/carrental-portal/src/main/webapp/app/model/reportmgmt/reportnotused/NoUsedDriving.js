Ext.define('Admin.model.reportmgmt.reportnotused.NoUsedDriving', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'usage/report/getIdleVehicleList',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data.resultList',
             successProperty: 'success',
             totalProperty:'data.totalRows'
        }
    },
    autoLoad: 'false',

    sorters: {
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'id'
    }
});