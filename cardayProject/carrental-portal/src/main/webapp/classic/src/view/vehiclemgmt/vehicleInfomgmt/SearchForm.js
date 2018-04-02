Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.HBox', 'Ext.form.field.ComboBox'],
    
    id: 'vehicleinfo_searchform_id',
    reference: 'searchForm',
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    header: false,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'container',
        layout: 'hbox',
        margin:'0 0 10 0',
        items: [{
            xtype: "button",
            text: "<i class='fa fa-plus'></i>&nbsp;新增车辆",
            itemId: 'addVehicle',
            handler: "addVehicle",
            margin:'0 10 0 0',
            listeners: {
                afterrender: function() {
                    var userType = window.sessionStorage.getItem('userType');
                    if (userType == '3') {
                        this.hidden = true;
                    }
                }
            }
        }, {
            xtype: "button",
            text: "<i class='fa fa-upload'></i>&nbsp;批量导入",
            handler: "openFileUpWindow",
            listeners: {
                afterrender: function() {
                    var userType = window.sessionStorage.getItem('userType');
                    if (userType == '3') {
                        this.hidden = true;
                    }
                }
            }
        }]
    }, {
        xtype: 'form',
        defaults:{
	        margin: '0 5 0 0'
        },
        fieldDefaults: {
	        labelWidth: 60
    	},
        layout: 'hbox',
        items: [{
            name: 'vehicleNumber',
            xtype: 'textfield',
            labelWidth: 45,
            width:190,
            emptyText: '车牌号',
            fieldLabel: '车牌号'
        }, {
            xtype: 'combo',
            width:250,
            valueField: 'value',
            name: 'vehicleType',
            fieldLabel: '公车性质',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '-1',
            emptyText: '请选择公车性质',
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "所有类型",
                    "value": "-1"
                }, {
                    "name": "应急机要通信接待用车",
                    "value": "0"
                }, {
                    "name": "行政执法用车",
                    "value": "1"
                },{
                    "name": "行政执法特种专业用车",
                    "value": "2"
                },{
                    "name": "一般执法执勤用车",
                    "value": "3"
                }, {
                    "name": "执法执勤特种专业用车",
                    "value": "4"
                }]
            }
        }, {
            xtype: 'combo',
            width:205,
            valueField: 'id',
            itemId: 'fromOrgId',
            name: 'fromOrgId',
            fieldLabel: '车辆来源',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            emptyText: '请选择车辆来源',
            value: '-1',
            store: Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: 'vehicle/listVehicleFrom',
                    reader: {
                        type: 'json',
                        rootProperty: 'data',
                        successProperty: 'status'
                    }
                },
                autoLoad: true,
                listeners: {
                    load: function(store, records, options) {
                        var userType = window.sessionStorage
                            .getItem('userType');
                        if (userType == '6') {
                            var organizationId = window.sessionStorage.getItem("organizationId");
                            for (var i in records) {
                                if (records[i].getData().id != organizationId) {
                                    store.remove(records[i]);
                                }
                            }

                        }
                        store.insert(0, {
                            "name": "所有来源",
                            "id": "-1"
                        })
                    }
                }
            })
        }, {
			xtype:'textfield',
			name:'deptId',
			hidden:true,
//			value: window.sessionStorage.getItem("organizationId"),
			listeners:{
        		afterrender: function(text){
        			text.setValue(window.sessionStorage.getItem("organizationId"));
        		}
		    },
		},{
            xtype: 'combo',
            width:205,
//            valueField: 'id',
            name: 'deptName',
            fieldLabel: '所属部门',
            itemId: 'deptId',
//            queryMode: 'remote',
            editable: false,
//            displayField: 'name',
//            value: '-1',
//            value: window.sessionStorage.getItem("organizationName"),
            emptyText: '请选择...',
            listeners: {
            	//现在企业管理员和部门管理员都可以修改，去掉限制
                /*afterrender: function() {
                    var userType = window.sessionStorage.getItem('userType');
                    if (userType == '3') {
                        this.hidden = true;
                    }
                },*/
                /*expand: function(combo, event) {
                    combo.getStore().load();
                }*/
                expand: 'openDeptChooseWin',
                afterrender: function(combo){
        			console.log("name: " + window.sessionStorage.getItem("organizationName"));
        			console.log("Id: " + window.sessionStorage.getItem("organizationId"));
        			combo.setValue(window.sessionStorage.getItem("organizationName"));
        		}
            },
            /*store: Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: 'organization/findLowerLevelOrgList',
                    reader: {
                        type: 'json',
                        rootProperty: 'data',
                        successProperty: 'status'
                    }
                },
                autoLoad: true,
                listeners: {
                    load: function(store, records, options) {
                        store.insert(0, {
                            "name": "全部",
                            "id": "-1"
                        })
                    }
                }
            })*/
        }, {
			xtype:'checkboxgroup',
			hideLabel:true,
	        columns: 2,
	        width:170,
	        flex:null,
	        vertical: true,
	        items: [
	            { boxLabel: '本部门', name: 'selfDept', inputValue: 1,checked: true, 
	            	width:70,
	            	listeners:{
            			change:'checkIsGroupNull',
	            	},
	            	xtype:'checkbox',
	            	style:{
	            		marginRight:'10px',
	            		marginLeft:'10px',
	            	}},
	            { boxLabel: '子部门', name: 'childDept', inputValue: 1,checked: true,
            		listeners:{
            			change:'checkIsGroupNull',
	            	},
	            	xtype:'checkbox',
	            	style:{
	            		marginRight:'10px',
	            	}},
	        ],
	        allowBlank : false,
    		blankText : '不能为空'
		}, {
            xtype: 'combo',
            width:205,
            valueField: 'id',
            name: 'arrangeEnt',
            fieldLabel: '分配企业',
            value: '-1',
            queryMode: 'local',
            typeAhead: false,
            editable: false,
            displayField: 'name',
            emptyText: '请选择...',
            listeners: {
                afterrender: function() {
                    var userType = window.sessionStorage.getItem('userType');
                    if (userType == '3' || userType == '2') {
                        this.hidden = true;
                    }
                }
            },
            store: Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: 'vehicle/listVehicleFrom',
                    reader: {
                        type: 'json',
                        rootProperty: 'data',
                        successProperty: 'status'
                    }
                },
                autoLoad: true,
                listeners: {
                    load: function(store, records, options) {
                        store.insert(0, {
                            "name": "全部",
                            "id": "-1"
                        })
                    }
                }
            })
        }, {
            margin: '0 0 0 5',
            xtype: 'button',
            text: '<i class="fa fa-search"></i>&nbsp;查询',
//            width: 100,
            id: 'searchVehicle',
            listeners: {
                click: 'onSearchClick'
            }
        }]
    }],
    /*// 和buttons配置是一样的效果
    dockedItems : [{
    	xtype : 'toolbar',
    	dock : 'bottom',
    	ui : 'footer',
    	style : "background-color:#FFFFFF",
    	margin : '10 0 0 0',
    	items : ['->', {
    				xtype : "button",
    				text : "<i class='fa fa-plus'></i>&nbsp;新增车辆",
    				itemId : 'addVehicle',
    				handler : "addVehicle",
    				listeners : {
    					afterrender : function() {
    						var userType = window.sessionStorage
    								.getItem('userType');
    						if (userType == '3') {
    							this.hidden = true;
    						}
    					}
    				}
    			}, {
    				xtype : "button",
    				text : "<i class='fa fa-upload'></i>&nbsp;批量导入",
    				handler : "openFileUpWindow",
    				listeners : {
    					afterrender : function() {
    						var userType = window.sessionStorage
    								.getItem('userType');
    						if (userType == '3') {
    							this.hidden = true;
    						}
    					}
    				}
    	}]
    }],*/
    initComponent: function() {
        this.callParent();
    }
});
