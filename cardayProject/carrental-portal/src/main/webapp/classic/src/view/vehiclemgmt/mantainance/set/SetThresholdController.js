/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.set.SetThresholdController', {
    extend: 'Ext.app.ViewController',
    alias : 'controller.setThresholdController',
    requires: [
    ],
    
//    addRoleClick: function() {
//    	win = Ext.widget('roleView');
//		win.show();
//    }
    onclickSet : function(btn) {
    	var thresholdInfo = this.getView().down('form').getForm().getValues();
    	console.log('id:' + thresholdInfo.mid);
    	console.log('maintenanceTime:' + thresholdInfo.maintenanceTime);
    	console.log('alertMileage:' + thresholdInfo.alertMileage);
    	var json = Ext.encode(thresholdInfo);
    	Ext.Ajax.request({
        	url:'maintenance/setThreshold',//?json='+Ext.encode(thresholdInfo),
			method:'POST',
      params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
			 		btn.up('window').close();
//				    Ext.getCmp("searchVehicle").fireEvent("click");
			 		Ext.Msg.alert("提示信息", '修改成功');
			 		
			 		//表数据加载
  					var pram={
  							'currentPage' : 1,
  							'numPerPage': 10
  					};
  					Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
  						"json" : Ext.encode(pram)
  					}
  					Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
				}else{
					Ext.Msg.alert("提示信息", appendData.error);
				}
			 }
      /*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
        });
    },

});

