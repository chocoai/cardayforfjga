/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],
	init: function(view) {
        this.roleType = window.sessionStorage.getItem('userType');
        this.getViewModel().set('roleType',this.roleType);
    },
    onBeforeLoad:function(){
    	
    	var frmValues=this.lookupReference('searchForm').getValues();
    	if(''==frmValues['selfDept']||null==frmValues['selfDept']){
    		frmValues['selfDept']=0;
    	}
    	if(''==frmValues['childDept']||null==frmValues['childDept']){
    		frmValues['childDept']=0;
    	}
/*    	if(''==frmValues['fromOrgId']||null==frmValues['fromOrgId']){
    		frmValues['fromOrgId']=-1;
    	}*/
    	
    	var page=Ext.getCmp('vehiclePage').store.currentPage;
    	
		var limit=Ext.getCmp('vehiclePage').pageSize;
		frmValues.currentPage=page;
		frmValues.numPerPage=limit;
		this.getViewModel().getStore("VehicleResults").proxy.extraParams = {
             "json":Ext.encode(frmValues)
        }
		
    },
    changeFromOrgIdValue:function(combo,record){
    	if(record.get('id')!=-1){
    		/*var fromOrgIdCombo=combo.up("form").getForm().findField('fromOrgId')
    		var fromOrgIdComboStore=fromOrgIdCombo.getStore();
    		var obj = fromOrgIdComboStore.findRecord(fromOrgIdCombo.valueField, -1); 
			fromOrgIdCombo.select(obj);*/
    		//两者效果一样
    		var fromOrgIdCombo=combo.up("form").getForm().findField('fromOrgId')
    		fromOrgIdCombo.setValue(-1);
    	}
    },
	onSearchClick:function(){
		var VehicleStore = this.lookupReference('gridvehicleinfo').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("VehicleResults").load();
	},
	
	//打开查看车辆信息窗体
	viewVehicleInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.ViewVehicleInfo');
		var objectModel = new Ext.data.Model();
		var data={
				'vehicleNumber':rec.data.vehicleNumber
		}
		var json = Ext.encode(data);
		Ext.Ajax.request({
        	url:'vehicle/findVehicleInfoByVehicleNumber',//?json='+Ext.encode(vehiclInfo),
			method:'POST',
	        params:{json:json},
			success: function(response){
				var respText = Ext.util.JSON.decode(response.responseText);
    	    	var data = respText.data;
            	var retStatus = respText.status;
    			if (retStatus == 'success') {
    				objectModel.data = data;
    				
    				//将未分配改为企业名显示
    				if (objectModel.data.arrangedOrgName=="未分配") {
//    					data.depName = '暂未分配';
    					var userType = window.sessionStorage.getItem('userType');
    					if(userType == '2' || userType == '6'){
    			      		//2是用车企业 6是租车企业
    			      		//3是部门
    			        	var entName = window.sessionStorage.getItem("organizationName");
    			        	objectModel.data.arrangedOrgName = entName;
    			      	}
    				}
    				
    				win.down("form").loadRecord(objectModel);
    				win.show();
    			}
			 }
	        /*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
        });
	},
	//打开修改车辆信息窗体
	updateVehicleInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		//分离排量数值和单位
		if(rec.data.vehicleOutput && /[T|L]$/.test(rec.data.vehicleOutput)){
			//获取排量单位
			rec.data.vehicleOutputUnit = rec.data.vehicleOutput.substring(rec.data.vehicleOutput.length-1);
			//获取排量数值
			rec.data.vehicleOutput = parseFloat(rec.data.vehicleOutput).toFixed(1);
		}
		if (rec.data.arrangedOrgName=="未分配") {
//			data.depName = '暂未分配';
			var userType = window.sessionStorage.getItem('userType');
			if(userType == '2' || userType == '6'){
	      		//2是用车企业 6是租车企业
	      		//3是部门
	        	var entName = window.sessionStorage.getItem("organizationName");
	        	rec.data.arrangedOrgName = entName;
	      	}
		}
		
		var win=Ext.create("Admin.view.vehiclemgmt.vehicleInfomgmt.EnterModifyVehicle");
		win.down('form').loadRecord(rec);
		win.show();
	},
	modifyVehicle:function(btn){
		var vehiclInfo = this.lookupReference('enterModifyVehicle').getValues();
		//如果输入排量值
		vehiclInfo.vehicleOutput && (vehiclInfo.vehicleOutput += vehiclInfo.vehicleOutputUnit);
		var json = Ext.encode(vehiclInfo);
		Ext.Ajax.request({
        	url:'vehicle/update',//?json='+Ext.encode(vehiclInfo),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
			 		btn.up('window').close();
				    Ext.getCmp("searchVehicle").fireEvent("click");
			 		Ext.Msg.alert("提示信息", '修改成功');
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
	//分配企业窗体
	openVehicleWindow:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var vehicleNo=rec.get('vehicleNumber');
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.VehicleAllocation',{
		//	title:'<span style="font-size:26px">分配车辆-'+vehicleNo+'</span>'
			title:'分配车辆-'+vehicleNo
		});
//		if (rec.get('arrangedOrgName') == '未分配') {
//			rec.data.arrangedOrgName = '暂不分配';
//			rec.data.arrangedOrgId = -1;
//		}
//		win.down("form").loadRecord(rec);
		win.down('form').getForm().findField('vehicleId').setValue(rec.get('id'));
		win.down('form').down('[name=allocTbtext]').text='分配到所属企业';
		win.down('form').down('[name=arrangedOrgName]').getStore().proxy.url = 'vehicle/listAvailableAssignedEnterprise';
	//	win.down('form').down('[name=arrangedOrgName]').setValue(rec.get('arrangedOrgName'));
		win.show();
	//	win.down('form').down('[itemId=itemVehicle]').show();
	//	win.down('form').down('[itemId=itemOrg]').hide();
	},
	
	//分配部门窗体
	openVehicleDeptWindow:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var vehicleNo=rec.get('vehicleNumber');
//		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.VehicleAllocation',{
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.DeptAllocation',{
			title:'分配车辆-'+vehicleNo
		});
		if (rec.get('arrangedOrgName') == '未分配') {
			var userType = window.sessionStorage.getItem('userType');
			if(userType == '2' || userType == '6'){
	        	//如果选中根节点，且目前是企业或者租车公司
	      		//2是用车企业 6是租车企业
	      		//3是部门
	        	var entName = window.sessionStorage.getItem("organizationName");
	        	rec.data.arrangedOrgName = entName;
				rec.data.arrangedOrgId = -1;
	      	}
//			rec.data.arrangedOrgName = '暂不分配';
//			rec.data.arrangedOrgId = -1;
		}
	//	win.down("form").loadRecord(rec);
		var deptId = rec.get('arrangedOrgId');
		var deptName = rec.get('arrangedOrgName');
		win.down('form').getForm().findField('vehicleId').setValue(rec.get('id'))
        	win.down('form').down('[name=allocTbtext]').text='分配到所属部门';
			win.down('form').down('[name=arrangedOrgId]').setValue(deptId);
			win.down('form').down('[name=arrangedOrgName]').setValue(deptName);
		
//        	win.down('form').down('[name=arrangedOrgName]').getStore().proxy.url = 'organization/findLowerLevelOrgList';
//			win.down('form').down('[itemId=itemVehicle]').hide();
//			win.down('form').down('[itemId=itemOrg]').show();
	//		win.down('form').down('[itemId=itemOrg]').setValue(rec.get('arrangedOrgName'));
		win.show();
		
	},
	
	//分配司机
	openArrangeDriverWin : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		if (rec.data.stationName=="" || rec.data.stationName==null) {
			Ext.Msg.alert('提示消息','该车辆尚未分配到站点，无法分配默认司机');
			return;
		}		
		var vehicleNo=rec.get('vehicleNumber');
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.DriverAllocation',{
			title:'分配默认司机-'+vehicleNo
		});
		Ext.getCmp('driver_allocate_id').setValue(rec.data.id);
		win.show();
	},
	
	//选择车辆分配部门，设置部门Id
	alloSelectOrgDone : function(items) {
		var orgId = items.getValue();
		Ext.getCmp('arranged_Org_Id').setValue(orgId);
	},
	//车辆回收
	withDrawVehicle:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var vehicleId=rec.get('id');
		var input = {
			'vehicleId': vehicleId
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	        	url:'vehicle/vehicleRecover',//?json='+Ext.encode(input),
				method:'POST',
        		params:{json:json},
				//headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
					    Ext.getCmp("searchVehicle").fireEvent("click");
						Ext.Msg.alert("提示信息", '回收成功');
					}else{
						Ext.Msg.alert("提示信息", "回收失败");
					}
				 }
        		/*,
				failure : function() {
					Ext.Msg.alert('Failure','Call interface error!');
				}*/
	        });
		
	},
	
	//分配车辆到企业或部门
	onAllocateVehicleClick:function(btn){
		var allocateVehicleInfo=btn.up('window').down('form').getValues();
		var input = {
			'orgId': allocateVehicleInfo.arrangedOrgName, //分配的机构Id
		//	'orgId': allocateVehicleInfo.arrangedOrgId, //分配的机构Id
			'orgName': allocateVehicleInfo.orgName,		//分配的名称
			'vehicleId': allocateVehicleInfo.vehicleId  //分配的车辆id
		};
		var userType = this.getViewModel().get('roleType');
		var urls='vehicle/vehicleAssigne';
		var json = Ext.encode(input);
		Ext.Ajax.request({
        	url:urls,//+'?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
					btn.up('window').close();
					Ext.getCmp("searchVehicle").fireEvent("click");
			 		Ext.Msg.alert("提示信息", '分配成功');
				}else{
					Ext.MessageBox.alert("提示信息","分配失败");
				}
			 	
			 }
	        /*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
	    });
		   	
	},
	
	//车辆分配司机
	onAllocateDriverClick : function(btn) {
		var allocateVehicleInfo=btn.up('window').down('form').getValues();
		var input = {
			'driverId': allocateVehicleInfo.arrangedDriver,//分配司机id
			'vehicleId': allocateVehicleInfo.vehicleId  //分配的车辆id
		};
		
		var json = Ext.encode(input);
		Ext.Ajax.request({
        	url:'vehicle/driverAllocate',
			method:'POST',
	        params:{json:json},
			success: function(res){
				var respData=Ext.JSON.decode(res.responseText);
				if(respData.status=='success'){
					btn.up('window').close();
					Ext.getCmp("searchVehicle").fireEvent("click");
			 		Ext.Msg.alert("提示信息", '分配成功');
				}else{
					Ext.MessageBox.alert("提示信息","分配失败");
				}
			 	
			 },
			failure : function() {
				Ext.Msg.alert('提示信息', '调用接口出错!');
			}
	    });
		
	},
	
	//打开添加车辆信息窗体
	addVehicle:function(grid, rowIndex, colIndex){
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.EnterAddVehicle');
		win.show();
	},
	//企业管理员添加车辆信息
	EnterAddVehicleClick:function(btn){
		var enterinfo = this.lookupReference('enterAddVehicleInfo').getValues();
		//如果输入排量值
		enterinfo.vehicleOutput && (enterinfo.vehicleOutput += enterinfo.vehicleOutputUnit);
		if (this.lookupReference('enterAddVehicleInfo').getForm().isValid()) {
			var json = Ext.encode(enterinfo);
			Ext.Ajax.request({
	        	url:'vehicle/createVehicle',//?json='+Ext.encode(enterinfo),
				method:'POST',
	        	params:{json:json},
				//headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						btn.up('window').close();
					    Ext.getCmp("searchVehicle").fireEvent("click");
						Ext.Msg.alert("提示信息", '添加成功');
					}else{
						Ext.Msg.alert("提示信息", appendData.error);
					}
				 }
	        	/*,
				failure : function() {
					Ext.Msg.alert('Failure','Call interface error!');
				}*/
	        });
		}

	},
	openFileUpWindow:function(){
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.FileUpLoad');
		win.show();
	},
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'vehicle/importCSV',
                method:'post',
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                    btn.up('window').close();
            		Ext.getCmp("searchVehicle").fireEvent("click");
        		 	Ext.MessageBox.show({
	                    title: '消息提示',
	                    msg: action.result.msg,
	                    icon: Ext.MessageBox.INFO,
	                    buttons: Ext.Msg.OK
                	});
                }
            });
			
		}
	},
	
	deleteVehicle:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var input={
			id:rec.get('id')
		};
		Ext.Msg.confirm('提示信息','确认删除车辆:'+rec.get('vehicleNumber'),function(btn){
			if (btn == 'yes') {
				var json = Ext.encode(input);
				Ext.Ajax.request({
		    	url:'vehicle/delete',//?json='+Ext.encode(input),
				method:'POST',
	        	params:{json:json},
				//headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						Ext.getCmp("searchVehicle").fireEvent("click");
			 			Ext.Msg.alert("提示信息", '删除成功');
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
	
	getCityByprovince: function(obj,newValue,oldValue){	
		  var input={
				  parentId:newValue
				};
		  var json=Ext.encode(input);
		  Ext.Ajax.request({
			method:'GET',
        	url:'area/queryAreaInfo/',
        	params:{json:json},
        	defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success: function(res){
				var appendData=Ext.util.JSON.decode(res.responseText);
				if(appendData.status=='success'){
				    Ext.getCmp("city").setStore(appendData);
				    var city=Ext.getCmp('city').getValue();
				    if((province==null || (province!=null && oldValue==null && city==null) || (newValue!=oldValue && oldValue!=null)) && appendData.data.length>0){
				    	 Ext.getCmp('city').setValue(appendData.data[0].regionId); 
				    }
				   
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
	

	editSelectProvince: function(){
		province=Ext.getCmp('province').getValue();
		var url= 'area/queryAreaInfo';
		Ext.Ajax.request({
		method:'GET',
       	url:url,
       	defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success: function(res){
				var appendData=Ext.util.JSON.decode(res.responseText);
				if(appendData.status=='success'){
				    Ext.getCmp("province").setStore(appendData);
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
	 
	callSpeedLimitCommand: function(){
		var obj = Ext.getCmp('EnterModifyVehicleWindow');
		obj.el.mask('执行下发限速中...', 'x-mask-loading');
		
		var vehiclInfo = this.lookupReference('enterModifyVehicle').getValues();
		var json = Ext.encode(vehiclInfo);
		var thisObj = this;
		Ext.Ajax.request({
        	url:'vehicle/updateSpeedLimit',
			method:'POST',
	        params:{json:json},
			success: function(res){
				obj.el.unmask();
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
					//var latestLimitSpeed = Ext.getCmp('latestLimitSpeed');
					//latestLimitSpeed.setValue(vehiclInfo.limitSpeed);
					//thisObj.configBtnchange(thisObj);
					//设置下发状态
					var commandStatus = Ext.getCmp('commandStatus');
					commandStatus.setValue("excuting");
			 		Ext.Msg.alert("提示信息", "下发限速成功");
				}else{
					var commandStatus = Ext.getCmp('commandStatus');
					commandStatus.setValue("failure");
					Ext.Msg.alert("提示信息", appendData.msg);
				}
			 },
			failure : function() {
				obj.el.unmask();
				Ext.Msg.alert('提示信息','下发限速失败');
			}
        });
	},
	
	configBtnchange: function(){
		var configBtn = Ext.getCmp('configBtn');
		var limitSpeed = Ext.getCmp('limitSpeed').value;
		var latestLimitSpeed = Ext.getCmp('latestLimitSpeed').value;
		if (limitSpeed == latestLimitSpeed) {
			configBtn.setDisabled(true);
		} else {
			configBtn.setDisabled(false);
		}
	},
	
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.vehiclemgmt.vehicleInfomgmt.DeptChooseWin",{
     		parentId:combo.up("form").getForm().findField("deptId").getValue()
     	});
     	win.down("treepanel").getStore().load();
     	win.show();
     },
     
     chooseDept: function(btn, e, eOpts){
     	var tree = btn.up("window").down("treepanel");
     	var selection = tree.getSelectionModel().getSelection();
     	if(selection.length == 0){
     		Ext.Msg.alert('提示', '请选择部门！');
     		return;
     	}
     	var select = selection[0].getData();
 		var deptId = select.id;
 		var deptName = select.text;
     	var form = Ext.getCmp("vehicleinfo_searchform_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
     
     openAddVehDeptChooseWin: function(combo, event, eOpts){
    	var win = Ext.create("Admin.view.vehiclemgmt.vehicleInfomgmt.DeptChooseWin",{
    		parentId:combo.up("form").getForm().findField("currentuseOrgId").getValue(),
    		parentName: combo.up("form").getForm().findField("currentuseOrgName").getValue(),
    		buttons:[{
	   	    	text:'确定',
	   	    	listeners:{
	   	    		click:'chooseAddVehDept',
	   	    	}
	   	    },{
	   	    	text:'取消',
	   	    	handler:function(){
	   	    		this.up("window").close();
	   	    	}
	   	    }], 
      	});
      	win.down("treepanel").getStore().load();
      	win.show();
     },
     
     chooseAddVehDept: function(btn, e, eOpts){
    	 var tree = btn.up("window").down("treepanel");
      	var selection = tree.getSelectionModel().getSelection();
      	if(selection.length == 0){
      		Ext.Msg.alert('提示', '请选择部门！');
      		return;
      	}
      	var select = selection[0].getData();
  		var deptId = select.id;
  		var deptName = select.text;
      	var form = Ext.getCmp("vehicleWindow").down("form").getForm();
//      	form.findField("currentuseOrgId").setValue(deptId);
//      	form.findField("currentuseOrgName").setValue(deptName);
//      	form.findField("currentuseOrgName").validate();
      	
      	var userType = window.sessionStorage.getItem('userType');
      	if(select.parentId == "root" && (userType == '2' || userType == '6')){
        	//如果选中根节点，且目前是企业或者租车公司
        	form.findField("currentuseOrgId").setValue("");
        	var entName = window.sessionStorage.getItem("organizationName");
          	form.findField("currentuseOrgName").setValue(entName);
          	form.findField("currentuseOrgName").validate();
      	}else{
      		form.findField("currentuseOrgId").setValue(deptId);
	      	form.findField("currentuseOrgName").setValue(deptName);
	      	form.findField("currentuseOrgName").validate();
      	}
      	btn.up("window").close();
     },
     
     openSetVehDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.vehiclemgmt.vehicleInfomgmt.DeptChooseWin",{
     		buttons:[{
 	   	    	text:'确定',
 	   	    	listeners:{
 	   	    		click:'chooseSetVehDept',
 	   	    	}
 	   	    },{
 	   	    	text:'取消',
 	   	    	handler:function(){
 	   	    		this.up("window").close();
 	   	    	}
 	   	    }], 
       	});
       	win.down("treepanel").getStore().load();
       	win.show();
      },
      
      chooseSetVehDept: function(btn, e, eOpts){
     	 var tree = btn.up("window").down("treepanel");
       	var selection = tree.getSelectionModel().getSelection();
       	if(selection.length == 0){
       		Ext.Msg.alert('提示', '请选择部门！');
       		return;
       	}
       	var select = selection[0].getData();
   		var deptId = select.id;
   		var deptName = select.text;
       	var form = Ext.getCmp("vehicleWindow").down("form").getForm();
       	form.findField("currentuseOrgId").setValue(deptId);
       	form.findField("currentuseOrgName").setValue(deptName);
       	form.findField("currentuseOrgName").validate();
       	btn.up("window").close();
      },
      
      openDeptSetWin:  function(combo, event, eOpts){
    	var win = Ext.create("Admin.view.vehiclemgmt.vehicleInfomgmt.DeptChooseWin",{
    		parentId:combo.up("form").getForm().findField("arrangedOrgId").getValue(),
    		parentName:combo.up("form").getForm().findField("arrangedOrgName").getValue(),
    		buttons:[{
	   	    	text:'确定',
	   	    	listeners:{
	   	    		click:'chooseDeptSetDept',
	   	    	}
	   	    },{
	   	    	text:'取消',
	   	    	handler:function(){
	   	    		this.up("window").close();
	   	    	}
	   	    }], 
      	});
      	win.down("treepanel").getStore().load();
      	win.show();
     },
     
     chooseDeptSetDept: function(btn, e, eOpts){
    	 var tree = btn.up("window").down("treepanel");
      	var selection = tree.getSelectionModel().getSelection();
      	if(selection.length == 0){
      		Ext.Msg.alert('提示', '请选择部门！');
      		return;
      	}
      	var select = selection[0].getData();
  		var deptId = select.id;
  		var deptName = select.text;
      	var form = Ext.getCmp("vehMgmtDeptAllocationWin").down("form").getForm();
//      	form.findField("arrangedOrgId").setValue(deptId);
//      	form.findField("arrangedOrgName").setValue(deptName);
//      	form.findField("arrangedOrgName").validate();
      	var userType = window.sessionStorage.getItem('userType');
      	if(select.parentId == "root" && (userType == '2' || userType == '6')){
        	//如果选中根节点，且目前是企业或者租车公司
      		//2是用车企业 6是租车企业
      		//3是部门
        	form.findField("arrangedOrgId").setValue("");
        	
        	var entName = window.sessionStorage.getItem("organizationName");
          	form.findField("arrangedOrgName").setValue(entName);
          	form.findField("arrangedOrgName").validate();
      	}else{
      		form.findField("arrangedOrgId").setValue(deptId);
      		form.findField("arrangedOrgName").setValue(deptName);
      		form.findField("arrangedOrgName").validate();
      	}
      	
      	btn.up("window").close();
     },
     
   //分配车辆到部门  new  原方法为：onAllocateVehicleClick
 	onAllocateDeptClick:function(btn){
 		var allocateVehicleInfo=btn.up('window').down('form').getValues();
 		var input = {
 			'orgId': allocateVehicleInfo.arrangedOrgId, //分配的机构Id
 			'orgName': allocateVehicleInfo.arrangedOrgName,		//分配的名称
 			'vehicleId': allocateVehicleInfo.vehicleId  //分配的车辆id
 		};
 		var userType = this.getViewModel().get('roleType');
 		var urls='vehicle/vehicleAssigne';
 		var json = Ext.encode(input);
 		Ext.Ajax.request({
         	url:urls,//+'?json='+Ext.encode(input),
 			method:'POST',
 	        params:{json:json},
 			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
 			success: function(res){
 				var appendData=Ext.JSON.decode(res.responseText);
 				if(appendData.status=='success'){
 					btn.up('window').close();
 					Ext.getCmp("searchVehicle").fireEvent("click");
 			 		Ext.Msg.alert("提示信息", '分配成功');
 				}else{
 					Ext.MessageBox.alert("提示信息","分配失败");
 				}
 			 	
 			 }
 	        /*,
 			failure : function() {
 				Ext.Msg.alert('Failure','Call interface error!');
 			}*/
 	    });
 		   	
 	},
 	
 	checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.selfDept == null && value.childDept == null){
//     		chk.setValue(true);
     		Ext.Msg.alert("提示信息", '本部门和子部门请至少选择一个！');
     		//setValue无法选中上次选中的，有bug
     		if(chk.boxLabel == "本部门"){
     			group.items.items[1].setValue(true);
     		}else{
     			group.items.items[0].setValue(true);
     		}
     	}
     },

 	//打开给车辆分配地理围栏窗口，显示当前车辆可分配地理围栏
	openArrangeGeofenceWin:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("assignGeofence", {
			title: '地理围栏管理',
			closable: false,
			buttonAlign : 'center',
			vehicleId: rec.data.id,
			vehicleNumber: rec.data.vehicleNumber,
			buttons : [{
				text : '返回',
				handler: 'toBackGeofenceView'
			}]
		});
		win.show();
	},

	//返回地理围栏管理页面
	toBackGeofenceView: function() {
     	console.log('to back vehicle view');
     	var VehicleStore = Ext.getCmp('vehicleInfomgmtId').getStore('VehicleResults');
		VehicleStore.currentPage = 1;
		Ext.getCmp('vehicleInfomgmtId').getStore("VehicleResults").load();
     	this.view.close();
    },

    onAfterLoadVehicleGeofence: function(){
      	var avialiableGeofenceStore = Ext.getCmp('assignedVehicleGeofencePage').store;
		avialiableGeofenceStore.currentPage = 1;
		Ext.getCmp("assignedVehicleGeofenceGrid").getViewModel().getStore("vehicleGeofenceAssignedStore").load();
    },

    loadVehicleGeofence: function() {
 		var vehicleNumber = Ext.getCmp("assignGeofence").vehicleNumber;
		var page=Ext.getCmp('assignedVehicleGeofencePage').store.currentPage;
		var limit=Ext.getCmp('assignedVehicleGeofencePage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"vehicleNumber":vehicleNumber,
			};
		var pram = Ext.encode(input);
		Ext.getCmp("assignedVehicleGeofenceGrid").getViewModel().getStore("vehicleGeofenceAssignedStore").proxy.extraParams = {
			"json" : pram
		};

    },

    //移除已分配地理围栏
	unassignGeofence: function(grid, rowIndex, colIndex) {
     	var rec = grid.getStore().getAt(rowIndex);
     	var vehicleId = Ext.getCmp("assignGeofence").vehicleId;
     	var vehicleNumber = Ext.getCmp("assignGeofence").vehicleNumber;
     	var msg = '是否确认将地理围栏 ' + rec.data.markerName + ' 从车辆 ' +  vehicleNumber + ' 中移除 ?';
     	console.log('+++unassignGeofence+++vehicleNumber: ' + vehicleNumber);
     	Ext.Msg.confirm('消息提示', msg, function(btn){
     		if (btn == 'yes') {
     			//调用部门移除接口 start
     			var input = {'vehicleId' : vehicleId,
     					     'markerId': rec.data.id};
     			var json = Ext.encode(input);
             	Ext.Ajax.request({
         			url : 'vehicle/unassignMarkers',
         			method : 'POST',
	        		params:{json:json},
         			success : function(response, options) {
         				var respText = Ext.util.JSON.decode(response.responseText);
         				var status = respText.status;
         		    	if(status == 'success') {
         		    		Ext.Msg.alert('消息提示', '移除成功');
					      	var avialiableGeofenceStore = Ext.getCmp('assignedVehicleGeofencePage').store;
							avialiableGeofenceStore.currentPage = 1;
							Ext.getCmp("assignedVehicleGeofenceGrid").getViewModel().getStore("vehicleGeofenceAssignedStore").load();
         		    	}
         			},
         			scope : this
         		});
     		}
     	});
     },

    //打开可分配地理围栏窗口
    showAvailiableGeofenceView: function(){
     	var vehicleId = Ext.getCmp("assignGeofence").vehicleId;
     	var vehicleNumber = Ext.getCmp("assignGeofence").vehicleNumber;
	    var input = {
    				"currentPage" : 1,
					"numPerPage" : 5,
					"vehicleId" : vehicleId};
	    var json = Ext.encode(input);
     	Ext.Ajax.request({
 			url : 'vehicle/findVehicleAvialiableMarkers',
 			method : 'POST',
	        params:{json:json},
 			success : function(response, options) {
 				var respText = Ext.util.JSON.decode(response.responseText);
 				var totalRows = respText.data.totalRows;
 				if(totalRows == '0'){
 					var win = Ext.widget("emptyAvialiableGeofencesView", {
			 			title: '地理围栏管理',
			 			buttonAlign : 'center',
			 			closable: false,
			 			vehicleId : vehicleId,
			 			vehicleNumber: vehicleNumber,
			 			buttons : [{
			 				text : '返回',
			 				handler: 'toAssignGeofenceView'
			 			}]
			 		});
			     	Ext.getCmp("assignGeofence").close();
			 		win.show();
 				}else{
			     	console.log('+++findVehicleAvialiableMarkers+++vehicleId: ' + vehicleId);
			     	var win = Ext.widget("addGeofenceToVehicle", {
			 			title: '地理围栏管理',
			 			buttonAlign : 'center',
			 			closable: false,
			 			vehicleId : vehicleId,
			 			vehicleNumber: vehicleNumber,
			 			buttons : [{
			 				text : '返回',
			 				handler: 'toAssignGeofenceView'
			 			}]
			 		});
			     	Ext.getCmp("assignGeofence").close();
			 		win.show();
 				}
 		    	
 			},
 			scope : this
 		});
     },

   	toAssignGeofenceView: function() {
     	if(Ext.getCmp("addGeofenceToVehicle")){
     		var vehicleId = Ext.getCmp("addGeofenceToVehicle").vehicleId;
     		var vehicleNumber = Ext.getCmp("addGeofenceToVehicle").vehicleNumber;
	 		/*清空存储的选中地理围栏list*/
	 		Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords.length = 0;
	        Ext.getCmp('addGeofenceToVehicleGrid').getSelectionModel().clearSelections();
     	}
     	if(Ext.getCmp("emptyAvialiableGeofencesView")){
     		var vehicleId = Ext.getCmp("emptyAvialiableGeofencesView").vehicleId;
     		var vehicleNumber = Ext.getCmp("emptyAvialiableGeofencesView").vehicleNumber;
     	}
     	
     	console.log('vehicleNumber id : ' + vehicleNumber);
     	var win = Ext.widget("assignGeofence", {
 			title: '地理围栏管理',
 			closable: false,
 			buttonAlign : 'center',
 			vehicleId: vehicleId,
 			vehicleNumber: vehicleNumber,
 			buttons : [{
 				text : '返回',
 				handler: 'toBackGeofenceView'
 			}]
 		});
     	this.view.close();
 		win.show();
    },

    onAfterVehicleAvialiableGeofence: function(){
      	var avialiableVehicleStore = Ext.getCmp('addGeofenceToVehiclePage').store;
		avialiableVehicleStore.currentPage = 1;
		Ext.getCmp("addGeofenceToVehicleGrid").getViewModel().getStore("vehicleGeofenceStore").load();
    },

    loadVehicleAvialiableGeofence: function() {
		var vehicleId = Ext.getCmp("addGeofenceToVehicle").vehicleId;
		var page=Ext.getCmp('addGeofenceToVehiclePage').store.currentPage;
		var limit=Ext.getCmp('addGeofenceToVehiclePage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"vehicleId" : vehicleId
			};
		var pram = Ext.encode(input);
		Ext.getCmp("addGeofenceToVehicleGrid").getViewModel().getStore("vehicleGeofenceStore").proxy.extraParams = {
			"json" : pram
		};

	},

	checkVehSelect: function (me, record, index, opts) {
        Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords.push(record);
    },

    checkVehdeSelect: function (me, record, index, opts) {
	    Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords = Ext.Array.filter(Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords, function (item) {
	        return item.get("id") != record.get("id");
	    });
	},

	loadCheckAvialiableGeofence: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('addGeofenceToVehicleGrid').getSelectionModel();
        Ext.Array.each(Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },

    confirmAddGeofence: function() {
     	var vehicleId = Ext.getCmp("addGeofenceToVehicle").vehicleId;
     	var gridPanel = Ext.getCmp('addGeofenceToVehicleGrid');
 		var record = Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords;
 		console.log('length:' + record.length);
 		if(gridPanel.getViewModel().getStore('vehicleGeofenceStore').totalCount == 0) {
 			Ext.Msg.alert('消息提示', '无地理围栏可选择');
 			return;
 		}
 		if(record.length == 0) {
 			Ext.Msg.alert('消息提示', '请选择地理围栏');
 			return;
 		}
 		var markerIds = '';
 		for (var i=0; i<record.length; i++) {
 			markerIds += record[i].data.id + ',';
 		}
 		console.log('vehicleId:' + vehicleId);
 		markerIds = markerIds.substr(0,markerIds.length-1);
 		console.log('markerIds:' + markerIds);
 		
 		var input = {
 			'vehicleId' : vehicleId,
 			'markerIds' : markerIds
 		};
 		var json = Ext.encode(input);
 		Ext.Ajax.request({
 			url : 'vehicle/assignMarkers',
 			method : 'POST',
	        params:{json:json},
 			success : function(response, options) {
 				var respText = Ext.util.JSON.decode(response.responseText);
 				var status = respText.status;
 				var input = {
 			 			'vehicleId' : Ext.getCmp("addGeofenceToVehicle").vehicleId
 			 		};
 				var win = Ext.widget("assignGeofence", {
 					title: '地理围栏管理',
 					closable: false,
 					buttonAlign : 'center',
 					vehicleId: Ext.getCmp("addGeofenceToVehicle").vehicleId,
 					vehicleNumber: Ext.getCmp("addGeofenceToVehicle").vehicleNumber,
 					buttons : [{
 						text : '返回',
 						handler: 'toBackGeofenceView'
 					}]
 				});
 		    	if(status == 'success') {
 		    		Ext.Msg.alert('消息提示', '添加成功', function(text) {
 		    			/*清空存储的选中车辆list*/
 						Ext.getCmp('addGeofenceToVehicle').AllSelectedRecords.length = 0;
        				Ext.getCmp('addGeofenceToVehicleGrid').getSelectionModel().clearSelections();
                       	Ext.getCmp("addGeofenceToVehicle").close();
 		    		    win.show();
                    });
 		    	}
 			},
 			scope : this
 		});
     },
});

function onEmptyforGeofenceMsg(){
	console.log('vehicle click');
	var main = Ext.getCmp('main').getController();
	main.redirectTo('geofencemgmt');
    var grid = Ext.getCmp("gridGeofence");
    if (grid) {
        Ext.getCmp('geofencesearchForm').reset();
        Ext.getCmp('geofencePage').store.currentPage = 1;
        Ext.getCmp('geofencePage').pageSize = 10;
        grid.getStore("geofenceResults").load();
    };
	Ext.getCmp("emptyAvialiableGeofencesView").close();
}

