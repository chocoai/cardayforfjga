/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.rulemgmt.ruleinfomgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.rulemgmt.ruleinfomgmt.View',
			'Admin.view.rulemgmt.ruleinfomgmt.SearchForm',
			'Admin.view.rulemgmt.ruleinfomgmt.AddRuleInfo',
			'Admin.view.rulemgmt.ruleinfomgmt.EditRuleInfo',
			'Admin.view.rulemgmt.ruleinfomgmt.ViewRuleInfo',
			],
	alias : 'controller.ruleinfomgmtcontroller',

	onBeforeLoad : function() {
		console.log('onBeforeLoad');
		this.getViewModel().getStore("ruleInfoResults").proxy.extraParams = {
			"json" : ""
		}
	},

	afterrenderRuleInfo :　function() {
		console.log('afterrenderRuleInfo');
		this.getViewModel().getStore("ruleInfoResults").load();
	},

	//打开新增用车规则窗口
	onAddRuleInfoClick : function() {
		win = Ext.widget('addRuleInfo');
		win.show();
	},

	getRuleInfo: function(ruleInfo){
		var ruleType;
		var vehicleType = '';
		if(ruleInfo.ruleType == '加班用车'){
            ruleType = '0';
		}else if(ruleInfo.ruleType == '日常用车'){
			ruleType = '1';
		}else if(ruleInfo.ruleType == '差旅用车'){
			ruleType = '2';
		}else{
			ruleType = '3';
		}

		if(typeof ruleInfo.vehicleType == 'string'){
            vehicleType = ruleInfo.vehicleType;
		}else{
			for(var i=0; i<ruleInfo.vehicleType.length; i++){
				if(i == ruleInfo.vehicleType.length-1){
				  vehicleType = vehicleType + ruleInfo.vehicleType[i];
				}else{
				  vehicleType = vehicleType + ruleInfo.vehicleType[i] + ',';
			    }
			}
		}

		var getOnList = new Object();
		getOnList.getOnType = ruleInfo.getOn;
		if(ruleInfo.getOn == '1'){
			var getOndata = new Array();
			if(typeof this.getView().down('form').getForm().findField('getOnCheckGroup').getValue().getOnCheck == 'number'){
				//getOndata.push(-1);
				getOndata.push(this.getView().down('form').getForm().findField('getOnCheckGroup').getValue().getOnCheck);
			}else{
				getOndata = this.getView().down('form').getForm().findField('getOnCheckGroup').getValue().getOnCheck;
			}
			getOnList.getOndata = getOndata;
		}

		var getOffList = new Object();
		getOffList.getOffType = ruleInfo.getOff;
		if(ruleInfo.getOff == '1'){
			var getOffdata = new Array();
			if(typeof this.getView().down('form').getForm().findField('getOffCheckGroup').getValue().getOffCheck == 'number'){
				//getOffdata.push(-1);
				getOffdata.push(this.getView().down('form').getForm().findField('getOffCheckGroup').getValue().getOffCheck);
			}else{
				getOffdata = this.getView().down('form').getForm().findField('getOffCheckGroup').getValue().getOffCheck;
			}
			getOffList.getOffdata = getOffdata;
		}

		var timeList = new Object();
		var holidayData = new Array();
		var weeklyData = new Array();
		var dateData = new Array();

		timeList.timeRangeType = ruleInfo.time;
		if(ruleInfo.time == '1'){
            if(ruleInfo.workDay == 'on'){
            	var workDay = new Object();
            	workDay.holidayType = '0';
            	workDay.startTime = ruleInfo.workDayStartTime;
            	workDay.endTime = ruleInfo.workDayEndTime;
                holidayData.push(workDay);
            }

            if(ruleInfo.holidayDay == 'on'){
            	var holidayDay = new Object();
            	holidayDay.holidayType = '1';
            	holidayDay.startTime = ruleInfo.holidayDayStartTime;
            	holidayDay.endTime = ruleInfo.holidayDayEndTime;
                holidayData.push(holidayDay);
            }

            timeList.holidayData = holidayData;
		}else if(ruleInfo.time == '2'){
            if(ruleInfo.monDay == 'on'){
            	var monDay = new Object();
            	monDay.weeklyType = '0';
            	monDay.startTime = ruleInfo.monDayStartTime;
            	monDay.endTime = ruleInfo.monDayEndTime;
                weeklyData.push(monDay);
            }

            if(ruleInfo.tueDay == 'on'){
            	var tueDay = new Object();
            	tueDay.weeklyType = '1';
            	tueDay.startTime = ruleInfo.tueDayStartTime;
            	tueDay.endTime = ruleInfo.tueDayEndTime;
                weeklyData.push(tueDay);
            }

            if(ruleInfo.wenDay == 'on'){
            	var wenDay = new Object();
            	wenDay.weeklyType = '2';
            	wenDay.startTime = ruleInfo.wenDayStartTime;
            	wenDay.endTime = ruleInfo.wenDayEndTime;
                weeklyData.push(wenDay);
            }

            if(ruleInfo.thursDay == 'on'){
            	var thursDay = new Object();
            	thursDay.weeklyType = '3';
            	thursDay.startTime = ruleInfo.thursDayStartTime;
            	thursDay.endTime = ruleInfo.thursDayEndTime;
                weeklyData.push(thursDay);
            }

            if(ruleInfo.friDay == 'on'){
            	var friDay = new Object();
            	friDay.weeklyType = '4';
            	friDay.startTime = ruleInfo.friDayStartTime;
            	friDay.endTime = ruleInfo.friDayEndTime;
                weeklyData.push(friDay);
            }

            if(ruleInfo.satDay == 'on'){
            	var satDay = new Object();
            	satDay.weeklyType = '5';
            	satDay.startTime = ruleInfo.satDayStartTime;
            	satDay.endTime = ruleInfo.satDayEndTime;
                weeklyData.push(satDay);
            }

            if(ruleInfo.sunDay == 'on'){
            	var sunDay = new Object();
            	sunDay.weeklyType = '6';
            	sunDay.startTime = ruleInfo.sunDayStartTime;
            	sunDay.endTime = ruleInfo.sunDayEndTime;
                weeklyData.push(sunDay);
            }

            timeList.weeklyData = weeklyData;
		}else if(ruleInfo.time == '3'){
			if(ruleInfo.startdt != undefined){
				if(typeof ruleInfo.endTime == 'string'){
	                var dateDataField = new Object();
	            	dateDataField.startDay = ruleInfo.startdt;
	            	dateDataField.endDay = ruleInfo.enddt;
	            	dateDataField.startTime = ruleInfo.startTime;
	            	dateDataField.endTime = ruleInfo.endTime;
	            	dateDataField.dateId = ruleInfo.dateId;
	                dateData.push(dateDataField);
				}else{
					for(var i=0; i<ruleInfo.endTime.length; i++){
						var dateDataField = new Object();
		            	dateDataField.startDay = ruleInfo.startdt[i];
		            	dateDataField.endDay = ruleInfo.enddt[i];
		            	dateDataField.startTime = ruleInfo.startTime[i];
		            	dateDataField.endTime = ruleInfo.endTime[i];
	            	    dateDataField.dateId = ruleInfo.dateId[i];
		                dateData.push(dateDataField);
					}
				}
	            timeList.dateData = dateData;
	        }else{
                Ext.Msg.alert('消息提示','日期不能为空！');
                return 1;
	        }

		}
		var input = {
        		'ruleName': ruleInfo.ruleName,
        		'ruleType': ruleType,
        		'timeList': timeList,
        		'getOnList': getOnList,
        		'getOffList': getOffList,
        		'vehicleType': vehicleType,
        		'useLimit': ruleInfo.useLimitRadio,
			};

	    if(input.timeList.timeRangeType == '1'){
				var holidayData = input.timeList.holidayData;
				for(var i = 0; i < holidayData.length; i++){
					var startTime = holidayData[i].startTime;
					var holidayType = holidayData[i].holidayType;
					var endTime = holidayData[i].endTime;
					if(startTime >= endTime && endTime != '00:00'){
						if(holidayType == '0'){
	                        Ext.Msg.alert('消息提示','法定工作日:结束时间应大于开始时间！');
	                        return 1;
	                        break;
                        }else{
                        	Ext.Msg.alert('消息提示','法定节假日:结束时间应大于开始时间！');
	                        return 1;
	                        break;
                        }
					}
				}
			}else if(input.timeList.timeRangeType == '2'){
				var weeklyData = input.timeList.weeklyData;
				for(var i = 0; i < weeklyData.length; i++){
					var startTime = weeklyData[i].startTime;
					var weeklyType = weeklyData[i].weeklyType;
					var endTime = weeklyData[i].endTime;
					if(startTime >= endTime && endTime != '00:00'){
						switch(weeklyType)
							{
							case '0':
		                        Ext.Msg.alert('消息提示','星期一:结束时间应大于开始时间！');
		                        return 1;
							  break;
							case '1':
		                        Ext.Msg.alert('消息提示','星期二:结束时间应大于开始时间！');
		                        return 1;
							  break;
							case '2':
		                        Ext.Msg.alert('消息提示','星期三:结束时间应大于开始时间！');
		                        return 1;
							  break;
							case '3':
		                        Ext.Msg.alert('消息提示','星期四:结束时间应大于开始时间！');
		                        return 1;
							  break;
							case '4':
		                        Ext.Msg.alert('消息提示','星期五:结束时间应大于开始时间！');
		                        return 1;
							  break;
							case '5':
		                        Ext.Msg.alert('消息提示','星期六:结束时间应大于开始时间！');
		                        return 1;
							  break;
							case '6':
		                        Ext.Msg.alert('消息提示','星期日:结束时间应大于开始时间！');
		                        return 1;
							  break;
							default:
		                        Ext.Msg.alert('消息提示','结束时间应大于开始时间！');
		                        return 1;
							}
					}
				}
			}else if(input.timeList.timeRangeType == '3'){
				var dateData = input.timeList.dateData;
				var today = new Date();
				var month = today.getMonth() + 1;
				var date = today.getDate();
				if(month < 10){
                   month = '0' + month;
				}
				if(date < 10){
                   date = '0' + date;
				}
				var todayStr = today.getFullYear() + month + date;
				for(var i = 0; i < dateData.length; i++){
					var startDay = dateData[i].startDay.replace(/\-/g,"");
					var endDay = dateData[i].endDay.replace(/\-/g,"");
					var startTime = dateData[i].startTime;
					var endTime = dateData[i].endTime;
					if(dateData[i].dateId == ''){
						if(startDay < todayStr){
						    var count = i+1;
							Ext.Msg.alert('消息提示','第' + count + '条:开始日期应不小于当前日期！');
			                return 1;
						}
					}
					if(startDay > endDay){
						var count = i+1;
						Ext.Msg.alert('消息提示','第' + count + '条:结束日期应不小于开始日期！');
		                return 1;
		                break;
					}else if(startTime >= endTime && endTime != '00:00'){
						var count = i+1;
						Ext.Msg.alert('消息提示','第' + count + '条:结束时间应大于开始时间！');
		                return 1;
		                break;
					}
				}

				for(var m = 0; m < dateData.length; m++){
					var startDay = dateData[m].startDay.replace(/\-/g,"");
					var endDay = dateData[m].endDay.replace(/\-/g,"");
					var startTime = dateData[m].startTime;
					var endTime = dateData[m].endTime;

					for(var n = m+1 ; n < dateData.length; n++){
						var count = 0;
						var startDayOther = dateData[n].startDay.replace(/\-/g,"");
						var endDayOther = dateData[n].endDay.replace(/\-/g,"");
						var startTimeOther = dateData[n].startTime;
						var endTimeOther = dateData[n].endTime;
						if(startDayOther == startDay){
							count++;
						}
						if(endDayOther == endDay){
							count++;
						}
						if(startTimeOther == startTime){
							count++;
						}
						if(endTimeOther == endTime){
							count++;
						}
                        if(count == 4){
                        	var start = m+1;
                        	var end = n+1;
                        	Ext.Msg.alert('消息提示','第' + start + '条时间段与第'+ end +'条时间段相同！');
			                return 1;
			                break;
                        }
					}

					if(startDay > endDay){
						var count = i+1;
						Ext.Msg.alert('消息提示','第' + count + '条:结束日期应不小于开始日期！');
		                return 1;
		                break;
					}else if(startTime >= endTime && endTime != '00:00'){
						var count = i+1;
						Ext.Msg.alert('消息提示','第' + count + '条:结束时间应大于开始时间！');
		                return 1;
		                break;
					}
				}
			}

		if(input.timeList.timeRangeType == '3'){
			var dateDataNew = new Array();
			for(var i = 0; i < input.timeList.dateData.length; i++){
				var dateDataField = new Object();
				dateDataField.startDay = input.timeList.dateData[i].startDay;
            	dateDataField.endDay = input.timeList.dateData[i].endDay;
            	dateDataField.startTime = input.timeList.dateData[i].startTime;
            	dateDataField.endTime = input.timeList.dateData[i].endTime;
                dateDataNew.push(dateDataField);
			}
			input.timeList.dateData = dateDataNew;
		}

	    return input;
	},

	//完成新增用车规则
	addRuleInfoDone : function(btn) {
		var myMask = new Ext.LoadMask({
					    msg    : '请稍后，正在添加规则信息........',
					    target : this.getView()
					});
        myMask.show();  

        var ruleInfo = this.getView().down('form').getForm().getValues();
		var input =this.getRuleInfo(ruleInfo);
		var flag = true;
		var getoncheckboxgroup = Ext.getCmp("getOnCheckGroup");
        var getoffcheckboxgroup = Ext.getCmp("getOffCheckGroup");

		if(ruleInfo.getOn == '1'){
			if(Ext.getCmp("getOnCheckGroup").items.items.length > 0 && getoncheckboxgroup.getChecked().length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','至少选择一个上车位置！');
	   	 	    myMask.hide();
			}else if(Ext.getCmp("getOnCheckGroup").items.items.length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','上车位置没有可选位置，请选择不限！');
	   	 	    myMask.hide();
			}
		}

		if(ruleInfo.getOff == '1'){
			if(Ext.getCmp("getOffCheckGroup").items.items.length > 0 && getoffcheckboxgroup.getChecked().length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','至少选择一个下车位置！');
	   	 	    myMask.hide();
			}else if(Ext.getCmp("getOffCheckGroup").items.items.length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','下车位置没有可选位置，请选择不限！');
	   	 	    myMask.hide();
			}
		}
        if(flag){

			var ruleInfo = this.getView().down('form').getForm().getValues();
			var input =this.getRuleInfo(ruleInfo);

			if(input == 1){
				myMask.hide();
			}else{

				if (this.getView().down('form').getForm().isValid()) {
				    var json = Ext.encode(input);
  			   	    Ext.Ajax.request({
						url : 'rule/addRule',
				        method : 'POST',
				        params:{json:json},
				        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
				        success : function(response,options) {
				        	var respText = Ext.util.JSON.decode(response.responseText);
				        	var retStatus = respText.status;
				        	var data = respText.data;
							if (retStatus == 'success') {
								btn.up('addRuleInfo').close();
								Ext.Msg.alert('提示信息','添加用车规则成功');
								Ext.getCmp("gridRuleInfo").getStore('ruleInfoResults').load();
							}else{
								btn.up('addRuleInfo').close();
								Ext.Msg.alert('消息提示','新增用车规则失败！');
								Ext.getCmp("gridRuleInfo").getStore('ruleInfoResults').load();
							}
				        }
				        /*,
				        failure : function() {
							btn.up('addRuleInfo').close();
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        }*/
				    });
			   	 }else{
			   	 	Ext.Msg.alert('消息提示','用车规则信息有误，请重新输入！');
			   	 	myMask.hide();
			   	 }

		   	 }

	   	}

	},

	showRuleInfoForm: function(editRuleInfoForm,data){
		switch(data.ruleType)
					{
					case '0':
					  editRuleInfoForm.findField('ruleType').setValue('加班用车');
					  break;
					case '1':
					  editRuleInfoForm.findField('ruleType').setValue('日常用车');
					  break;
					case '2':
					  editRuleInfoForm.findField('ruleType').setValue('差旅用车');
					  break;
					case '3':
					  editRuleInfoForm.findField('ruleType').setValue('其他');
					  break;
					default:
					   editRuleInfoForm.findField('ruleType').setValue('加班用车');
					}

					var vehicleTypeList = editRuleInfoForm.findField('vehicleTypeList');

					var vehicleTypeArray = data.vehicleType.split(',');

					for (var i = 0; i < vehicleTypeList.items.items.length; i++)
		            {
                        vehicleTypeList.items.items[i].setValue(false);
		            	for (var j = 0; j < vehicleTypeArray.length; j++)
			            {
			                if(vehicleTypeList.items.items[i].inputValue == vehicleTypeArray[j]){
			                	vehicleTypeList.items.items[i].setValue(true);
			                }
			            }
		            }

		            var useLimitList = editRuleInfoForm.findField('useLimit');
					for (var i = 0; i < useLimitList.items.items.length; i++)
		            {
		                if(useLimitList.items.items[i].inputValue == data.useLimit){
		                	useLimitList.items.items[i].setValue(true);
		                }
		            }

		            if(data.getOffList.getOffType == '0'){
                        editRuleInfoForm.findField('getOffList').items.items[0].setValue(true);
		            }else{
		            	editRuleInfoForm.findField('getOffList').items.items[1].setValue(true);
		            	var offList = editRuleInfoForm.findField('getOffCheckGroup');
		            	var offData = data.getOffList.getOffdata;
		            	console.log('OffListLength:' + offList.items.items.length);
		            	for (var i = 0; i < offList.items.items.length; i++)
			            {
	                        offList.items.items[i].setValue(false);
			            	for (var j = 0; j < offData.length; j++)
				            {
				                if(offList.items.items[i].inputValue == String(offData[j])){
				                	offList.items.items[i].setValue(true);
				                }
				            }
			            }
		            }

		            if(data.getOnList.getOnType == '0'){
                        editRuleInfoForm.findField('getOnList').items.items[0].setValue(true);
		            }else{
		            	editRuleInfoForm.findField('getOnList').items.items[1].setValue(true);
		            	var onList = editRuleInfoForm.findField('getOnCheckGroup');
		            	var onData = data.getOnList.getOndata;
		            	console.log('OnListLength:' + onList.items.items.length);
		            	for (var i = 0; i < onList.items.items.length; i++)
			            {
	                        onList.items.items[i].setValue(false);
			            	for (var j = 0; j < onData.length; j++)
				            {
				                if(onList.items.items[i].inputValue == String(onData[j])){
				                	onList.items.items[i].setValue(true);
				                }
				            }
			            }
		            }

		            if(data.timeList.timeRangeType == '0'){
                        editRuleInfoForm.findField('timeList').items.items[0].setValue(true);
		            }else if(data.timeList.timeRangeType == '1'){
		            	editRuleInfoForm.findField('timeList').items.items[1].setValue(true);
		            	if(data.timeList.holidayData != null){
		            		for (var i = 0; i < data.timeList.holidayData.length; i++)
				            {

				                if(data.timeList.holidayData[i].holidayType == '0'){
		            		       editRuleInfoForm.findField('workDay').setValue(true);
		            		       editRuleInfoForm.findField('workDayStartTime').setValue(data.timeList.holidayData[i].startTime);
		            		       editRuleInfoForm.findField('workDayEndTime').setValue(data.timeList.holidayData[i].endTime);
				                }else{
		            		       editRuleInfoForm.findField('holidayDay').setValue(true);
					                 if(data.timeList.holidayData.length < 2){
			            		       editRuleInfoForm.findField('workDay').setValue(false);
					                 }
		            		       editRuleInfoForm.findField('holidayDayStartTime').setValue(data.timeList.holidayData[i].startTime);
		            		       editRuleInfoForm.findField('holidayDayEndTime').setValue(data.timeList.holidayData[i].endTime);
				                }
				            }
		            	}
		            }else if(data.timeList.timeRangeType == '2'){
		            	editRuleInfoForm.findField('timeList').items.items[2].setValue(true);
		            	if(data.timeList.weeklyData != null){
		            		var flag = false;
		            		for (var i = 0; i < data.timeList.weeklyData.length; i++){
		            			if(data.timeList.weeklyData[i].weeklyType == '0'){
		            				flag = true;
		            			}
		            		}
		            		for (var i = 0; i < data.timeList.weeklyData.length; i++)
				            {
								switch(data.timeList.weeklyData[i].weeklyType)
									{
									case '0':
			            		       editRuleInfoForm.findField('monDay').setValue(true);
			            		       editRuleInfoForm.findField('monDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('monDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									case '1':
			            		       editRuleInfoForm.findField('tueDay').setValue(true);
			            		       if(!flag){
			            		         editRuleInfoForm.findField('monDay').setValue(false);
			            		       }
			            		       editRuleInfoForm.findField('tueDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('tueDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									case '2':
			            		       editRuleInfoForm.findField('wenDay').setValue(true);
			            		       if(!flag){
			            		         editRuleInfoForm.findField('monDay').setValue(false);
			            		       }
			            		       editRuleInfoForm.findField('wenDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('wenDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									case '3':
			            		       editRuleInfoForm.findField('thursDay').setValue(true);
			            		       if(!flag){
			            		         editRuleInfoForm.findField('monDay').setValue(false);
			            		       }
			            		       editRuleInfoForm.findField('thursDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('thursDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									case '4':
			            		       editRuleInfoForm.findField('friDay').setValue(true);
			            		       if(!flag){
			            		         editRuleInfoForm.findField('monDay').setValue(false);
			            		       }
			            		       editRuleInfoForm.findField('friDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('friDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									case '5':
			            		       editRuleInfoForm.findField('satDay').setValue(true);
			            		       if(!flag){
			            		         editRuleInfoForm.findField('monDay').setValue(false);
			            		       }
			            		       editRuleInfoForm.findField('satDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('satDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									case '6':
			            		       editRuleInfoForm.findField('sunDay').setValue(true);
			            		       if(!flag){
			            		         editRuleInfoForm.findField('monDay').setValue(false);
			            		       }
			            		       editRuleInfoForm.findField('sunDayStartTime').setValue(data.timeList.weeklyData[i].startTime);
			            		       editRuleInfoForm.findField('sunDayEndTime').setValue(data.timeList.weeklyData[i].endTime);
									  break;
									default:
			            		       editRuleInfoForm.findField('monDay').setValue(true);
									}
				            }
		            	}
		            }else if(data.timeList.timeRangeType == '3'){
		            	editRuleInfoForm.findField('timeList').items.items[3].setValue(true);
		            	if(data.timeList.dateData != null){
		            		for(var i = 0; i < data.timeList.dateData.length; i++){
		            			if(i > 0){
	    							var item = {
							        	xtype:'ruleinfodatefield',
							        };
							        Ext.getCmp('dateField').add(item);
						        }

						        Ext.getCmp('dateField').items.items[i+1].items.items[0].setValue(data.timeList.dateData[i].dateId);
								Ext.getCmp('dateField').items.items[i+1].items.items[1].setReadOnly(true);
						        Ext.getCmp('dateField').items.items[i+1].items.items[1].setValue(data.timeList.dateData[i].startDay);
								Ext.getCmp('dateField').items.items[i+1].items.items[2].setReadOnly(true);
						        Ext.getCmp('dateField').items.items[i+1].items.items[2].setValue(data.timeList.dateData[i].endDay);
								Ext.getCmp('dateField').items.items[i+1].items.items[3].setReadOnly(true);
						        Ext.getCmp('dateField').items.items[i+1].items.items[3].setValue(data.timeList.dateData[i].startTime);
								Ext.getCmp('dateField').items.items[i+1].items.items[4].setReadOnly(true);
						        Ext.getCmp('dateField').items.items[i+1].items.items[4].setValue(data.timeList.dateData[i].endTime);
		            		}
		            	}
		            }
	},

	//查看规则信息
	viewRuleInfo : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewRuleInfo");
		win.down("form").loadRecord(rec);
		var input = {
			'ruleId': rec.data.ruleId,
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'rule/findRuleById',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
	        	var data = respText.data;
	        	var editRuleInfoForm = Ext.getCmp('viewRuleInfo').down('form').getForm();
				if (retStatus == 'success' && data != "") {
					console.log('showRuleInfoForm - Add');
					this.showRuleInfoForm(editRuleInfoForm,data);
					console.log('showRuleInfoForm + Add');
					win.center();
				}else{
				    btn.up('viewRuleInfo').close();
				}

				for(var i = 1; i < Ext.getCmp('dateField').items.items.length; i++){
		            Ext.getCmp('dateField').items.items[i].items.items[5].hide();
				}
	        },
//	        failure : function() {
//				btn.up('viewRuleInfo').close();
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
		win.show();
		win.center();
	},

	//打开用车规则信息修改,根据id查询信息
	editRuleInfo : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("editRuleInfo");
		win.down("form").loadRecord(rec);
		var input = {
			'ruleId': rec.data.ruleId,
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'rule/findRuleById',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	       // defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
	        	var data = respText.data;
	        	var editRuleInfoForm = Ext.getCmp('editRuleInfo').down('form').getForm();
				if (retStatus == 'success' && data != "") {
					console.log('showRuleInfoForm - Edit');
					this.showRuleInfoForm(editRuleInfoForm,data);
					console.log('showRuleInfoForm + Edit');
				}else{
                    Ext.getCmp('editRuleInfo').close();
				}
	        },
//	        failure : function() {
//				btn.up('editRuleInfo').close();
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
		win.show();	
	},

    //完成用车规则信息修改
	editRuleInfoDone: function(btn){
		var myMask = new Ext.LoadMask({
			    msg    : '请稍后，正在修改用车规则........',
			    target : this.getView()
			});
        myMask.show(); 

        var ruleInfo = this.getView().down('form').getForm().getValues();
		var input =this.getRuleInfo(ruleInfo);
		input.ruleId = ruleInfo.ruleId;
		var flag = true;
		var getoncheckboxgroup = Ext.getCmp("getOnCheckGroup");
        var getoffcheckboxgroup = Ext.getCmp("getOffCheckGroup");

		if(ruleInfo.getOn == '1'){
			if(Ext.getCmp("getOnCheckGroup").items.items.length > 0 && getoncheckboxgroup.getChecked().length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','至少选择一个上车位置！');
	   	 	    myMask.hide();
			}else if(Ext.getCmp("getOnCheckGroup").items.items.length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','下车位置没有可选位置，请选择不限！');
	   	 	    myMask.hide();
			}
		}

		if(ruleInfo.getOff == '1'){
			if(Ext.getCmp("getOffCheckGroup").items.items.length > 0 && getoffcheckboxgroup.getChecked().length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','至少选择一个下车位置！');
	   	 	    myMask.hide();
			}else if(Ext.getCmp("getOffCheckGroup").items.items.length == 0){
				flag = false;
				Ext.Msg.alert('消息提示','下车位置没有可选位置，请选择不限！');
	   	 	    myMask.hide();
			}
		}

        if(flag){

        	if(input == 1){
				myMask.hide();
			}else{
				if (this.getView().down('form').getForm().isValid()) {
				    var json = Ext.encode(input);
				   	 Ext.Ajax.request({
						url : 'rule/updateRule',
				        method : 'POST',
				        params:{json:json},
				        success : function(response,options) {
				        	var respText = Ext.util.JSON.decode(response.responseText);
				        	var retStatus = respText.status;
				        	var data = respText.data;
							if (retStatus == 'success') {
								btn.up('editRuleInfo').close();
								Ext.Msg.alert('提示信息','修改用车规则成功');
								Ext.getCmp("gridRuleInfo").getStore('ruleInfoResults').load();
							}else{
								btn.up('editRuleInfo').close();
								Ext.Msg.alert('消息提示','修改用车规则失败！');
								Ext.getCmp("gridRuleInfo").getStore('ruleInfoResults').load();
							}
				        }
				        /*,
				        failure : function() {
							btn.up('editRuleInfo').close();
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        }*/
				    });
			   	 }else{
			   	 	Ext.Msg.alert('消息提示','用车规则信息有误，请重新输入！');
			   	 	myMask.hide();
			   	 }
		   	}

        }

   },

   	//删除
	deleteRuleInfo : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var input = {
                    'ruleId': grid.getStore().getAt(rowIndex).data.ruleId
				};
				var json = Ext.encode(input);
				Ext.Ajax.request({
		   		url: 'rule/removeRule',//?json='+ Ext.encode(input),
		        method : 'POST',
	        	params:{json:json},
		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
					        Ext.getCmp("gridRuleInfo").getStore('ruleInfoResults').load();
						}else{
							Ext.Msg.alert('消息提示','删除用车规则失败！');
							Ext.getCmp("gridRuleInfo").getStore('ruleInfoResults').load();
					   }
			        },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});		
			}
		});
	},

	LoadingGetOnCheckGroup: function(){
		console.log('LoadingGetOnCheckGroup!');
				Ext.Ajax.request({
		   		url: 'rule/getOnAddressListForAdd',
		        method : 'POST',
		        async: false,
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
		                    console.log('LoadingGetOnCheckGroup start!');
							var checkboxgroup = Ext.getCmp("getOnCheckGroup");
					            for (var i = 0; i < respText.data.length; i++) {
					            	var checkbox;
					            	if(i==0){
					            	  checkbox = new Ext.form.Checkbox(
						                  {
						                      boxLabel: respText.data[i].boxLabel,
						                      name: 'getOnCheck',
						                      inputValue: respText.data[i].inputValue,
						                      checked: true
						                  });
					            	}else{
					            		checkbox = new Ext.form.Checkbox(
						                  {
						                      boxLabel: respText.data[i].boxLabel,
						                      name: 'getOnCheck',
						                      inputValue: respText.data[i].inputValue,
						                      checked: false
						                  });
					            	}

					                checkboxgroup.items.add(checkbox);
					            }
		                    console.log('LoadingGetOnCheckGroup end!');
						}
			        },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});
	},

	LoadingGetOffCheckGroup: function(){
		console.log('LoadingGetOffCheckGroup!');
				Ext.Ajax.request({
		   		url: 'rule/getOnAddressListForAdd',
		        method : 'POST',
		        async: false,
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
		                    console.log('LoadingGetOffCheckGroup start!');
							var checkboxgroup = Ext.getCmp("getOffCheckGroup");
					            for (var i = 0; i < respText.data.length; i++) {
					            	var checkbox;
					            	if(i==0){
					            	  checkbox = new Ext.form.Checkbox(
						                  {
						                      boxLabel: respText.data[i].boxLabel,
						                      name: 'getOffCheck',
						                      inputValue: respText.data[i].inputValue,
						                      checked: true
						                  });
					            	}else{
					            		checkbox = new Ext.form.Checkbox(
						                  {
						                      boxLabel: respText.data[i].boxLabel,
						                      name: 'getOffCheck',
						                      inputValue: respText.data[i].inputValue,
						                      checked: false
						                  });
					            	}

					                checkboxgroup.items.add(checkbox);
					            }
						}
		                console.log('LoadingGetOffCheckGroup end!');
			        },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});
	},

	LoadingGetOnCheckGroupforView: function(){
		console.log('LoadingGetOnCheckGroup View!');
		Ext.Ajax.request({
   		url: 'rule/getOnAddressListForAdd',
        method : 'POST',
		async: false,
        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
        success : function(response,options) {
			var respText = Ext.util.JSON.decode(response.responseText);
			var	retStatus = respText.status;
				if (retStatus == 'success') {
		            console.log('LoadingGetOnCheckGroup View Start!');
					var checkboxgroup = Ext.getCmp("getOnCheckGroup");
			            for (var i = 0; i < respText.data.length; i++) {
			            	var checkbox = new Ext.form.Checkbox(
				                  {
				                      boxLabel: respText.data[i].boxLabel,
				                      name: 'getOnCheck',
				                      inputValue: respText.data[i].inputValue,
				                      checked: false,
				                      readOnly: true
				                  });
			                checkboxgroup.items.add(checkbox);
			            }
		            console.log('LoadingGetOnCheckGroup View End!');
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},

	LoadingGetOffCheckGroupforView: function(){
		console.log('LoadingGetOffCheckGroup View!');
		Ext.Ajax.request({
   		url: 'rule/getOnAddressListForAdd',
        method : 'POST',
		async: false,
        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
        success : function(response,options) {
			var respText = Ext.util.JSON.decode(response.responseText);
			var	retStatus = respText.status;
				if (retStatus == 'success') {
		            console.log('LoadingGetOffCheckGroup View Start!');
					var checkboxgroup = Ext.getCmp("getOffCheckGroup");
			            for (var i = 0; i < respText.data.length; i++) {
			            	var checkbox = new Ext.form.Checkbox(
				                  {
				                      boxLabel: respText.data[i].boxLabel,
				                      name: 'getOffCheck',
				                      inputValue: respText.data[i].inputValue,
				                      checked: false,
				                      readOnly: true
				                  });
			                checkboxgroup.items.add(checkbox);
			            }
		            console.log('LoadingGetOffCheckGroup View End!');
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},



});
