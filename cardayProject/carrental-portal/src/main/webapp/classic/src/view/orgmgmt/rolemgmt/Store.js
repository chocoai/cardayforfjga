/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.orgmgmt.rolemgmt.Store', {
	extend: 'Ext.data.Store',
	alias: 'store.rolemgmtReport',
    fields: [
        'date', 'sim_no', 'iccid', 'state'
    ],
    data: { items: [
                    { name: "李青", sex: "男", phone_number: '13986211836', Email: "qing.li@cm-dt.com", operation: "324" },
                    { name: "古东旭", sex: "女", phone_number: '18616862881', Email: "dongxu.gu@cm-dt.com", operation: "324" },
                    { name: "丁国庆", sex: "男", phone_number: '18801880037', Email: "guoqing.ding@cm-dt.com", operation: "324" },
                    { name: "张为峰", sex: "男", phone_number: '18801880021', Email: "weifeng.zhang@cm-dt.com", operation: "324" },
                    { name: "练成栋", sex: "男", phone_number: '13817512053', Email: "13817512053@139.com", operation: "324" },
                    { name: "李林", sex: "男", phone_number: '13641604938', Email: "lin.li@cm-dt.com", operation: "324" },
                    { name: "李林", sex: "男", phone_number: '18627728703', Email: "sen.li@cm-dt.com", operation: "324" }
    ]},
    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'items'
        }
    }
});
