/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllotVehicleSearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'allotVehicleSearchForm',
    bodyPadding: 8,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 60,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 0 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: {
            type: 'column',
        },
        flex:3,
        items: [{
            name:'userID',
            fieldLabel: '车牌号',
            xtype: 'textfield',
            emptyText:'车牌号',
        	}, {
            	fieldLabel: '车辆品牌',
            	xtype: 'combo',
            	name: 'carBrand',
            	displayField: 'type',
    		    //valueField: 'id',
            	emptyText: '全部',
            	typeAhead:false,
            	editable:false,
//    		    store : {
//    				fields : ['type','value' ],
//    				data : [
//    				        {'type' : '全部','value' : 'all'},
//    				        {'type' : 'BMW','value' : 'bmw'}
//    				]
//    			},
            	/*store:new Ext.data.Store({
            		proxy: {
            			type: 'ajax',
            			url: 'app/data/ordermgmt/allocatecar/carBrandData.json',
            			actionMethods : 'get',
            			reader: {
            				type: 'json',
            				totalProperty: 'totalCount',
            				rootProperty: 'data'
            			}
            		},
            		autoLoad : true,
            		listeners:{
            			load : function(store, records, options ) {
            				store.insert(0,{'type' : '全部','value':'all'});
            			}
            		}
            	}),
            	*/
            }, {
        	fieldLabel: '公车性质',
        	xtype: 'combo',
        	displayField: 'type',
		    //valueField: 'id',
        	emptyText: '全部',
        	typeAhead:false,
        	editable:false,
		    store : {
				fields : ['type','value' ],
				data : [
				        {'type' : '所有类型','value' : 'all'},
				        {'type' : '应急机要通信接待用车','value' : 'jj'},
				        {'type' : '行政执法用车','value' : 'ss'},
				        {'type' : '行政执法特种专业用车','value' : 'sw'},
				        {'type' : '一般执法执勤用车','value' : 'hh'},
                        {'type' : '执法执勤特种专业用车','value' : 'hf'}
				]
			},
        }, {
        	 name:'seatNum',
             fieldLabel: '车座数',
             xtype: 'textfield',
        }/*, {
        	fieldLabel: '车辆颜色',
        	xtype: 'combo',
        	displayField: 'type',
		    //valueField: 'id',
		    store : {
				fields : ['type','value' ],
				data : [
				        {'type' : '全部','value' : 'all'},
				        {'type' : '黑色','value' : 'black'},
				        {'type' : '白色','value' : 'white'}
				]
			},
        }*/,{
        	fieldLabel: '站点',
        	xtype: 'combo',
        	displayField: 'position',
        	emptyText: '全部',
        	typeAhead:false,
        	editable:false,
		    //valueField: 'id',
//		    store : {
//				fields : ['type','value' ],
//				data : [
//				        {'type' : '长寿路停车场','value' : 'sh'}
//				]
//			},
        	store:new Ext.data.Store({
        		fields : ['position','id' ],
        		proxy: {
        			type: 'ajax',
        			url: 'station/findByStationName',
        			actionMethods : 'POST',
        			reader: {
        				type: 'json',
        				totalProperty: 'totalCount',
        				rootProperty: 'data'
        			}
        		},
        		autoLoad : false,
        		listeners:{
        			load : function(store, records, options ) {
        				store.insert(0,{'position' : '全部','id':'all'});
        			}
        		}
        	}),
        }, {
            name:'used_start_time',
            fieldLabel: '可用时间',
            xtype: 'datefield',
			format: 'Y-m-d H:i:s',
        }, {
        	fieldLabel: '——',
            name:'used_end_time',
            labelSeparator: '',
            xtype: 'datefield',
    		format: 'Y-m-d H:i:s',
         }, {
         	xtype:'field',
         }, {
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
//        	style:'float:right;width:100px;left:-69px;top:10px;',
        	handler: 'onSearchClick',
        }, {
        	xtype:'field',
        }, {
        	xtype:'button',
        	text: '<i class="fa fa-refresh"></i>&nbsp;重置',
//        	style:'float:right;width:100px;left:-69px;top:10px;',
        	handler: 'onSearchClick',
        }]
	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
