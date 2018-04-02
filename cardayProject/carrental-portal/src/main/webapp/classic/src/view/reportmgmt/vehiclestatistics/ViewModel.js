/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.vehiclestatistics.ViewModel', {
    extend: 'Ext.app.ViewModel',
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {        
        totalNum: {
        	autoLoad: false,
            fields: ['name','commonNumAuthorized', 'specialNumAuthorized', 'commonNumUsed', 'specialNumAuthorized'],
            data: [
                    {name: "福州市公安局", commonNumAuthorized: "100",specialNumAuthorized: "50", commonNumUsed:"76", specialNumAuthorized: "33"},
                    {name: "厦门市公安局", commonNumAuthorized: "90",specialNumAuthorized: "30", commonNumUsed:"67", specialNumAuthorized: "28"}
                  ]
        },       
        
        vehicleList: {
            model: 'Admin.model.reportmgmt.vehiclestatistics.VehicleModel',
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