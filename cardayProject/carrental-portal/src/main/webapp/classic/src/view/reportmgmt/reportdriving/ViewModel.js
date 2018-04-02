Ext.define('Admin.view.reportmgmt.reportdriving.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.drivingmodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
        drivingGridMainStore: {
            model: 'Admin.model.reportmgmt.reportdriving.DrivingMain',
            autoLoad: false,
            remoteFilter: true,
            listeners: {
                load: function(store,records,successful,operation,eOpts){
                    console.log('Load reportdriving data');
                    if(records && records.length == 0){
                        return;
                    }else{
                        for (var i = 1; i < records.length; i++) {
                            store.remove(records[i]);
                        }
                    }
                }
            }
        },

    	drivingGridStore: {
    		model: 'Admin.model.reportmgmt.reportdriving.Driving',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'tracetime',
                direction: 'ASC'
            }],
            listeners: {
                load: function(store,records,successful,operation,eOpts){
                     console.log('Load reportdriving data');
                     if(records && records.length == 0){
                        var param = Ext.decode(operation.request.initialConfig.params.json);
                        var searchTimeStr = param.starttime == param.endtime ? 
                        		(param.starttime+'&nbsp;'):
                        			(param.starttime+'至'+param.endtime+'</br>');
                        Ext.MessageBox.alert("消息提示", searchTimeStr +  "车辆 " +param.vehicleNumber +" 无行驶明细记录!");
                     }
                }
            }
        },
    }
});
