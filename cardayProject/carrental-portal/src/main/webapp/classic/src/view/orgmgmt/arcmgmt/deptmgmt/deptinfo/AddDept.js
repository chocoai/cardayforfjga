/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.AddDept', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	id:'deptMgmtAddDeptWin',
	reference : 'searchForm',
	bodyPadding : 10,
	controller:{
		xclass:'Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptInfoController'
	},
	listeners:{
	},
	bodyPadding: 10,
    constrain: true,
    closable: true,
    resizable: false,
    modal: true,
    width:500,
    height:600,
    resizable: false,// 窗口大小是否可以改变
	title: '添加组织',
	bodyPadding:20,
    items: [{
		 xtype:'form',
		 width:330,
		 height:530,
		 style:{
			 margin:'0 auto',
		 },
	     defaults: {
	        layout: 'form',
	        defaultType: 'textfield',
	     },
	     items: [/*{
        	xtype:'textfield',
        	fieldLabel:'部门ID',
        	name:'id',
        	editable:false,
    	},*/{
            xtype : 'combo',
            valueField : 'value',
            name : 'deptType',
            fieldLabel : '类别',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            queryMode : 'local',
            editable : false,
            emptyText : '请选择类型...',
            displayField : 'name',
            store : {
                    fields : ['name', 'value'],
                    data : [{
                                "name" : "单位",
                                "value" : "0"
                            }, {
                                "name" : "部门",
                                "value" : "1"
                            }]
                },
            listeners:{
                 select: function(combo,record,eOpts){
                    if(record.data.value == '0'){
                        Ext.getCmp('companyName').show();
                        Ext.getCmp('deptName').hide();
                        this.up('form').getForm().findField('institutionCode').show();
                        this.up('form').getForm().findField('institutionFeature').show();
                        this.up('form').getForm().findField('institutionLevel').show();
                        
                        this.up('form').getForm().findField('address').show();
                        this.up('form').getForm().findField('vehicleAdministrator').show();
                        this.up('form').getForm().findField('phone').show();
                        this.up('form').queryById('parentOrgId').show();
                        
                    }else{
                        Ext.getCmp('companyName').hide();
                        Ext.getCmp('deptName').show();
                        this.up('form').getForm().findField('institutionCode').hide();
                        this.up('form').getForm().findField('institutionFeature').hide();
                        this.up('form').getForm().findField('institutionLevel').hide();
                        
                        this.up('form').getForm().findField('address').hide();
                        this.up('form').getForm().findField('vehicleAdministrator').hide();
                        this.up('form').getForm().findField('phone').hide();
                        this.up('form').queryById('parentOrgId').hide();
                    }
                 }
            }
        },{
    		xtype:'textfield',
    		fieldLabel:'单位名称',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            id:'companyName',
            hidden:true,
    		name:'name',
    		blankText : '不能为空'
    	},{
            xtype:'textfield',
            fieldLabel:'部门名称',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            hidden:true,
            id:'deptName',
            name:'name',
            blankText : '不能为空'
        },{
            xtype:'textfield',
            fieldLabel:'组织机构代码',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            name:'institutionCode',
            hidden:true,
        },{
            xtype:'textfield',
            fieldLabel:'单位性质',
            hidden:true,
            name:'institutionFeature',
        },{
            xtype : 'combo',
            valueField : 'value',
            name : 'institutionLevel',
            fieldLabel : '单位等级',
            queryMode : 'local',
            editable : false,
            emptyText : '请选择单位等级',
            displayField : 'name',
            hidden:true,
            store : {
                    fields : ['name', 'value'],
                    data : [{
                                "name" : "正厅级",
                                "value" : 0
                            }, {
                                "name" : "副厅级",
                                "value" : 1
                            }, {
                                "name" : "处级",
                                "value" : 2
                            }, {
                                "name" : "副处级",
                                "value" : 3
                            }, {
                                "name" : "科级",
                                "value" : 6
                            }, {
                                "name" : "副科级",
                                "value" : 7
                            },{
                                "name" : "县级",
                                "value" : 4
                            }, {
                                "name" : "乡科级",
                                "value" : 5
                            }]
                }
        },{
    		xtype:'fieldcontainer',
    		layout:'hbox',
    		id:'parentOrgId',
    		height:32,
            hidden:true,
    		items:[{
        		xtype:'displayfield',
        		fieldLabel: '上级单位',
        		width:105,
//    			margin:'3 0 0 0'
        	},{
        		xtype:'combo',
        		hideLabel:true,
//        		fieldLabel:'上级部门',
        		name:'parentName',
//        		disabled:true,
        	}]
    	},{
    		xtype:'textfield',
    		name:'parentId',
    		hidden:true,
    	},{
    		xtype:'fieldcontainer',
    		layout:'hbox',
            hidden:true,
    		height:32,
    		items:[{
        		xtype:'displayfield',
        		fieldLabel: '部门级别',
        		width:105,
//    			margin:'3 0 0 0'
        	},{
        		xtype:'textfield',
        		hideLabel:true,
        		fieldLabel:'部门级别',
        		name:'level',
//        		disabled:true,
        		editable:false,
        	}]
    	},{
            xtype:'textfield',
            fieldLabel:'单位地址',
            hidden:true,
            name:'address',
        },{
            xtype:'textfield',
            fieldLabel:'负责人',
            hidden:true,
            name:'vehicleAdministrator',
        },{
            xtype:'textfield',
            fieldLabel:'电话',
            hidden:true,
            name:'phone',
        }],
    	dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			items : [
					'->', {
						text : '保存',
						disabled : true,
						formBind : true,
						handler : 'addDept'
					}, {
						text : '取消',
						handler : function() {
							this.up('window').close();
						}
					},'->']
		}],
    }],
	initComponent : function() {
		this.callParent();
	}
});
