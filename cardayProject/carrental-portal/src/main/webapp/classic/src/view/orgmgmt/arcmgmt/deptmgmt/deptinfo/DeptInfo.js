/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptInfo', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	        'Ext.form.field.Date',
	        'Ext.form.FieldContainer',
	        'Ext.form.field.ComboBox',
	        'Ext.form.FieldSet',
	],
	id:'deptMgmtDeptInfo',
	reference : 'searchForm',
	bodyPadding : 10,
	controller:{
		xclass:'Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptInfoController'
	},
	listeners:{
		activate:'loadDeptInfo',
	},
	header : false,
    margin: '10 0 20 0',
    layout:{
    	type:'auto'
    },
    style:"border-top:2px solid #0d85ca;",
    loadData: function(){
    	console.log("deptMgmtDeptInfo.loadData()");
    	this.getController().loadDeptInfo();
    },
    items: [{
    	xtype:'fieldcontainer',
    	style:{
			 margin:'0 auto',
			 padding:'10px',
		},
	    defaults: {
	        layout: 'form',
	    },
    	items:[{
        	xtype:'textfield',
        	fieldLabel:'id',
        	name:'id',
        	hidden:true,
    	},{
        	xtype:'textfield',
        	fieldLabel:'部门ID',
        	name:'organizationId',
            hidden:true,
        	editable:false,
    	},{
            xtype:'textfield',
            fieldLabel:'单位名称',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            id:'companyId',
            name:'name',
        },{
    		xtype:'textfield',
    		fieldLabel:'部门名称',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            id:'deptId',
    		name:'name',
    	},{
            xtype:'textfield',
            fieldLabel:'组织机构代码',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            name:'institutionCode',
        },{
            xtype:'textfield',
            fieldLabel:'单位/部门',
            name:'isInstitution',
            hidden:true,
        },{
            xtype:'textfield',
            fieldLabel:'单位性质',
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
    		/*xtype:'fieldcontainer',
    		layout:'hbox',
    		height:32,
    		items:[{
        		xtype:'displayfield',
        		fieldLabel: '上级部门',
        		width:105,
//    			margin:'3 0 0 0'
        	},{*/
        		xtype:'combo',
//        		hideLabel:true,
        		fieldLabel:'上级单位',
        		name:'parentName',
        		allowBlank: false,//不允许为空
		        blankText: '不能为空',//提示信息
        		editable:false,
//                hidden:true,
    			listeners:{
    	        	expand: 'openDeptChooseWin'
    		    },
        	/*}]*/
    	},{
    		xtype:'textfield',
    		name:'parentId',
    		hidden:true,
    		
    	},{
    		xtype:'fieldcontainer',
    		layout:'hbox',
    		height:32,
            hidden:true,
    		items:[{
        		xtype:'displayfield',
        		fieldLabel: '部门级别',
        		width:105,
//    			margin:'3 0 0 0'
        	},{
        		xtype:'textfield',
        		hideLabel:true,
        		allowBlank: false,//不允许为空
		        blankText: '不能为空',//提示信息
        		fieldLabel:'部门级别',
        		name:'level',
        		editable:false,
        	}]
    	},{
            xtype:'textfield',
            fieldLabel:'单位地址',
            name:'address',
        },{
            xtype:'textfield',
            fieldLabel:'负责人',
            name:'vehicleAdministrator',
        },{
            xtype:'textfield',
            fieldLabel:'电话',
            name:'phone',
        },]
    }],
    dockedItems : [{
		xtype : 'toolbar',
		dock : 'bottom',
		ui : 'footer',
		style : "background-color:#FFFFFF",
		items : [
				'->', {
					xtype:'button',
					id:'deptInfoSaveBtn',
					text:'保存',
					handler:'editDept',
					formBind:true,
				},{
					xtype:'button',
					id:'deptInfoDelBtn',
					text:'删除部门',
					handler:'deleteDept',
					style:"background-color:red; color:white; border-color:red;"
				},'->']
	}],
	initComponent : function() {
		this.callParent();
	}
});
