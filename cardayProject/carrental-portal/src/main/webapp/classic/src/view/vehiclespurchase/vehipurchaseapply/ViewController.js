Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoadApply:function(){
        var page=Ext.getCmp('vehiPurchaseApplyPage').store.currentPage;
        var limit=Ext.getCmp('vehiPurchaseApplyPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehiPurchaseApplyResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiPurchaseApplyResults").load();
	},

	onBeforeLoadRefuse:function(){
        var page=Ext.getCmp('vehiPurchaseRefusePage').store.currentPage;
        var limit=Ext.getCmp('vehiPurchaseRefusePage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehiPurchaseRefuseResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiPurchaseRefuseResults").load();
	},

	onBeforeLoadAudited:function(){
        var page=Ext.getCmp('vehiPurchaseAuditedPage').store.currentPage;
        var limit=Ext.getCmp('vehiPurchaseAuditedPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehiPurchaseAuditedResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiPurchaseAuditedResults").load();
	},

    viewVehiPurchaseApply:function(grid, rowIndex, colIndex){
        var win = Ext.widget("viewVehiPurchase", {
            title: '车辆购置申请中单查看',
        });
        var rec = grid.getStore().getAt(rowIndex);
        win.down("form").loadRecord(rec);
        win.show();
    },

    viewVehiPurchaseRefuse:function(grid, rowIndex, colIndex){
        var win = Ext.widget("viewVehiPurchase", {
            title: '车辆购置已驳回单查看',
        });
        var rec = grid.getStore().getAt(rowIndex);
        Ext.getCmp('printButton').hide();
        win.down("form").loadRecord(rec);
        win.show();
    },

    viewVehiPurchaseAudited:function(grid, rowIndex, colIndex){
        var win = Ext.widget("viewVehiPurchase", {
            title: '车辆购置审核通过单查看',
        });
        var rec = grid.getStore().getAt(rowIndex);
        Ext.getCmp('printButton').hide();
        win.down("form").loadRecord(rec);
        win.show();
    },

    onBeforeLoadView:function(){
        this.getViewModel().getStore("vehiPurchaseTypeResults").load();
        this.getViewModel().getStore("vehiPurchaseInfoResults").load();
    },

    updateVehiPurchaseApply:function(grid, rowIndex, colIndex){
        var win = Ext.widget("updateVehiPurchase", {
            title: '车辆购置申请中单修改',
        });
        var rec = grid.getStore().getAt(rowIndex);
        win.down("form").loadRecord(rec);
        win.show();
    },

    updateVehiPurchaseRefuse:function(grid, rowIndex, colIndex){
        var win = Ext.widget("updateVehiPurchase", {
            title: '车辆购置已驳回单修改',
        });
        var rec = grid.getStore().getAt(rowIndex);
        win.down("form").loadRecord(rec);
        win.show();
    },

    onBeforeLoadUpdate:function(){
        this.getViewModel().getStore("vehiPurchaseTypeResults").load();
        this.getViewModel().getStore("vehiPurchaseInfoResults").load();
    },

    addVehicle:function(grid, rowIndex, colIndex){
        var win = Ext.widget("addVehicle");
        win.show();
    },

    removeVehicle: function(){
        Ext.Msg.confirm('消息提示','确定要删除此条申请车辆?',function(btn){
            if (btn == 'yes') {
                console.log('Remove Vehicle!');         
                Ext.Msg.alert('提示', '删除此申请车！');
            }
        });
    },

    addVehiPurchase:function(grid, rowIndex, colIndex){
        var win = Ext.widget("addVehiPurchase");
        win.show();
    },

    onBeforeLoadAdd:function(){
        this.getViewModel().getStore("vehiPurchaseTypeAddResults").load();
        this.getViewModel().getStore("vehiPurchaseInfoResultsAdd").load();
    },

    approvalProcess:function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var data;
        if(rec.data.status == "0"){
            data = '[{"id": "101","approvalTime":"2017-08-04 13:56:54","approvalUser":"王警官","approvalStatus":"0","approvalComment":"已提交市局"},';   
            data = data + '{"id": "102","approvalTime":"2017-08-05 10:14:32","approvalUser":"陈部长","approvalStatus":"1","approvalComment":"退回申请单位"},';
            data = data + '{"id": "103","approvalTime":"2017-08-10 14:16:11","approvalUser":"刘局长","approvalStatus":"2","approvalComment":"市局审核通过"},';
            data = data + '{"id": "104","approvalTime":"2017-08-11 16:26:13","approvalUser":"刘局长","approvalStatus":"0","approvalComment":"提交省厅"}]';
        }else if(rec.data.status == "1"){
            data = '[{"id": "101","approvalTime":"2017-08-04 13:56:54","approvalUser":"王警官","approvalStatus":"0","approvalComment":"已提交市局"},'; 
            data = data + '{"id": "102","approvalTime":"2017-08-05 10:14:32","approvalUser":"陈部长","approvalStatus":"1","approvalComment":"退回申请单位"},';
            data = data + '{"id": "103","approvalTime":"2017-08-10 14:16:11","approvalUser":"刘局长","approvalStatus":"2","approvalComment":"市局审核通过"},';
            data = data + '{"id": "104","approvalTime":"2017-08-11 16:26:13","approvalUser":"刘局长","approvalStatus":"0","approvalComment":"提交省厅"},';
            data = data + '{"id": "105","approvalTime":"2017-08-18 10:17:13","approvalUser":"张处长","approvalStatus":"1","approvalComment":"退回市局"}]';
        }else if(rec.data.status == "2"){
            data = '[{"id": "101","approvalTime":"2017-08-04 13:56:54","approvalUser":"王警官","approvalStatus":"0","approvalComment":"已提交市局"},'; 
            data = data + '{"id": "102","approvalTime":"2017-08-05 10:14:32","approvalUser":"陈部长","approvalStatus":"1","approvalComment":"退回申请单位"},';
            data = data + '{"id": "103","approvalTime":"2017-08-10 14:16:11","approvalUser":"刘局长","approvalStatus":"2","approvalComment":"市局审核通过"},';
            data = data + '{"id": "104","approvalTime":"2017-08-11 16:26:13","approvalUser":"刘局长","approvalStatus":"0","approvalComment":"提交省厅"},';
            data = data + '{"id": "105","approvalTime":"2017-08-18 10:17:13","approvalUser":"张处长","approvalStatus":"1","approvalComment":"退回市局"},';
            data = data + '{"id": "106","approvalTime":"2017-08-21 13:14:12","approvalUser":"李处长","approvalStatus":"2","approvalComment":"省厅通过"},';
            data = data + '{"id": "107","approvalTime":"2017-08-27 09:01:11","approvalUser":"王科长","approvalStatus":"2","approvalComment":"已提交财政预审"},';
            data = data + '{"id": "108","approvalTime":"2017-09-10 15:18:41","approvalUser":"赵处长","approvalStatus":"1","approvalComment":"已退回省厅"},';
            data = data + '{"id": "109","approvalTime":"2017-09-15 14:12:46","approvalUser":"李处长","approvalStatus":"2","approvalComment":"财政通过"}]';
        }
        var win = Ext.widget("approvalProcessWin");                
        Ext.getCmp("approvalProcessWinGrid").getViewModel().getStore("approvalProcessResults").loadData(JSON.parse(data));
        win.show();
    },

});

