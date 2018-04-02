/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.add.WindowController', {
    extend: 'Ext.app.ViewController',
    alias: "controller.mantainanceAddController",
    requires: [
    ],
    initComboStore : function() {
    	Ext.getCmp('maintanance_vehicleNumber_combo').getStore().load();
    },

  //车牌号
	checkVehicleNumberValid:function(field){
		var me=this;
		var fieldValue=field.getValue();
		var input={
			vehicleNumber:fieldValue
		};
		var json = Ext.encode(input);
		if(fieldValue!=''){
			Ext.Ajax.request({
		    	url:'vehicle/findCountValByVehicleNumber',//?json='+Ext.encode(input),
				method:'POST',
	        	params:{json:json},
				//headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						var validflag=appendData.data.validflag;
//						console.log('-----validflag---' + validflag);
						if(validflag=='1'){
							field.setValue('');
							field.setActiveError('该车牌号已被绑定,请重新输入!');me.getView().down('form').down('toolbar').query('#errormsg')[0].setText(appendData.data.message+',请重新输入!');
						}
				 	}
				}
	        	/*,
				failure : function() {
					Ext.Msg.alert('Failure','Call interface error!');
				}*/
    	});
		}
		
	},
	onAddMantainClick : function(btn) {
		var manmainInfo = this.lookupReference('addMantainanceView').getValues();
		console.log('object::' + JSON.stringify(manmainInfo));
		var input = {
						'vehicleId' : manmainInfo.vehicleId,
						'deviceNumber' : manmainInfo.deviceNumber,
						'curTime' :  manmainInfo.lastMantainTime + ' 00:00:00',
						'headerMaintenanceMileage' : manmainInfo.headerMaintenanceMileage,
						'maintenanceMileage' : manmainInfo.mantainMileage,
						'maintenanceTime' :  manmainInfo.maintenanceTime
						
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
//			url : 'maintenance/create?json=' + Ext.encode(input),
			url : 'maintenance/create',
	        method : 'POST',
//	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        params:{json:json},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	console.log('status:' + respText.status);
	        	if(respText.status == 'failure') {
	        		Ext.Msg.alert('消息提示',respText.data);
	        		return;
	        	}
	        	
	        	btn.up("addMantainanceView").close();
	        	
	        	//表数据加载
	        	var pram={
	        			'currentPage' : 1,
	        			'numPerPage': 10
	        	};
	        	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
	    			"json" : Ext.encode(pram)
	    		}
	        	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        }
	    });
	},
	fillVehicleInfo : function(combo , record , eOpts) {
//		console.log('+++fillVehicleInfo+++' + record.data.id);
		Ext.Ajax.request({
			url : 'vehicle/monitor/' + record.data.id + '/update',
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var data = [];
		    	data[0] = respText.data;
		    	console.log('divice::' + data[0].vehicleNumber);
		    	var form = Ext.getCmp('addMantainanceView').down('form').getForm();
		    	console.log('object:' + form.findField('vehicleNumber'));
		    	form.findField('vehicleNumber').setRawValue(data[0].vehicleNumber);
//		    	Ext.getCmp('maintanance_vehicleNumber_combo').setValue(data[0].vehicleNumber);
		    	form.findField('vehicleBrand').setValue(data[0].vehicleBrand);
		    	form.findField('vehicleIdentification').setRawValue(data[0].vehicleIdentification);
//		    	form.findField('vehicleIdentification').setRawValue(data[0].vehicleIdentification);
		    	form.findField('deviceNumber').setValue(data[0].deviceNumber);
		    	form.findField('simNumber').setValue(data[0].simNumber);
		    	form.findField('vehicleModel').setValue(data[0].vehicleModel);
		    	form.findField('vehicleFromName').setValue(data[0].vehicleFromName);
	        	form.findField('arrangedOrgName').setValue(data[0].arrangedOrgName);
	        	form.findField('vehicleId').setValue(data[0].id);
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        }
	    });
	},
	
});

