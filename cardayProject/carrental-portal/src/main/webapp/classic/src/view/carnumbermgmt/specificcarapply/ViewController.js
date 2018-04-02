Ext.define('Admin.view.carnumbermgmt.specificcarapply.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],
			
	alias : 'controller.specificcarapplycontroller',
	
	onBeforeLoadApply : function() {
		this.getViewModel().getStore("applyResults").load();
	},
	
	onBeforeLoadRefuse : function() {
		this.getViewModel().getStore("refuseResults").load();
	},
	
	onBeforeLoadAudited : function() {
		this.getViewModel().getStore("auditedResults").load();
	},
	
	//审核通过车牌查看
	viewAudited : function(grid, rowIndex, colIndex) {
		var auditedInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewaudited");
		win.down("form").loadRecord(auditedInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},
	
	//已驳回车牌查看
	viewRefuse : function(grid, rowIndex, colIndex) {
		var refuseInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewrefuse");
		win.down("form").loadRecord(refuseInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},
	
	//申请中车牌查看
	viewApply : function(grid, rowIndex, colIndex) {
		var applyInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewapply");
		win.down("form").loadRecord(applyInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},
	
	//新增申请中车牌
	addApply : function() {
		var win = Ext.widget('addcarnumber');
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();	
	},
	
	//完成新增申请车牌
	addApplyDone : function(btn) {
		btn.up('addcarnumber').close();
		Ext.Msg.alert('提示信息','添加成功');
	},
	
	//修改申请中车牌
	editApply : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('editapply');
		win.down("form").loadRecord(rec);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();	
	},
	
	//完成修改申请车牌
	editApplyDone : function(btn) {
		btn.up('editapply').close();
		Ext.Msg.alert('提示信息','修改成功');
	},
	
	//修改已驳回车牌
	editRefuse : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('editrefuse');
		win.down("form").loadRecord(rec);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();	
	},
	
	//完成已驳回车牌
	editRefuseDone : function(btn) {
		btn.up('editrefuse').close();
		Ext.Msg.alert('提示信息','修改成功');
	},
});

