/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.drivermgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'driver_searchform_id',
    reference: 'searchForm',
    //bodyPadding: 8,
    header: false,
//    fieldDefaults: {
//        // labelAlign: 'right',
//        labelWidth: 40,
//        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
//        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
//    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        flex:1,
        items: [
		{
			fieldLabel: '司机姓名',
		    name:'realname',
		    xtype: 'textfield',
		    labelWidth: 60,
			width:180,
		},
		{
			fieldLabel: '手机号码',
			margin: '0 0 0 20', //上，右，下，左
		    name:'phone',
		    xtype: 'textfield',
		    labelWidth: 60,
			width:200,
            msgTarget: 'side',
            regex: /^\d{0,11}$/,
            regexText: '请输入至多11位数字',
		},{
            xtype: 'combo',
            margin: '0 0 0 20', //上，右，下，左
            width:180,
            labelWidth: 60,
            valueField: 'value',
            name: 'drvStatus',
            fieldLabel: '司机状态',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '-1',
            emptyText: '请选择司机状态',
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "所有状态",
                    "value": -1
                }, {
                    "name": "短途出车",
                    "value": 0
                }, {
                    "name": "长途出车",
                    "value": 1
                },{
                    "name": "在岗",
                    "value": 2
                },{
                    "name": "值班锁定",
                    "value": 3
                }, {
                    "name": "补假/休假",
                    "value": 4
                }, {
                    "name": "计划锁定",
                    "value": 5
                }, {
                    "name": "出场",
                    "value": 6
                }, {
                    "name": "下班",
                    "value": 7
                }]
            }
        },
		{
    		fieldLabel: '所属部门ID',
		    name:'organizationId',
		    xtype: 'textfield',
		    hidden:true,
//		    value:window.sessionStorage.getItem("organizationId")
		    listeners:{
        		afterrender: function(text){
        			text.setValue(window.sessionStorage.getItem("organizationId"));
        		}
		    },
		},
		{
			margin: '0 0 0 30', //上，右，下，左
			fieldLabel: '所属部门',
			xtype: 'combo',
//			name: 'organizationId',
			name: 'organizationName',
//			queryMode: 'local',
			labelWidth: 60,
			emptyText : '请选择...',
			width:180,
			editable:false,
		
		    /*只有企业管理员可以进行部门的筛选*/
			listeners:{
				expand: 'openDeptChooseWin',
				afterrender: function(combo){
        			console.log("name: " + window.sessionStorage.getItem("organizationName"));
        			console.log("Id: " + window.sessionStorage.getItem("organizationId"));
        			combo.setValue(window.sessionStorage.getItem("organizationName"));
        		}
		        /*afterrender: function(){
		    		var userType = window.sessionStorage.getItem('userType');
		    		//console.log("*******"+userType+"*********");
		    		if(userType != '2' && userType != '6'){
		                this.hidden=true;
		           }
		    	},*/
		    },
		    //hidden:true,
		},
		{
			xtype:'checkboxgroup',
			hideLabel:true,
	        columns: 2,
	        vertical: true,
	        items: [
	            { boxLabel: '本部门', name: 'selfDept', inputValue: true,checked: true, 
	            	listeners:{
	            		change:'checkIsGroupNull',
	            	},
	            	width:70,
	            	style:{
	            		marginRight:'10px',
	            		marginLeft:'10px',
	            	}},
	            { boxLabel: '子部门', name: 'childDept', inputValue: true,checked: true,
	            		listeners:{
	            			change:'checkIsGroupNull',
		            	}
	            },
	        ],
	        allowBlank : false,
    		blankText : '不能为空'
		},
		{
			margin: '0 0 0 30', //上，右，下，左
			xtype:'button',
			text: '<i class="fa fa-search"></i>&nbsp;查询',
			formBind : true,
			handler: 'onSearchClick',
		}]
	},
	{
    	xtype:'container',
    	//flex:1,
    	items:[{
    		xtype:'button',
    		text:'<i class="fa fa-plus"></i>&nbsp;新增司机',
    		margin: '0 20 0 0',
    		//style:'float:right',
    		handler:'onAddClick',
    		listeners:{
			//部门管理员，只能查看司机信息，不能新增
	        afterrender: function(){
        		var userType = window.sessionStorage.getItem('userType');
        		if(userType == '3'){
	                this.hidden=true;
        			}
        		}
    		}
    	},
    	{
        	xtype: "button",
    		text: "<i class='fa fa-upload'></i>&nbsp;批量导入",
    		handler: "openFileUpWindow",
    		listeners:{
    			//部门管理员，只能查看司机信息，不能新增
    	        afterrender: function(){
            		var userType = window.sessionStorage.getItem('userType');
            		if(userType == '3'){
    	                this.hidden=true;
            		}
            	}
        	}
        },/*{
        	xtype: "button",
    		text: "<i class='fa fa-upload'></i>&nbsp;test",
    		handler: function(){
    			var parm = {
    				    orderId: "",
    				    source: "1",
    				    status: "0",
    				    orderTimeFrom: "",
    				    orderTimeTo: "",
    				    orderFeeMin: "",
    				    orderFeeMax: ""
    				};
    			var json = Ext.encode(parm);
    			//跨域访问api
    			Ext.Ajax.request({
    				url: 'http://10.11.103.188:8888/boss-order/ws/boss/internal/order/1/10/query',
    		        method : 'POST',
    				params:json,
    				headers : {'Content-Type' : 'application/json;charset=UTF-8'},
    		        success : function(response,options) {
    					var respText = Ext.util.JSON.decode(response.responseText);
    					if(respText.resultCode == '200'){
    						console.log(respText.result);
    					}else{
    	                    console.log(respText.resultMsg);
    					}
    		        },
    		        scope: this
    		    });

    		}
        }*/]
	},
	],
    
    initComponent: function() {
        this.callParent();
    }
});
