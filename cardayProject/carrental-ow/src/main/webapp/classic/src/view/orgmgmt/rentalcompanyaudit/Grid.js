Ext.define('Admin.view.orgmgmt.rentalcompanyaudit.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date'
	],
	id:'auditRentalId',
	reference: 'gridAuditRentalInfo',
	//cls : 'user-grid',  //图片用户样式
	title : '租车公司审核列表',
	bind : {
		store : '{auditResults}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    forceFit: false,
	scrollable : false,
	stateful: true,
   	columnLines: true, 
	columns : [{
				width : 120,
				dataIndex : 'name',
				header : '企业名称',
				align: 'center',
				menuDisabled: true,
				sortable: true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				flex : 1,
				dataIndex : 'address',
				header : '企业地址',
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
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
				header : '联系人电话',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},{
				header : '服务期限',
				width:200,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function(v, cellValues, rec){
					return Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'一'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d");
				}
			},{
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
			},{
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
			},{
				xtype : 'actioncolumn',
				sortable: false,
				menuDisabled: true,
				align: 'center',
				//align: 'left',
				items : [{
                	   	   getClass: function(v, meta, record) {
/*                	   	   		var marginValue = this.width/2-20;                        
                        		this.setMargin('0 -'+marginValue+' 0 '+marginValue);*/
		                       var status = record.get('status');
		                       if (status =='0') {
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = true;
				            	   this.items[3].hidden = false;
				            	   this.items[4].hidden = true;
				            	   this.items[5].hidden = true;
				            	   this.items[6].hidden = true;
				            	   this.items[7].hidden = true;
				            	   this.items[8].hidden = false;
                				}else if(status =='1'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = true;
				            	   this.items[3].hidden = true;
				            	   this.items[4].hidden = true;
				            	   this.items[5].hidden = true;
				            	   this.items[6].hidden = true;
				            	   this.items[7].hidden = true;
				            	   this.items[8].hidden = true;
                				}else if(status =='2'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = false;
				            	   this.items[3].hidden = true;
				            	   this.items[4].hidden = true;
				            	   this.items[5].hidden = true;
				            	   this.items[6].hidden = true;
				            	   this.items[7].hidden = true;
				            	   this.items[8].hidden = true;
                				}else if(status =='3'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = true;
				            	   this.items[3].hidden = true;
				            	   this.items[4].hidden = false;
				            	   this.items[5].hidden = false;
				            	   this.items[6].hidden = true;
				            	   this.items[7].hidden = false;
				            	   this.items[8].hidden = true;
                				}else if(status =='4'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = true;
				            	   this.items[3].hidden = true;
				            	   this.items[4].hidden = true;
				            	   this.items[5].hidden = true;
				            	   this.items[6].hidden = true;
				            	   this.items[7].hidden = false;
				            	   this.items[8].hidden = true;
                				}else if(status =='5'){
				            	   this.items[1].hidden = false;
				            	   this.items[2].hidden = true;
				            	   this.items[3].hidden = true;
				            	   this.items[4].hidden = true;
				            	   this.items[5].hidden = false;
				            	   this.items[6].hidden = false;
				            	   this.items[7].hidden = false;
				            	   this.items[8].hidden = true;
                				}
                			}
                		},{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler :'checkRentalAuditInfo'
						},
						{
							xtype : 'button',
							tooltip : '开通',
							iconCls : 'x-fa fa-folder-open',
							handler :'onOpenClick'
						},
						{
							xtype : 'button',
							tooltip : '通过',
							iconCls : 'x-fa fa-thumbs-up',
							handler :'onApproveClick'
						},
						{
							xtype : 'button',
							tooltip : '暂停',
							iconCls : 'x-fa fa-pause',
							handler :'onPauseClick'
						},
						{
							xtype : 'button',
							tooltip : '停止',
							iconCls : 'x-fa fa-stop',
							handler :'onStopClick'
						},
						{
							xtype : 'button',
							tooltip : '继续',
							iconCls : 'x-fa fa-play',
							handler :'onContinueClick'
						},
						{
							xtype : 'button',
							tooltip : '续约',
							iconCls : 'x-fa fa-plus',
							handler :'onExtendClick'
						},
						{
							xtype : 'button',
							tooltip : '驳回',
							iconCls : 'x-fa fa-remove',
							handler :'onRefuseClick'
						}],

				cls : 'content-column',
				width : 180,
				header : '操作'
			}],
	dockedItems : [{
            id:'auditRentalIdPage',
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
                store: '{auditResults}'
            },
            displayInfo: true
        }],

	initComponent : function() {
		this.callParent();
	}
});