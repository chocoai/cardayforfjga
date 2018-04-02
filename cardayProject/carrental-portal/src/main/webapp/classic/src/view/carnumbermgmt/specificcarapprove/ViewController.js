Ext.define('Admin.view.carnumbermgmt.specificcarapprove.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],
			
	
			
	onBeforeLoadApply : function() {
		this.getViewModel().getStore("applyResults").load();
	},
	
	onBeforeLoadAudited : function() {
		this.getViewModel().getStore("auditedResults").load();
	},
	
	//已审核车牌查看（审核通过、已驳回）
	viewAudited : function(grid, rowIndex, colIndex) {
		var auditedInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewaudited");
		win.down("form").loadRecord(auditedInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},
	
	
	//待审核车牌查看（申请中）
	viewApply : function(grid, rowIndex, colIndex) {
		var applyInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewapply");
		win.down("form").loadRecord(applyInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},

    refuseSpecifiCcarApproving:function(grid, rowIndex, colIndex){
        var win = Ext.widget("vehPurchaseRefuseWin");
        win.show();
    },

    specificCarApproved:function(grid, rowIndex, colIndex){
        var win = Ext.widget("vehPurchaseApprovedWin");
        win.show();
    },

});

