Ext.define('Admin.view.systemmgmt.rolemgmt.ViewPrivilegeWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewprivilegewindow",
    controller: 'rolemgmtcontroller',
	reference: 'viewprivilegewindow',
	title : '查看权限',
	width : 400,
	height : 500,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: 'fit',
	items:[
   	{
   		id: 'view_privilegetree_id',
   		xtype: 'treepanel',
   		//checkPropagation: 'none',
   		rootVisible: false,
   	    useArrows: true,
   	    frame: true,
   	    width: 280,
   	    height: 300,
   	    bufferedRenderer: false,
   	    checked:false,
   	    //animate: true,
    }   
	],

	buttonAlign : 'center',
	buttons : [{
				text: '关闭',
				handler: function(btn) {
					btn.up('viewprivilegewindow').close();
				}
			} ]
});