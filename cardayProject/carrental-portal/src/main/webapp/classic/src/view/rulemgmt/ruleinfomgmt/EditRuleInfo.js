Ext.define('Admin.view.rulemgmt.ruleinfomgmt.EditRuleInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editRuleInfo",
    id: 'editRuleInfo',
    controller: 'ruleinfomgmtcontroller',
	reference: 'editRuleInfo',
	title : '修改用车规则',
	width : 1000,
	//height: 355,
	minHeight: 355,
//	maxHeight: 460,
	y:Ext.Element.getViewSize().height/6,
	maxHeight:Ext.Element.getViewSize().height/6 * 4,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	scrollable: 'y',

	items : [{
					xtype:'form',
					layout : 'form',		
					items: [
					{
						xtype: 'fieldcontainer',
						layout: 'hbox',
						fieldLabel: '用车规则名称',
						items: [
	                            {
									fieldLabel: '用车规则ID',
									xtype: 'textfield',
									allowBlank: false, 
									blankText: '用车规则ID不能为空',
							        name: 'ruleId',
							        labelWidth: 100,
						            margin: '0 50 0 0', 
						            hidden:true
								}, 
	                            {
//									fieldLabel: '用车规则名称',
									xtype: 'textfield',
									allowBlank: false, 
									blankText: '用车规则名称不能为空',
							        name: 'ruleName',
							        labelWidth: 100,
						            margin: '0 50 0 0', 
								}, 
	                            {
									fieldLabel: '用车场景',
									xtype: 'combo',
						        	displayField: 'value',
									allowBlank: false, 
									blankText: '用车场景不能为空',
						        	name: 'ruleType',
						        	labelWidth: 60,
						            typeAhead:false,
						            editable:false,
						            value:'加班用车',
								    store : {
										fields : ['id','value' ],
										data : [
										        {'id' : '0','value' : '加班用车'},
										        {'id' : '1','value' : '日常用车'},
										        {'id' : '2','value' : '差旅用车'},
										        {'id' : '3','value' : '其他'},
										]
									},
								}, 
						]

			        },
			        {
				        xtype: 'radiogroup',
				        fieldLabel: '时间范围',
						name: 'timeList',
				        columns: 4,
				        vertical: true,
				        width:690,
				        items: [
				            { boxLabel: '不限', name: 'time', inputValue: '0', checked: true},
				            { boxLabel: '法定工作日/节假日', name: 'time', inputValue: '1'},
				            { boxLabel: '按星期定义', name: 'time', inputValue: '2' },
				            { boxLabel: '按日期定义', name: 'time', inputValue: '3' },
				        ],
				        listeners: {
		                    change: function(me,newValue,oldValue,eOpts){
	                              if(newValue.time == '1'){
	                              	Ext.getCmp('workDayField').show();
	                              	Ext.getCmp('weeklyField').hide();
	                              	Ext.getCmp('dateField').hide();
	                              }else if(newValue.time == '2'){
	                              	Ext.getCmp('workDayField').hide();
	                              	Ext.getCmp('weeklyField').show();
	                              	Ext.getCmp('dateField').hide();
	                              }else if(newValue.time == '3'){
	                              	Ext.getCmp('workDayField').hide();
	                              	Ext.getCmp('weeklyField').hide();
	                              	Ext.getCmp('dateField').show();
	                              }else{
	                              	Ext.getCmp('workDayField').hide();
	                              	Ext.getCmp('weeklyField').hide();
	                              	Ext.getCmp('dateField').hide();
	                              }
		                        }
		                    }
				    },
				    {
				    	xtype: 'fieldcontainer',
                 		id:'workDayField',
						layout: 'vbox',
						hidden: true,
						items: [{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '法定工作日',
												xtype: 'checkboxfield',
										        name: 'workDay',
									            margin: '0 50 0 0',
									            checked: true,
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('workDayField').items.items[1].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个法定工作日/节假日!");
			                                                 Ext.getCmp('workDayField').items.items[1].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'workDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'workDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]},
									{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '法定节假日',
												xtype: 'checkboxfield',
										        name: 'holidayDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('workDayField').items.items[0].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个法定工作日/节假日!");
			                                                 Ext.getCmp('workDayField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'holidayDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'holidayDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								}]
					},
					{
						xtype: 'fieldcontainer',
                 		id:'weeklyField',
						layout: 'vbox',
						hidden:true,
						items: [{
								xtype: 'fieldcontainer',
								layout: 'hbox',
								items: [{
											boxLabel: '星期一',
											xtype: 'checkboxfield',
									        name: 'monDay',
								            margin: '0 50 0 0',
								            checked: true,
								            listeners: {
							                    change: function(me,newValue,oldValue,eOpts){
						                              if(!newValue && !Ext.getCmp('weeklyField').items.items[1].down().getValue() 
						                              	           && !Ext.getCmp('weeklyField').items.items[2].down().getValue() 
						                              	           && !Ext.getCmp('weeklyField').items.items[3].down().getValue() 
						                              	           && !Ext.getCmp('weeklyField').items.items[4].down().getValue() 
						                              	           && !Ext.getCmp('weeklyField').items.items[5].down().getValue() 
						                              	           && !Ext.getCmp('weeklyField').items.items[6].down().getValue()){
		                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
		                                                 Ext.getCmp('weeklyField').items.items[1].down().setValue(true);
						                              }
							                        }
							                    }
										},
										{
											fieldLabel: '自',
											xtype: 'timefield',
									        name: 'monDayStartTime',
									        format: 'H:i',
									        value: '09:00',
									        increment: 5,
									        anchor: '100%',
								            labelWidth:15,
								            width:120,
								            editable:false,
								            margin: '0 30 0 0',
										},
										{
											fieldLabel: '至',
											xtype: 'timefield',
									        name: 'monDayEndTime',
									        format: 'H:i',
									        value: '18:00',
									        increment: 5,
									        anchor: '100%',
								            labelWidth:15,
								            width:120,
								            editable:false,
										},
								  ]},
								  {
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '星期二',
												xtype: 'checkboxfield',
										        name: 'tueDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('weeklyField').items.items[0].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[2].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[3].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[4].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[5].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[6].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
			                                                 Ext.getCmp('weeklyField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'tueDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'tueDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								},
								{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '星期三',
												xtype: 'checkboxfield',
										        name: 'wenDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('weeklyField').items.items[0].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[1].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[3].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[4].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[5].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[6].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
			                                                 Ext.getCmp('weeklyField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'wenDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'wenDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								},
								{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '星期四',
												xtype: 'checkboxfield',
										        name: 'thursDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('weeklyField').items.items[0].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[1].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[2].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[4].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[5].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[6].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
			                                                 Ext.getCmp('weeklyField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'thursDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'thursDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								},
								{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '星期五',
												xtype: 'checkboxfield',
										        name: 'friDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('weeklyField').items.items[0].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[1].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[2].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[3].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[5].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[6].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
			                                                 Ext.getCmp('weeklyField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'friDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'friDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								},
								{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '星期六',
												xtype: 'checkboxfield',
										        name: 'satDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('weeklyField').items.items[0].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[1].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[2].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[3].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[4].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[6].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
			                                                 Ext.getCmp('weeklyField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'satDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'satDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								},
								{
			                 		xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [{
												boxLabel: '星期日',
												xtype: 'checkboxfield',
										        name: 'sunDay',
									            margin: '0 50 0 0',
									            listeners: {
								                    change: function(me,newValue,oldValue,eOpts){
							                              if(!newValue && !Ext.getCmp('weeklyField').items.items[0].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[1].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[2].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[3].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[4].down().getValue() 
							                              	           && !Ext.getCmp('weeklyField').items.items[5].down().getValue()){
			                                                 Ext.MessageBox.alert("消息提示","请至少选中一个星期!");
			                                                 Ext.getCmp('weeklyField').items.items[0].down().setValue(true);
							                              }
								                        }
								                    }
											},
											{
												fieldLabel: '自',
												xtype: 'timefield',
										        name: 'sunDayStartTime',
										        format: 'H:i',
										        value: '09:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
									            margin: '0 30 0 0',
											},
											{
												fieldLabel: '至',
												xtype: 'timefield',
										        name: 'sunDayEndTime',
										        format: 'H:i',
										        value: '18:00',
										        increment: 5,
										        anchor: '100%',
									            labelWidth:15,
									            width:120,
									            editable:false,
											},
									]
								},]
					},
					{
						xtype: 'fieldcontainer',
                 		id:'dateField',
						layout: 'vbox',
						hidden:true,
						items: [
								{
									xtype:'button',
								    text: '新增',
									margin: '0 0 10 0',
								    handler: function() {
								        var item = {
								        	xtype:'ruleinfodatefield',
								        };
								        Ext.getCmp('dateField').add(item);
								    }
								},{
								xtype: 'fieldcontainer',
								layout: 'hbox',
								items: [
			                            {
											fieldLabel: '日期ID',
											xtype: 'textfield',
											blankText: '日期ID不能为空',
									        name: 'dateId',
									        labelWidth: 100,
								            hidden:true
										},{
								            name:'startdt',
								            xtype: 'datefield',
								            emptyText: '请选择开始日期',
								            format: 'Y-m-d',
								            itemId: 'startdt',
								            endDateField: 'enddt',
								            editable: false,
								            margin: '0 30 0 0',
								            value:new Date()
								        },
								        {
								            name:'enddt',
								            emptyText: '请选择结束日期',
								            xtype: 'datefield',
								            format: 'Y-m-d',
								            itemId: 'enddt',
								            startDateField: 'startdt',
								            editable: false,
								            margin: '0 50 0 0',
								            value:new Date()
								        },
										{
											fieldLabel: '自',
											xtype: 'timefield',
									        name: 'startTime',
									        format: 'H:i',
									        value: '09:00',
									        increment: 5,
									        anchor: '100%',
								            labelWidth:15,
								            width:120,
								            editable:false,
								            margin: '0 30 0 0',
										},
										{
											fieldLabel: '至',
											xtype: 'timefield',
									        name: 'endTime',
									        format: 'H:i',
									        value: '18:00',
									        increment: 5,
									        anchor: '100%',
								            labelWidth:15,
								            width:120,
								            editable:false,
								            margin: '0 50 0 0',
										},
							            {
							                xtype:'button',
							                text: '删除',
							                handler: function(me) {
							                    Ext.getCmp('dateField').remove(me.up());
							                }
							            }
								]},]
					},
				    {
				        xtype: 'radiogroup',
				        fieldLabel: '上车位置',
						name: 'getOnList',
				        width:400,
				        columns: 2,
				        vertical: true,
				        items: [
				            { boxLabel: '不限', name: 'getOn', inputValue: '0', checked: true},
				            { boxLabel: '限制上车位置', name: 'getOn', inputValue: '1',},
				        ],
				        listeners: {
		                    change: function(me,newValue,oldValue,eOpts){
		                    	if(Ext.getCmp("getOnCheckGroup").items.items.length > 0){
			                    	  console.log('newValue.getOn :' + newValue.getOn);
		                              if(newValue.getOn == '1'){
		                              	Ext.getCmp('getOnCheckGroup').show();
		                              }else{
		                              	Ext.getCmp('getOnCheckGroup').hide();
		                              	Ext.getCmp('getOnCheckGroup').items.items[0].setValue(true);
		                              }
	                              }
		                        }
		                    }
				    },
					{
						xtype: 'checkboxgroup',
						id:'getOnCheckGroup',
						name:'getOnCheckGroup',
//						columns: 4,
						columns: 3,
//						vertical: true,
//				        width:1000,
				        width:800,
				        hidden: true,
						//allowBlank: false,
						listeners: {
                            beforerender: 'LoadingGetOnCheckGroup',
                        }
					  },
				    {
				        xtype: 'radiogroup',
				        fieldLabel: '下车位置',
						name: 'getOffList',
				        columns: 2,
				        vertical: true,
				        width:400,
				        items: [
				            { boxLabel: '不限', name: 'getOff', inputValue: '0', checked: true},
				            { boxLabel: '限制下车位置', name: 'getOff', inputValue: '1',},
				        ],
				        listeners: {
		                    change: function(me,newValue,oldValue,eOpts){
		                    	if(Ext.getCmp("getOffCheckGroup").items.items.length > 0){
			                    	  console.log('newValue.getOff :' + newValue.getOff);
		                              if(newValue.getOff == '1'){
		                              	Ext.getCmp('getOffCheckGroup').show();
		                              }else{
		                              	Ext.getCmp('getOffCheckGroup').hide();
		                              	Ext.getCmp('getOffCheckGroup').items.items[0].setValue(true);
		                              }
	                              }
		                        }
		                    }
				    },
					{
						xtype: 'checkboxgroup',
						id: 'getOffCheckGroup',
						name:'getOffCheckGroup',
//						columns: 4,
						columns: 3,
//						vertical: true,
				        hidden: true,
//				        width:1000,
				        width:800,
						//allowBlank: false,
						listeners: {
                            beforerender: 'LoadingGetOffCheckGroup',
                        }
					  },
					{
						xtype: 'checkboxgroup',
				        fieldLabel: '公车性质',
						name: 'vehicleTypeList',
						columns: 4,
						vertical: true,
				        width:400,
						allowBlank: false,
						items: [
								{ boxLabel: '应急机要通信接待用车', name: 'vehicleType', inputValue: '0' , checked: true},
								{ boxLabel: '行政执法用车', name: 'vehicleType', inputValue: '1',},
								{ boxLabel: '行政执法特种专业用车', name: 'vehicleType', inputValue: '2' },
								{ boxLabel: '一般执法执勤用车', name: 'vehicleType', inputValue: '3' },
								{ boxLabel: '执法执勤特种专业用车', name: 'vehicleType', inputValue: '4' },
								]
					  },
				    {
				        xtype: 'radiogroup',
				        fieldLabel: '用车额度',
						name: 'useLimit',
				        columns: 2,
				        vertical: true,
				        width:400,
				        items: [
				            { boxLabel: '不占用', name: 'useLimitRadio', inputValue: '0', checked: true},
				            { boxLabel: '占用', name: 'useLimitRadio', inputValue: '1',},
				        ]
				    },],
				buttonAlign : 'center',
			    buttons: [{
						text : '确定',
						disabled : true,
		                formBind : true,
						handler: 'editRuleInfoDone',
					},{
						text: '取消',
						handler: function(btn) {
							btn.up('editRuleInfo').close();
						}
					}]
		    }

	   ],
});