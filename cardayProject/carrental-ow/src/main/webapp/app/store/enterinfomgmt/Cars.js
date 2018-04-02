Ext.define('Admin.store.enterinfomgmt.Cars', {
    extend: 'Ext.data.Store',

    alias: 'store.cars',

    model: 'Admin.model.enterinfo.Car',

    proxy:{
    	type: 'ajax',
         url: 'app/data/enterinfo/cars.json',
         reader: {
             type: 'json',
             rootProperty: 'cars',
             successProperty: 'success'
         }
    },

    autoLoad: false,

    sorters: {
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'seatsNumber'
    }
});
