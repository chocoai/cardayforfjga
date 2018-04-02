/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportdriving.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			],
	onSearchClick :　function() {
		console.log('onSearchClick');
		var regex=/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
		var frmValues = this.lookupReference('searchForm').getValues();
		var vehicleNum=Ext.getCmp('add_device_vehicleNumber_id').getValue();

		if(frmValues.vehNum == null || frmValues.vehNum == ''){
			Ext.MessageBox.alert("消息提示","车牌号不能为空!");
		}else if(frmValues.vehNum == '没有匹配的车辆'){
			Ext.MessageBox.alert("消息提示","没有匹配的车辆");
		}/*else if((frmValues.vehNum).match(regex)==null){
			Ext.MessageBox.alert("消息提示","请输入完整的车牌号");
		}*/else if(!this.lookupReference('searchForm').getForm().isValid()){
			Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
		}else if(!isExistVehicleNumber(vehicleNum)){
			Ext.Msg.alert('消息提示','车辆  '+frmValues.vehNum+' 在系统中不存在');
		}else{
        	var input = {
			  //"drivingtime" : frmValues.drivingDate,
			  "vehicleNumber":frmValues.vehNum,
			  "starttime" : frmValues.startDate,
			  "endtime" : frmValues.endDate
			};
			var pram = Ext.encode(input);
			this.getViewModel().getStore("drivingGridStore").proxy.extraParams = {
				"json" : pram
			},
			this.getViewModel().getStore("drivingGridStore").load();
			
			this.getViewModel().getStore("drivingGridMainStore").proxy.extraParams = {
				"json" : pram
			},
			this.getViewModel().getStore("drivingGridMainStore").load();
			}
	},

	onExcelClick :　function() {
		console.log('onExcelClick');
		var regex=/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
		var frmValues = this.lookupReference('searchForm').getValues();
	
		if(frmValues.vehNum == null || frmValues.vehNum == ''){
			Ext.MessageBox.alert("消息提示","车牌号不能为空!");
		}else if(frmValues.vehNum == '没有匹配的车辆'){
			Ext.MessageBox.alert("消息提示","没有匹配的车辆");
		}/*else if((frmValues.vehNum).match(regex)==null){
			Ext.MessageBox.alert("消息提示","请输入完整的车牌号");
		}*/else if(!this.lookupReference('searchForm').getForm().isValid()){
			Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
		}else{
			var input = {
					//"drivingtime" : frmValues.drivingDate,
			        "vehicleNumber":frmValues.vehNum,
			        "starttime" : frmValues.startDate,
					"endtime" : frmValues.endDate
				};
			window.location.href = 'usage/report/exportDrivingDetailedReport?json='  + Ext.encode(input);
		}
	},
	
});

function isExistVehicleNumber(vehicleNumber){
	var flag=false;
	  var input={
			  vehicleNumber:vehicleNumber
			};
	  var json=Ext.encode(input);
	Ext.Ajax.request({
    	url:'vehicle/listVehicleAutoComplete',//?json='+Ext.encode(vehiclInfo),
		method:'GET',
		params:{json:json},
		async:false,
        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		success: function(response){
			var res = Ext.util.JSON.decode(response.responseText);
			if(res.status=='success' && res.data!=null && res.data.length>0){
				 flag=true;
			}
		 },
    });
	return flag;
}