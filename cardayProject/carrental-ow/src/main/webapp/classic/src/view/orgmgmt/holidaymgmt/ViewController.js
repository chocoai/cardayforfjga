/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.holidaymgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.orgmgmt.holidaymgmt.SearchForm',
			'Admin.view.orgmgmt.holidaymgmt.Grid',
			],
	init: function(view) {
        this.roleType = window.sessionStorage.getItem('userType');
        this.getViewModel().set('status',this.roleType);

    },

    onBeforeload: function(){
		console.log('onBeforeLoad');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('holidayPageId').store.currentPage;
		var limit=Ext.getCmp('holidayPageId').pageSize;

		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"year": frmValues.holidayYear,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("holidayResults").proxy.extraParams = {
			"json" : pram
		}
	},
// 渲染View,加载数据
	onSearchClick:function(){
		console.log('onSearchClick');
		var holidayStore = this.lookupReference('gridHoliday').getStore();
		holidayStore.currentPage = 1;
		this.getViewModel().getStore('holidayResults').load();
	},
/**
 * 查看节假日记录
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	viewHoliday : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('viewHoliday');
		win.down("form").loadRecord(rec);
		win.show();
    },

//点击添加按钮，弹出添加客户来电记录信息窗口
	onAddClick : function() {
		var win = Ext.widget('addHoliday');
		win.show();
	},

/**
 * 添加节假日记录
 * @param {} btn
 */
	addHolidayDone : function(btn){
		var holidayRecord = this.lookupReference('addHoliday').getValues();
		if (this.lookupReference('addHoliday').getForm().isValid()) {

			var input = {
				"holidayYear" 		: holidayRecord.holidayYear,
				"holidayType" 	    : holidayRecord.holidayType,
				"holidayTime"       : holidayRecord.holidayTime,
				"adjustHolidayTime" : holidayRecord.adjustHolidayTime,
			};

			var json = Ext.encode(input);

	        Ext.Ajax.request({
	        	url:'holiday/createRuleHoliday',
				method:'POST',
				params:{ json:json},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						btn.up('addHoliday').close();
				 		Ext.Msg.alert("提示信息", '添加节假日记录成功');
				 		Ext.getCmp("holidayId").getStore('holidayResults').load();
					}else{
						btn.up('addHoliday').close();
						Ext.MessageBox.alert("提示信息","添加节假日记录失败");
				 		Ext.getCmp("holidayId").getStore('holidayResults').load();
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
 * 修改节假日记录
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	editHoliday : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('editHoliday');
		win.down("form").loadRecord(rec);
		win.show();
	},
	
/**
 * 更新节假日记录
 * @param {} rec
 * @param {} enterinfo
 * @param {} grid
 */
	editHolidayDone: function(btn){
		var holidayRecord = this.lookupReference('editHoliday').getValues();
		if (this.lookupReference('editHoliday').getForm().isValid()) {

 			var input = {
 				"id"  			    : holidayRecord.id,
				"holidayYear" 		: holidayRecord.holidayYear,
				"holidayType" 	    : holidayRecord.holidayType,
				"holidayTime"       : holidayRecord.holidayTime,
				"adjustHolidayTime" : holidayRecord.adjustHolidayTime,
			};

			var json = Ext.encode(input);
		
	        Ext.Ajax.request({
				url:'holiday/updateRuleHoliday',
				method:'POST',
				params:{ json:json},
	            success: function(response) {
	                var data=Ext.JSON.decode(response.responseText);
	                if(data.status=='success'){
	                	btn.up('editHoliday').close();
	               		Ext.Msg.alert("消息提示", '节假日记录修改成功！');
				 		Ext.getCmp("holidayId").getStore('holidayResults').load();
	               	}else{
	                	btn.up('editHoliday').close();
	                    Ext.Msg.alert("消息提示", '节假日记录修改失败！');
				 		Ext.getCmp("holidayId").getStore('holidayResults').load();
	               	}
	            },
	            failure : function() {
	                btn.up('editHoliday').close();
					Ext.Msg.alert('失败提醒','调用接口失败!');
				}	
			});
		 }else{
		 	Ext.Msg.alert('消息提示', '输入的信息格式不正确!');
		 }
	},

	//删除客户来电记录
	deleteHoliday : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var holidayId = grid.getStore().getAt(rowIndex).id;

				var input = {
					"id" : holidayId,
				};
				var json = Ext.encode(input);

				Ext.Ajax.request({
		   		url: 'holiday/removeRuleHoliday',
		        method : 'POST',
				params:{ json:json},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
				 		  Ext.getCmp("holidayId").getStore('holidayResults').load();
						}else{
						  Ext.Msg.alert('错误提示','删除节假日记录失败！');
						  Ext.getCmp("holidayId").getStore('holidayResults').load();
						}
			        },
			       /* failure : function() {
			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			        },*/
			        scope:this
				});
			}
		});
	},

});
