Ext.define('Admin.view.orgmgmt.enterinfoaudit.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date'
	],
	id:'auditId',
	cls : 'user-grid',  //图片用户样式
	title : '待审核列表',
	bind : {
		store : '{auditResults}'
	},
	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
	scrollable : false,
	//	multiSelect: true,
   	columnLines: true,  //表格线
//	plugins: 'gridfilters',
//	hideHeaders:true,  隐藏表格的头
//	header:false,  隐藏grid.panel的title
	columns : [{
				xtype : 'gridcolumn',
				width : 120,
				dataIndex : 'name',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '企业名称'

			}, {
				xtype : 'gridcolumn',
				flex : 1,
				dataIndex : 'address',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '企业地址'
			}, {
				xtype : 'gridcolumn',
				flex : 1,
				dataIndex : 'vehileNum',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '计划用车数'
			},{
				xtype : 'gridcolumn',
				flex : 1,
				dataIndex : 'city',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '用车城市'
			},{
				xtype : 'gridcolumn',
				dataIndex : 'linkman',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '联系人',
				flex : 1
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'linkmanPhone',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '联系电话',
				flex : 1
			},{
				xtype : 'datecolumn',
			//	dataIndex : 'endTime',
				text : '服务期限',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				width:200,
				renderer:function(v, cellValues, rec){
					return Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'一'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d");
				}
			},{
				xtype : 'gridcolumn',
				dataIndex : 'status',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '状态',
				/*filter: {
           			type: 'string',
            		value: '0',  // setting a value makes the filter active. 
            		itemDefaults: {
            		}
        		},*/
				renderer: function(val) {
					if(val=="0" || val=="待审核") {
						return "<span style='color:green;'>待审核</span>";
					}else if(val=="3" || val=="审核未通过") {
						return "<span style='color:red;'>审核未通过</span>";
					}else if(val=="1" || val=="审核通过"){
						return "<span style='color:red;'>审核通过</span>";
					}
				},
				flex : 1	
			},{
				xtype : 'actioncolumn',
	            align: 'center',
				items : [{
                	   	   getClass: function(v, meta, record) {
		                       var userType = window.sessionStorage.getItem('userType');
		                       if (userType=='0') {
				            	   this.items[2].hidden = true;
				            	   this.items[3].hidden = true;
                				} 
                			}
                		},{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler :'checkEnterInfo'
						},
						{
							xtype : 'button',
							tooltip : '审核通过',
							iconCls : 'x-fa fa-check',
							handler :'onApproveClick'
						},
						{
							xtype : 'button',
							tooltip : '驳回',
							iconCls : 'x-fa fa-close',
							handler :'onRefuseClick'
						}
						/*{
							 getClass: function(v, meta, record) {  
					              if (record.get('status') == "1") {
					                  return 'x-hidden';
					              } else if(record.get('status') == "2"){
					              	return 'x-hidden';
					              }else{
					             //    this.items[1].tooltip = '通过';
					                 return 'x-fa fa-check';
					              }
				              },
				              handler:function(){
				              	Ext.MessageBox.confirm('Confirm', '确定要审核通过?', function(btn){
				              		alert(btn);
				              	});
				              },
				           
				           getTip:function(v, meta, record){
				           	  if (record.get('status') == "0") {
				                  return '审核通过';
				              } 
				           }
						}, {
					        getClass: function(v, meta, record) {  
				              if (record.get('status') == "0") {
				                  return 'x-hidden';
				              }else if(record.get('status') == "1"){
				              	   return 'x-hidden';
				              }else {
				       //          this.items[2].tooltip = '开通服务';
				                 return 'x-fa fa-chevron-circle-right';
				              }
				           },
				           getTip:function(v, meta, record){
				           	  if (record.get('status') == "2") {
				                  return '开通服务';
				              } 
				           }
						}, {
							getClass: function(v, meta, record) {  
				              if (record.get('status') == "2") {
				                  return 'x-hidden';
				              }else {
				                 return 'x-fa fa-close';
				              }
				           },
				           getTip:function(v, meta, record){
				           	  if (record.get('status') == "0"||record.get('status') == "1") {
				                  return '驳回';
				              } 
				           }
							
						}*/],

				cls : 'content-column',
				width : 160,
				text : '操作'
		//		tooltip : 'edit '
			}],
	dockedItems : [{
		xtype : 'pagingtoolbar',
		dock : 'bottom',
		itemId : 'userPaginationToolbar',
		displayInfo : true
			// bind: '{usersResults}'
		}],
	/*
	 * title: '部门列表', bind: { store: {type: "flowReport"} }, viewConfig: {
	 * loadMask: true, loadingText: '加载中...' }, listeners: { }, stateful: true,
	 * collapsible: true, forceFit: false, mask: true, // collapsed: true,
	 * multiSelect: false, columnLines: true, // 加上表格线 align: 'center',
	 */

	initComponent : function() {
		this.callParent();
	}
});