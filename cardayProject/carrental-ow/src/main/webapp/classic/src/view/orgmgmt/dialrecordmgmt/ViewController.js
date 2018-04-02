/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.dialrecordmgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.orgmgmt.dialrecordmgmt.SearchForm',
			'Admin.view.orgmgmt.dialrecordmgmt.Grid',
			],
	init: function(view) {
        this.roleType = window.sessionStorage.getItem('userType');
        this.getViewModel().set('status',this.roleType);

    },
/**
 * 查看客户来电记录
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	checkDialRecord : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkDialRecord');
		win.down("form").loadRecord(rec);
		win.show();
    },

//点击添加按钮，弹出添加客户来电记录信息窗口
	onAddClick : function() {
		var win = Ext.widget('addDialRecord');
		win.getController().selectHourAndMinute();
		win.show();
	},

/**
 * 添加客户来电记录
 * @param {} btn
 */
	addDialRecord : function(btn){
		var dialRecord = this.lookupReference('addDialRecord').getValues();
		var dialTime = dialRecord['dialDate'] + " " + dialRecord['hour'] + ":" + dialRecord['minute'] + ":00";
		//var recorder = window.sessionStorage.getItem("userName");
		if (this.lookupReference('addDialRecord').getForm().isValid()) {

 			var input = {
				"dialTime" 		    : dialTime,
				"dialName" 	        : dialRecord['dialName'],
				"dialOrganization"  : dialRecord['dialOrganization'],
				"dialPhone" 	    : dialRecord['dialPhone'],
				"dialType" 		    : dialRecord['dialType'],
				"dialContent" 	    : dialRecord['dialContent'],
				"vehicleNumber" 	: dialRecord['vehicleNumber'],
				"orderNo" 	        : dialRecord['orderNo'],
				"deviceNo" 		    : dialRecord['deviceNo'],
				"dealResult" 	    : dialRecord['dealResult'],
				//"recorder"          : recorder,
			};

 			var json_input = Ext.encode(input);
	        Ext.Ajax.request({
	        	url:'dialcenter/addDialRecord',
				method:'POST',
				params:{ json:json_input},
//				headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						btn.up('addDialRecord').close();
				 		Ext.Msg.alert("提示信息", '添加客户来电记录成功');
				 		Ext.getCmp("dialRecordId").getStore('dialrecordResults').load();
					}else{
						btn.up('addDialRecord').close();
						Ext.MessageBox.alert("提示信息","添加客户来电记录失败");
				 		Ext.getCmp("entdialRecordIderId").getStore('dialrecordResults').load();
					}
				 	
				 },
				failure : function() {
					Ext.Msg.alert('失败提醒','调用接口失败!');
				}
	        });
		 }else{
		 	Ext.Msg.alert('消息提示', '输入的信息格式不正确');
		 }

	},
	
/**
 * 修改客户来电记录
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	editDialRecord : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('editDialRecord');
		win.down("form").loadRecord(rec);
		var timeArray = rec.data.dialTime.split(' ');

		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');
		if(timeArray[0]==nowDate){
			this.selectHourAndMinute();
		}else{
			this.selectCallTime();

		}
		Ext.getCmp("editDialRecord").down('form').form.findField('dialDate').setValue(timeArray[0]);
		var time=timeArray[1].split(":");
		Ext.getCmp("editDialRecord").down('form').form.findField('hour').setValue(time[0]);
		Ext.getCmp("editDialRecord").down('form').form.findField('minute').setValue(time[1]);
		win.show();
	},
	
/**
 * 更新客户来电记录
 * @param {} rec
 * @param {} enterinfo
 * @param {} grid
 */
	updateDialRecord: function(btn){
        var businessTypeArray = new Array();
		var dialRecord = this.lookupReference('editDialRecord').getValues();
		var dialTime = dialRecord['dialDate'] + " " + dialRecord['hour'] + ":" + dialRecord['minute'] + ":00";
		//var recorder = window.sessionStorage.getItem("userName");
		if (this.lookupReference('editDialRecord').getForm().isValid()) {

 			var input = {
 				"id"  			    : dialRecord['id'],
				"dialTime" 		    : dialTime,
				"dialName" 	        : dialRecord['dialName'],
				"dialOrganization"  : dialRecord['dialOrganization'],
				"dialPhone" 	    : dialRecord['dialPhone'],
				"dialType" 		    : dialRecord['dialType'],
				"dialContent" 	    : dialRecord['dialContent'],
				"vehicleNumber" 	: dialRecord['vehicleNumber'],
				"orderNo" 	        : dialRecord['orderNo'],
				"deviceNo" 		    : dialRecord['deviceNo'],
				"dealResult" 	    : dialRecord['dealResult'],
				//"recorder"          : recorder,
			};
		
 			var json_input = Ext.encode(input);
	        Ext.Ajax.request({
				url:'dialcenter/updateDialRecord',
				method:'POST',
				params:{ json:json_input},
//				headers: {'Content-Type':'application/json','charset':'UTF-8'},
	            success: function(response) {
	                var data=Ext.JSON.decode(response.responseText);
	                if(data.status=='success'){
	                	btn.up('editDialRecord').close();
	               		Ext.Msg.alert("消息提示", '客户来电记录修改成功！');
				 		Ext.getCmp("dialRecordId").getStore('dialrecordResults').load();
	               	}else{
	                	btn.up('editDialRecord').close();
	                    Ext.Msg.alert("消息提示", '客户来电记录修改失败！');
				 		Ext.getCmp("dialRecordId").getStore('dialrecordResults').load();
	               	}
	            },
	            failure : function() {
					Ext.Msg.alert('失败提醒','调用接口失败!');
				}	
			});
		 }else{
		 	Ext.Msg.alert('消息提示', '输入的信息格式不正确!');
		 }
	},

	//删除客户来电记录
	deleteDialRecord : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var dialRecordId = grid.getStore().getAt(rowIndex).id;
				var url = 'dialcenter/'+dialRecordId+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
				 		  Ext.getCmp("dialRecordId").getStore('dialrecordResults').load();
						}else{
						  Ext.Msg.alert('错误提示','删除客户来电记录失败！');
						  Ext.getCmp("dialRecordId").getStore('dialrecordResults').load();
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

	onBeforeload: function(){
		console.log('onBeforeLoad');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('dialRecordPage').store.currentPage;
		var limit=Ext.getCmp('dialRecordPage').pageSize;
		var startTime,endTime;
		if(frmValues.startTime=='' && frmValues.endTime==''){
			Ext.getCmp("endTime").setMinValue (null);
			Ext.getCmp("startTime").setMaxValue(new Date());	
		}
		
       if(frmValues.startTime != ''){
		  startTime = frmValues.startTime + " " +"00:00:00";
       }else{
          startTime = '';
       }

       if(frmValues.endTime != ''){
		  endTime = frmValues.endTime + " " +"23:59:59";
       }else{
          endTime = '';
       }
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"dialPhone":frmValues.dialPhone,
				"dialName" : frmValues.dialName,
				"startTime":startTime,
				"endTime" : endTime,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("dialrecordResults").proxy.extraParams = {
			"json" : pram
		}
	},
// 渲染View,加载数据
	onSearchClick:function(){
		console.log('onSearchClick');
		var dialRecordStore = this.lookupReference('gridDialRecord').getStore();
		dialRecordStore.currentPage = 1;
		this.getViewModel().getStore('dialrecordResults').load();
	},

	onChangeForDialPhone: function(me,newValue,oldValue,eOpts){
		console.log('onChangeForDialPhone');
		//判断客户来电号码是否填写
		if (me.value) {
			var input = {
					"dialPhone": me.value,
			};
			var json_input = Ext.encode(input);
			Ext.Ajax.request({
				url:'dialcenter/findUserInfobyPhone',
				method:'POST',
				params:{ json:json_input},
//			headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						var company = appendData.data.company;
						var realName = appendData.data.realName;
						if(Ext.getCmp('addDialRecord') == undefined){						
							Ext.getCmp('editDialRecord').down('form').form.findField('dialName').setValue(realName);
							Ext.getCmp('editDialRecord').down('form').form.findField('dialOrganization').setValue(company);
						}else{
							Ext.getCmp('addDialRecord').down('form').form.findField('dialName').setValue(realName);
							Ext.getCmp('addDialRecord').down('form').form.findField('dialOrganization').setValue(company);
						}
					}else{
						//Ext.Msg.alert('失败提醒','请输入有效客户的来电号码!');
						if(Ext.getCmp('addDialRecord') == undefined){						
							Ext.getCmp('editDialRecord').down('form').form.findField('dialName').setValue("");
							Ext.getCmp('editDialRecord').down('form').form.findField('dialOrganization').setValue("");
						}else{
							Ext.getCmp('addDialRecord').down('form').form.findField('dialName').setValue("");
							Ext.getCmp('addDialRecord').down('form').form.findField('dialOrganization').setValue("");
						}
					}
				},
				failure : function() {
					Ext.Msg.alert('失败提醒','调用接口失败!');
				}
			});
		}
	},

	onBlurFororderNo: function(me,event,eOpts){
		console.log('onBlurFororderNo');
		var input = {
				"orderNo": me.value,
		};
		
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
        	url:'dialcenter/findVehicleInfobyOrderNo',
			method:'POST',
			params:{ json:json_input},
//			headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
					var vehicleNumber = appendData.data.vehicleNumber;
					var deviceNo = appendData.data.deviceNumber;
					if(Ext.getCmp('addDialRecord') == undefined){						
					    Ext.getCmp('editDialRecord').down('form').form.findField('vehicleNumber').setValue(vehicleNumber);
					    Ext.getCmp('editDialRecord').down('form').form.findField('deviceNo').setValue(deviceNo);
				    }else{
					    Ext.getCmp('addDialRecord').down('form').form.findField('vehicleNumber').setValue(vehicleNumber);
					    Ext.getCmp('addDialRecord').down('form').form.findField('deviceNo').setValue(deviceNo);
				    }
				}else{
				    //Ext.Msg.alert('失败提醒','请输入有效的订单号!');
				}
			 },
			failure : function() {
				Ext.Msg.alert('失败提醒','调用接口失败!');
			}
	    });
	},

	//添加、修改来电记录，日期选定之后，设置对应小时、分钟
	selectHourAndMinute : function() {
		var comstore = new Ext.data.Store();
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();

		switch (nowHour)
		{
		case 0:
			comstore = {
	    		data: [
	    		    {'id': '0', 'time': '00'}]
			}
		break;

		case 1:
			comstore = {
	    		data: [
	    		    {'id': '0', 'time': '00'},{'id': '1', 'time': '01'}]
			}
		break;

		case 2:
			comstore = {
	    		data: [
	    		      {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'}]
			}
		break;

		case 3:
			comstore = {
	    		data: [
	    		      {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'}]
			}
		break;

		case 4:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'}]
			}
		break;

		case 5:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'}]
			}
		break;

		case 6:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				       {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'}]
			}
		break;

		case 7:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				       {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'}]
			}
		break;

		case 8:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				       {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				       {'id': '8', 'time': '08'}]
			}
		break;

		case 9:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'}]
			}
		break;

		case 10:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'}]
			}
		break;

		case 11:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'}]
			}
		break;

		case 12:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'}
				       ]
			}
		break;

		case 13:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'}]
			}
		break;

		case 14:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'}]
			}
		break;

		case 15:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'}]
			}
		break;

		case 16:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'}]
			}
		break;

		case 17:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'}]
			}
		break;

		case 18:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'}]
			}
		break;

		case 19:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'}]
			}
		break;

		case 20:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
				        {'id': '20', 'time': '20'}]
			}
		break;
			
		case 21:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'}]
			}
		break;
		case 22:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
				        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'}]
			}
		break;
		case 23:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		}

		if (Ext.getCmp('call_hour_id') && Ext.getCmp('call_minute_id')) {//添加来电记录
			Ext.getCmp('call_hour_id').setValue(nowHour);
			Ext.getCmp('call_hour_id').setStore(comstore);
			Ext.getCmp('call_minute_id').setValue(nowMinute);
			Ext.getCmp('call_minute_id').setMaxValue(nowMinute);

		} else if (Ext.getCmp('edit_call_hour_id') && Ext.getCmp('edit_call_minute_id')) { //修改来电记录
			Ext.getCmp('edit_call_hour_id').setValue('00');
			Ext.getCmp('edit_call_hour_id').setStore(comstore);
			Ext.getCmp('edit_call_minute_id').setValue('0');
			Ext.getCmp('edit_call_minute_id').setMaxValue(nowMinute);
		}
	},

	//添加，修改来电时间，完成来电时间选择
	selectCallTime : function() {
		var callTime = '';
		if (Ext.getCmp('add_call_time_id')) {
			callTime = Ext.util.Format.date(Ext.getCmp('add_call_time_id').getValue(),'Y-m-d');
		} else if(Ext.getCmp('edit_call_time_id')) {
			callTime = Ext.util.Format.date(Ext.getCmp('edit_call_time_id').getValue(),'Y-m-d');
		}

		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');
		if (callTime != nowDate) {
			var comstore = new Ext.data.Store();

			comstore = {
		    		data: [
		    		    {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
				        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
				}

			if (Ext.getCmp('call_hour_id') &&　Ext.getCmp('call_minute_id')) { //添加来电记录
				Ext.getCmp('call_hour_id').setValue('00');
				Ext.getCmp('call_hour_id').setStore(comstore);
				Ext.getCmp('call_minute_id').setValue('0');
				Ext.getCmp('call_minute_id').setMaxValue('59');
			} else if (Ext.getCmp('edit_call_hour_id')　&& Ext.getCmp('edit_call_minute_id')) { //修改来电记录
				Ext.getCmp('edit_call_hour_id').setValue('00');
				Ext.getCmp('edit_call_hour_id').setStore(comstore);
				Ext.getCmp('edit_call_minute_id').setValue('0');
			}
		} else {

			this.selectHourAndMinute();
		}
	},
	
	selectHourTime: function(){
		var callTime = '';
		if (Ext.getCmp('add_call_time_id')) {
			callTime = Ext.util.Format.date(Ext.getCmp('add_call_time_id').getValue(),'Y-m-d');
		} else if(Ext.getCmp('edit_call_time_id')) {
			callTime = Ext.util.Format.date(Ext.getCmp('edit_call_time_id').getValue(),'Y-m-d');
		}
		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();
		if (Ext.getCmp('call_hour_id') &&　Ext.getCmp('call_minute_id')) { //添加来电记录
			if(callTime==nowDate){
				var selAddHour=Ext.getCmp('call_hour_id').getValue();
				if(selAddHour==nowHour){
					Ext.getCmp('call_minute_id').setValue('0');
					Ext.getCmp('call_minute_id').setMaxValue(nowMinute);
				}else{
					Ext.getCmp('call_minute_id').setValue('0');
					Ext.getCmp('call_minute_id').setMaxValue('59');
				}
			}else{
				Ext.getCmp('call_minute_id').setValue('0');
			}
		}else if (Ext.getCmp('edit_call_hour_id')　&& Ext.getCmp('edit_call_minute_id')) { //修改来电记录
			
			if(callTime==nowDate){
				var selEditHour=Ext.getCmp('edit_call_hour_id').getValue();
				if(selEditHour==nowHour){
					Ext.getCmp('edit_call_minute_id').setValue('0');
					Ext.getCmp('edit_call_minute_id').setMaxValue(nowMinute);
				}else{
					Ext.getCmp('edit_call_minute_id').setValue('0');
					Ext.getCmp('edit_call_minute_id').setMaxValue('59');
				}
			}else{
				Ext.getCmp('edit_call_minute_id').setValue('0');
			}
		}

	},

});
