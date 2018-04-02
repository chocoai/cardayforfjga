/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.empmgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'emp_searchform_id',
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
            	fieldLabel: '民警姓名',
	            name:'realname',
	            xtype: 'textfield',
	            //emptyText:'民警姓名',
	            labelWidth: 60,
				width:220,
        	},
			{
        		fieldLabel: '手机号码',
        		margin: '0 0 0 30', //上，右，下，左
			    name:'phone',
			    xtype: 'textfield',
			    labelWidth: 60,
				width:220,
                msgTarget: 'side',
                regex: /^\d{0,11}$/,
                regexText: '请输入至多11位数字',
			},
			{
        		fieldLabel: '所属部门ID',
			    name:'organizationId',
			    xtype: 'textfield',
//			    value:window.sessionStorage.getItem("organizationId"),
			    listeners:{
	        		afterrender: function(text){
	        			text.setValue(window.sessionStorage.getItem("organizationId"));
	        		}
			    },
			    hidden:true,
			},
        	{
    			margin: '0 0 0 30', //上，右，下，左
    			fieldLabel: '所属部门',
    			xtype: 'combo',
    			name: 'organizationName',
//    			value:window.sessionStorage.getItem("organizationName"),
//    			queryMode: 'local',
    			labelWidth: 60,
    			width:180,
    			editable:false,
//    			displayField:'name',
//    			valueField: 'id',
//    			value: '全部',
    			emptyText : '请选择...',

    	        /*只有企业管理员可以进行部门的筛选*/
            	listeners:{
            		expand: 'openDeptChooseWin',
            		afterrender: function(combo){
            			console.log("name: " + window.sessionStorage.getItem("organizationName"));
            			console.log("Id: " + window.sessionStorage.getItem("organizationId"));
            			combo.setValue(window.sessionStorage.getItem("organizationName"));
            		}
            		//现在去掉
    		        /*afterrender: function(){
    	        		var userType = window.sessionStorage.getItem('userType');
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
    	            	width:70,
    	            	listeners:{
	            			change:'checkIsGroupNull',
		            	},
    	            	style:{
    	            		marginRight:'10px',
    	            		marginLeft:'10px',
    	            	}},
    	            { boxLabel: '子部门', name: 'childDept', inputValue: true,checked: true,
    	            	listeners:{
	            			change:'checkIsGroupNull',
		            	}},
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
    	items:[
    	{
    		xtype:'button',
    		text:'<i class="fa fa-plus"></i>&nbsp;新增民警',
    		//style:'float:right',
    		handler:'onAddClick',
    		margin: '0 20 0 0',
    	},
    	{
        	xtype: "button",
    		text: "<i class='fa fa-upload'></i>&nbsp;批量导入",
    		handler: "openFileUpWindow",
        }]
    },

    ],
    
    initComponent: function() {
        this.callParent();
    }
});
