/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportexception.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [ 
			'Ext.window.MessageBox'
	],
	//alias : 'controller.viewcontroller',

	onSearchClick : function(it, e) {
		console.log('onSearchClick hahah');
		this.onBeforeLoad();
	},


	
	onBeforeLoad : function() {
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

		var startDay = searchParam.startDate;
		var endDay = searchParam.endDate;

		if(endDay == ""){
			var time = new Date();
            var m = time.getMonth() + 1;
            endDay = time.getFullYear() + "-" + m + "-" + time.getDate();
		}

		if(startDay == ""){
            var initStartDay = new Date(endDay);
            /*查询30天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
            this.lookupReference('searchForm').getForm().findField('startDate').setValue(initStartDay);
            var m = initStartDay.getMonth() + 1;
            startDay = initStartDay.getFullYear() + "-" + m + "-" + initStartDay.getDate();
		}


        if(this.lookupReference('searchForm').getForm().isValid()){		
	 		var input = {
	 				'startDay': startDay, 
	 				'endDay': endDay,
					'orgId':searchParam.deptId,
					'selfDept': selfDept,				
					'childDept': childDept,
	 			};
	 		var json = Ext.encode(input);
			Ext.Ajax.request({
				url : 'vehicleAlert/findVehicleAlertPieAndColumnarData',//?json=' + Ext.encode(input),
				method : 'POST',
				async: false,
		        params:{json:json},
				//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
				success : function(response,options) {	
					//console.info(response.responseText);
						
					var respText = Ext.util.JSON.decode(response.responseText);
					if (respText.status == 'success' && respText.data != "") {
						var overspeedAlarmflag = true;
						var enterAlarmflag = true;
						var overborderAlarmflag = true;
						var overborderMileageflag = true;
						var averageOverspeedAlarmflag = true;
						var averageEnterAlarmflag = true;
						var averageOverborderAlarmflag = true;
						var averageOverborderMileageflag = true;

							for(var i = 0; i < respText.data.pieList.length; i++){
								if(respText.data.pieList[i].alertType == 'OVERSPEED'){
									overspeedAlarmflag = false;
									var overspeedAlarm = respText.data.pieList[i].dataList;
							        this.getViewModel().getStore("overspeedAlarm").loadData(overspeedAlarm);
								}else if(respText.data.pieList[i].alertType == 'VEHICLEBACK'){
									enterAlarmflag = false;
									var enterAlarm = respText.data.pieList[i].dataList;
							        this.getViewModel().getStore("enterAlarm").loadData(enterAlarm);
								}else if(respText.data.pieList[i].alertType == 'OUTBOUND'){
									overborderAlarmflag = false;
									var overborderAlarm = respText.data.pieList[i].dataList;
							        this.getViewModel().getStore("overborderAlarm").loadData(overborderAlarm);
								}else if(respText.data.pieList[i].alertType == 'OUTBOUNDKILOS'){
									overborderMileageflag = false;
									var overborderMileage = respText.data.pieList[i].dataList;
							        this.getViewModel().getStore("overborderMileage").loadData(overborderMileage);
								}
							}

							if(overspeedAlarmflag){
								this.getViewModel().getStore("overspeedAlarm").loadData('');
							}
							if(enterAlarmflag){
								this.getViewModel().getStore("enterAlarm").loadData('');
							}
							if(overborderAlarmflag){
								this.getViewModel().getStore("overborderAlarm").loadData('');
							}
							if(overborderMileageflag){
								this.getViewModel().getStore("overborderMileage").loadData('');
							}


							for(var i = 0; i < respText.data.columnarList.length; i++){
								if(respText.data.columnarList[i].alertType == 'OVERSPEED'){
									averageOverspeedAlarmflag = false;
									Ext.getCmp('averageOverspeedAlarmId').setHtml("平均值："+respText.data.columnarList[i].averageNumber);
									var averageOverspeedAlarm = respText.data.columnarList[i].dataList;
							        this.getViewModel().getStore("averageOverspeedAlarm").loadData(averageOverspeedAlarm);
								}else if(respText.data.columnarList[i].alertType == 'VEHICLEBACK'){
									averageEnterAlarmflag = false;							        
									Ext.getCmp('averageEnterAlarmId').setHtml("平均值："+respText.data.columnarList[i].averageNumber);
									var averagEnterAlarm = respText.data.columnarList[i].dataList;
							        this.getViewModel().getStore("averagEnterAlarm").loadData(averagEnterAlarm);
								}else if(respText.data.columnarList[i].alertType == 'OUTBOUND'){
									averageOverborderAlarmflag = false;
									Ext.getCmp('averageOverborderAlarmId').setHtml("平均值："+respText.data.columnarList[i].averageNumber);
									var averageOverborderAlarm = respText.data.columnarList[i].dataList;
							        this.getViewModel().getStore("averageOverborderAlarm").loadData(averageOverborderAlarm);
								}else if(respText.data.columnarList[i].alertType == 'OUTBOUNDKILOS'){
									averageOverborderMileageflag = false;
									Ext.getCmp('averageOverborderMileageId').setHtml("平均值："+respText.data.columnarList[i].averageNumber);
									var averageOverborderMileage = respText.data.columnarList[i].dataList;
							        this.getViewModel().getStore("averageOverborderMileage").loadData(averageOverborderMileage);
								}
							}

							if(averageOverspeedAlarmflag){
								this.getViewModel().getStore("averageOverspeedAlarm").loadData('');
							}
							if(averageEnterAlarmflag){
								this.getViewModel().getStore("averagEnterAlarm").loadData('');
							}
							if(averageOverborderAlarmflag){
								this.getViewModel().getStore("averageOverborderAlarm").loadData('');
							}
							if(averageOverborderMileageflag){
								this.getViewModel().getStore("averageOverborderMileage").loadData('');
							}
					}else{
						Ext.Msg.alert('异常提示','未获得用车异常统计数据！');
						this.getViewModel().getStore("overspeedAlarm").loadData('');
						this.getViewModel().getStore("enterAlarm").loadData('');
						this.getViewModel().getStore("overborderAlarm").loadData('');
						this.getViewModel().getStore("overborderMileage").loadData('');
						this.getViewModel().getStore("averageOverspeedAlarm").loadData('');
						this.getViewModel().getStore("averagEnterAlarm").loadData('');
						this.getViewModel().getStore("averageOverborderAlarm").loadData('');
						this.getViewModel().getStore("averageOverborderMileage").loadData('');
					}
				},
//				failure : function() {
//					Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//				},
				scope:this
				});
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

		var startDay = frmValues.startDate;
		var endDay = frmValues.endDate;

		if(endDay == ""){
			var time = new Date();
            var m = time.getMonth() + 1;
            endDay = time.getFullYear() + "-" + m + "-" + time.getDate();
		}

		if(startDay == ""){
            var initStartDay = new Date(endDay);
            /*查询10天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
            this.lookupReference('searchForm').getForm().findField('startDate').setValue(initStartDay);
            var m = initStartDay.getMonth() + 1;
            startDay = initStartDay.getFullYear() + "-" + m + "-" + initStartDay.getDate();
		}

        if(this.lookupReference('searchForm').getForm().isValid()){		
	 		var input = {
	 				'startTime': startDay + ' 00:00:00', 
	 				'endTime': endDay + ' 23:59:59',
					'orgId':frmValues.deptId,
					'selfDept': selfDept,				
					'childDept': childDept,
	 			};
			window.location.href = 'vehicleAlert/exportAllAlertData?json='  + Ext.encode(input);
		}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
	},

    onExcelClickDemo :　function() {
        Ext.getCmp('exceptionReportSearch').hide();
        Ext.getCmp('export3pdf').hide();
        Ext.getCmp('export3excel').hide();
        ES6Promise.polyfill();
        var targetDom = $("#tbdc_2");
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
        Ext.getCmp('exceptionReportSearch').show();
        Ext.getCmp('export3pdf').show();
        Ext.getCmp('export3excel').show();

    },

	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.reportmgmt.reportexception.DeptChooseWin",{
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
     	var form = Ext.getCmp("reportExceptionSearchForm").getForm();
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
