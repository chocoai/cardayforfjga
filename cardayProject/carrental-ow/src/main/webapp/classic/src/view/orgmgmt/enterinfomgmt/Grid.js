Ext.define('Admin.view.orgmgmt.enterinfomgmt.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
	],
	id:'enterId',
	reference: 'gridEnterInfo',
	//cls : 'user-grid',  //图片用户样式
	title : '用车企业列表',
	forceFit: false,
	bind : {
		store : '{usersResults}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : false,
	stateful: true,
   	columnLines: true, 
	columns : [
			{
				width : 120,
				dataIndex : 'id',
				header : 'ID',
				hidden:true,
				align: 'center',
				menuDisabled: true,
				sortable: true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				width : 120,
				dataIndex : 'name',
				header : '企业名称',
				menuDisabled: true,
				sortable: true,
				align: 'center',
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				flex : 1,
				dataIndex : 'address',
				header : '企业地址',
				menuDisabled: true,
				align: 'center',
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'linkman',
				header : '联系人',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				dataIndex : 'linkmanPhone',
				header : '联系电话',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				dataIndex : 'serviceTime',
				header : '服务期限',
				//format:'Y-m-d',
				width:200,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function(v, cellValues, rec){
					//Ext.util.Format.date(v7, "Y-m-d H:i:s")
					return Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'—'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d");
				}
			}, 
			{
				dataIndex : 'businessType',
				header : '业务类型',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, 
			{
				dataIndex : 'availableCredit',
				header : '可分配额度',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
					if(value == null){						
	                	metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('0') + '"'; 
						return '0'
					}else{
		                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
		                return value; 						
					} 
	            }
			},
			{
				dataIndex : 'status',
				header : '状态',
				align: 'center',
				menuDisabled: true,
				sortable: false,
				width: 100,
				renderer: function(val) {
					if(val=="0" || val=="待审核") {
						return "<span style='color:green;'>待审核</span>";
					}else if(val=="1" || val=="审核未通过") {
						return "<span style='color:red;'>审核未通过</span>";
					}else if(val=="2" || val=="待服务开通"){
						return "<span style='color:blue;'>待服务开通</span>";
					}else if(val=="3" || val=="服务中"){
						return "<span style='color:dimgray;'>服务中</span>";
					}else if(val=="4" || val=="服务到期"){
						return "<span style='color:brown;'>服务到期</span>";
					}else if(val=="5" || val=="服务暂停"){
						return "<span style='color:darkgoldenrod;'>服务暂停</span>";
					}
				},
				//flex : 1	
			}, {
				xtype: 'actioncolumn',
				width: 180,
				align: "center",
				cls : 'content-column',
				header : '操作',
				//align: 'left',
				menuDisabled: true,
				//tooltip : 'edit',
				items : [{
                	   	   getClass: function(v, meta, record) {
/*                	   	   		var marginValue = this.width/2-20;                        
                        		this.setMargin('0 -'+marginValue+' 0 '+marginValue);*/

                	   	   	   var businessType = record.get('businessType');
                	   	   	   var status = record.get('status');
                	   	   	   if(businessType.indexOf('自有车') >= 0 && businessType.length == 3){               	   	   	   	
				            	   this.items[5].hidden = true;
                	   	   	    if (status =='0' || status =='1') {
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = true;
                				}else if(status =='2' || status =='3' || status =='5'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = false;
                				}else if(status =='4'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = false;
                				}
                	   	   	   }else{
                	   	   	    if (status =='0' || status =='1') {
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = true;
                				}else if(status =='2' || status =='3' || status =='5'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = false;
				            	   this.items[5].hidden = false;
                				}else if(status =='4'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = false;
				            	   this.items[5].hidden = false;
                				}
                	   	   	   }
                			}
                		},{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'checkEnterInfo'
						},{
							xtype : 'button',
							tooltip : '审核信息',
							iconCls : 'x-fa fa-check',
							handler : 'showAuditInfo'
						},{
							xtype : 'button',
							tooltip : '编辑',
							iconCls : 'x-fa fa-edit',
							handler : 'editEnterInfo'
						},{
							xtype : 'button',
							tooltip : '资源',
							iconCls : 'x-fa fa-asterisk',
							handler : 'resourcesInfo'
						},{
							xtype : 'button',
							tooltip : '租赁',
							iconCls : 'x-fa fa-stop',
							handler : 'rentalInfo'
						},{
							xtype : 'button',
							tooltip : '用车额度',
							iconCls : 'x-fa fa-money',
							handler : 'creditLimitInfo'
						},]
			}],

	dockedItems : [{
            id:'enterIdPage',
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
                store: '{usersResults}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});