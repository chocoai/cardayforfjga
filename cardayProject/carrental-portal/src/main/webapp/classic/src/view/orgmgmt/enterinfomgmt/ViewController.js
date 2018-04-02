/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfomgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.orgmgmt.enterinfomgmt.SearchForm',
			'Admin.view.orgmgmt.enterinfomgmt.Grid',
			'Admin.view.orgmgmt.enterinfomgmt.EditAdmin'
			],
	init: function(view) {
        this.roleType = window.sessionStorage.getItem('userType');
        this.getViewModel().set('status',this.roleType);

    },
/**
 * 查看企业信息
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	checkEnterInfo : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkEnterInfo');
		/*if(rec.get('comments')!=""){
			var comm=Ext.create('Ext.form.DisplayField',{
				fieldLabel: '驳回原因',
	       	 	name: 'comments'
			});
			win.down("form").add(comm);
		}*/
		//padding: 100px
		if(rec.get('status')=="3"){
			var comm=Ext.create('Ext.form.field.TextArea',{
        		border: false,
				name      : 'comments',
        		fieldLabel: '驳回原因',
        		readOnly:true,
	       	 	value:rec.get('comments')
			});
			win.down('form').add(comm);
		}
		
		win.down("form").loadRecord(rec);
		win.show();
    },
    
/**
 * 添加企业
 * @param {} btn
 */
	addEnterInfo : function(btn){
		var enterinfo = this.lookupReference('addEnterInfo').getValues();	
		if (this.lookupReference('addEnterInfo').getForm().isValid()) {
 			var input = {
				"name" 			: enterinfo['name'],
				"shortname" 	: enterinfo['shortname'],
				"linkman" 		: enterinfo['linkman'],
				"linkmanPhone" 	: enterinfo['linkmanPhone'],
				"linkmanEmail" 	: enterinfo['linkmanEmail'],
				"vehileNum" 	: enterinfo['vehileNum'],
				"city" 			: enterinfo['city'],
				"startTime" 	: enterinfo['startTime'],
				"endTime" 		: enterinfo['endTime'],
				"address" 		: enterinfo['address'],
				"introduction" 	: enterinfo['introduction']
			};
			var json = Ext.encode(input);
	        Ext.Ajax.request({
	        	url:'organization/appendChild',//?json='+Ext.encode(input),
				method:'POST',
	        	params:{json:json},
				//headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						btn.up('addEnterInfo').close();
				 		Ext.Msg.alert("提示信息", '添加成功');
				 		Ext.getCmp("enterId").getStore('usersResults').load();
					}else{
						Ext.MessageBox.alert("提示信息","添加失败");
					}
				 	
				 }
	        	/*,
				failure : function() {
					Ext.Msg.alert('Failure','Call interface error!');
				}*/
	        });
		 }else{
		 	Ext.Msg.alert('消息提示', '输入的信息格式不正确');
		 }

	},
	
/**
 * 修改企业信息
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	editEnterInfo : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var me=this;
		var win = Ext.widget("addEnterInfo", {
			title: '修改企业信息',
			buttonAlign : 'center',
			buttons : [{
				text: '修改',
				handler:function(btn){
					var enterinfo=btn.up('addEnterInfo').down('form').getForm().getValues();
					btn.up("addEnterInfo").close();
					me.updateEnterInfo(rec,enterinfo,grid);
				}
			},{
				text : '关闭',
				handler: function(btn){
					btn.up("addEnterInfo").close();
				}
			}]
			
		});
		var input = {
						"id":rec.get('id')
					};
					
		Ext.Ajax.request({
        	url:'organization/findById?json='+Ext.encode(input),
			method:'GET',
			headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var org=Ext.JSON.decode(res.responseText);
				if(org.status=='success'){
					win.down("form").getForm().setValues(org.data);
					win.show();
				}else{
				}
			 	
			 }
			/*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
        });
	},
	
/**
 * 更新企业信息
 * @param {} rec
 * @param {} enterinfo
 * @param {} grid
 */
	updateEnterInfo: function(rec,enterinfo,grid){	
		var input = {
						"id"  			 : rec.get('id'),
						"name" 			 : enterinfo['name'],
						"shortname" 	 : enterinfo['shortname'],
						"linkmanUsername": enterinfo['linkmanUsername'],
						"linkman" 		 : enterinfo['linkman'],
						"linkmanPhone" 	 : enterinfo['linkmanPhone'],
						"linkmanEmail" 	 : enterinfo['linkmanEmail'],
						"vehileNum" 	 : enterinfo['vehileNum'],
						"city" 			 : enterinfo['city'],
						"startTime" 	 : enterinfo['startTime'],
						"endTime" 		 : enterinfo['endTime'],
						"address" 		 : enterinfo['address'],
						"introduction" 	 : enterinfo['introduction']
					};
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url:'organization/'+rec.get('id')+'/update',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
            success: function(response) {
                var data=Ext.JSON.decode(response.responseText);
               		Ext.Msg.alert("提示信息", '修改成功');
               		grid.getStore('usersResults').load();
//				 	rec.set("name",enterinfo['name']);
//				 	grid.getStore().commitChanges();
//				 	rec.set("");暂时只有姓名可以更改
            }
	        /*,
            failure : function() {
						Ext.Msg.alert('Failure','Call interface error!');
			}	*/
		});
	},
	
/**
 * 查询企业
 * @param {} btn
 */
	onSearchClick : function(btn){
		var enterinfo = this.lookupReference('searchEnterInfo').getValues();
		var me=this;
		var compName=enterinfo['name'];
		if(null==compName||''==compName){
			Ext.Msg.alert('消息提示','请输入要查询的公司名称');
			return;
		}
		var input={
			"name":compName
		};
		Ext.Ajax.request({
	   		url: 'organization/listByOrgName?json='+Ext.encode(input),
	        method : 'GET',
	        headers : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
				me.getViewModel().getStore('usersResults').loadData(data)
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
 		});
	},
		
		
	/*	var input={
			"name":compName
		};
		this.getViewModel().getStore('usersResults').proxy.url='organization/listByOrgName';
		this.getViewModel().getStore('usersResults').proxy.extraParams = {
			"json" : Ext.encode(input)
		};
		this.getViewModel().getStore('usersResults').load();
	},*/
	
/**
 * 删除企业
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	delEnterInfo:function(grid, rowIndex, colIndex){
		var id=grid.getStore().getAt(rowIndex).get('id');
		var msg=grid.getStore().getAt(rowIndex).get('name')+'?';
		Ext.Msg.confirm('提示信息','确认删除企业:'+msg,function(btn){
			if (btn == 'yes') {
				Ext.Ajax.request({
					url:'organization/'+id+'/delete',
					method:'POST',
					headers: {'Content-Type':'application/json','charset':'UTF-8'},
		            success: function(response) {
		                var data=Ext.JSON.decode(response.responseText);
		                if(data.status=='success'){
		          			grid.getStore().removeAt(rowIndex);
		                }else{
		                    Ext.MessageBox.alert("提示信息","Operation failed!");
		                }
		            }
					/*,
		            failure : function() {
								Ext.Msg.alert('Failure','Call interface error!');
							}*/
					});
			}
		});
	},
	
// 渲染View,加载数据
	onView:function(){
		this.getViewModel().getStore('usersResults').load();
	},
	
	
//渲染form,加载combo下拉列表
	onComboRender :function(c){
		var enterId=c.ownerCt.getComponent('enterId').value;
		var input={
					'orgId':enterId
					};
		
         c.getStore().proxy.extraParams = {
			"json" : Ext.encode(input)
		 };
		 /*var comstore=Ext.create('Ext.data.Store', {
     		model: 'Admin.model.enterinfo.AdminUser',
     		proxy: {
	         	type: 'ajax',
	         	url: 'user/listOrgAdminListByOrgId',
	         	extraParams:{'json':Ext.encode(input)},
	     		reader: {
		        	type: 'json',
		         	rootProperty: 'data',
		         	successProperty: 'status'
	     		}
     		},
         	autoLoad: false 
         });*/
	},

	onSelect:function(combo,rec){
		console.log(combo);
		combo.up('form').getForm().findField('linkmanPhone').setValue(rec.get('phone'));
		combo.up('form').getForm().findField('linkmanEmail').setValue(rec.get('email'));
		combo.up('form').getForm().findField('username').setValue(rec.get('username'));
		
	},
/*	comboChange:function(){
		alert('值改变');
	
	},*/
	onEditAdmin:function(btn){
		alert('进入');
		var admin=btn.up('editAdmin').down('form').getForm().getValues();
		
		var input = {
						"id"  			: admin['id'],//企业Id号
						"username"		: admin['username'],
						"realname"      : admin['realname'],
						"phone" 		: admin['linkmanPhone'],
						"email" 		: admin['linkmanEmail']
					};
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url:'organization/changeOrgAdmin',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
            success: function(response) {
                var data=Ext.JSON.decode(response.responseText);
                if(data.status=='success'){
          			btn.up('editAdmin').close();
	 				Ext.Msg.alert("提示信息", '修改成功');
	 				Ext.getCmp("enterId").getStore('usersResults').load();
                }else{
                    Ext.MessageBox.alert("提示信息","Operation failed!");
                }
            }
	        /*,
            failure : function() {
						Ext.Msg.alert('Failure','Call interface error!');
					}*/
		});
		
	},
		//重新提交
	reSubmit:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		Ext.Msg.confirm('提示信息','确认重新审核'+rec.get('name')+'企业',function(btn){
			if (btn == 'yes') {
				var input = {
								"id" : rec.get('id'),//企业Id号
								"status":'0'
							};
				var json = Ext.encode(input);
				Ext.Ajax.request({
					url:'organization/audit/check',//?json='+Ext.encode(input),
					method:'POST',
	        		params:{json:json},
					//headers: {'Content-Type':'application/json','charset':'UTF-8'},
		            success: function(response) {
		                var data=Ext.JSON.decode(response.responseText);
		                if(data.status=='success'){
			 				Ext.getCmp("enterId").getStore('usersResults').load();
		                }else{
		                    Ext.MessageBox.alert("提示信息","Operation failed!");
		                }
		            }
	        		/*,
		            failure:function() {
							Ext.Msg.alert('Failure','Call interface error!');
					}*/
				});
			}
		});
	},
//停止服务
	stopServiceWindow:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var htmlContent='';
		htmlContent+=rec.get('name')+'的企业用车服务期限为:<br>';
		htmlContent+= Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'—'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d")+'<br>';
		htmlContent+='停止服务后,'+rec.get('name')+'的所有用户将无法登陆系统,确认停止服务？'
		var me=this;
		var win = Ext.widget("openServiceWindow", {
			title: '停止服务',
			buttonAlign : 'center',
			buttons : [{
				text: '停止',
				handler:this.stopService
			},{
				text : '取消',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
			
		});
		win.down('container').html=htmlContent;
		win.down('hidden').setValue(rec.get('id'));
		win.show();
		
	},
	stopService:function(btn){
		var enterId=btn.up('window').down('hidden').getValue();
		var input = {
						"id" : enterId,//企业Id号
						"status":'1'
					};
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url:'organization/service/terminate',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
            success: function(response) {
                var data=Ext.JSON.decode(response.responseText);
                if(data.status=='success'){
          			btn.up('window').close();
	 				Ext.getCmp("enterId").getStore('usersResults').load();
                }else{
                    Ext.MessageBox.alert("提示信息","Operation failed!");
                }
            }
	        /*,
            failure : function() {
						Ext.Msg.alert('Failure','Call interface error!');
			}*/
		});
	},
//续约服务
	resumeServiceWindow:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('resumeServiceWindow');
		var htmlContent='';
		htmlContent+=rec.get('name')+'的企业用车服务期限为:<br>';
		htmlContent+= Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'—'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d")+'<br>';
		htmlContent+='目前服务已到期，需设置新的服务期限';
		win.down('form').down('container').html=htmlContent;
		win.down('form').getForm().findField('id').setValue(rec.get('id'));
		win.show();
	},
	resumeService:function(btn){
		var resumeServiceForm=this.getView().down('form').getForm().getValues();
		resumeServiceForm.status='4';
		var json = Ext.encode(resumeServiceForm);
		Ext.Ajax.request({
			url:'organization/service/activate',//?json='+Ext.encode(resumeServiceForm),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
            success: function(response) {
                var data=Ext.JSON.decode(response.responseText);
                if(data.status=='success'){
          			btn.up('window').close();
	 				Ext.getCmp("enterId").getStore('usersResults').load();
                }else{
                    Ext.MessageBox.alert("提示信息","Operation failed!");
                }
            }
	        /*,
            failure : function() {
						Ext.Msg.alert('Failure','Call interface error!');
					}*/
		});
	},
//开通服务
	openServiceWindow:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var htmlContent='';
		htmlContent+=rec.get('name')+'的企业用车服务期限为:<br>';
		htmlContent+= Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'—'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d")+'<br>';
		htmlContent+='确认开通服务?'
		var win = Ext.widget('openServiceWindow');
		win.down('container').html=htmlContent;
		win.down('hidden').setValue(rec.get('id'));
		win.show();
	},
	openService:function(btn){
		var enterId=this.getView().down('hidden').getValue();
		var input = {
						"id" : enterId,//企业Id号
						"status":'4'
					};
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url:'organization/service/activate',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
            success: function(response) {
                var data=Ext.JSON.decode(response.responseText);
                if(data.status=='success'){
          			btn.up('window').close();
	 				Ext.getCmp("enterId").getStore('usersResults').load();
                }else{
                    Ext.MessageBox.alert("提示信息","Operation failed!");
                }
            }
	        /*,
            failure : function() {
						Ext.Msg.alert('Failure','Call interface error!');
					}*/
		});
	},
//点击添加按钮，弹出添加企业信息窗口
	onAddClick : function() {
		var win = Ext.widget('addEnterInfo');
		win.show();
	},
	
	editAdmin : function(grid, rowIndex, colIndex){
	var rec = grid.getStore().getAt(rowIndex);
	var win = Ext.widget('editAdmin');
		win.down("form").loadRecord(rec);
	//	win.down("form").getForm().findField('orgId').setValue(rec.get('id'));
	//  win.down("form").getForm().setValues({'id':rec.get('id')});
		win.show();
	}
});
