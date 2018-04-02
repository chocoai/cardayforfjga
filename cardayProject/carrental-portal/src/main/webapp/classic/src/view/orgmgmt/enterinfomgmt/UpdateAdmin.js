Ext.define('Admin.view.orgmgmt.enterinfomgmt.UpdateAdmin', {
			extend : 'Ext.window.Window',
			alias : "widget.updateAdmin",
			reference : 'updateAdmin',
			title : '修改企业管理员信息',
			width : 330,
			resizable : false,// 窗口大小是否可以改变
			draggable : true,// 窗口是否可以拖动
			modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
			frame : true,

			controller : {
				xclass : 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
			},
			items : [{
						xtype : 'form',
						layout : 'form',
						bodyStyle : "background-color:#FFF0F5",
						fieldDefaults : {
							labelAlign : 'right'
						},
						items : [{
									xtype : 'hidden',
									name : 'id'
								}, {
									fieldLabel : '姓名',
									xtype : 'textfield',
									name : 'linkman'
								}, {
									xtype : 'fieldcontainer',
									fieldLabel : '性别',
									defaultType : 'radiofield',
									defaults : {
										flex : 1
									},
									layout : 'hbox',
									items : [{
										boxLabel : '男',
										name : 'gender',
										inputValue : 'male'
											// checked:true
										}, {
										boxLabel : '女',
										name : 'gender',
										inputValue : 'female'
									}]
								}, {
									fieldLabel : '手机号码',
									xtype : 'linkmanPhone',
									name : 'linkMan'
								}, {
									fieldLabel : '管理员密码',
									xtype : 'textfield',
									inputType : 'password',
									name : 'adminPwd'
								}, {
									fieldLabel : '邮箱',
									xtype : 'textfield',
									name : 'linkEmail'
								}]
					}],
			buttonAlign : 'center',
			buttons : [{
						text : '修改',
						handler : function(btn) {
							Ext.Msg.alert("提示信息", '修改成功');
							btn.up("window").close();

							// alert(btn.up("window").down("form").getForm().getValues()['orgId']);
						}
					}, {
						text : '关闭',
						handler : function(btn) {
							btn.up("updateAdmin").close();
						}
					}]
		});