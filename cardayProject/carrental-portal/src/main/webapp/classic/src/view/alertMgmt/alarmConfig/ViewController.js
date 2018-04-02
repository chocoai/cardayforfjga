Ext.define('Admin.view.alertMgmt.alarmConfig.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.alertMgmt.alarmConfig.View',
			],
	alias : 'controller.alarmConfigController',	

 	onAfterrender:function(){
    	this.getViewModel().getStore("alarmConfigResults").load();
    },

    modifyAlarmConfig: function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex); 
        var isEnable,eventType;
        var messageTitle,messageConent;
        if(rec.data.enable){
            isEnable = false;
            messageTitle = '关闭报警';
            messageConent = '确定是否关闭 ';
        }else{
            isEnable = true;
            messageTitle = '开启报警';
            messageConent = '确定是否开启 ';
        }  

        switch(rec.data.eventType){
			case 'OVERSPEED':
                eventType = '超速报警';
                break;
			case 'OUTBOUND':
                eventType = '越界报警';
                break;
			case 'VEHICLEBACK':
                eventType = '回车报警';
                break;
			case 'VIOLATE':
                eventType = '违规用车报警';
                break;
		}    
        Ext.MessageBox.confirm(messageTitle, messageConent + eventType +' 的报警服务?', function(btn){
            if(btn=='yes'){
                var input={
                    "eventType":rec.data.eventType,
                    "isEnable":isEnable
                };
                
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url: 'vehicleAlert/alarm/config',
                method : 'POST',
                params:{ json:json},
                scope:this,
                success : function(response,options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    if(respText.status == "success"){
                        Ext.Msg.alert('消息提示', '设置成功！');
                    }else{
                        Ext.Msg.alert('消息提示', '设置失败！');
                    }

                    Ext.getCmp("alarmConfigGrid").getStore("alarmConfigResults").load();                 

                    }                
                }); 
            }   
        },this);
    },

});