/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.devicemgmt.deviceOrderInstall.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.devicemgmt.deviceOrderInstall.SearchForm',
			'Admin.view.devicemgmt.deviceOrderInstall.Grid',
			],

	onBeforeloadforOrderInstallList: function(){
		console.log('onBeforeloadforOrderInstallList');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('orderInstallPage').store.currentPage;
		var limit=Ext.getCmp('orderInstallPage').pageSize;
		var startTime,endTime;
		
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
				"orderNumber":frmValues.orderNumber,
				"corporateCustomer" : frmValues.corporateCustomer,
				"status" : frmValues.status,
				"startTime":startTime,
				"endTime" : endTime,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("orderInstallResults").proxy.extraParams = {
			"json" : pram
		}
	},


// 渲染View,加载数据
	onSearchClick:function(){
		console.log('onSearchClick');
		var dialRecordStore = this.lookupReference('gridDeviceOrderInstall').getStore();
		dialRecordStore.currentPage = 1;
		this.getViewModel().getStore('orderInstallResults').load();
	},


//点击添加按钮，弹出添加客户来电记录信息窗口
	onAddClick : function() {
/*		var win = Ext.widget('addDialRecord');
		win.getController().selectHourAndMinute();
		win.show();*/
	},

/**
 * 添加客户来电记录
 * @param {} btn
 */
	addDialRecord : function(btn){
/*		var dialRecord = this.lookupReference('addDialRecord').getValues();
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
		 }*/

	},

});
