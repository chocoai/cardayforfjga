Ext.define('Admin.store.vehiclemgmt.realtime_monitoring', {
    extend: 'Ext.data.Store',

    alias: 'store.realtimeMonitoring',

    model: 'Admin.model.vehiclemgmt.realtime_monitoring.realtimeMonitoring',

    proxy:{
    	type: 'ajax',
        url: 'department/listDirectChildrenWithCountByOrgId',
//        url: 'app/data/vehiclemgmt/realtime_monitoring/vehicleData.json',
//      pageSize:5,
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status'
//           totalProperty:14
        }
    },
    autoLoad: 'false',

    sorters: {
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'id'
    }
});
