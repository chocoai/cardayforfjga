Ext.define('Admin.view.systemmgmt.driverAllowanceSetting.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.driverAllowanceSetting.View',
			],
	alias : 'controller.allowanceConfigController',	

 	onAfterrender:function(){
    	this.getViewModel().getStore("allowanceConfigResults").load();
    },
    
    
    editAllowanceConfig : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		
		var win = Ext.widget("editAllowance");
		win.down("form").loadRecord(rec);
		win.show();
		
	},
	
	editClickDone : function(btn) {
		//TODO后台需要参数
		var form=this.getView().down('form').getForm().getValues();
		
		Ext.MessageBox.confirm("补贴配置", '是否确定修改补贴信息?', function(btn){
            if(btn=='yes'){
                var input={
                		"id":form.id,
                        "allowanceName":form.allowanceName,
                        "allowanceValue":form.allowanceValue
                };
                
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url: 'allowance/update',
                method : 'POST',
                params:{ json:json},
                scope:this,
                success : function(response,options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    if(respText.status == "success"){
                    	Ext.getCmp('editAllowance_id').close();
                        Ext.Msg.alert('消息提示', '设置成功！');
                    }else{
                        Ext.Msg.alert('消息提示', '设置失败！');
                    }
                    
                    Ext.getCmp("allowanceConfigGrid").getStore("allowanceConfigResults").load();                 

                    }                
                }); 
            }   
        },this);
	},
	
    modifyAllowanceConfig: function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex); 
        
        Ext.MessageBox.confirm("驾驶员补贴设置", '是否确认修改?', function(btn){
            if(btn=='yes'){
                var input={
                	"id":rec.data.id,
                    "allowanceName":rec.data.eventType,
                    "allowanceValue":isEnable
                };
                
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url: 'allowance/update',
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
                    
                    Ext.getCmp("allowanceConfigGrid").getStore("allowanceConfigResults").load();                 

                    }                
                }); 
            }   
        },this);
    },

});