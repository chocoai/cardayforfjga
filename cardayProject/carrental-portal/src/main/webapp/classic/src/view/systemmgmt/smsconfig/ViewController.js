Ext.define('Admin.view.systemmgmt.smsconfig.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.smsconfig.View',
			],
	alias : 'controller.smsconfigController',	

 	onAfterrender:function(){
    	this.getViewModel().getStore("smsconfigResults").load();
    },

    modifysmsconfig: function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex); 
        var isEnable,eventType;
        var messageTitle,messageConent;
        if(rec.data.enable){
            isEnable = false;
            messageTitle = '关闭短信提醒';
            messageConent = '确定是否关闭 ';
        }else{
            isEnable = true;
            messageTitle = '开启短信提醒';
            messageConent = '确定是否开启 ';
        }  

        switch(rec.data.eventType){
			case 'ALLOCATE':
                eventType = '分配车辆';
                break;
		}    
        Ext.MessageBox.confirm(messageTitle, messageConent + eventType +' 的短信提醒服务?', function(btn){
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

                    Ext.getCmp("smsconfigGrid").getStore("smsconfigResults").load();                 

                    }                
                }); 
            }   
        },this);
    },

    openConfigTemplate: function(grid, rowIndex, colIndex){
    	var rec = grid.getStore().getAt(rowIndex);
    	var moduleName = rec.data.eventType;
    	
    	var win = Ext.create("Admin.view.systemmgmt.smsconfig.SmsTemplateConfig");
    	win.down("form").getForm().findField("moduleName").setValue(moduleName);
    	win.down("form").getForm().findField("moduleNameDisplay").setValue(moduleName);
    	win.show();
    	var input = {moduleName: moduleName};
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
            url: 'sms/template/query',
            method : 'GET',
            params:{ json:json},
            scope:this,
            success : function(response,options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                if(respText.status == "success"){
                	if(respText.data != null){
//                		win.down("form").getForm().findField("moduleName").setValue(respText.data.moduleName);
                		if(respText.data.smsContent != null && respText.data.smsContent != 'null'){
                			win.down("form").getForm().findField("smsContent").setValue(respText.data.smsContent);
                		}
                	}
                }
            } 
        });
    },
    
    configSmsTemplate: function(btn){
    	var form = btn.up("window").down("form");
    	var input = form.getValues();
    	if(input.smsContent == null || input.smsContent.trim() == ""){
    		Ext.Msg.alert("消息提示", "短信模板不能为空");
    		return;
    	}
    	
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
            url: 'sms/template/config',
            method : 'POST',
            params:{ json:json},
            scope:this,
            success : function(response,options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                if(respText.status == "success"){
                	Ext.Msg.alert("消息提示", "短信模板配置成功");
                	btn.up("window").close();
                }
            } 
        });
    }
});