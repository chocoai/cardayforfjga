Ext.define('Admin.view.main.mainpage.ViewFullScreen', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewFullScreen",

	reference: 'viewFullScreen',
	id:'viewFullScreenId',

	controller: {
        xclass: 'Admin.view.main.mainpage.ViewController'
    },

    listeners:{
        afterrender: 'afterrenderMapWindow'
    },

	//width : 1200,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	header: false,

	layout: {
	            type: 'hbox',
	            pack: 'start',
	            align: 'stretch'
	        },
    margin: '0 0 3 0',
    defaults: {
        flex: 1,
        frame: true,
    },

	items : [
              {
    	        xclass: 'Admin.view.main.mainpage.MainPageMap',
		      }
            ],

});