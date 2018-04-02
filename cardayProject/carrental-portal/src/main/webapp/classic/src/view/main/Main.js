Ext.define('Admin.view.main.Main', {
    extend: 'Ext.container.Viewport',

    requires: [
        'Ext.button.Segmented',
        'Ext.list.Tree'
    ],
    id:'main',
    controller: 'main',
    viewModel: 'main',

    vehicelList: null,

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
                    style: 'margin:0 20px',
                    src: 'resources/images/icon_home_logo_fjga.png',
                    width: 200,
                    height: 50,
                },
/*                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    text: '首页',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-navigationMain',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    id: 'mainPageButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },*/
                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-vehicleMonitoringMain',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '车辆监控',
                    id: 'vehicleMonitoringMainButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },
/*                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-vehicleMonitoring',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '车辆监控',
                    id: 'vehicleMonitoringButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },*/
                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-orderMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '派车管理',
                    id: 'orderMgmtButton',
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
                    iconCls: 'app-header-toolbar-button-alertMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '报警管理',
                    id: 'alertMgmtButton',
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
                    iconCls: 'app-header-toolbar-button-vehicleMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '车辆管理',
                    id: 'vehicleMgmtButton',
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
                    iconCls: 'app-header-toolbar-button-stationMaintainMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '站点管理',
                    id: 'stationMaintainMgmtButton',
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
                    iconCls: 'app-header-toolbar-button-reportMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '统计报表',
                    id: 'reportMgmtButton',
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
                    text: '组织机构',
                    id: 'permissionMgmtButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },
                /*{
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-organizationMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '机构管理',
                    id: 'organizationMgmtButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                }, */
                {
                    xtype: 'button',
                    iconAlign: 'top',
                    scale: 'large',
                    cls: 'app-header-toolbar-button',
                    iconCls: 'app-header-toolbar-button-ruleMgmt',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '规则管理',
                    id: 'ruleMgmtButton',
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
                    iconCls: 'app-header-toolbar-button-vehiclesPurchase',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '购置管理',
                    id: 'vehiclesPurchaseButton',
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
                    iconCls: 'app-header-toolbar-button-carNumber',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '号牌管理',
                    id: 'carNumberButton',
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
                    iconCls: 'app-header-toolbar-button-vehicleMix',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '三定一统',
                    id: 'vehicleMixButton',
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
                    iconCls: 'app-header-toolbar-button-systemConfiguration',
                    style:'margin-left: 0px;border-color: #0D85CA;background-color:#0D85CA;border-radius: 8px;',
                    text: '流程设置',
                    id: 'systemConfigurationButton',
                    handler: 'navigationMainClick',
                    width: 70,
                    height: 70,
                    hidden:true,
                },
                '->',

                {
                    xtype: 'tbtext',
                    id: 'vehMaintainInfo',
                    width: 35,
                    html: "<span data-ischeck=0 onmouseover='showVehMaintainInfoOver(this);' ><div class='alarmRemind'>0</div><img id='mainPopImg' src='resources/images/icons/icon_todo_pre.png' alt='vehMaintainInfo'><div class='pop' id='pop' onmouseout='showVehMaintainInfoOut()'><div id='triangle' class='triangle-top'></div><div id='square' class='square-content' style='z-index:100' ><table ><tr><th style='cursor:pointer' onclick='countOnClick(this)'>保养 0</th><th style='cursor:pointer' onclick='countOnClick(this)'>车险 0</th><th style='cursor:pointer' onclick='countOnClick(this)'>年检 0</th></tr></table></div></div></span>",
                 },                
/*                {
                    xtype: 'button',
                    scale: 'medium',
                    iconCls: 'button-vehMaintainInfo',
                    cls: 'app-vehMaintainInfo-button',
                    id: 'vehMaintainInfo',
                },*/
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
                    id: 'logoutUrl',
                    ui: 'header',
                    width:24,
                    html: '<a href="./logout"><img src="resources/images/icons/icon_quit.png" alt="logout"></a>',
                    cls: 'top-logout'
                }
            ]
        },
/*        {            
         xclass: 'Admin.view.main.MainPageContainer',
         flex: 1,
         hidden:true,
        },*/
        {            
         xclass: 'Admin.view.main.VehicleMonitoringContainer',
         flex: 1,
         //hidden: true
        },
        {
         xclass: 'Admin.view.main.MainContainerWrap',
         flex:1,
         hidden: true
        },

    ]
});
