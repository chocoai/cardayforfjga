/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    bodyPadding: 8,
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
    margin: '10 10 0 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: {
            type: 'column',
        },
        flex:4,
        items: [{
            name:'vehicleNumber',
            fieldLabel: '车牌号',
            xtype: 'textfield',
            emptyText:'车牌号',
        	},{
        	fieldLabel: '公车性质',
        	xtype: 'combo',
        	name:'vehicleType',
        	displayField: 'type',
        	valueField: 'value',
		    //valueField: 'id',
        	emptyText: '请选择公车性质',
        	typeAhead:false,
        	editable:false,
		    store : {
				fields : ['type','value' ],
				data : [
				        {'type' : '所有类型','value' : '-1'},
				        {'type' : '应急机要通信接待用车','value' : '0'},
				        {'type' : '行政执法用车','value' : '1'},
				        {'type' : '行政执法特种专业用车','value' : '2'},
				        {'type' : '一般执法执勤用车','value' : '3'},
                        {'type' : '执法执勤特种专业用车','value' : '4'}
				]
			},
        },{
        	fieldLabel: '车辆来源',
        	xtype: 'combo',
        	displayField: 'name',
        	name: 'vehicleFromName',
		    valueField: 'id',
        	emptyText: '请选择车辆来源',
        	typeAhead:false,
        	editable:false,
        	listeners:{
		        afterrender: function(){
	        		var userType = window.sessionStorage.getItem('userType');
	        		if(userType == '1'){
		                this.hidden=true;
                   }
	        	},
		    },
        	store:Ext.create('Ext.data.Store', {
		     	proxy: {
		         	type: 'ajax',
		         	url: 'vehicle/listVehicleFrom',
		     		reader: {
			        	type: 'json',
			         	rootProperty: 'data',
			         	successProperty: 'status'
		     		}
		     	},
		       	autoLoad:false,
		       	listeners:{
		       		load:function(store,records,options){
		       			store.insert(0,{"name":"所有来源","id":"-1"})
		       		}
		       	}
	     	})
        },{
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
	        		if(!(userType == '2' || userType == '0' )){
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
		       			store.insert(0,{"name":"所有部门","id":"-1"})
		       		}
		       	}
	     	})
        },{
        	fieldLabel: '所属城市',
        	xtype: 'combo',
        	name: 'city',
        	displayField: 'type',
        	valueField: 'value',
        	emptyText: '请选择城市',
        	listeners:{
		        afterrender: function(){
	        		var userType = window.sessionStorage.getItem('userType');
	        		if(!(userType == '0' || userType == '1')){
		                this.hidden=true;
                   }
	        	},
		    },
		    store : {
				fields : ['type','value' ],
				data : [
				        {'type' : '上海','value' : '0'},
				        {'type' : '北京','value' : '1'},
				        {'type' : '重庆','value' : '2'},
				        {'type' : '武汉','value' : '3'}
				]
			},
        },/*,{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	style:'float:right;width:100px;left:-69px;top:10px;',
        	handler: 'onSearchClick',
        }*/
        {
            margin: '0 0 0 20',
            xtype:'button',
            text: '<i class="fa fa-search"></i>&nbsp;查询',
            handler: 'onSearchClick'
        }
        ]
	}/*,
	{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text: '<i class="fa fa-search"></i>&nbsp;查询',
    		style:'float:right',
    		handler:'onAddClick'
    	}]
	}*/
    ],
/*    dockedItems: [{
	    xtype: 'toolbar',
	    dock: 'bottom',
	    ui: 'footer',
	    style : "background-color:#FFFFFF",
//	    margin:'10 0 0 0',
	    items: [{ 
	    		xtype: 'component', flex: 1 
	    	},{
	        	xtype:'button',
	        	text: '<i class="fa fa-search"></i>&nbsp;查询',
	        	handler: 'onSearchClick'
          	}
	    ]
	}],*/
    initComponent: function() {
        this.callParent();
    }
});
