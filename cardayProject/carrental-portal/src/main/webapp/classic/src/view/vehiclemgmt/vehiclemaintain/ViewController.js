Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoad : function() {
        var frmValues = this.lookupReference('searchForm').getValues();
        var page=Ext.getCmp('vehicleMaintainPage').store.currentPage;
        var limit=Ext.getCmp('vehicleMaintainPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehicleMaintainResults").proxy.extraParams = {
            "json" : pram
        }
    },

    onSearchClick :　function() {
        var VehicleStore = Ext.getCmp('vehicleMaintainGrid').getStore();
        VehicleStore.currentPage = 1;
        this.getViewModel().getStore("vehicleMaintainResults").load();
    },

    viewVehicleMaintain : function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.create('Admin.view.vehiclemgmt.vehiclemaintain.ViewVehicleMaintain');
        if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > rowIndex){
            rec.data.vehicleNumber = Ext.getCmp('main').vehicelList[rowIndex].vehicleNumber;
        }
        win.down("form").loadRecord(rec);
        win.show();
    },

    addVehicleMaintain : function() {
        var win = Ext.create('Admin.view.vehiclemgmt.vehiclemaintain.AddVehicleMaintain');
        win.show();
    },

    updateVehicleMaintain : function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.create('Admin.view.vehiclemgmt.vehiclemaintain.UpdateVehicleMaintain');
        if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > rowIndex){
            rec.data.vehicleNumber = Ext.getCmp('main').vehicelList[rowIndex].vehicleNumber;
        }
        win.down("form").loadRecord(rec);
        win.show();
    },

    deleteVehicleMaintain : function(grid, rowIndex, colIndex) {
        Ext.Msg.confirm('消息提示','确定要删除此车辆维修信息吗?',function(btn){
            if (btn == 'yes') {
                console.log('Delete Vehicle Maintain!');
                Ext.Msg.alert('提示', '删除成功！');
            }
        });
    },

    openDeptChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.vehiclemgmt.vehiclemaintain.DeptChooseWin",{
            deptId:combo.up("form").getForm().findField("deptId").getValue()
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
        var form = Ext.getCmp("vehiclemaintainSearchformId").getForm();
        form.findField("deptId").setValue(deptId);
        form.findField("deptName").setValue(deptName);
        btn.up("window").close();
    },

    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
        var group = chk.up("checkboxgroup");
        var value = group.getValue();
        if(value.includeSelf == null && value.includeChild == null){
//          chk.setChecked(true);
            Ext.Msg.alert("提示信息", '本部门和子部门请至少选择一个！');
            //setValue无法选中上次选中的，有bug
            if(chk.boxLabel == "本部门"){
                group.items.items[1].setValue(true);
            }else{
                group.items.items[0].setValue(true);
            }
        }
     },

});

