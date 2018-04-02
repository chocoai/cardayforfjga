Ext.define('Admin.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.main',
    listen : {
        controller : {
            '#' : {
                unmatchedroute : 'onRouteChange'
            }
        }
    },

    routes: {
        ':node': 'onRouteChange'        //浏览器路径更改,触发onNavigationTreeSelectionChange函数,同时会执行方法onRouteChange,
    },
/**
 * 当初次登陆系统，因为Applicatioin.js入口文件中有 defaultToken : 'dashboard',并且NavigationTree.js中有 routeId: 'dashboard',
 * 首先会进入dashboard页面，先触发onMainViewRender函数，然后执行onRouteChange函数，最后会执行onNavigationTreeSelectionChange,
 * 当点击tree中的节点时，首先onNavigationTreeSelectionChange会触发，然后浏览器路径变更，然后执行onRouteChange函数。
 *
 * @type 
 */
    lastView: null,

    //获取新的跳转页面信息，干掉原来的页面，显示新的页面
    setCurrentView: function(hashTag) {
        var length = Ext.getCmp('navigationTreeList').getStore().data.items.length;
        if(hashTag == 'dashboard'){
            if(length < 2){
                var userId = window.sessionStorage.userId;
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();
                var main = Ext.getCmp('main').getController();
                this.treeItemExpand(navigationTreeListStore);
                if(userId == '1'){
                    navigationTreeListStore.getNodeById('permissionMgmt').expand();
                    var firstChildItemId = navigationTreeListStore.getNodeById('permissionMgmt').firstChild.data.viewType;
                    main.redirectTo(firstChildItemId);
                }else{
                    navigationTreeListStore.getNodeById('organizationMgmt').expand();
                    var firstChildItemId = navigationTreeListStore.getNodeById('organizationMgmt').firstChild.data.viewType;
                    main.redirectTo(firstChildItemId);
               }
           }
        }else{
        hashTag = (hashTag || '').toLowerCase();

        var me = this,
            refs = me.getReferences();


        var navigationList = refs.navigationTreeList,
            store = navigationList.getStore(),
            mainCard = refs.mainCardPanel,
            mainLayout = mainCard.getLayout();

        var node = store.findNode('routeId', hashTag) ||
                   store.findNode('viewType', hashTag),
            view = (node && node.get('viewType')) || 'page404',
            lastView = me.lastView,
            existingItem = mainCard.child('component[routeId=' + hashTag + ']'),
            newView;

        // Kill any previously routed window
        if (lastView && lastView.isWindow) {
            lastView.destroy();
        }

        lastView = mainLayout.getActiveItem();

        if (!existingItem) {
            newView = Ext.create({
                xtype: view,
                routeId: hashTag,  // for existingItem search later
                hideMode: 'offsets'
            });
        }

        if (!newView || !newView.isWindow) {
            // !newView means we have an existing view, but if the newView isWindow
            // we don't add it to the card layout.
            if (existingItem) {
                // We don't have a newView, so activate the existing view.
                if (existingItem !== lastView) {
                    mainLayout.setActiveItem(existingItem);
                }
                newView = existingItem;
            }
            else {
                // newView is set (did not exist already), so add it and make it the
                // activeItem.
                Ext.suspendLayouts();
                mainLayout.setActiveItem(mainCard.add(newView));
                Ext.resumeLayouts(true);
            }
        }

        navigationList.setSelection(node);

        if (newView.isFocusable(true)) {
            newView.focus();
        }
        me.lastView = newView;
    }
    },

    onNavigationTreeSelectionChange: function (tree, node) {
//    	alert('onNavigationTreeSelectionChange');
        var to = node && (node.get('routeId') || node.get('viewType'));

        if (to) {
            this.redirectTo(to);
        };
            	//Ext.Msg.alert('onTreeItemClick', info.node.data);
    },
//调整左边栏的大小
    onToggleNavigationSize: function () {
        var me = this,
            refs = me.getReferences(),
            navigationList = refs.navigationTreeList,
            wrapContainer = refs.mainContainerWrap,
            collapsing = !navigationList.getMicro(),
            new_width = collapsing ? 64 : 250;

        if (Ext.isIE9m || !Ext.os.is.Desktop) {
            Ext.suspendLayouts();

            refs.senchaLogo.setWidth(new_width);

            navigationList.setWidth(new_width);
            navigationList.setMicro(collapsing);

            Ext.resumeLayouts(); // do not flush the layout here...

            // No animation for IE9 or lower...
            wrapContainer.layout.animatePolicy = wrapContainer.layout.animate = null;
            wrapContainer.updateLayout();  // ... since this will flush them
        }
        else {
            if (!collapsing) {
                // If we are leaving micro mode (expanding), we do that first so that the
                // text of the items in the navlist will be revealed by the animation.
                navigationList.setMicro(false);
            }

            // Start this layout first since it does not require a layout
            refs.senchaLogo.animate({dynamic: true, to: {width: new_width}});

            // Directly adjust the width config and then run the main wrap container layout
            // as the root layout (it and its chidren). This will cause the adjusted size to
            // be flushed to the element and animate to that new size.
            navigationList.width = new_width;
            wrapContainer.updateLayout({isRoot: true});
            navigationList.el.addCls('nav-tree-animating');

            // We need to switch to micro mode on the navlist *after* the animation (this
            // allows the "sweep" to leave the item text in place until it is no longer
            // visible.
            if (collapsing) {
                navigationList.on({
                    afterlayoutanimation: function () {
                        navigationList.setMicro(true);
                        navigationList.el.removeCls('nav-tree-animating');
                    },
                    single: true
                });
            }
        }
    },

    onMainViewRender:function() {
//    	alert('onMainViewRender------------'+window.location.hash);//首先执行
    	Admin.ux.Util.onPrepareMenu();
    },

    onRouteChange:function(id){
        console.log(id);
        this.setCurrentView(id);
    },

    
    treeItemExpand: function(treeStore){
        for(var i in treeStore.data.items){
            treeStore.data.items[i].collapse();
        }
    },
/*顶部导航栏点击影响左侧导航栏收缩和伸展*/
    navigationMainClick: function(button, e){
        var buttonId = button.id;
        var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();
        var main = Ext.getCmp('main').getController();
            switch(buttonId){
                case "permissionMgmtButton":
                    $("#permissionMgmtButton").css({"border-color":"#86C2E4","background-color":"#86C2E4"});
                    $(".app-header-toolbar-button-permissionMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_ordermgmt_pre.png) no-repeat center");                           
                    $("#permissionMgmt-btnInnerEl").css("color","#fff");
                    this.treeItemExpand(navigationTreeListStore);
                    navigationTreeListStore.getNodeById('permissionMgmt').expand();
                    var firstChildItemId = navigationTreeListStore.getNodeById('permissionMgmt').firstChild.data.viewType;
                    main.redirectTo(firstChildItemId);
                break;
                case "organizationMgmtButton":
                    $("#organizationMgmtButton").css({"border-color":"#86C2E4","background-color":"#86C2E4"});
                    $(".app-header-toolbar-button-organizationMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_orgmgmt_pre.png) no-repeat center");                           
                    $("#organizationMgmtButton-btnInnerEl").css("color","#fff");
                    this.treeItemExpand(navigationTreeListStore);
                    navigationTreeListStore.getNodeById('organizationMgmt').expand();
                    var firstChildItemId = navigationTreeListStore.getNodeById('organizationMgmt').firstChild.data.viewType;
                    main.redirectTo(firstChildItemId);
                break;
            }
    },

    onTreeItemClick : function(sender, info, eOpts) {
        var itemNode = info.node;
        var treeStore = info.tree.getStore();
/*左侧导航栏排他性收缩和伸展*/
        if(itemNode.hasChildNodes()){

            var node = treeStore.getNodeById(itemNode.id);
            var childNodes = node.parentNode.childNodes;
            for(var i = 0; i < childNodes.length; i++){
                if(childNodes[i].data.id != node.data.id){
                    childNodes[i].collapse();
                }
            }
        }
    	var viewType = info.node.data.viewType;    	
		//判断viewType,刷新相应的页面
        var main = Ext.getCmp('main').getController();
    	switch(viewType){
    		case "Enterinfoxx":
    			var grid = Ext.getCmp("enterId");
    			if (grid){
                      Ext.getCmp('enterinfosearchForm').reset();
                      Ext.getCmp('enterIdPage').store.currentPage = 1;
                      Ext.getCmp('enterIdPage').pageSize = 10;
                      grid.getStore("usersResults").load();
    			};
	    		break;
            case "RentalCompanyMgmt":
                var grid = Ext.getCmp("rentalCompanyId");
                if (grid){
                  Ext.getCmp('rentalsearchForm').reset();
                  Ext.getCmp('rentalCompanyIdPage').store.currentPage = 1;
                  Ext.getCmp('rentalCompanyIdPage').pageSize = 10;
                  grid.getStore("usersResults").load();
                };
                break;
            case "dialRecordMgmt":
            var grid = Ext.getCmp("dialRecordId");
            if (grid){
              Ext.getCmp('dialrecordsearchForm').reset();
              Ext.getCmp('dialRecordPage').store.currentPage = 1;
              Ext.getCmp('dialRecordPage').pageSize = 10;
              grid.getStore("dialrecordResults").load();
            };
            break;
            case "syncUsermgmt":
                var grid = Ext.getCmp("syncUserId");
                if (grid){
                	Ext.getCmp('syncUserPage').store.currentPage = 1;
                    Ext.getCmp('syncUserPage').pageSize = 10;
                  grid.getStore("syncUserResult").load();
                };
                break;
    		case "EnterinfoAudit":
				var grid = Ext.getCmp("auditId");
                if (grid){
                      Ext.getCmp('enterAuditsearchForm').reset();
                      Ext.getCmp('auditIdPage').store.currentPage = 1;
                      Ext.getCmp('auditIdPage').pageSize = 10;
                      grid.getStore("auditResults").load();
                };
                break;
            case "RentalCompanyAudit":
                var grid = Ext.getCmp("auditRentalId");
                if (grid){
                      Ext.getCmp('rentalAuditsearchForm').reset();
                      Ext.getCmp('auditRentalIdPage').store.currentPage = 1;
                      Ext.getCmp('auditRentalIdPage').pageSize = 10;
                      grid.getStore("auditResults").load();
                };
                break;
            case "holidayMgmt":
                var grid = Ext.getCmp("holidayId");
                if (grid){
                      Ext.getCmp('holidayPageId').store.currentPage = 1;
                      Ext.getCmp('holidayPageId').pageSize = 10;
                      grid.getStore("holidayResults").load();
                };
                break;
    		case 'usermgmt':
                var grid = Ext.getCmp("griduserid");
                if (grid){
                      Ext.getCmp('usermgmtsearchForm').reset();
                      Ext.getCmp('userpageid').store.currentPage = 1;
                      Ext.getCmp('userpageid').pageSize = 20;
                      grid.getStore("usersResults").load();
                };
    	    	break;
    		case 'rolemgmt':
                var grid = Ext.getCmp("gridroleid");
                if (grid){
                    Ext.getCmp('rolemgmtsearchForm').reset();
                    Ext.getCmp('gridroleid').store.currentPage = 1;
                    Ext.getCmp('gridroleid').pageSize = 20;
                    Ext.getCmp('gridroleid').AllSelectedRoleRecords.length = 0;
                    Ext.getCmp('gridroleid').getSelectionModel().clearSelections();
                    grid.getStore("rolesResults").load();
                };
                break;
            case 'devicemgmt':
                var grid = Ext.getCmp("griddeviceid");
                if (grid){
                      Ext.getCmp('devicemgmtsearchForm').reset();
                      Ext.getCmp('griddeviceid').store.currentPage = 1;
                      //Ext.getCmp('griddeviceid').store.pageSize = 20;
                      grid.getStore("devicesResults").load();
                };
                break;
    	}
	},
	
	viewInfo : function() {
		//alert('个人信息查看');
		var rec = new Ext.data.Model();
		
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
				
				if (respText.success == true) {
					var win = Ext.widget("ViewUserBaseInfo");
					if (data.organizationName==null || data.organizationName=='') {
						data.organizationName = '--';
					}
					rec.data = data;
					win.down("form").loadRecord(rec);
					win.show();
				}
	        }
	        /*,       
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//个人信息修改
	changeUserInfo : function(btn) {
		var userInfo = this.getView().down('form').getForm().getValues();
/*		//手机号码验证
		var regx1 = /[1][3578]\d{9}$/;
		var res1 = regx1.test(userInfo.phone);
		if (!res1) {
			Ext.Msg.alert('提示信息', '你输入的手机号码有误，请重新输入！');
			return;
		}
		
		//邮箱验证
		var regx2 = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		var res2 = regx2.test(userInfo.email);
		if (!res2) {
			Ext.Msg.alert('提示信息', '你输入的邮箱地址有误，请重新输入！');
			return;
		}*/
		
		var input = {
				'realname' : userInfo.realname,
				'phone': userInfo.phone,
				'email': userInfo.email,
		}
		
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
			url: 'user/changeUserInfo',
	        method : 'POST',
//	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        params:{ json:json_input},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
				
				if (respText.status == 'success') {
                    btn.up('ViewUserBaseInfo').close();
                    Ext.Msg.alert('提示信息','修改成功');
                    var usernameText = Ext.ComponentQuery.query('#mainView')[0].down('toolbar').query('#usernameText')[0];
                    usernameText.setText(userInfo.realname);
				} else if (respText.status == 'failure') {
					Ext.Msg.alert('提示信息',respText.msg);
				}
	        },       
	        /*failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	        scope:this
	    });
	},
	
	modifyPassword : function() {
		//alert('修改密码');
		win = Ext.widget('modifypassword');
		win.show();
	},
	
	modifypasswordDone : function(btn) {
		//alert('完成密码修改');
		var userInfo = this.getView().down('form').getForm().getValues();
		var oldpassword = userInfo.oldpassword;
		var newpassword = userInfo.newpassword;
		var confirmpassword = userInfo.confirmpassword;
		
		if (newpassword != confirmpassword) {
			Ext.Msg.alert('提示信息', '你两次输入的新密码不一致，请重新输入！');
			return;
		}
		
		var input = {
				'oldPassword' : userInfo.oldpassword,
				'newPassword': userInfo.newpassword,
		}
		
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
			url: 'user/changePassword',
	        method : 'POST',
//	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        params:{ json:json_input},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
				if (respText.status == 'success') {
					Ext.Msg.alert('提示信息', '密码修改成功！');
					btn.up('modifypassword').close();
				} else if (respText.status == 'failure') {
					Ext.Msg.alert('提示信息', respText.msg);
				}
	        }
	        /*,       
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
		
	}
});
