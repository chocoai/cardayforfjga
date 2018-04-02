Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox'
			],

	onBeforeLoad : function() {
        var frmValues = this.lookupReference('searchForm').getValues();
        var page=Ext.getCmp('authorizedVehiclePage').store.currentPage;
        var limit=Ext.getCmp('authorizedVehiclePage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
            "deptName":frmValues.deptName,
            "levelType":frmValues.levelType,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("authorizedVehicleResults").proxy.extraParams = {
            "json" : pram
        }
    },

    onSearchClick :　function() {
        var VehicleStore = Ext.getCmp('authorizedVehicleId').getStore();
        VehicleStore.currentPage = 1;
        this.getViewModel().getStore("authorizedVehicleResults").load();
    },

    viewAuthorizedVehicle : function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.widget("viewauthorizedveh");
        win.down("form").loadRecord(rec);
        switch(rec.data.levelType){
            case 0:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('正厅级');
                    break;
                case 1:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('副厅级');
                    break;
                case 2:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('处级');
                    break;
                case 3:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('副处级');
                    break;
                case 6:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('科级');
                    break;
                case 7:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('副科级');
                    break;
                case 4:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('县级');
                    break;
                case 5:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('乡科级');
                    break;
        }
        Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('emergencyVehNum').setValue(rec.data.emergencyVehNum + '辆');
        Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('enforcementVehNum').setValue(rec.data.enforcementVehNum + '辆');
        Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('specialVehNum').setValue(rec.data.specialVehNum + '辆');
        Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('normalVehNum').setValue(rec.data.normalVehNum + '辆');
        Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('majorVehNum').setValue(rec.data.majorVehNum + '辆');
        win.show();
    },

    addAuthorizedVeh : function() {
        win = Ext.widget('addAuthorizedVeh');
        win.show();
    },

    updateAuthorizedVehicle : function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.widget("editAuthorizedVeh");
        win.down("form").loadRecord(rec);
        switch(rec.data.levelType){
            case 0:
                    Ext.getCmp('editAuthorizedVeh').down('form').getForm().findField('levelType').setValue('正厅级');
                    break;
                case 1:
                    Ext.getCmp('editAuthorizedVeh').down('form').getForm().findField('levelType').setValue('副厅级');
                    break;
                case 2:
                    Ext.getCmp('editAuthorizedVeh').down('form').getForm().findField('levelType').setValue('处级');
                    break;
                case 3:
                    Ext.getCmp('editAuthorizedVeh').down('form').getForm().findField('levelType').setValue('副处级');
                    break;
                case 6:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('科级');
                    break;
                case 7:
                    Ext.getCmp('viewauthorizedveh').down('form').getForm().findField('levelType').setValue('副科级');
                    break;
                case 4:
                    Ext.getCmp('editAuthorizedVeh').down('form').getForm().findField('levelType').setValue('县级');
                    break;
                case 5:
                    Ext.getCmp('editAuthorizedVeh').down('form').getForm().findField('levelType').setValue('乡科级');
                    break;
        }
        win.show();
    },

    deleteAuthorizedVehicle : function(grid, rowIndex, colIndex) {
        Ext.Msg.confirm('消息提示','确定要删除此编制记录吗?',function(btn){
            if (btn == 'yes') {
                console.log('Delete Authorized Vehicle!');
                Ext.Msg.alert('提示', '删除此编制！');
            }
        });
    },

    openDeptChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.vehiclemgmt.authorizedmgmt.DeptChooseWin",{
            deptId:combo.up("form").getForm().findField("deptId").getValue()
        });
        win.down("treepanel").getStore().load();
        win.show();
    },

    chooseDept: function(btn, e, eOpts){
        var tree = btn.up("window").down("treepanel");
        var selection = tree.getSelectionModel().getSelection();
        if(selection.length == 0){
            Ext.Msg.alert('提示', '请选择单位！');
            return;
        }       
        var select = selection[0].getData();
        var deptId = select.id;
        var deptName = select.text;
        var form;
        if(Ext.getCmp("addAuthorizedVeh")){
            form = Ext.getCmp("addAuthorizedVeh").down('form').getForm();
        }else if(Ext.getCmp("addAuthorizedVehApply")){
            form = Ext.getCmp("addAuthorizedVehApply").down('form').getForm();
        }
        form.findField("deptId").setValue(deptId);
        form.findField("deptName").setValue(deptName);
        btn.up("window").close();
    },
    
    onBeforeLoadApply:function(){
        var page=Ext.getCmp('authorizedApplyPage').store.currentPage;
        var limit=Ext.getCmp('authorizedApplyPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("authorizedApplyResults").proxy.extraParams = {
            "json" : pram
        }

        this.getViewModel().getStore("authorizedApplyResults").load();
	},
	
	addAuthorizedVehApply : function() {
        win = Ext.widget('addAuthorizedVehApply');
        win.show();
	},

	 openAddVehDeptChooseWin: function(combo, event, eOpts){
	    	var win = Ext.create("Admin.view.vehiclemgmt.vehicleInfomgmt.DeptChooseWin",{
	    		parentId:combo.up("form").getForm().findField("deptId").getValue(),
	    		parentName: combo.up("form").getForm().findField("deptName").getValue(),
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
	
	    AddAuthorizedApplyClick:function(btn){
	 		/*var enterinfo = this.lookupReference('addAuthorizedVehApply').getValues();
	 		
	 		if (this.lookupReference('addAuthorizedVehApply').getForm().isValid()) {
	 			var json = Ext.encode(enterinfo);
	 			Ext.Ajax.request({
	 	        	url:'vehicle/authorized/create',//?json='+Ext.encode(enterinfo),
	 				method:'POST',
	 	        	params:{json:json},
	 				//headers: {'Content-Type':'application/json','charset':'UTF-8'},
	 				success: function(res){
	 					var appendData=Ext.JSON.decode(res.responseText);
	 					if(appendData.status=='success'){
	 						btn.up('window').close();
	 					   // Ext.getCmp("searchVehicle").fireEvent("click");
	 						Ext.Msg.alert("提示信息", '添加成功');
	 						Ext.getCmp("gridAuthorizedApply").getStore('authorizedApplyResults').load();
	 					}else{
	 						Ext.Msg.alert("提示信息", appendData.error);
	 					}
	 				 }
	 	        	,
	 				failure : function() {
	 					Ext.Msg.alert('Failure','Call interface error!');
	 				}
	 	        });
	 		}*/
	 		
	 	   var form=this.getView().down('form').getForm();
			if (form.isValid()) {
	            form.submit({
	                url: 'vehicle/authorized/create',
	                method:'post',
	          //      defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	                //async: true,
	                waitMsg: 'Uploading your file...',
	                success: function(form,action) {
	                	console.log('成功'+action.response.responseText);
	                	var result = Ext.util.JSON.decode(action.response.responseText);
	                	btn.up('window').close();
	                	Ext.Msg.alert("提示信息", '添加成功');
 						Ext.getCmp("gridAuthorizedApply").getStore('authorizedApplyResults').load();
	                },
			        failure : function(form,action) {
			        	console.log('失败'+action.response.responseText);
			        	var result = Ext.util.JSON.decode(action.response.responseText);
			        	if (result.msg!=null || result.msg!='') {
			        		Ext.Msg.alert('消息提示', result.msg);
			        	}
			        },
		        });
			}
 	},
 	
 	onSearchClick:function(){
		var VehicleStore = this.lookupReference('gridAuthorizedVehicle').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("authorizedVehicleResults").load();
	},
	
	onBeforeLoadRefuse:function(){
        
        var VehicleStore = this.lookupReference('gridAuthorizedRefuse').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("authorizedApplyRefuseResults").load();
	},
	
	viewAuthorizedVehiApply : function(grid, rowIndex, colIndex) {
	        var rec = grid.getStore().getAt(rowIndex);
	        var win = Ext.widget("viewAuthorizedVehApply");
	        var authorizedAttach=rec.data.attachName;
	        if(authorizedAttach!=null){
	        	 var attaches= authorizedAttach.split(",");
	  	       // var url=window.sessionStorage.getItem("imageUrl")+'resources/upload/authorized/';
	  	        for(var i=0;i<attaches.length;i++){
	  	        	var	url=window.sessionStorage.getItem("imageUrl")+'resources/upload/authorized/'+attaches[i];
	  	        	var txfield = Ext.create("Ext.toolbar.TextItem",{
	  	        		html:"<a href="+url+" target='_blank' style='margin-left:30px'>编制申请附件"+(i+1)+"</a>"
	  	        	});
	  	        	win.down("form").add(txfield);
	  	        }
	        }
	       
	        
	        win.down("form").loadRecord(rec);
	        win.show();
	    },
	    
	AddAttach:function(){
		var count = Ext.ComponentQuery.query('textfield[name= "file"]').length;
		if(count<3){
			 var obj =  Ext.getCmp('addAuthorizedVehApply').down('form').getForm().findField("file").cloneConfig();
			 Ext.getCmp('addAuthorizedVehApply').down('form').add(obj);
		}else{
			Ext.Msg.alert('消息提示', "最多只能上传3个附件");
		}
	 },

    refuseAuthorizedVehi:function(grid, rowIndex, colIndex){
        var win = Ext.widget("authorizedVehiRefuseWin");
        win.show();
    },

    authorizedVehiApproved:function(grid, rowIndex, colIndex){
        var win = Ext.widget("authorizedVehiApprovedWin");
        win.show();
    },
});

