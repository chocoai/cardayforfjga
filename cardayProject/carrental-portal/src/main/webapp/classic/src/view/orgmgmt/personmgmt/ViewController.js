/**
 * This class is the template view for the application.
 */

function findchildnode(node,nodeId){
	var childnodes = node.childNodes;
	for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
        var rootnode = childnodes[i];
        if(rootnode.data.id == nodeId){
        	rootnode.set('checked', true);
        }else{
        	rootnode.set('checked', false);
        }
        if(rootnode.childNodes.length>0){  //判断子节点下是否存在子节点
            findchildnode(rootnode,nodeId);    //如果存在子节点  递归
        }   
    }
}

Ext.define('Admin.view.orgmgmt.personmgmt.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    alias : 'controller.personmgmtcontroller',
	    onItemClick : function(node, e) {
		var roonodes = Ext.getCmp("personmgmtTreeId").getRootNode();
		findchildnode(roonodes, e.id); // 开始递归

		var url = 'user/' + e.id + '/listByOrgId';
		Ext.Ajax.request({
			url : url,
			method : 'GET',
			defaultHeaders : {
				'Content-type' : 'application/json;utf-8'
			},
			success : function(response, options) {

				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
				// this.getViewModel().getStore("personmgmtReport").loadData(data);
				Ext.getCmp("orgPersonGridId").getStore("personmgmtReport")
						.loadData(data);
			},
//			failure : function() {
//				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//			},
			scope : this
		});

	},

    addPersonClick: function() {
    	var selNodes = Ext.getCmp("personmgmtTreeId").getChecked();
    	if(selNodes.length<1){
    		Ext.Msg.alert('消息提示', '请选择部门！');
    		return;
    	}
    	win = Ext.widget('personView');
		win.show();
    },
    addPersonInfo: function(btn) {
//    	alert('111');
    	var selNodes = Ext.getCmp("personmgmtTreeId").getChecked();
		var departmentId = '';
		var text = new Array();
		for (var i=0; i<selNodes.length; i++) {
			text[i] = selNodes[i].data.id;
//			alert(text[i]);
		}
		var organizationId = selNodes[0].data.id;
    	var userInfo = this.getView().down('form').getForm().getValues();
//    	console.log('username:' + userInfo.username);
//    	console.log('password:' + userInfo.pwd)
//    	console.log('organizationId:' + selNodes[0].data.id);
		var input = {
        		'username': userInfo.username,
        		'password': userInfo.pwd,
        		'roleId': '1',
        		'organizationId': organizationId,
        		'userCategory': '1',
			};
		var json = Ext.encode(input);
	   	 Ext.Ajax.request({
			url : 'user/create',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	       //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
	        	console.log('retStatus:' + retStatus);
				if (retStatus == 'success') {
					btn.up('personView').close();
					Ext.Msg.alert('提示信息','添加成功');
					var url = 'user/' + organizationId + '/listByOrgId';
					Ext.Ajax.request({
						url : url,
						method : 'GET',
						defaultHeaders : {
							'Content-type' : 'application/json;utf-8'
						},
						success : function(response, options) {

							var respText = Ext.util.JSON.decode(response.responseText);
							var data = respText.data;
							// this.getViewModel().getStore("personmgmtReport").loadData(data);
							Ext.getCmp("orgPersonGridId").getStore("personmgmtReport")
									.loadData(data);
						},
//						failure : function() {
//							Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//						},
						scope : this
					});
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
	    });
    },
    searchClick: function() {
    	var userName = this.lookupReference('searchForm').getValues().userName;	
		if (userName == '') {
			Ext.Msg.alert('提示信息', '请输入要查询的用户名称！')
			return;
		}
		
    	
		var input = {
        		'name': userName
			};
		console.log('search name: ' + userName);
//    	this.getViewModel().getStore("personResults").proxy.extraParams = {
//            "json":pram
//        };
		Ext.Ajax.request({
			url : 'user/findByName?json='+ Ext.encode(input),
			method : 'GET',
			defaultHeaders : {
				'Content-type' : 'application/json;utf-8'
			},
			success : function(response, options) {

				var respText = Ext.util.JSON.decode(response.responseText);
				var data = [];
		    	data[0] = respText.data;
		    	console.log('length:' + data.length);
		    	//接口只返回了一条记录
		    	if (data.length == 1) {
		    		Ext.getCmp("orgPersonGridId").getStore("personmgmtReport")
					.loadData(data);
		    	}
			},
//			failure : function() {
//				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//			},
			scope : this
		});
    	console.log('load search data');
    },
    
    querypersonmgmtInfo: function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("queryAndDeletePersonView", {
			title: '查看人员信息',
			buttonAlign : 'center',
			buttons : [{
				text : '关闭',
				handler: 'clickDone'
			}]
		});
		console.dir(win.down("form"));
		win.down("form").loadRecord(rec);
		win.show();
    },
    clickDone: function() {
    	this.view.close();
    },
    deletepersonmgmtInfo: function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("queryAndDeletePersonView", {
			title: '查看人员信息',
			buttonAlign : 'center',
			buttons : [{
				text : '关闭',
				handler: 'clickDone'
			},{
				text: '删除',
				handler: 'delelteFormInfo'
			}]
		});
		console.dir(win.down("form"));
		win.down("form").loadRecord(rec);
		win.show();
    },
    delelteFormInfo: function(grid, rowIndex, colIndex) {
    	Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var userID = grid.getStore().getAt(rowIndex).id;
				console.log('userID:' + userID);
				var url = 'user/'+userID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							//删除成功后，刷新页面
							Ext.getCmp("orgPersonGridId").getStore("personmgmtReport").load();
						}
			        },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});		
			}
		});
    },
    editpersonInfo: function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("personView", {
			title: '修改人员信息',
			buttonAlign : 'center',
			buttons : [{
				text : '关闭',
				handler: 'clickDone'
			},{
				text: '修改',
				handler: 'editFormInfo'
			}]
		});
		console.dir(win.down("form"));
		win.down("form").loadRecord(rec);
		win.show();
    },
    setSexFieldValue: function() {
    	var objectjson = this.getView().down('form').getForm().getValues();
    	if(objectjson.sex=='male') {
    		this.getView().down('form').getForm().findField('sex').setValue('男');
    	}else {
    		this.getView().down('form').getForm().findField('sex').setValue('女');
    	}
    	if(objectjson.vehcle_limit=='non_limit') {
    		this.getView().down('form').getForm().findField('vehcle_limit').setValue('不限');
    	}else{
    		var outputValue = objectjson.month_limit_mileage;
    		this.getView().down('form').getForm().findField('vehcle_limit').setValue('月累计限额' + outputValue + 'Km');
    	}
    	if(objectjson.call_limit=='month_limit') {
    		var outputValue = objectjson.month_limit_amount;
    		this.getView().down('form').getForm().findField('call_limit').setValue('约车月累计限额' + outputValue + '元');
    	}else{
    		this.getView().down('form').getForm().findField('call_limit').setValue('不限');
    	}
    	//公车性质
    	var carTypeItem = objectjson.use_car_type;  
        var carTypeTemp =  carTypeItem.split(",");
        var useCarType = '';
        for(var i=0;i<carTypeTemp.length;i++) {
        	var tempParam = carTypeTemp[i];
        	if(tempParam == 'jjx') {
        		useCarType += '应急机要通信接待用车(5人座),'
        	}else if(tempParam == 'ssx') {
        		useCarType += '行政执法用车(5人座),'
        	}else if(tempParam == 'swx') {
        		useCarType += '行政执法特种专业用车(7人座),'
        	}else if(tempParam == 'hhx') {
        		useCarType += '一般执法执勤用车(5人座),'
        	}
        }
        if(useCarType.length>0) {
        	useCarType = useCarType.substring(0,useCarType.length-1);
        }
        this.getView().down('form').getForm().findField('use_car_type').setValue(useCarType);
        //用车范围
        var cbgItem = objectjson.car_range;  
        var temp =  cbgItem.split(",");
        var useCarRange = '';
        for(var i=0;i<temp.length;i++) {
        	var tempParam = temp[i];
        	if(tempParam == 'haved') {
        		useCarRange += '已有车辆,'
        	}else if(tempParam == 'sq') {
        		useCarRange += '首汽约车,'
        	}
        }
        if(useCarRange.length>0) {
        	useCarRange = useCarRange.substring(0,useCarRange.length-1);
        }
        this.getView().down('form').getForm().findField('car_range').setValue(useCarRange);
//        alert('您选择的是' + cbgItem);  
    },
    editFormInfo: function() {
    	var objectjson = this.getView().down('form').getForm().getValues();
		var name = objectjson.username;
		var sex = objectjson.sex;
		var phone_number = objectjson.phone_number;
		var pwd = objectjson.pwd;
		var Email = objectjson.Email;
		var vehcle_limit = objectjson.vehcle_limit;
		var call_limit = objectjson.call_limit;
		var use_car_type = objectjson.use_car_type;
		var car_range = objectjson.car_range;
		var organizationId = '2';
		
		alert(name + ';' + sex + ';' + phone_number + ';' + pwd + ';' + Email);
		alert(vehcle_limit + ';' + call_limit + ';' + use_car_type + ';' + car_range);
		
		var cbgItem = objectjson.use_car_type;  
//        Ext.Array.each(cbgItem, function(item){  
//            itcIds = itcIds + ',' + item.inputValue;  
//        });  
      
        Ext.MessageBox.alert('提示', '您选择的是' + cbgItem);  
        
        //start call interface
        
        var input = {
        		'id': '17',
        		'username': name,
        		'password': pwd,
        		'roleId': '1',
        		'organizationId': organizationId,
        		'userCategory': '1',
			};
		var json = Ext.encode(input);
	   	 Ext.Ajax.request({
			url : 'user/update',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
	        	console.log('retStatus:' + retStatus);
				if (retStatus == 'success') {
//					btn.up('personView').close();
					Ext.Msg.alert('提示信息','添加成功');
					var url = 'user/' + organizationId + '/listByOrgId';
					Ext.Ajax.request({
						url : url,
						method : 'GET',
						defaultHeaders : {
							'Content-type' : 'application/json;utf-8'
						},
						success : function(response, options) {

							var respText = Ext.util.JSON.decode(response.responseText);
							var data = respText.data;
							// this.getViewModel().getStore("personmgmtReport").loadData(data);
							Ext.getCmp("orgPersonGridId").getStore("personmgmtReport")
									.loadData(data);
						},
//						failure : function() {
//							Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//						},
						scope : this
					});
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
	    });
        
        //end call interface
		
		//重新加载store
		
		this.view.close();
    }
});

