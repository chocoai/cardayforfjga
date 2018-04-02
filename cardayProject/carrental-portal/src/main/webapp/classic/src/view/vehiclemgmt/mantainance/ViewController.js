/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    onBeforeLoad : function() {
    	var currentPage=Ext.getCmp('mantainGridPage').getStore().currentPage;
    	var parm = this.lookupReference('searchForm').getValues();
        if(window.sessionStorage.getItem("mantainance") == '1'){
            parm.searchScope = '1M';
            this.lookupReference('searchForm').getForm().findField('searchScope').setValue('1M');
            window.sessionStorage.setItem("mantainance", '0');
        }
    	var vehicleNumber = parm.vehicleNumber;

        var selfDept,childDept;

        if(parm.includeSelf == undefined){
            selfDept = false;
        }else{
            selfDept = true;
        }

        if(parm.includeChild == undefined){
            childDept = false;
        }else{
            childDept = true;
        }

    	var pram={
    			'currentPage' : currentPage,
    			'numPerPage': 10,
    			'vehicleNumber' : vehicleNumber,
    			'searchScope':parm.searchScope,
                'deptId':parm.deptId,
                'includeSelf': selfDept,               
                'includeChild': childDept,
    	};
    	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
			"json" : Ext.encode(pram)
		}
    },
    loadGirdData : function() {
//    	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.url = "app/data/vehiclemgmt/mantainance/mantainData.json";
    	var pram={
    			'currentPage' : 1,
    			'numPerPage': 10
    	};
    	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
			"json" : Ext.encode(pram)
		}
    	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
    },
    onClickReset : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
//    	console.log('selected id is ' + rec.data.id);
    	var curTime =  this.getCurrentDate() + ' 00:00:00';
    	var input = {
    					'id' : rec.data.id,
    					'curTime' : curTime,
    					'headerMaintenanceMileage' : rec.data.headerMaintenanceMileage
    	};
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
			url : 'maintenance/reset',//?json=' + Ext.encode(input),
	        method : 'POST',
            params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	
	        	//表数据加载
	        	var pram={
	        			'currentPage' : 1,
	        			'numPerPage': 10
	        	};
	        	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
	    			"json" : Ext.encode(pram)
	    		}
	        	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
	        }
//            ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        }
	    });
    },
    getCurrentDate : function() {
    	var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
        return currentdate;
    },
    onClickSearch : function() {
//    	console.log('++++on click search++++');



    	Ext.getCmp('mantainGridPage').getStore().currentPage = 1;
    	var parm = this.lookupReference('searchForm').getValues();
    	var vehicleNumber = parm.vehicleNumber;
//    	console.log('vehicleNumber:' + vehicleNumber);
    	var arrangedOrgName = parm.arrangedOrgName;
//    	console.log('arrangedOrgName:' + arrangedOrgName);
    	var pram={
    			'currentPage' : 1,
    			'numPerPage' : 10,
    			'vehicleNumber' : vehicleNumber,
    			'deptId' : arrangedOrgName
    	};
    	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
			"json" : Ext.encode(pram)
		}
    	Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
    },
    onExcelClick : function() {
//    	console.log('导出excel...');
    	var gridPanel = Ext.getCmp('mantaingrid');
		var record = gridPanel.getSelectionModel().getSelection();
		if(record.length == 0) {
			Ext.Msg.alert('消息提示', '请选择导出文件');
			return;
		}
		var id = '';
		for (var i=0; i<record.length; i++) {
			id += record[i].data.id + ',';
		}
//		console.log('id:' + id.substring(0,id.length-1));
		var input = {
						'ids' : id.substring(0,id.length-1)
		};
		window.location.href = 'maintenance/export?json='  + Ext.encode(input);
    },
    onUploadClick: function() {
    	var win = Ext.widget("uploadMantainFile", {
		title: '批量导入',
		closable: true,
		buttonAlign : 'center',
	});
	win.show();
    },
    onAddCilick: function() {
    	var win = Ext.widget("addMantainanceView", {
    		title: '新增保养记录',
    		closable: true,
    		buttonAlign : 'center',
    	});
    	win.show();
    },
    submitTable : function() {
    	function saveReport() {
    		console.log('++++saveReport++++');
    	}
    	console.log('++++submitTable++++');
        document.write("<form action='maintenance/export'  target='_blank' method='post' name='form1' style='display:none'>");  
        document.write("</form>");  
        document.form1.submit();
        
    },
    showResetView : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("resetMantainView", {
    		title: '确认车辆已经保养？重置后里程数将更新。',
    		closable: true,
    		buttonAlign : 'center',
    		recordId : rec.data.id,
    		maintenanceTime : rec.data.maintenanceTime
    	});
    	win.show();
    },
    resetMantain : function(btn) {
    	var id = Ext.getCmp('resetMantainView').recordId;
//    	var maintenanceTime = Ext.getCmp('resetMantainView').maintenanceTime;
    	var manmainInfo = this.lookupReference('resetMantainView').getValues();
//    	console.log('id:' + id);
//    	console.log('totalMileage:' + manmainInfo.totalMileage);
//    	console.log('lastMantainTime:' + manmainInfo.lastMantainTime);
//    	console.log('mantainMileage:' + manmainInfo.mantainMileage);
//    	console.log('maintenanceTime:' + manmainInfo.maintenanceTime);
    	
    	var input = {
				'id' : id,
				'curTime' : manmainInfo.lastMantainTime + ' 00:00:00',
				'headerMaintenanceMileage' : manmainInfo.totalMileage,
				'maintenanceTime' : manmainInfo.maintenanceTime,
				'mantainMileage' : manmainInfo.mantainMileage
    	};
        var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'maintenance/reset',//?json=' + Ext.encode(input),
		    method : 'POST',
            params:{json:json},
		    //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		    success : function(response,options) {
//		    	var respText = Ext.util.JSON.decode(response.responseText);
		    	
		    	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
	        	console.log('retStatus:' + retStatus);
				if (retStatus == 'success') {
					Ext.Msg.alert('提示信息','重置操作已完成');
					//关闭窗口
					btn.up("resetMantainView").close();
					//表数据加载
					var pram={
							'currentPage' : 1,
							'numPerPage': 10
					};
					Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
						"json" : Ext.encode(pram)
					}
					Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
				}else {
					Ext.Msg.alert('消息提示',respText.data);
				}
		    	
		    }
//            ,
//		    failure : function() {
//		        Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		    }
		});
    },
    onClickSetup : function(grid, rowIndex, colIndex) {
//    	console.log('++++onClickSetup++++');
    	var rec = grid.getStore().getAt(rowIndex);
//    	console.log('id:' + rec.data.id);
    	var win = Ext.widget("setThresholdWindow", {
    		title: '设置维保规则',
    		closable: true,
    		buttonAlign : 'center',
    		mid : rec.data.id,
    	});
    	win.down("form").loadRecord(rec);
    	win.show();
    },

    openDeptChooseWin: function(combo, event, eOpts){
        var win = Ext.create("Admin.view.vehiclemgmt.mantainance.DeptChooseWin",{
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
        var form = Ext.getCmp("mantainanceSearchForm").getForm();
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
     }
    
    
});

