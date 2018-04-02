/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.dispatchvehicle.ViewModel', {
    extend: 'Ext.app.ViewModel',
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
        totalMileage: {
            autoLoad: false,
            fields: ['name', 'data'],
            data: [
                    {name: "福州市公安局", data: "206"},
                    {name: "厦门市公安局", data: "487"}
                  ]
        },       
        
        vehicleList: {
            model: 'Admin.model.reportmgmt.dispatchvehicle.VehicleModel',
            pageSize: 10,
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'ASC'
            }],
        },        
    }
});