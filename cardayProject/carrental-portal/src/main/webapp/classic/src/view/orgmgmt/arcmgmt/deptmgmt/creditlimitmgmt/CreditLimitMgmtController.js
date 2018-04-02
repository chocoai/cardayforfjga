Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.CreditLimitMgmtController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],

    onActivateCreditLimit: function(){
    	Ext.getCmp('gridCreditLimitMgmt').getStore().load();
    },

    beforeEditforCreditLimit: function(editor,e) {
        if(e.rowIdx == 0){
            return false;
        }else{
            return true;
        }
    },

    editforCreditLimit:function(editor,context,eOpts){
        //var availableCredit = context.record.data.availableCredit;
        var deptId = context.record.data.id;
        var originalCredit,availableCredit;

        /*获取部门的初始额度*/
		Ext.Ajax.request({
        	url:'department/findOrgById/'+ deptId,
			method:'GET',
			async: false,
			success: function(response){
				var respText = Ext.util.JSON.decode(response.responseText);
    			if (respText.status == 'success') {
    				if(respText.data.limitedCredit == null){
    					originalCredit = 0;
    				}else{
						originalCredit = respText.data.limitedCredit;
    				}

                    if(respText.data.availableCredit == null){
                        availableCredit = 0;
                    }else{
                        availableCredit = respText.data.availableCredit;
                    }
    			}
			 },
			failure : function() {
				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
			},
        });
        //originalCredit = 2000;
        var currentCredit = context.value;
        var parentCredit = context.record.parentNode.data.limitedCredit;
        var childNodes = context.record.parentNode.childNodes;
        var exceptCredit = 0;
        for(var i = 0; i < context.record.parentNode.childNodes.length; i++){
            if(context.record.parentNode.childNodes[i].data.id != deptId){
                exceptCredit += parseInt(context.record.parentNode.childNodes[i].data.limitedCredit);
            }
        }
        var regu = /^\+?(0|[1-9][0-9]*)$/;

        var minValue = parseInt(originalCredit)-parseInt(availableCredit);
        var maxValue = parseInt(parentCredit)-parseInt(exceptCredit);
        if(!regu.test(currentCredit)){
			Ext.Msg.alert('消息提示', '请输入正确的用车额度 ！');
            Ext.getCmp('saveCreditlimit').disable();
        }else if(parseInt(currentCredit) < minValue){
        	Ext.Msg.alert('消息提示', '请注意，' + context.record.data.text + ' 用车额度应不小于 '+ minValue +' ！');
            Ext.getCmp('saveCreditlimit').disable();
        }else if(parseInt(currentCredit) > maxValue){        	
        	Ext.Msg.alert('消息提示', '请注意，' + context.record.data.text + ' 用车额度应不大于 '+ maxValue +' ！');
            Ext.getCmp('saveCreditlimit').disable();
        }else{
            var rootNode = Ext.getCmp('gridCreditLimitMgmt').getStore().root.childNodes[0];
            rootNode.set('availableCredit',maxValue - parseInt(currentCredit));
            rootNode.childNodes[context.rowIdx -1].set('availableCredit',parseInt(currentCredit) - minValue);
        	Ext.getCmp('saveCreditlimit').enable();
        }
    },

    saveCreditlimit: function() {
    	var creditLimitArray = new Array();
    	var rootValue = Ext.getCmp('gridCreditLimitMgmt').getStore().root.childNodes[0];
    	var creditLimit = new Object();
    	creditLimit.orgId = rootValue.data.id; 	
    	creditLimit.limitedCredit = parseInt(rootValue.data.limitedCredit);	
    	creditLimit.availableCredit = parseInt(rootValue.data.availableCredit);
    	creditLimitArray.push(creditLimit);
    	for(var i = 0; i < rootValue.childNodes.length; i++){
	    	var creditLimitChildNode = new Object();
	    	creditLimitChildNode.orgId = rootValue.childNodes[i].data.id;  
	    	creditLimitChildNode.limitedCredit = parseInt(rootValue.childNodes[i].data.limitedCredit);	
	    	creditLimitChildNode.availableCredit = parseInt(rootValue.childNodes[i].data.availableCredit);
	    	creditLimitArray.push(creditLimitChildNode);
    	}

		var json = Ext.encode(creditLimitArray);
    	Ext.Ajax.request({
        	url:'department/updateCredit',
			method:'POST',
	        params:{json:json},
			async: false,
			success: function(response){
				var respText = Ext.util.JSON.decode(response.responseText);
    			if (respText.status == 'success') {
        			Ext.Msg.alert('消息提示', '用车额度修改成功');
    				Ext.getCmp('gridCreditLimitMgmt').getStore().load();
    			}else{    				
        			Ext.Msg.alert('消息提示', '用车额度修改失败，请重新输入');
    				Ext.getCmp('gridCreditLimitMgmt').getStore().load();
    			}
			 },
			failure : function() {
				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
			},
        });
    },

    cancelCreditlimit: function() {
    	this.onActivateCreditLimit();
    	Ext.getCmp('saveCreditlimit').enable();
    }
})