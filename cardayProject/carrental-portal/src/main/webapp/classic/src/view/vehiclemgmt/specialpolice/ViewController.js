Ext.define('Admin.view.vehiclemgmt.specialpolice.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoadforNoSecret:function(){
        var frmValues=this.lookupReference('searchFormNoSecret').getValues();
        if(''==frmValues['selfDept']||null==frmValues['selfDept']){
            frmValues['selfDept']=0;
        }
        if(''==frmValues['childDept']||null==frmValues['childDept']){
            frmValues['childDept']=0;
        }

        frmValues.arrangeEnt = "-1";
        frmValues.fromOrgId = "-1";
        frmValues.enableSecret = 0;
        
        var page=Ext.getCmp('vehiNoSecretPage').store.currentPage;
        
        var limit=Ext.getCmp('vehiNoSecretPage').pageSize;
        frmValues.currentPage=page;
        frmValues.numPerPage=limit;

        var pram = Ext.encode(frmValues);
        this.getViewModel().getStore("vehiNoSecretResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiNoSecretResults").load();
	},

    onSearchClickforNoSecret:function(){
        var VehicleStore = this.lookupReference('gridVehicleNoSecret').getStore();
        VehicleStore.currentPage = 1;
        this.onBeforeLoadforNoSecret();
    },

    onActivateforNoSecret: function(me,eOpts){       
        Ext.getCmp('searchFormNoSecret').reset();
        Ext.getCmp('searchFormNoSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
        Ext.getCmp('searchFormNoSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
        Ext.getCmp("gridVehicleNoSecret").getStore("vehiNoSecretResults").load();
    },

	onBeforeLoadforSecret:function(){
        var frmValues=this.lookupReference('searchFormSecret').getValues();
        if(''==frmValues['selfDept']||null==frmValues['selfDept']){
            frmValues['selfDept']=0;
        }
        if(''==frmValues['childDept']||null==frmValues['childDept']){
            frmValues['childDept']=0;
        }

        frmValues.arrangeEnt = "-1";
        frmValues.fromOrgId = "-1";
        frmValues.enableSecret = 1;
        var page=Ext.getCmp('vehiSecretPage').store.currentPage;
        var limit=Ext.getCmp('vehiSecretPage').pageSize;
        frmValues.currentPage=page;
        frmValues.numPerPage=limit;

        var pram = Ext.encode(frmValues);
        this.getViewModel().getStore("vehiSecretResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("vehiSecretResults").load();
	},

    onSearchClickforSecret:function(){
        var VehicleStore = this.lookupReference('gridVehicleSecret').getStore();
        VehicleStore.currentPage = 1;
        this.onBeforeLoadforSecret();
    },

    onActivateforSecret: function(me,eOpts){       
        Ext.getCmp('searchFormSecret').reset();
        Ext.getCmp('searchFormSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
        Ext.getCmp('searchFormSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
        Ext.getCmp("gridVehicleSecret").getStore("vehiSecretResults").load();    
    },

    //打开查看车辆信息窗体
    viewVehicleInfo:function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.create('Admin.view.vehiclemgmt.specialpolice.ViewVehicleInfo');
        var objectModel = new Ext.data.Model();
        var data={
                'vehicleNumber':rec.data.vehicleNumber
        }
        var json = Ext.encode(data);
        Ext.Ajax.request({
            url:'vehicle/findVehicleInfoByVehicleNumber',//?json='+Ext.encode(vehiclInfo),
            method:'POST',
            params:{json:json},
            success: function(response){
                var respText = Ext.util.JSON.decode(response.responseText);
                var data = respText.data;
                var retStatus = respText.status;
                if (retStatus == 'success') {
                    objectModel.data = data;
                    
                    //将未分配改为企业名显示
                    if (objectModel.data.arrangedOrgName=="未分配") {
//                      data.depName = '暂未分配';
                        var userType = window.sessionStorage.getItem('userType');
                        if(userType == '2' || userType == '6'){
                            //2是用车企业 6是租车企业
                            //3是部门
                            var entName = window.sessionStorage.getItem("organizationName");
                            objectModel.data.arrangedOrgName = entName;
                        }
                    }
                    
                    win.down("form").loadRecord(objectModel);
                    win.show();
                }
             },
            failure : function() {
                Ext.Msg.alert('Failure','Call interface error!');
            }
        });
    },

    updateTrafficPkg: function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex); 
        var enableTrafficPkg;
        var messageTitle,messageConent;
        if(rec.data.enableTrafficPkg == 0){
            enableTrafficPkg = 1;
            messageTitle = '开启流量状态';
            messageConent = '确定是否开启车辆 ';
        }else{
            enableTrafficPkg = 0;
            messageTitle = '关闭流量状态';
            messageConent = '确定是否关闭车辆 ';
        }      
        Ext.MessageBox.confirm(messageTitle, messageConent + rec.data.vehicleNumber+' 的流量状态?', function(btn){
            if(btn=='yes'){
                var input={
                    "vehicleNumber":rec.data.vehicleNumber,
                    "enableTrafficPkg":enableTrafficPkg
                };
                
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url: 'vehicle/setTrafficPackage',
                method : 'POST',
                params:{ json:json},
                scope:this,
                success : function(response,options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    if(respText.status == "success"){
                        Ext.Msg.alert('消息提示', '设置M2M状态成功！');
                    }else{
                        Ext.Msg.alert('消息提示', '设置M2M状态失败！');
                    }

                    Ext.getCmp('searchFormNoSecret').reset();
                    Ext.getCmp('searchFormNoSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('searchFormNoSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp("gridVehicleNoSecret").getStore("vehiNoSecretResults").load();

                    Ext.getCmp('searchFormSecret').reset();
                    Ext.getCmp('searchFormSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('searchFormSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp("gridVehicleSecret").getStore("vehiSecretResults").load();                   

                    }                
                }); 
            }   
        },this);
/*        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.create('Admin.view.vehiclemgmt.specialpolice.EditM2MInfo');
        win.down("form").loadRecord(rec);
        win.show();*/
    },

    modifyApproveStatus: function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex); 
        var noNeedApprove;
        var messageConent;
        if(rec.data.noNeedApprove == 1){
            messageConent = '确定是否关闭车辆 ';
        }else{
            messageConent = '确定是否开启车辆 ';
        }      
        Ext.MessageBox.confirm("修改车辆审批状态", messageConent + rec.data.vehicleNumber+' 的免审批状态?', function(btn){
            if(btn=='yes'){
                var input={
                    "id":rec.data.id,
                };
                
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url: 'vehicle/setNoNeedApprove',
                method : 'POST',
                params:{ json:json},
                scope:this,
                success : function(response,options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    if(respText.status == "success"){
                        Ext.Msg.alert('消息提示', '设置审批状态成功！');
                    }else{
                        Ext.Msg.alert('消息提示', '设置审批状态失败！');
                    }

                    Ext.getCmp('searchFormNoSecret').reset();
                    Ext.getCmp('searchFormNoSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('searchFormNoSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp("gridVehicleNoSecret").getStore("vehiNoSecretResults").load();

                    Ext.getCmp('searchFormSecret').reset();
                    Ext.getCmp('searchFormSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('searchFormSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp("gridVehicleSecret").getStore("vehiSecretResults").load();                   

                    }                
                }); 
            }   
        },this);
    },

    modifySecretStatus: function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex); 
        var enableSecret;
        var messageTitle,messageConent;
        if(rec.data.enableSecret == 0){
            enableSecret = 1;
            messageTitle = '开启涉密服务';
            messageConent = '确定是否开启车辆 ';
        }else{
            enableSecret = 0;
            messageTitle = '关闭涉密服务';
            messageConent = '确定是否关闭车辆 ';
        }      
        Ext.MessageBox.confirm(messageTitle, messageConent + rec.data.vehicleNumber+' 的涉密服务?', function(btn){
            if(btn=='yes'){
                var input={
                    "vehicleNumber":rec.data.vehicleNumber,
                    "enableSecret":enableSecret
                };
                
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url: 'vehicle/setVehSecret',
                method : 'POST',
                params:{ json:json},
                scope:this,
                success : function(response,options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    if(respText.status == "success"){
                        Ext.Msg.alert('消息提示', '设置涉密成功！');
                    }else{
                        Ext.Msg.alert('消息提示', '设置涉密失败！');
                    }

                    Ext.getCmp('searchFormNoSecret').reset();
                    Ext.getCmp('searchFormNoSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('searchFormNoSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp("gridVehicleNoSecret").getStore("vehiNoSecretResults").load();

                    Ext.getCmp('searchFormSecret').reset();
                    Ext.getCmp('searchFormSecret').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('searchFormSecret').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp("gridVehicleSecret").getStore("vehiSecretResults").load();                   

                    }                
                }); 
            }   
        },this);
    },

    openDeptChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.vehiclemgmt.specialpolice.DeptChooseWin",{
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
        var form;
        if(Ext.getCmp("searchFormNoSecret")){
            form = Ext.getCmp("searchFormNoSecret").getForm();
        }else{
            form = Ext.getCmp("searchFormSecret").getForm();
        }
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

