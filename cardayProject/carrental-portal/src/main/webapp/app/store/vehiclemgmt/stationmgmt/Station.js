Ext.define('Admin.store.vehiclemgmt.stationmgmt.Station', {
    extend: 'Ext.data.Store',

    alias: 'store.stationmgmt',

    model: 'Admin.model.vehiclemgmt.stationmgmt.Station',

    proxy:{
    	type: 'ajax',
        url: 'station/findByStationName',
    	//url: 'app/data/vehiclemgmt/stationmgmt/stations.json',
//      pageSize:5,
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
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'id'
    }
});
