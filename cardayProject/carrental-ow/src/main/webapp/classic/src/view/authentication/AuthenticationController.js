Ext.define('Admin.view.authentication.AuthenticationController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.authentication',

    onLoginReturn: function(options, success, response) {
        options = options.original;
        //200ok
        if (success) {
            console.log('Login success');
            console.log(response);
            resp = Ext.decode(response.responseText);
            if (resp.success) {
                Ext.callback(options.success, options.scope, [
                    options.data.username, options.data.password,
                    options.data.isRememberMe
                ]);
                // Ext.ComponentQuery.query('#mainView')[0].getViewModel().setData(resp);
                //                window.sessionStorage.setItem('mainpage', resp.data.mainpage);
                window.sessionStorage.setItem('platform', resp.data.platform);
                window.sessionStorage.setItem('platform_account', resp.data.platform_account);
                window.sessionStorage.setItem('platform_news', resp.data.platform_news);
                window.sessionStorage.setItem('platform_insurance', resp.data.platform_insurance);
                window.sessionStorage.setItem('platform_permission', resp.data.platform_permission);
                window.sessionStorage.setItem('waybill', resp.data.waybill);
                window.sessionStorage.setItem('sendorder', resp.data.sendorder);
                window.sessionStorage.setItem('sendorder_start', resp.data.sendorder_start);
                window.sessionStorage.setItem('sendorder_list', resp.data.sendorder_list);
                window.sessionStorage.setItem('receiveorder', resp.data.receiveorder);
                window.sessionStorage.setItem('receiveorder_assign', resp.data.receiveorder_assign);
                window.sessionStorage.setItem('receiveorder_start', resp.data.receiveorder_start);
                window.sessionStorage.setItem('receiveorder_list', resp.data.receiveorder_list);
                window.sessionStorage.setItem('account', resp.data.account);
                window.sessionStorage.setItem('account_mine', resp.data.account_mine);
                window.sessionStorage.setItem('account_company', resp.data.account_company);
                window.sessionStorage.setItem('vehicle', resp.data.vehicle);
                window.sessionStorage.setItem('vehicle_head', resp.data.vehicle_head);
                window.sessionStorage.setItem('vehicle_trailer', resp.data.vehicle_trailer);
                window.sessionStorage.setItem('driver', resp.data.driver);
                window.sessionStorage.setItem('statistics', resp.data.statistics);
                window.sessionStorage.setItem('statistics_main', resp.data.statistics_main);
                window.sessionStorage.setItem('statistics_charge', resp.data.statistics_charge);
                window.sessionStorage.setItem('statistics_payment', resp.data.statistics_payment);
                window.sessionStorage.setItem('statistics_usertype', resp.data.statistics_usertype);
                window.sessionStorage.setItem('permission_base', resp.data.permission_base);
                window.sessionStorage.setItem('runningability', resp.data.runningability);
                Admin.ux.Util.onPrepareMenu();
            } else {
                Ext.callback(options.failure, options.scope, [resp.status,
                    resp.failureMsg
                ]);
            }
            return;
        }

    },

    onFaceBookLogin : function() {
        this.redirectTo('dashboard', true);
    },

    onLoginButton: function() {
        this.redirectTo('dashboard', true);
    },

    onLoginAsButton: function() {
        this.redirectTo('login', true);
    },

    onNewAccount:  function() {
        this.redirectTo('register', true);
    },

    onSignupClick:  function() {
        this.redirectTo('dashboard', true);
    },

    onResetClick:  function() {
        this.redirectTo('dashboard', true);
    }
});