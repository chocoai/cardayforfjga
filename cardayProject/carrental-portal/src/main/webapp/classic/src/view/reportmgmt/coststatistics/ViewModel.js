/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.coststatistics.ViewModel', {
    extend: 'Ext.app.ViewModel',
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {        
        totalCost: {
        	autoLoad: false,
            fields: ['name', 'maintainCost','insuranceCost','trafficCost','totalCost'],
            data: [
                    {name: "福州市公安局", maintainCost: "595",insuranceCost: "4000", trafficCost:"400", totalCost: "295"},
                    {name: "厦门市公安局", maintainCost: "300",insuranceCost: "3000", trafficCost:"500", totalCost: "345"}
                  ]
        }, 


        sumCost: {
            autoLoad: false,
            fields: ['name', 'sumCost'],
            data: [
                    {name: "福州市公安局", sumCost: "5340"},
                    {name: "厦门市公安局", sumCost: "4195"}
                  ]
        },       
        
        vehicleList: {
            model: 'Admin.model.reportmgmt.coststatistics.VehicleModel',
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