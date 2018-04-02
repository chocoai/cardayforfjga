Ext.define('Admin.ux.Util', {
    statics: {
        renderDate: function(value, record) {
            var d = new Date(value);
            return d.toLocaleDateString();
        },
        renderBoolean: function(value, record) {
            if (value) {
                return "是";
            } else {
                return "否";
            }

        },
        renderBooleanHas: function(value, record) {
            if (value) {
                return "有";
            } else {
                return "无";
            }

        },
        /**
         * 普遍的get方法，使用jsonp取得resp，填充到一个memorystore中，这个memorystore绑定到view上。Admin.ux.Util.globals.businessServerAddr + ':' + Admin.ux.Util.globals.businessServerPort + '/' + Admin.ux.Util.globals.businessRootPath + '/' + 'services/report/findReports',
         * @param  {[type]} businessParams [description]'report/findReports'
         * @return {[type]}                [description]
         */
        nativeLoad: function(businessParams, serviceUrl, me) {
            Ext.data.JsonP.request({
                // Example
                // url: 'http://dev.sencha.com/ext/5.1.0/examples/restful/app.php/users',
                // url: 'app/store/mockdata/Users.json',
                url: Admin.ux.Util.globals.businessServerAddr + ':' + Admin.ux.Util.globals.businessServerPort + '/' + Admin.ux.Util.globals.businessRootPath + '/' + 'services/' + serviceUrl,
                callbackKey: 'callback',
                timeout: Admin.ux.Util.globals.jsonptimeout,
                scope: me,
                params: businessParams,
                /**
                 * [Resp header code == 200, then go into this method, should not be used in normal condition.]
                 * @param  {[type]} resp [description]
                 * @return {[type]} null [description]
                 */
                success: function(resp) {
                    me.onNativeLoadSuccess(resp)
                },
                /**
                 * Protocol level fail.
                 * @param  {[type]} result [description]
                 * @return {[type]}        [description]
                 */
                failure: function(result) {
                    me.onNativeLoadFail(result)
                }
            });

        },
        /**
         * [promptServiceConnectionFailure description]
         * @param  {[type]} result [error]
         * @param  {[type]} me     [viewcontroller]
         * @return {[type]}        [description]
         */
        promptServiceConnectionFailure: function(result, me) {
            var tabName = me.getView().tabName.split(";")[1];
            Ext.MessageBox.show({
                title: tabName + ": " + result,
                msg: '请求后台服务失败，请稍后再试，或联系网络管理员',
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.Msg.OK
            });
        },
        /**
         * [promptServiceReqFailure description]
         * @param  {[type]} resp [http response, json format]
         * @param  {[type]} this [viewcontroller]
         * @return {[type]}      [description]
         */
        promptServiceReqFailure: function(resp, me) {
            var tabName = this.getView().tabName.split(";")[1];
            Ext.MessageBox.show({
                title: tabName + ": " + '请求失败',
                msg: resp.failureMsg,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.Msg.OK
            });
            me.getView().setLoading(false);
        },

        /**
         * [promptServiceReqFailure description]
         * @param  {[type]} resp [http response, json format]
         * @param  {[type]} this [viewcontroller]
         * @return {[type]}      [description]
         */
        promptLocalSearchValidationFailure: function(me) {
            var tabName = me.getView().tabName.split(";")[1];
            Ext.MessageBox.show({
                title: tabName + ": " + '搜索失败',
                msg: '请填写任意查询条件，以进行搜索。点击重置会清除筛选条件',
                icon: Ext.MessageBox.INFO,
                buttons: Ext.Msg.OK
            });
            me.getView().setLoading(false);
        },

        globals: {
            // authServerAddr: "http://10.25.140.130",
            authServerAddr: "http://localhost",
            authServerPort: "8080",
            authRootPath: '/',
            // businessServerAddr: "http://10.25.140.130",
            businessServerAddr: "http://localhost",
            businessServerPort: "8080",
            businessRootPath: '/',
            deptId: null,
            jsonptimeout: 30000,
            loadingtext: "正在读取数据，请稍后......",
        },

        onPrepareMenu: function() {
            var usernameText = Ext.ComponentQuery.query('#mainView')[0].down('toolbar').query('#usernameText')[0];
            var rolenameText = Ext.ComponentQuery.query('#mainView')[0].down('toolbar').query('#rolenameText')[0];
            //var orgnameText = Ext.ComponentQuery.query('#mainView')[0].down('toolbar').query('#orgnameText')[0];
            Ext.Ajax.request({
                url: './user/loadCurrentUser',
                method: 'POST',
                success: function(response) {
                    var resp = Ext.decode(response.responseText);
                    if (typeof(resp.data.username) != 'undefined') {
                        rolenameText.setText(resp.data.roleName);
                        usernameText.setText(resp.data.realname);  //将登录名记录到控件#usernameText中
                        //orgnameText.setText(resp.data.organizationName);
                        window.sessionStorage.clear();
                        window.sessionStorage.setItem("userType", resp.data.userCategory);
                        window.sessionStorage.setItem("userId", resp.data.id);
                        
                        //显示用户所在的企业名称和logo。
                        var userType = resp.data.userCategory;
/*                    	if (userType==0 || userType==1) {
                    		//Ext.getCmp('top_org_name_id').setHidden(true);
                    		Ext.getCmp('top_org_image_id').setHidden(true);
                    	} else {
                    		//Ext.getCmp('top_org_name_id').setHidden(false);
                    		Ext.getCmp('top_org_image_id').setHidden(false);
                    	}*/
                        window.sessionStorage.setItem("organizationId", resp.data.organizationId);
                        window.sessionStorage.setItem("organizationName", resp.data.organizationName);
                        window.sessionStorage.setItem("userName", resp.data.realname);
                        window.sessionStorage.setItem("roleName", resp.data.roleName);
                    }
                },
                failure: function(response) {
                    if (typeof(window.sessionStorage.getItem('userName')) != 'undefined') {
                        usernameText.setText( window.sessionStorage.getItem('userName'));
                        rolenameText.setText( window.sessionStorage.getItem('roleName'));
                    }
                    var resp = Ext.decode(response.responseText);
                    console.log('loadCurrentUser failed: ' + resp);
                    // Admin.view.util.RespUtil.handleFailResponse(resp);
                }
            });

            //destroy no permission menu according to login user
            Ext.Ajax.request({
                url: './resource/loadUserNoPermissionMenus',
                method: 'POST',
                success: function(response) {
                	var resp = Ext.decode(response.responseText);
                	if (typeof(resp) != 'undefined') {
                		//var items = Ext.getCmp("maincontainerwrap").down('treelist').itemMap;
                        var itemsNodes = Ext.getCmp('navigationTreeList').getStore().data.items;
                        var navItems = Ext.ComponentQuery.query('#mainView')[0].items.items[0].items.items;
                        for (var i in resp){
                        	 var menu = resp[i].name;

                             for(var x in itemsNodes){
                                        if(itemsNodes[x].data.text == menu){
                                            itemsNodes[x].remove();
                                        }else{
                                            if(!itemsNodes[x].isLeaf()){
                                                for(var a in itemsNodes[x].childNodes){
                                                        if(itemsNodes[x].childNodes[a].data.text == menu){
                                                            itemsNodes[x].childNodes[a].remove();
                                                        }
                                                }
                                            }
                                        }
                             }
/*隐藏头部button*/
                        }

                        for (var n in navItems) {
                            var flag = false;
                            for (var m in resp){
                                var menuNav = resp[m].name;
                                if (navItems[n].text == menuNav) {
                                     flag = true;
                                     break;
                                 }
                            }
                            if(!flag){
                               navItems[n].show();
                            }
                        }

                        $("#permissionMgmtButton").css({"border-color":"#86C2E4","background-color":"#86C2E4"});
                        $(".app-header-toolbar-button-permissionMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_ordermgmt_pre.png) no-repeat center");                           
                        $("#permissionMgmt-btnInnerEl").css("color","#fff");

                        $("#organizationMgmtButton").css({"border-color":"#86C2E4","background-color":"#86C2E4"});
                        $(".app-header-toolbar-button-organizationMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_orgmgmt_pre.png) no-repeat center");                           
                        $("#organizationMgmtButton-btnInnerEl").css("color","#fff");

                    }
/*设置参数之后，强制进入dashboard*/
                    var main = Ext.getCmp('main').getController();
                    main.setCurrentView('dashboard');
                },
                failure: function(response) {
                }
            });
            
          //destroy no permission button according to login user
            Ext.Ajax.request({
                url: './resource/loadUserNoPermissionButtons',
                method: 'POST',
                success: function(response) {
                	var resp = Ext.decode(response.responseText);
                	if (typeof(resp) != 'undefined') {
                		var items = Ext.ComponentQuery.query('form container > button');
                        for (var i in resp){
                        	 var button = resp[i].name;
                        	 for (var j in items) {
                                 if (items[j].text == button) {
                                     items[j].destroy();
                                 }
                             }
                        }
                    }
                },
                failure: function(response) {
                }
            });
            
        },
        /**
         * validate remote filed exists like username, etc
         * @param  {[type]} field        [description]
         * @param  {[type]} newValue     [description]
         * @param  {[type]} url          [description]
         * @param  {[type]} paramNameExp [description]
         * @return {[type]}              [description]
         */
        validateFieldRemote: function(field, newValue, ajaxMethod, url, paramNameExp, fieldText) {
            var request = field.validationXhr;

            // Cancel any existing requests
            if (request) {
                Ext.Ajax.abort(request);
            }

            // Send your params for validation
            field.validationXhr = Ext.Ajax.request({
                url: url,
                method: ajaxMethod,
                params: paramNameExp,
                success: function(response) {
                    var numResponse = Ext.decode(response.responseText);
                    if (numResponse === 0) {
                        // markInvalid only displays an error message. It doesn't actually
                        // set the field state to invalid. Extra logic may be needed.
                        // field.markInvalid(fieldText + " " + newValue + " 已存在！");
                        field.setValidation(fieldText + " " + newValue + " 已存在！")
                    } else {
                        field.setValidation(true);
                    }
                }
            });
        }
    },


});
