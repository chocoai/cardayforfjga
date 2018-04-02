Ext.define('Admin.view.main.Main', {
    extend: 'Ext.container.Viewport',

    requires: [
        'Ext.button.Segmented',
        'Ext.list.Tree'
    ],
    id:'main',
    controller: 'main',
    viewModel: 'main',

    cls: 'sencha-dash-viewport',
    itemId: 'mainView',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    minWidth: 1600,
    scrollable: 'x',

    listeners: {
    	beforerender: 'onMainViewRender'
    },

    items: [
        {
            xtype: 'toolbar',
            cls: 'sencha-dash-dash-headerbar shadow',
            style:'background-color:#0D85CA',
            height: 100,
            itemId: 'headerBar',
            items: [
                {
                	id: 'top_org_image_id',
                    xtype: 'image',
                    width: 210,
                    height: 60,
                    style: 'margin:0 20px',
                    src: 'resources/images/icon_home_logo.png'
                },
                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-organizationMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '业务管理',
                    id: 'organizationMgmtButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },
                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-permissionMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '系统管理',
                    id: 'permissionMgmtButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },
                '->',
                {
                  xtype: 'tbtext',
                  ui: 'header',
                  itemId: 'rolenameText',
                  text: '',
                  cls: 'top-role-name'
                },
	            {
	              xtype: 'tbtext',
	              ui: 'header',
	              itemId: 'usernameText',
	              text: '',
	              cls: 'top-user-name'
	          	},
	          	{
	          		style : "background-color:#0D85CA",
		          	xtype: 'button',
		          	id:'mainUserMenuButton',
		          	border: false,
		          	width: 30,
		          	menu: {
			        	id:'mainUserMenu',
			        	items:[
				               {
					            	  text:'个人信息',
					            	  handler : 'viewInfo',
					               },
					               {
					            	  text:'修改密码',
					            	  handler : 'modifyPassword',
						           }
					        ], 
					     style:{
					    	// marginLeft:'-100px !important',
					     },
					     listeners:{
					    	 show: function(menu){
					    		 var arrowX = Ext.getCmp("mainUserMenuButton").getX() ;
					    		 menu.setX(arrowX - 105);
					    	 }
					     }
			        },       
			        onFocus: function() {
			        	this.blur();
			        }
		         },
/*                {
                    xtype: 'image',
                    cls: 'header-right-profile-image',
                    height: 35,
                    width: 35,
                    alt:'current user image',
                    src: 'resources/images/user-profile/2.png'
                },*/
                {
                    xtype: 'tbtext',
                    ui: 'header',
                    width:24,
                    html: '<a href="./logout"><img src="resources/images/icons/icon_quit.png" alt="logout"></a>',
                    cls: 'top-logout'
                }
            ]
        },
        {
         xclass: 'Admin.view.main.MainContainerWrap',
         flex:1,
        },

    ]
});
