/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    id:'mantainanceSearchForm',
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 65,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
		xtype: 'form',
        layout: 'hbox',
//        flex:1,
        items: [{
            name:'vehicleNumber',
            fieldLabel: '车牌号',
            xtype: 'textfield',
            emptyText:'车牌号',
            width:180,
        	},/*{
        	fieldLabel: '所属部门',
        	xtype: 'combo',
        	valueField: 'id',
        	name: 'arrangedOrgName',
        	displayField: 'name',
        	typeAhead:false,
        	editable:false,
        	emptyText: '请选择部门',
        	listeners:{
		        afterrender: function(){
	        		var userType = window.sessionStorage.getItem('userType');
	        		if(!(userType == '2' || userType == '0' || userType == '6')){
		                this.hidden=true;
                   }
	        	},
		    },
		    store:Ext.create('Ext.data.Store', {
		     	proxy: {
		         	type: 'ajax',
		         	url: 'organization/findLowerLevelOrgList',
		     		reader: {
			        	type: 'json',
			         	rootProperty: 'data',
			         	successProperty: 'status'
		     		}
		     	},
		       	autoLoad:false,
		       	listeners:{
		       		load:function(store,records,options){
		       			store.insert(0,{"name":"所有部门","id":"0"})
		       		}
		       	}
	     	})
        },*/{
                xtype:'textfield',
                name:'deptId',
                hidden:true,
                value: window.sessionStorage.getItem("organizationId"),               
                listeners:{
                    afterrender: function(text){
                        text.setValue(window.sessionStorage.getItem("organizationId"));
                    }
                },
            },{
                xtype : 'combo',
                name:'deptName',
                fieldLabel : '所属部门',
                itemId : 'deptId',
                editable : false,
                emptyText : '请选择...',
                value: window.sessionStorage.getItem("organizationName"),
                listeners:{
                    expand: 'openDeptChooseWin',
                    afterrender: function(combo){
                        console.log("name: " + window.sessionStorage.getItem("organizationName"));
                        console.log("Id: " + window.sessionStorage.getItem("organizationId"));
                        combo.setValue(window.sessionStorage.getItem("organizationName"));
                    }
                },
            }, {
                xtype:'checkboxgroup',
                hideLabel:true,
                columns: 2,
                width:180,
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
            xtype: 'combo',
            valueField: 'value',
            labelWidth: 85,
            name: 'searchScope',
            fieldLabel: '车辆保养状态',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '',
            emptyText: '请选择维保日期',
            width:210,
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "全部",
                    "value": ""
                }, {
                    "name": "已过期",
                    "value": "-1"
                }, {
                    "name": "7天内到期",
                    "value": "7"
                }, {
                    "name": "30天内到期",
                    "value": "30"
                }, {
                    "name": "次月到期",
                    "value": "1M"
                }]
            }
        },{
            margin: '0 0 0 10',
            xtype:'button',
            text:'<i class="fa fa-search"></i>&nbsp;查询',
            width:70,
            handler:'onClickSearch'
        }
        ]
	},
	{
    	xtype:'container',
    	flex:1,
//    	margin:'10 0 0 0',
    	items:[
            {
                xtype:'button',
                style:'float:right',
                text: '<i class="fa fa-plus"></i>&nbsp;新增记录',
                handler: 'onAddCilick'
            },
            {
                xtype:'field',
                style:'float:right',
                width:3,
            },
            {
                xtype:'button',
                style:'float:right',
                text: '<i class="fa fa-upload"></i>&nbsp;批量导入',
                handler: 'onUploadClick'
            },
            {
                xtype:'field',
                style:'float:right',
                width:3,
            },{
                xtype:'button',
                style:'float:right',
                text: '<i class="fa fa-download"></i>&nbsp;批量导出',
                handler: 'onExcelClick'
            }]
	}
	
    ],
    initComponent: function() {
        this.callParent();
    }
});
