Ext.define('Admin.view.carnumbermgmt.carnumberapprove.ViewController', {
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
		var win = Ext.widget("viewaudited",{
			title: '警车号牌申请审批信息查看',
		});
		win.down("form").loadRecord(auditedInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},
	
	
	//待审核车牌查看（申请中）
	viewApply : function(grid, rowIndex, colIndex) {
		var applyInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewapply",{
			title: '警车号牌审批信息查看',
		});
		win.down("form").loadRecord(applyInfo);
		win.down('form').getForm().findField('applyOrganization').setValue(window.sessionStorage.getItem("organizationName"));
		win.show();
	},

    refuseCarnumberApproving:function(grid, rowIndex, colIndex){
        var win = Ext.widget("carnumberRefuseWin");
        win.show();
    },

    carnumberApproved:function(grid, rowIndex, colIndex){
        var win = Ext.widget("carnumberApprovedWin");
        win.show();
    },
});

