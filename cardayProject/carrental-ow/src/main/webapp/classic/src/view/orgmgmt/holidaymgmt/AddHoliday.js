Ext.define('Admin.view.orgmgmt.holidaymgmt.AddHoliday',{
	extend: 'Ext.window.Window',	
    alias: "widget.addHoliday",
	title : '新增节假日',
	controller: {
        xclass: 'Admin.view.orgmgmt.holidaymgmt.ViewController'
    },
	
	id: 'addHoliday',
	width: 500,
    layout: 'fit',
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,

	items:[{
		xtype:'form',
		reference: 'addHoliday',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        fieldDefaults: {
            msgTarget: 'side'
        },	
		items:[{
			fieldLabel: '<span style="color:red;">*</span>年份',
            xtype : 'combo',
            queryMode : 'local',
            editable : false,
            store : new Ext.data.ArrayStore({
                fields : ['id','name'],
                data : []
            }),
            name: 'holidayYear',
            valueField : 'name',
            displayField : 'id',
            triggerAction : 'all',
            autoSelect : true,
            value: Ext.Date.format(new Date(),'Y'),
            listeners : {
                beforerender :  function(){
                    var newyear = Ext.Date.format(new Date(),'Y');//这是为了取现在的年份数
                    var yearlist = [];
                     for(var i = Number(newyear) + 1 ; i >= Number(newyear) - 50; i--){
                      yearlist.push([i,i]);
                    }
                    this.store.loadData(yearlist);
                }
            }
        },{
			fieldLabel: '<span style="color:red;">*</span>节假日',
			xtype: 'combo',
        	displayField: 'value',
			allowBlank: false, 
			blankText: '节假日不能为空',
        	name: 'holidayType',
            editable:false,
            value:'春节',
	        width: 300,
		    store : {
				fields : ['id','value' ],
				data : [
				        {'id' : '0','value' : '元旦'},
				        {'id' : '1','value' : '春节'},
				        {'id' : '2','value' : '清明节'},
				        {'id' : '3','value' : '劳动节'},
				        {'id' : '4','value' : '端午节'},
				        {'id' : '5','value' : '国庆节'},
				        {'id' : '6','value' : '中秋节'},
				]
			},
			listeners : {
	            select: function(combo , record , eOpts){
                    Ext.getCmp('addHoliday').down('form').getForm().findField('holidayTime').setValue('');
                    Ext.getCmp('addHoliday').down('form').getForm().findField('adjustHolidayTime').setValue('');
		        }
	        },
		},{
			xtype:'fieldcontainer',
			layout: {
	            type: 'hbox', 
	            align: 'stretch'
	        },
	        items:[
					{
						fieldLabel: '<span style="color:red;">*</span>休息日',
						xtype: 'textfield',
				        name: 'holidayTime',
				        readOnly: true,
				        allowBlank:false
					},
					{
			            xtype:'field',
			            width:3,
			        },
					{
						fieldLabel: '',
						xtype: 'datefield',
				        name: 'restDate',
				        format:'m-d',
				        value: new Date(),
				        editable:false,
				        listeners: {
		                    select: function(field,value,eOpts){
			                    	var month = value.getMonth() + 1;
			                    	var date = value.getDate();
			                    	if(month < 10){
			                    		month = '0' + month;
			                    	}
			                    	if(date < 10){
			                    		date = '0' + date;
			                    	}
			                    	var dateItem = month + '.' + date;
			                    	var holidayTime = Ext.getCmp('addHoliday').down('form').getForm().findField('holidayTime').getValue();
			                    	if(holidayTime != ''){
			                    	   Ext.getCmp('addHoliday').down('form').getForm().findField('holidayTime').setValue(holidayTime + ',' + dateItem);
			                    	}else{
			                    		Ext.getCmp('addHoliday').down('form').getForm().findField('holidayTime').setValue(dateItem);
			                    	}
		                        }
		                    }
				    },]
		},{
			xtype:'fieldcontainer',
			layout: {
	            type: 'hbox', 
	            align: 'stretch'
	        },
	        items:[
					{
						fieldLabel: '<span style="color:red;">*</span>调休上班日',
						xtype: 'textfield',
				        name: 'adjustHolidayTime',
				        readOnly: true,
					},
					{
			            xtype:'field',
			            width:3,
			        },
					{
						fieldLabel: '',
						xtype: 'datefield',
				        name: 'workDate',
				        format:'m-d',
				        value: new Date(),
				        editable:false,
				        listeners: {
		                    select: function(field,value,eOpts){
			                    	var month = value.getMonth() + 1;
			                    	var date = value.getDate();
			                    	if(month < 10){
			                    		month = '0' + month;
			                    	}
			                    	if(date < 10){
			                    		date = '0' + date;
			                    	}
			                    	var dateItem = month + '.' + date;
			                    	var adjustHolidayTime = Ext.getCmp('addHoliday').down('form').getForm().findField('adjustHolidayTime').getValue();
			                    	if(adjustHolidayTime != ''){
			                    	   Ext.getCmp('addHoliday').down('form').getForm().findField('adjustHolidayTime').setValue(adjustHolidayTime + ',' + dateItem);
			                    	}else{
			                    		Ext.getCmp('addHoliday').down('form').getForm().findField('adjustHolidayTime').setValue(dateItem);
			                    	}
		                        }
		                    }
				    }]
		},
		],
      	buttonAlign : 'center',
	    buttons : [{
				text : '确定',
				disabled : true,
                formBind : true,
				handler:'addHolidayDone'
			},{
				text: '取消',
				handler: function(btn){
					btn.up('addHoliday').close();
				}
			}]
	}
	],
});