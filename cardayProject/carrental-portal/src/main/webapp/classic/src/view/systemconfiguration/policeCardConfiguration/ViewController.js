Ext.define('Admin.view.systemconfiguration.policeCardConfiguration.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoad : function() {
        var frmValues = this.lookupReference('searchForm').getValues();
        var page=Ext.getCmp('policeCardPage').store.currentPage;
        var limit=Ext.getCmp('policeCardPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("policeCardResults").proxy.extraParams = {
            "json" : pram
        }
    },

    onSearchClick :　function() {
        var VehicleStore = Ext.getCmp('gridPoliceCardConfiguration').getStore();
        VehicleStore.currentPage = 1;
        this.getViewModel().getStore("policeCardResults").load();
    },

    viewPoliceCard : function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.create('Admin.view.systemconfiguration.policeCardConfiguration.ViewPoliceCard');
        win.down("form").loadRecord(rec);
        switch(rec.data.approvalRating){
            case '0':
                win.down("form").getForm().findField('adminNameFirst').show();
                win.down("form").getForm().findField('adminNameFirst').setValue('赵处长');
                win.down("form").getForm().findField('adminNameSecond').hide();
                win.down("form").getForm().findField('adminNameThird').hide();
                win.down("form").getForm().findField('adminNameForth').hide();
                win.down("form").getForm().findField('adminNameFifth').hide();
                break;
            case '1':
                win.down("form").getForm().findField('adminNameFirst').show();
                win.down("form").getForm().findField('adminNameFirst').setValue('赵处长');
                win.down("form").getForm().findField('adminNameSecond').show();
                win.down("form").getForm().findField('adminNameSecond').setValue('王处长');
                win.down("form").getForm().findField('adminNameThird').hide();
                win.down("form").getForm().findField('adminNameForth').hide();
                win.down("form").getForm().findField('adminNameFifth').hide();
                break;
            case '2':
                win.down("form").getForm().findField('adminNameFirst').show();
                win.down("form").getForm().findField('adminNameFirst').setValue('赵处长');
                win.down("form").getForm().findField('adminNameSecond').show();
                win.down("form").getForm().findField('adminNameSecond').setValue('王处长');
                win.down("form").getForm().findField('adminNameThird').show();
                win.down("form").getForm().findField('adminNameThird').setValue('陈主任');
                win.down("form").getForm().findField('adminNameForth').hide();
                win.down("form").getForm().findField('adminNameFifth').hide();
                break;
            case '3':
                win.down("form").getForm().findField('adminNameFirst').show();
                win.down("form").getForm().findField('adminNameFirst').setValue('赵处长');
                win.down("form").getForm().findField('adminNameSecond').show();
                win.down("form").getForm().findField('adminNameSecond').setValue('王处长');
                win.down("form").getForm().findField('adminNameThird').show();
                win.down("form").getForm().findField('adminNameThird').setValue('陈主任');
                win.down("form").getForm().findField('adminNameForth').show();
                win.down("form").getForm().findField('adminNameForth').setValue('李处长');
                win.down("form").getForm().findField('adminNameFifth').hide();
                break;
            case '4':
                win.down("form").getForm().findField('adminNameFirst').show();
                win.down("form").getForm().findField('adminNameFirst').setValue('赵处长');
                win.down("form").getForm().findField('adminNameSecond').show();
                win.down("form").getForm().findField('adminNameSecond').setValue('王处长');
                win.down("form").getForm().findField('adminNameThird').show();
                win.down("form").getForm().findField('adminNameThird').setValue('陈主任');
                win.down("form").getForm().findField('adminNameForth').show();
                win.down("form").getForm().findField('adminNameForth').setValue('李处长');
                win.down("form").getForm().findField('adminNameFifth').show();
                win.down("form").getForm().findField('adminNameFifth').setValue('徐主任');
                break;
            }

        win.show();
    },

    addPoliceCard : function() {
        var win = Ext.create('Admin.view.systemconfiguration.policeCardConfiguration.AddPoliceCard');
        win.show();
    },

    updatePoliceCard : function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.create('Admin.view.systemconfiguration.policeCardConfiguration.UpdatePoliceCard');
        win.down("form").loadRecord(rec);
        switch(rec.data.approvalRating){
            case '0':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameFirst').down('textfield').setValue('赵处长');
                Ext.getCmp('adminNameSecond').hide();
                Ext.getCmp('adminNameThird').hide();
                Ext.getCmp('adminNameForth').hide();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '1':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameFirst').down('textfield').setValue('赵处长');
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameSecond').down('textfield').setValue('王处长');
                Ext.getCmp('adminNameThird').hide();
                Ext.getCmp('adminNameForth').hide();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '2':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameFirst').down('textfield').setValue('赵处长');
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameSecond').down('textfield').setValue('王处长');
                Ext.getCmp('adminNameThird').show();
                Ext.getCmp('adminNameThird').down('textfield').setValue('陈主任');
                Ext.getCmp('adminNameForth').hide();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '3':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameFirst').down('textfield').setValue('赵处长');
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameSecond').down('textfield').setValue('王处长');
                Ext.getCmp('adminNameThird').show();
                Ext.getCmp('adminNameThird').down('textfield').setValue('陈主任');
                Ext.getCmp('adminNameForth').show();
                Ext.getCmp('adminNameForth').down('textfield').setValue('李处长');
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '4':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameFirst').down('textfield').setValue('赵处长');
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameSecond').down('textfield').setValue('王处长');
                Ext.getCmp('adminNameThird').show();
                Ext.getCmp('adminNameThird').down('textfield').setValue('陈主任');
                Ext.getCmp('adminNameForth').show();
                Ext.getCmp('adminNameForth').down('textfield').setValue('李处长');
                Ext.getCmp('adminNameFifth').show();
                Ext.getCmp('adminNameFifth').down('textfield').setValue('徐主任');
                break;
            }
        win.show();
    },

    deletePoliceCard : function(grid, rowIndex, colIndex) {
        Ext.Msg.confirm('消息提示','确定要删除此警牌审批设置吗?',function(btn){
            if (btn == 'yes') {
                console.log('Delete Police Card Configuration Information!');
                Ext.Msg.alert('提示', '删除成功！');
            }
        });
    },

    onApprovalRatingSelect: function(combo,record,eOpts){
        switch(record.data.value){
            case '0':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameSecond').hide();
                Ext.getCmp('adminNameThird').hide();
                Ext.getCmp('adminNameForth').hide();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '1':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameThird').hide();
                Ext.getCmp('adminNameForth').hide();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '2':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameThird').show();
                Ext.getCmp('adminNameForth').hide();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '3':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameThird').show();
                Ext.getCmp('adminNameForth').show();
                Ext.getCmp('adminNameFifth').hide();
                break;
            case '4':
                Ext.getCmp('adminNameFirst').show();
                Ext.getCmp('adminNameSecond').show();
                Ext.getCmp('adminNameThird').show();
                Ext.getCmp('adminNameForth').show();
                Ext.getCmp('adminNameFifth').show();
                break;
        }
    },

    openAdminChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.systemconfiguration.policeCardConfiguration.AdminChooseWin",{
            adminId:combo.up().id
        });
        win.down("treepanel").getStore().load();
        win.show();
    },

    chooseAdmin: function(btn, e, eOpts){
        var tree = btn.up("window").down("treepanel");
        var selection = tree.getSelectionModel().getSelection();
        if(selection.length == 0){
            Ext.Msg.alert('提示', '请选择员工！');
            return;
        }       
        var select = selection[0].getData();
        if(select.level < 2){
            return;
        }
        //var deptId = select.id;
        var adminName = select.text;
        if(Ext.getCmp("addPoliceCardWindow")){            
            var form = Ext.getCmp("addPoliceCardWindow").down('form').getForm();
        }else{
            var form = Ext.getCmp("updatePoliceCardWindow").down('form').getForm();            
        }
        //form.findField("deptId").setValue(deptId);
        switch(btn.up("window").adminId){
            case 'adminNameFirst':
                form.findField("adminNameFirst").setValue(adminName);
                break;
            case 'adminNameSecond':
                form.findField("adminNameSecond").setValue(adminName);
                break;
            case 'adminNameThird':
                form.findField("adminNameThird").setValue(adminName);
                break;
            case 'adminNameForth':
                form.findField("adminNameForth").setValue(adminName);
                break;
            case 'adminNameFifth':
                form.findField("adminNameFifth").setValue(adminName);
                break;
        }
        btn.up("window").close();
    },

});

