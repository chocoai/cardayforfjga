Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.Grid', {
	extend : 'Ext.grid.Panel',
	reference: 'gridvehicleinfo',
	itemId: 'gridvehicleinfo',
	requires : [
		'Ext.grid.column.Action'
	],
	id: 'vehicleInfomgmtId',
	title : '车辆列表',
    bind:{
    	store:'{VehicleResults}'
    },
/*     listeners: {
        	render:'onGridRender'
        },*/
	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
 //   cls : 'user-grid',  //图片用户样式
	stateful: true,
	multiSelect: false,
	forceFit: false,
    mask: true,
   	columnLines: true,  //表格线
/*  defaults: {  //对items的组件生效
        frame: true
    },
    hideHeaders:true,
	header:false,*/
/*	 selModel: {
		injectCheckbox: 0,
		mode: 'SINGLE',
//		checkOnly: true,
		allowDeselect:true
	},
	selType: 'checkboxmodel',	//自5.1.0已废弃
*/	
	
//	selModel: {
//			type: 'checkboxmodel',
//            mode:'SINGLE',
//            allowDeselect:true
//        },
    actions: {
        view: {
            tooltip : '车辆查看',
			iconCls : 'x-fa fa-eye',
			handler: 'viewVehicleInfo'
        },
        modify: {
            tooltip : '车辆修改',
			iconCls : 'x-fa fa-edit',
            handler: 'updateVehicleInfo'
        },
        arrange: {
           tooltip : '分配企业',
		   iconCls : 'x-fa fa-automobile',
           handler: 'openVehicleWindow'
        },
        drop: {
            tooltip : '车辆删除',
			iconCls : 'x-fa fa-close',
            handler : 'deleteVehicle'
        },
        widthdraw:{
           tooltip : '车辆收回',
		   iconCls : 'x-fa fa-rotate-right',
           handler : 'withDrawVehicle'
        },
        arrangedept: {
            tooltip : '分配部门',
 		   iconCls : 'x-fa fa-user-plus',
            handler: 'openVehicleDeptWindow'
        },
        arrangedriver: {
            tooltip : '分配司机',
 		   iconCls : 'x-fa fa-user',
            handler: 'openArrangeDriverWin'
        },
        arrangegeofence: {
            tooltip : '分配地理围栏',
           iconCls : 'x-fa fa-align-justify',
            handler: 'openArrangeGeofenceWin'
        }
    },
	columns:{
		defaults:{
			align:'center',
			sortable: false,
			menuDisabled: true,
			flex:1
			},
		items:[/*{
	    	header:'选择',
	    	xtype:'templatecolumn',
	    	tpl:'<input name=action type=radio value={vehicleNumber}" />'
		  },*/
		  {
            header: '车辆id',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '车牌号',
            dataIndex: 'vehicleNumber',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          /*  renderer:function(value,metaData,record,rowIndex){
            	return '<a href="javascript:void(0);" onclick="showVin(\"'+value+'\")">' + value + '</a>';
            }*/
            },{
            header: '公车性质',
            dataIndex: 'vehicleType',
            renderer: function(val,metaData) {
        		switch(val){
        			case '0':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('应急机要通信接待用车') + '"'; 
	        				return '应急机要通信接待用车';
	        			case '1':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('行政执法用车') + '"'; 
	        				return '行政执法用车';
	        			case '2':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('行政执法特种专业用车') + '"'; 
	        				return '行政执法特种专业用车';
	        			case '3':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('一般执法执勤用车') + '"'; 
	        				return '一般执法执勤用车';
                        case '4':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('执法执勤特种专业用车') + '"'; 
                            return '执法执勤特种专业用车';
        		}
            }
    	  },{
            header: '车辆品牌',
            dataIndex: 'vehicleBrand',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '车辆型号',
            dataIndex: 'vehicleModel',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
	        header: '购买时间',
	        //formatter: 'date("Y-m-d")',
	        dataIndex: 'vehicleBuyTime',
	        renderer:function (value, metaData){
                value = Ext.util.Format.date(value,'Y-m-d');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },  
   		  },{
	        header: '保险到期日',
	        //formatter: 'date("Y-m-d")',
	        dataIndex: 'insuranceExpiredate',
	        renderer:function (value, metaData){
                value = Ext.util.Format.date(value,'Y-m-d');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            }, 
   		  },{
	        header: '年检到期日',
	        //formatter: 'date("Y-m-d")',
	        dataIndex: 'inspectionExpiredate',
	        hidden: true,
	        renderer:function (value, metaData){
                value = Ext.util.Format.date(value,'Y-m-d');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            }, 
   		  },{
            header: '车辆用途',
            dataIndex: 'vehiclePurpose',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '车辆来源',
            dataIndex: 'vehicleFromName',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
              header: '所属站点',
              dataIndex: 'stationName',
          	  hidden: true,
          	  renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            	}
      	  },{
            header: '所属部门',
            dataIndex: 'arrangedOrgName',
        	renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                if (value=='未分配') {
            		var organizationName = window.sessionStorage.getItem('organizationName');
                	var userCategory = window.sessionStorage.getItem('userType');	
                	if (userCategory==2 || userCategory==6) {
                		value = organizationName;
                	}
                	metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                	return value;
            	}else{
            		return value;
            	}
            }
    	  },{
            header: '分配企业',
            dataIndex: 'arrangedEntName',
	        listeners:{
		        afterrender: function(){
	        		var userType = window.sessionStorage.getItem('userType');
	        		 if(userType == '3'||userType == '2'){
			                this.hidden=true;
	                 }
	        	}
		    },
		    renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
	        header: '分配司机姓名',
	        dataIndex: 'realname',
  		    renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
            },
            hidden: true
      	  },{
            header: '司机手机号码',
            dataIndex: 'phone',
  		    renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
            },
      	  	hidden: true
      	  },{
            header: '车辆状态',
            dataIndex: 'vehStatus',
            sortable: true,
            renderer: function(val,metaData) {
                switch(val){
                    case 0:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('已出车') + '"'; 
                        return '已出车';
                    case 1:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('待维修') + '"'; 
                        return '待维修';
                    case 2:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('维修') + '"'; 
                        return '维修';
                    case 3:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('保养') + '"'; 
                        return '保养';
                    case 4:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('年检') + '"'; 
                        return '年检';
                    case 5:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('备勤') + '"'; 
                        return '备勤';
                    case 6:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('机动') + '"'; 
                        return '机动';
                    case 7:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('专用') + '"'; 
                        return '专用';
                    case 8:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('不调度') + '"'; 
                        return '不调度';
                    case 9:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('计划锁定') + '"'; 
                        return '计划锁定';
                    case 10:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('封存') + '"'; 
                        return '封存';
                    case 11:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('报废') + '"'; 
                        return '报废';
                }
            }
          },
            {
                header: '登记编号',
                dataIndex: 'registrationNumber',
                renderer:function (value, metaData){  
                      metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                      return value;  
                },
            },
            {
                header: '编制文号',
                dataIndex: 'authorizedNumber',
                renderer:function (value, metaData){  
                      metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                      return value;  
                },
            },
            {
                header: '变动原因',
                dataIndex: 'reasonOfChanging',
                renderer:function (value, metaData){  
                      metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                      return value;  
                },
            },{
			xtype : 'actioncolumn',
			cls : 'content-column',
			width:180,
			header : '操作',
//            align: 'left',
            align: 'center',
			items: [{
		   	   getClass: function(v, meta, record) {
//                    var marginValue = this.width/2-20;                        
//                    this.setMargin('0 -'+marginValue+' 0 '+marginValue);

	               var userType = window.sessionStorage.getItem('userType');
            	   var organizationId = window.sessionStorage.getItem('organizationId');
            	   var fromId = record.get('vehicleFromId');
		           var isInt = record.get('isInternalUse');
	               if(userType=='3'){
	               		this.items[1].hidden = false;
	               		this.items[2].hidden = true;
	               		this.items[3].hidden = true;
	               		this.items[4].hidden = true;
	               		this.items[5].hidden = true;
	               		this.items[6].hidden = true;
                        this.items[7].hidden = true;
	               }
	               if(userType=='6'){
	               		if (organizationId != fromId) {
	               			this.items[1].hidden = false;
	            	   		this.items[2].hidden = true;
	            	   		this.items[3].hidden = false; //来自其他租车公司
	            	   		this.items[4].hidden = true;
	            	   		this.items[5].hidden = false;
	            	   		this.items[6].hidden = false;
                            this.items[7].hidden = false;
						}else if(organizationId == fromId){
							if(isInt){
								//internal use
								this.items[1].hidden = false;
		            	   		this.items[2].hidden = false;
		            	   		this.items[3].hidden = false;
		            	   		this.items[4].hidden = true;
		            	   		this.items[5].hidden = false;
		            	   		this.items[6].hidden = false;
                                this.items[7].hidden = false;
							}else{
								//external use
								this.items[1].hidden = false;
		            	   		this.items[2].hidden = false;
		            	   		this.items[3].hidden = true;
		            	   		this.items[4].hidden = false;
		            	   		this.items[5].hidden = true;
		            	   		this.items[6].hidden = false;
                                this.items[7].hidden = false;
							}
						}
	               	
	               }
	               if(userType=='2'){
	               		if (organizationId != fromId) {
	               			this.items[1].hidden = false;
	            	   		this.items[2].hidden = true;
	            	   		this.items[3].hidden = true;
	            	   		this.items[4].hidden = true;
	            	   		this.items[5].hidden = false;
	            	   		this.items[6].hidden = false;
                            this.items[7].hidden = false;
						}else if(organizationId == fromId){
							if(isInt){
								//internal use
								this.items[1].hidden = false;
		            	   		this.items[2].hidden = false;
		            	   		this.items[3].hidden = true;
		            	   		this.items[4].hidden = true;
		            	   		this.items[5].hidden = false;
		            	   		this.items[6].hidden = false;
                                this.items[7].hidden = false;
							}else{
								//external use
								this.items[1].hidden = false;
		            	   		this.items[2].hidden = false;
		            	   		this.items[3].hidden = true;
		            	   		this.items[4].hidden = true;
		            	   		this.items[5].hidden = false;
		            	   		this.items[6].hidden = false;
                                this.items[7].hidden = false;
							}
						}
	               }
	               
	               //fjga 分配企业 和 回收 都去掉
	               this.items[3].hidden = true;
       	   		   this.items[4].hidden = true;
				},
				hidden:true
			},'@view', '@modify','@arrange','@widthdraw','@arrangedept','@arrangedriver','@arrangegeofence']
    	  }]
      },
	dockedItems : [{
			id:'vehiclePage',
			pageSize:10,
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            displayMsg: '第 {0} - {1} 条记录，共 {2} 条记录',
            emptyMsg : '无数据！',
            beforePageText: "第",
            afterPageText: "页，共{0}页",
            nextText: "下一页",
            prevText: "上一页",
            refreshText: "刷新",
            firstText: "第一页",
            lastText: "最后一页",
            bind: {
                store: '{VehicleResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});