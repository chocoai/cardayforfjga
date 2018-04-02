Ext.define('Admin.view.orgmgmt.enterinfomgmt.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
	],
	id:'enterId',
	cls : 'user-grid',  //图片用户样式
	title : '企业列表',
	bind : {
		store : '{usersResults}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : false,
	stateful: true,
//	multiSelect: true,
   	columnLines: true,  //表格线
/*  defaults: {  //对items的组件生效
        frame: true
    },
    hideHeaders:true,
	header:false,*/
	columns : [
			{
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
			}, {
				xtype : 'datecolumn',
				dataIndex : 'serviceTime',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '服务期限',
				//format:'Y-m-d',
				width:200,
				renderer:function(v, cellValues, rec){
					//Ext.util.Format.date(v7, "Y-m-d H:i:s")
					return Ext.util.Format.date(rec.get('startTime'), "Y/m/d")+'—'+ Ext.util.Format.date(rec.get('endTime'), "Y/m/d");
				}
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'status',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				renderer: function(val) {
					switch(val){
						case '0':
							return '待审核';
						case '3':
							return '审核未通过';
						case '1':
							return '待服务开通';
						case '4':
							return '服务中';
						case '5':
							return '服务到期';
					}
					/*if(val=="0" || val=="待审核") {
						return "待审核";
					}else if(val=="3" || val=="审核未通过") {
						return "审核未通过";
					}else if(val=="1" || val=="待服务开通"){
						return "待服务开通";
						//return "<span style='color:blue;'>待服务开通</span>";
					}*/
				},
				text : '状态',
				flex : 1
			}, {
				xtype : 'actioncolumn',
				width: 200,
				align: "center",
				cls : 'content-column',
				text : '操作',
				//tooltip : 'edit',
				items : [{
                	   	   getClass: function(v, meta, rec) {
		                       var userType = window.sessionStorage.getItem('userType');
		                       if (userType=='0') {
				            	   	this.items[2].hidden = true;
				            	  	this.items[3].hidden = true;
				            	  	this.items[4].hidden=true;
	        						this.items[5].hidden=true;
	        						this.items[6].hidden=true;
	        						this.items[7].hidden=true;
                				}
                				switch(rec.get('status')){
                					case '0':
                						this.items[4].hidden=true;
                						this.items[5].hidden=true;
                						this.items[6].hidden=true;
                						this.items[7].hidden=true;
                						break;
                					case '1':
                						this.items[4].hidden=false;
                						this.items[5].hidden=true;
                						this.items[6].hidden=true;
                						this.items[7].hidden=true;
                						break;
                					case '3':
                						this.items[4].hidden=true;
                						this.items[5].hidden=true;
                						this.items[6].hidden=true;
                						this.items[7].hidden=false;
                						break;
                					case '4':
                						this.items[4].hidden=true;
                						this.items[5].hidden=true;
                						this.items[6].hidden=false;
                						this.items[7].hidden=true;
                						break;
                					case '5':
                						this.items[4].hidden=true;
                						this.items[5].hidden=false;
                						this.items[6].hidden=true;
                						this.items[7].hidden=true;
                						break;
                				}
                			}
                		},{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'checkEnterInfo'
						},{
							xtype : 'button',
							tooltip : '企业信息编辑',
							iconCls : 'x-fa fa-edit',
							handler : 'editEnterInfo'
						},{
							xtype : 'button',
							tooltip : '删除',
							iconCls : 'x-fa fa-remove',
							handler	: 'delEnterInfo'
						},{
							xtype : 'button',
							tooltip : '开通服务',
							iconCls : 'x-fa fa-check',
							handler : 'openServiceWindow'
						},{
							xtype : 'button',
							tooltip : '续约服务',
							iconCls : 'x-fa fa-hand-o-right',
							handler : 'resumeServiceWindow'
						},{
							xtype : 'button',
							tooltip : '停止服务',
							iconCls : 'x-fa fa-hand-o-down',
							handler : 'stopServiceWindow'
						},{
							xtype : 'button',
							tooltip : '重新提交',
							iconCls : 'x-fa fa-reply',
							handler : 'reSubmit'
						}]
						/*, {
							xtype : 'button',
							tooltip : '车辆分配',
							iconCls : 'x-fa fa-automobile',
							handler:'vehicleEdit'
							
						},{
							xtype : 'button',
							tooltip : '修改联系人',
							iconCls : 'x-fa fa-user',
							handler:'editAdmin'
						}*/
			}],
	dockedItems : [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            displayMsg: 'show {0} - {1} records, {2} records in total',
            emptyMsg : 'No relevant records, please modify the query conditions!',
//            bind: {
//                store: '{usersResults}'
//            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});