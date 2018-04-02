Ext.define('Admin.model.enterinfo.Car', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'carNo'
        },
        {
            type: 'string',
            name: 'carMark'
        },
        {
            type: 'string',
            name: 'carStyle'
        },
        {
        	type: 'string',
            name: 'seatsNumber'
        },
        {
            type: 'string',
            name: 'carColor'
        }
    ]
});
