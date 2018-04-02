Ext.define('Admin.view.vehiclemgmt.vehoilmgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoad : function() {

        var input = {
                "vehicleNumber": "",
                "vehicleType": "-1",
                "fromOrgId": "-1",
                "deptId": window.sessionStorage.getItem("organizationId"),
                "deptName": window.sessionStorage.getItem("organizationName"),
                "selfDept": 1,
                "childDept": 1,
                "arrangeEnt": "-1",
                "currentPage": 1,
                "numPerPage": 10
            };
        var json = Ext.encode(input);
        Ext.Ajax.request({
            url : "vehicle/listVhicle",
            method : 'POST',
            params:{ json:json},
            sync:false,
            success : function(response, options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var status = respText.status;
                if(status == 'success') {
                    Ext.getCmp('vehoilmgmtGrid').vehoilmgmtList = respText.data.resultList;
                }
            },
            scope : this
        });

        var frmValues = this.lookupReference('searchForm').getValues();
        var page=Ext.getCmp('vehoilmgmtPage').store.currentPage;
        var limit=Ext.getCmp('vehoilmgmtPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("vehoilmgmtResults").proxy.extraParams = {
            "json" : pram
        }
    },

    onSearchClick :　function() {
        var VehicleStore = Ext.getCmp('vehoilmgmtGrid').getStore();
        VehicleStore.currentPage = 1;
        this.getViewModel().getStore("vehoilmgmtResults").load();
    },

    viewVehoilmgmt : function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.create('Admin.view.vehiclemgmt.vehoilmgmt.ViewOilInfo');
        if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > rowIndex){
            rec.data.vehicleNumber = Ext.getCmp('main').vehicelList[rowIndex].vehicleNumber;
            rec.data.vehicleType = Ext.getCmp('main').vehicelList[rowIndex].vehicleType;
            rec.data.vehicleBrand = Ext.getCmp('main').vehicelList[rowIndex].vehicleBrand;
            rec.data.vehicleModel = Ext.getCmp('main').vehicelList[rowIndex].vehicleModel;
        }
        win.down("form").loadRecord(rec);
        win.show();
    },

    addVehoilmgmt : function() {
        var win = Ext.create('Admin.view.vehiclemgmt.vehoilmgmt.AddOilInfo');
        win.show();
    },

    updateVehoilmgmt : function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.create('Admin.view.vehiclemgmt.vehoilmgmt.UpdateOilInfo');
        if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > rowIndex){
            rec.data.vehicleNumber = Ext.getCmp('main').vehicelList[rowIndex].vehicleNumber;
            rec.data.vehicleType = Ext.getCmp('main').vehicelList[rowIndex].vehicleType;
            rec.data.vehicleBrand = Ext.getCmp('main').vehicelList[rowIndex].vehicleBrand;
            rec.data.vehicleModel = Ext.getCmp('main').vehicelList[rowIndex].vehicleModel;
        }
        win.down("form").loadRecord(rec);
        win.show();
    },

    deleteVehoilmgmt : function(grid, rowIndex, colIndex) {
        Ext.Msg.confirm('消息提示','确定要删除此加油信息吗?',function(btn){
            if (btn == 'yes') {
                console.log('Delete Oil Information!');
                Ext.Msg.alert('提示', '删除成功！');
            }
        });
    },

    openDeptChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.vehiclemgmt.vehoilmgmt.DeptChooseWin",{
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
        var form = Ext.getCmp("vehoilmgmtSearchformId").getForm();
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

      openAddVehDeptChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.vehiclemgmt.vehoilmgmt.DeptChooseWin",{
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
        var form = Ext.getCmp("oilInfoWindow").down("form").getForm();
        
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

});

