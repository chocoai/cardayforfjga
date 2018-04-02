/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.runningstatistic.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [ 
			'Ext.window.MessageBox'
	],
	alias : 'controller.runningstatisticcontroller',
	
	onSearchClick : function(it, e) {
		var VehicleStore = this.lookupReference('gridvehicle').getStore();
		VehicleStore.currentPage = 1;
		this.onBeforeLoad();
	},
	
	onBeforeLoad : function() {		
 		//车辆列表
 		this.getViewModel().getStore("vehicleList").load();

			//总里程、平均里程、总耗油量、平均耗油量、总时长、平均时长
	 		var searchParam = this.lookupReference('searchForm').getValues();
		    var selfDept,childDept;

			if(searchParam.includeSelf == undefined){
				selfDept = false;
			}else{
				selfDept = true;
			}

			if(searchParam.includeChild == undefined){
				childDept = false;
			}else{
				childDept = true;
			}

			if(Ext.getCmp('runningstatistic_SearchForm_id').getForm().isValid()){
				var page=Ext.getCmp('GridVehicle_page').store.currentPage;
				var limit=Ext.getCmp('GridVehicle_page').store.pageSize;

				parm = {
				    "currentPage" 	: page,
					"numPerPage" : limit,
					"vehicleNumber": searchParam.vehicleNumber,
					'orgId':searchParam.deptId,
					'selfDept': selfDept,				
					'childDept': childDept,
				};
				parm = Ext.encode(parm);		
				this.getViewModel().getStore("vehicleList").proxy.extraParams = {
					"json" : parm
				}
			}else{
	        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
	        }
	},
	
	onExcelClick :　function() {
		console.log('onExcelClick');
		var frmValues = this.lookupReference('searchForm').getValues();

	    var selfDept,childDept;

		if(frmValues.includeSelf == undefined){
			selfDept = false;
		}else{
			selfDept = true;
		}

		if(frmValues.includeChild == undefined){
			childDept = false;
		}else{
			childDept = true;
		}

		if(Ext.getCmp('runningstatistic_SearchForm_id').getForm().isValid()){	
			var input = {
					"vehicleNumber": frmValues.vehicleNumber,
					'orgId':frmValues.deptId,
					'selfDept': selfDept,				
					'childDept': childDept,
				};
			window.location.href = './resources/template/reportmgmt/driving_report.xlsx';
		}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
	},

	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.reportmgmt.runningstatistic.DeptChooseWin",{
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
     	var form = Ext.getCmp("runningstatistic_SearchForm_id").getForm();
     	form.findField("deptId").setValue(deptId);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
    },

    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.includeSelf == null && value.includeChild == null){
     		Ext.Msg.alert("提示信息", '本部门和子部门请至少选择一个！');
     		if(chk.boxLabel == "本部门"){
     			group.items.items[1].setValue(true);
     		}else{
     			group.items.items[0].setValue(true);
     		}
     	}
     }
	
});
