/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.vehicleusereport.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [ 
			'Ext.window.MessageBox'
	],
	alias : 'controller.viewcontroller',
	
	onSearchClick : function(it, e) {
		var VehicleStore = this.lookupReference('gridvehicle').getStore();
		VehicleStore.currentPage = 1;
		this.onBeforeLoad();
	},
	
	onBeforeLoadVehicleUse : function() {
		var searchParam = Ext.getCmp('vehicleusereport_SearchForm_id').getValues();	
		var starttime = searchParam.startDate;
		var endtime = searchParam.endDate;		
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

		if(starttime == ''){
            var initStartDay = new Date();
            /*查询30天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
    		Ext.getCmp('vehicleusereport_SearchForm_id').getForm().findField('startDate').setValue(initStartDay);
    		starttime=Ext.getCmp('vehicleusereport_SearchForm_id').getValues().startDate;
		}

        if(Ext.getCmp('vehicleusereport_SearchForm_id').getForm().isValid()){
			var parm = {
	 				'starttime': starttime, 
	 				'endtime': endtime,
					'orgId':searchParam.deptId,
					'selfDept': selfDept,				
					'childDept': childDept,
	 			};
			parm = Ext.encode(parm);		
			this.getViewModel().getStore("vehicleUse").proxy.extraParams = {
				"json" : parm
			}
		}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
	},
	
	onBeforeLoadVehicle : function() {
		var searchParam = Ext.getCmp('vehicleusereport_SearchForm_id').getValues();
		var starttime = searchParam.startDate;
		var endtime = searchParam.endDate;		
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
		var page=Ext.getCmp('GridVehicle_id').store.currentPage;
		var limit=Ext.getCmp('GridVehicle_id').store.pageSize;
		
		if(starttime == ''){
            var initStartDay = new Date();
            /*查询30天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
    		Ext.getCmp('vehicleusereport_SearchForm_id').getForm().findField('startDate').setValue(initStartDay);
    		starttime=Ext.getCmp('vehicleusereport_SearchForm_id').getValues().startDate;
		}

		if(Ext.getCmp('vehicleusereport_SearchForm_id').getForm().isValid()){
			parm = {
					'starttime': starttime, 
	 				'endtime': endtime,
				    "currentPage" 	: page,
					"numPerPage" : limit,
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
	
	onBeforeLoad : function() {		
 		var userType = window.sessionStorage.getItem('userType');
// 		if (userType == '2') {                                               
// 			this.getViewModel().getStore("vehicleUse").getProxy().api.read = 'usage/report/getVehicleLinePropertyDataByDayRange';
//			this.getViewModel().getStore("vehicleList").getProxy().api.read = 'usage/report/getVehiclePropertyDataByDayRange';
// 		}
		
 		//车辆使用情况曲线图
 		this.getViewModel().getStore("vehicleUse").load();
 		//车辆列表
 		this.getViewModel().getStore("vehicleList").load();
 		
/* 		// 设置平均里程, 平均油耗
 		var me = this;
 		this.getViewModel().getStore("vehicleUse").reload({
 			callback:function(r,options,success){
 				if(success){
 					//调用平均里程, 平均油耗重置方法
 					chart = Ext.getCmp('chart');
 		            if (chart != null) {
 		            	 axisMileage = chart.getAxis(0);
 		                 me.onAvgMileageChange(axisMileage);
 		                 axisFuel = chart.getAxis(1);
 		                 me.onAvgFuelChange(axisFuel);
 		            }
 				};
 			}
 		});*/
 		
		//登录的无子部门的部门管理员不可以查看
		var flag = false;
		Ext.Ajax.request({
            url: './department/tree',
            method: 'GET',
            async: false,
            success: function(response) {
                var resp = Ext.decode(response.responseText);
                if(resp.children[0].children.length == 0){
                	flag = true;
                }else{
                	flag = false;
                }
            },
            failure: function(response) {
                Ext.Msg.alert('消息提示', '无法获取节点信息！');
                return;
            }
        });
		if (!(userType == '3' && flag)) {
			//总里程、平均里程、总耗油量、平均耗油量、总时长、平均时长
	 		var searchParam = this.lookupReference('searchForm').getValues();
	 		var starttime = searchParam.startDate;
		    var endtime = searchParam.endDate;
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

			if(starttime == ''){
	            var initStartDay = new Date();
	            /*查询30天前数据*/
	            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
	    		Ext.getCmp('vehicleusereport_SearchForm_id').getForm().findField('startDate').setValue(initStartDay);
	    		starttime=Ext.getCmp('vehicleusereport_SearchForm_id').getValues().startDate;
			}

			if(Ext.getCmp('vehicleusereport_SearchForm_id').getForm().isValid()){
		 		var input = {
		 				'starttime': starttime, 
		 				'endtime': endtime,
						'orgId':searchParam.deptId,
						'selfDept': selfDept,				
						'childDept': childDept,
		 			};
		 	    var json = Ext.encode(input);
				Ext.Ajax.request({
					url : 'usage/report/getPieAndColumnarData',//?json=' + Ext.encode(input),
					method : 'POST',
					async: false,
		        	params:{json:json},
					//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
					success : function(response,options) {	
						//console.info(response.responseText);
						var respText = Ext.util.JSON.decode(response.responseText);
						if (respText.status == 'success') {
							if(respText.data != null && respText.data !=""){
								if(respText.data.pieList != null){
									//总里程,总耗油量，总行使时长
									var totalMileage = respText.data.pieList[0].dataList;
									var totalFuel = respText.data.pieList[1].dataList;
									var totalTravelTime = respText.data.pieList[2].dataList;
									this.getViewModel().getStore("totalMileage").loadData(totalMileage);
									this.getViewModel().getStore("totalFuel").loadData(totalFuel);
									this.getViewModel().getStore("totalTravelTime").loadData(totalTravelTime);
								}else{									
									this.getViewModel().getStore("totalMileage").loadData('');
									this.getViewModel().getStore("totalFuel").loadData('');
									this.getViewModel().getStore("totalTravelTime").loadData('');
								}
								if(respText.data.pieList != null){
									//平均里程，平均耗油量，平均时长
									var averageMileage = respText.data.columnarList[0].dataList;
									var averageFuel = respText.data.columnarList[1].dataList;
									var averageTravelTime = respText.data.columnarList[2].dataList;
									Ext.getCmp('averageMileageId').setHtml("平均值："+respText.data.columnarList[0].avgVal);
									Ext.getCmp('averageFuelId').setHtml("平均值："+respText.data.columnarList[1].avgVal);
									Ext.getCmp('averageTraveltimeId').setHtml("平均值："+respText.data.columnarList[2].avgVal);
									this.getViewModel().getStore("averageMileage").loadData(averageMileage);
									this.getViewModel().getStore("averageFuel").loadData(averageFuel);
									this.getViewModel().getStore("averageTravelTime").loadData(averageTravelTime);	
								}else{							
									this.getViewModel().getStore("averageMileage").loadData('');
									this.getViewModel().getStore("averageFuel").loadData('');
									this.getViewModel().getStore("averageTravelTime").loadData('');
								}
							}								
						} 	
					},
//					failure : function() {
//						Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//					},
					scope:this
					});
			}else{
	        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
	        }
		}else{
			Ext.getCmp('vehicleusereport_totalmileage_id').setHidden(true);
			Ext.getCmp('vehicleusereport_averagemileage_id').setHidden(true);
			Ext.getCmp('vehicleusereport_totalfuel_id').setHidden(true);
			Ext.getCmp('vehicleusereport_averagefuel_id').setHidden(true);
			Ext.getCmp('vehicleusereport_totaltraveltime_id').setHidden(true);
			Ext.getCmp('vehicleusereport_averagetraveltime_id').setHidden(true);
		}
	},
	
	onResetClick : function() {
		this.lookupReference('searchForm').getForm().reset();
		this.getViewModel().getStore("myChartByTraffic").removeAll();
	},
	
	onExcelClick :　function() {
		console.log('onExcelClick');
		var frmValues = this.lookupReference('searchForm').getValues();

		var starttime = frmValues.startDate;
	    var endtime = frmValues.endDate;
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

		if(starttime == ''){
            var initStartDay = new Date();
            /*查询30天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
    		Ext.getCmp('vehicleusereport_SearchForm_id').getForm().findField('startDate').setValue(initStartDay);
    		starttime=Ext.getCmp('vehicleusereport_SearchForm_id').getValues().startDate;
		}
		if(Ext.getCmp('vehicleusereport_SearchForm_id').getForm().isValid()){	
			var input = {
					"starttime" : starttime,
					"endtime" : endtime,
					'orgId':frmValues.deptId,
					'selfDept': selfDept,				
					'childDept': childDept,
				};
			window.location.href = 'usage/report/exportVehiclePropertyData?json='  + Ext.encode(input);
		}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
	},

    onExcelClickDemo :　function() {
        Ext.getCmp('vehicleReportSearch').hide();
        Ext.getCmp('export2pdf').hide();
        Ext.getCmp('export2excel').hide();
        ES6Promise.polyfill();
        var targetDom = $("#tbdc_1");
        //把需要导出的pdf内容clone一份，这样对它进行转换、微调等操作时才不会影响原来界面
        var bodyDom = $('body');
        var copyDom = bodyDom.clone();
        //新的div宽高跟原来一样，高度设置成自适应，这样才能完整显示节点中的所有内容（比如说表格滚动条中的内容）
        copyDom.width(bodyDom.width() + "px");
        copyDom.height(bodyDom.height()+ 1000 + "px");
        //ps:这里一定要先把copyDom append到body下，然后再进行后续的glyphicons2canvas处理，不然会导致图标为空
        //$('#maincontainerwrap-targetEl').append(copyDom);
        $('body').append(copyDom);

        html2canvas(targetDom, {
            onrendered: function (canvas) {

                var imgData = canvas.toDataURL('image/jpeg');
                var img = new Image();
                img.src = imgData;
                //根据图片的尺寸设置pdf的规格，要在图片加载成功时执行，之所以要*0.225是因为比例问题
                img.onload = function () {
                    //此处需要注意，pdf横置和竖置两个属性，需要根据宽高的比例来调整，不然会出现显示不完全的问题
                    if (this.width > this.height) {
                        var doc = new jsPDF('l', 'mm', [this.width * 0.225, this.height * 0.225]);
                    } else {
                        var doc = new jsPDF('p', 'mm', [this.width * 0.225, this.height * 0.225]);
                    }
                    doc.addImage(imgData, 'jpeg', 0, 0, this.width * 0.225, this.height * 0.225);
                    //根据下载保存成不同的文件名
					setTimeout(function(){
                        doc.save('pdf_' + new Date().getTime() + '.pdf');
					},1000);
                };
                //删除复制出来的div
                copyDom.remove();
            },
            background: "#fff",
            //这里给生成的图片默认背景，不然的话，如果你的html根节点没设置背景的话，会用黑色填充。
            allowTaint: true //避免一些不识别的图片干扰，默认为false，遇到不识别的图片干扰则会停止处理html2canvas
        });
        Ext.getCmp('vehicleReportSearch').show();
        Ext.getCmp('export2pdf').show();
        Ext.getCmp('export2excel').show();
        // $("#button-1139").show();
        // $("#button-1141").show();
    },

	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.reportmgmt.vehicleusereport.DeptChooseWin",{
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
     	var form = Ext.getCmp("vehicleusereport_SearchForm_id").getForm();
     	form.findField("deptId").setValue(deptId);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
    },

    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.includeSelf == null && value.includeChild == null){
//     		chk.setChecked(true);
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
