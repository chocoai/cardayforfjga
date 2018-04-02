/**
 * 搜索公司名称 This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.overSpeedAlarm.SearchForm', {
	extend : 'Ext.form.Panel',
	requires : ['Ext.layout.container.HBox',
				'Ext.form.field.ComboBox',
				 'Admin.view.orgmgmt.enterinfomgmt.AdvancedVType'],
	reference : 'searchForm',
	id:'overSpeedAlarmSearchForm',
	header : false,
	fieldDefaults : {
		labelWidth : 60,
		flex:1,
		margin : '0 0 0 5',
	},
	layout: {
        type: 'hbox',
        align: 'stretch',
    },
	items : [{
				name : 'vehicleNumber',
				xtype : 'textfield',
				labelWidth : 45,
				emptyText : '车牌号',
				fieldLabel : '车牌号',
			}, {
				xtype : 'combo',
				valueField : 'value',
				name : 'vehicleType',
				fieldLabel : '公车性质',
				queryMode : 'local',
				editable : false,
				displayField : 'name',
				emptyText : '请选择公车性质',
				value:'所有类型',
				store : {
					fields : ['name', 'value'],
					data : [{
								"name" : "所有类型",
								"value" : "-1"
							}, {
								"name" : "应急机要通信接待用车",
								"value" : "0"
							}, {
								"name" : "行政执法用车",
								"value" : "1"
							}, {
								"name" : "行政执法特种专业用车",
								"value" : "2"
							}, {
								"name" : "一般执法执勤用车",
								"value" : "3"
							}, {
								"name" : "执法执勤特种专业用车",
								"value" : "4"
							}]
				}
			}, {
				xtype : 'combo',
				valueField : 'id',
				name : 'fromOrgId',
				fieldLabel : '车辆来源',
				queryMode : 'remote',
				typeAhead:false,
				editable : false,
				displayField : 'name',
				emptyText : '请选择车辆来源',
				value:'-1',
				store : Ext.create('Ext.data.Store', {
					proxy : {
						type : 'ajax',
						url : 'vehicle/listVehicleFrom',
						reader : {
							type : 'json',
							rootProperty : 'data',
							successProperty : 'status'
						}
					},
					autoLoad : true,
					listeners : {
						load : function(store, records, options) {
							store.insert(0, {
										"name" : "所有来源",
										"id" : "-1"
							})
						}
					}
				})
			}, {
				xtype:'textfield',
				name:'deptId',
				hidden:true,
//				value: window.sessionStorage.getItem("organizationId"),
				listeners:{
            		afterrender: function(text){
            			text.setValue(window.sessionStorage.getItem("organizationId"));
            		}
    		    },
			},{
				xtype:'textfield',
				name:'alertType',
				value:'OVERSPEED',
				hidden:true,
			},{
				xtype : 'combo',
//				valueField : 'id',
//				name : 'deptId',
				name:'deptName',
				fieldLabel : '所属部门',
				itemId : 'deptId',
//				queryMode : 'remote',
				editable : false,
//				displayField : 'name',
//				value:'-1',
				// width:220,
				emptyText : '请选择...',
//				value: window.sessionStorage.getItem("organizationName"),
				listeners:{
            		expand: 'openDeptChooseWin',
            		afterrender: function(combo){
            			console.log("name: " + window.sessionStorage.getItem("organizationName"));
            			console.log("Id: " + window.sessionStorage.getItem("organizationId"));
            			combo.setValue(window.sessionStorage.getItem("organizationName"));
            		}
    		    },

				/*store : Ext.create('Ext.data.Store', {
					proxy : {
						type : 'ajax',
						url : 'organization/findLowerLevelOrgList',
						reader : {
							type : 'json',
							rootProperty : 'data',
							successProperty : 'status'
						}
					},
					autoLoad : true,
					listeners : {
						load : function(store, records, options) {
							store.insert(0, {
										"name" : "所有部门",
										"id" : "-1"
							})
						}
					}
				})*/
			}, {
    			xtype:'checkboxgroup',
    			hideLabel:true,
    	        columns: 2,
    	        width:160,
    	        flex:null,
    	        vertical: true,
    	        items: [
    	            { boxLabel: '本部门', name: 'includeSelf', inputValue: true,checked: true, 
    	            	width:70,
    	            	listeners:{
                			change:'checkIsGroupNull',
    	            	},
    	            	style:{
    	            		marginRight:'10px',
    	            		marginLeft:'10px',
    	            	}},
    	            { boxLabel: '子部门', name: 'includeChild', inputValue: true,checked: true,
	            		listeners:{
	            			change:'checkIsGroupNull',
		            	},
    	            	style:{
    	            		marginRight:'10px',
    	            	}},
    	        ],
    	        allowBlank : false,
        		blankText : '不能为空'
    		},{
				fieldLabel : '起始时间',
				name : 'startTime',
				xtype : 'datefield',
				emptyText : '请选择起始时间',
				editable : false,
				itemId: 'startdt',
	       		vtype: 'daterange',
	        	endDateField: 'enddt',
				format : 'Y-m-d',
			}, {
				fieldLabel : '结束时间',
				name : 'endTime',
				xtype : 'datefield',
				format : 'Y-m-d',
				itemId: 'enddt',
	        	editable:false,
	        	vtype: 'daterange',
				emptyText : '请选择结束时间',
				value: new Date(),
				maxValue : new Date()
				// limited to the current date or prior
		}],
	// 和buttons配置是一样的效果
	dockedItems : [{
		xtype : 'toolbar',
		dock : 'bottom',
		ui : 'footer',
		style : "background-color:#FFFFFF",
		margin : '10 0 0 0',
		items : ['->', {
					xtype : 'button',
					text : '<i class="fa fa-search"></i>&nbsp;查询',
					listeners : {
						click : 'onSearchClick'
					}
		},{xtype : 'button',
					text:'<i class="fa fa-download"></i>&nbsp;导出Excel',
					handler:'onExcelClick'
					}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
