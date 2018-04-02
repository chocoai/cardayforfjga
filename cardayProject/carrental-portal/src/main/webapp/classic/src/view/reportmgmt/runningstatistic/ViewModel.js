/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.runningstatistic.ViewModel', {
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
                    {name: "福州市公安局", data: "120"},
                    {name: "厦门市公安局", data: "123"}
                  ]
        },
        
        totalCost: {
        	autoLoad: false,
            fields: ['name', 'data'],
            data: [
                    {name: "福州市公安局", data: "220"},
                    {name: "厦门市公安局", data: "223"}
                  ]
        },
        
        totalCrossRoad: {
        	autoLoad: false,
            fields: ['name', 'data'],
            data: [
                    {name: "福州市公安局", data: "20"},
                    {name: "厦门市公安局", data: "23"}
                  ]
        },        
        
        vehicleList: {
            model: 'Admin.model.reportmgmt.runningstatistic.VehicleModel',
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