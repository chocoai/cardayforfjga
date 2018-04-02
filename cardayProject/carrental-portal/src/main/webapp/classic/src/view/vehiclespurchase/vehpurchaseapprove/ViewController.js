Ext.define('Admin.view.vehiclespurchase.vehpurchaseapprove.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoadApproving:function(){
        var page=Ext.getCmp('vehiPurchaseApprovingPage').store.currentPage;
        var limit=Ext.getCmp('vehiPurchaseApprovingPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehiPurchaseApprovingResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiPurchaseApprovingResults").load();
	},

	onBeforeLoadApproved:function(){
        var page=Ext.getCmp('vehiPurchaseApprovedPage').store.currentPage;
        var limit=Ext.getCmp('vehiPurchaseApprovedPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehiPurchaseApprovedResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiPurchaseApprovedResults").load();
	},

    viewVehiPurchaseApproving:function(grid, rowIndex, colIndex){
        var win = Ext.widget("viewVehiPurchaseApprove", {
            title: '车辆购置待审核单查看',
        });
        var rec = grid.getStore().getAt(rowIndex);
        win.down("form").loadRecord(rec);
        win.show();
    },

    viewVehiPurchaseApproved:function(grid, rowIndex, colIndex){
        var win = Ext.widget("viewVehiPurchaseApprove", {
            title: '车辆购置已审核单查看',
        });
        var rec = grid.getStore().getAt(rowIndex);
        win.down("form").loadRecord(rec);
        win.show();
    },

    onBeforeLoadView:function(){
        this.getViewModel().getStore("vehiPurchaseTypeResults").load();
        this.getViewModel().getStore("vehiPurchaseInfoResults").load();
    },

    refuseVehiPurchaseApproving:function(grid, rowIndex, colIndex){
        var win = Ext.widget("vehPurchaseRefuseWin");
        win.show();
    },

    vehiPurchaseApproved:function(grid, rowIndex, colIndex){
        var win = Ext.widget("vehPurchaseApprovedWin");
        win.show();
    },

});

