Ext.define('Admin.view.reportmgmt.vehiclestatistics.TotalNum', {
	extend: 'Ext.chart.Chart',
    alias: "widget.columnchart",
    //alias: "widget.chart",
    requires: [
			'Ext.chart.CartesianChart',
			'Ext.chart.axis.Numeric',
			'Ext.chart.axis.Time',
			'Ext.draw.modifier.Highlight',
			'Ext.chart.interactions.ItemHighlight',
    ],
    
    controller: 'vehiclestatisticscontroller',
    id: 'vehiclestatistics_totalNum_id',
    tbar: [
        {
         xtype:'tbtext',
           text: '单位：辆',
       },
       {
         xtype:'component',
         flex:1,
         html:''
       },
       {
         xtype:'tbtext',
           text:'车辆数统计',
           textAlign:'center',
           height:35,
           style:{
             fontSize: '22px',
               fontWeight: 'bold',
               lineHeight:'35px',
           }
       },
        {
          xtype:'component',
          flex:1,
          html:''
        },
        {
          xtype:'tbtext',
          html: '',
        },
   ],

    items:[
      {
       xtype: 'cartesian',
       animate: true,
	   bind: {
		   store: '{totalNum}'
	   },

      legend: {
          type: 'sprite',
          docked: 'bottom'
      },

	   width: '100%',
       height: 450,

        axes: [{
            type: 'numeric',
            position: 'left',
            fields: ['commonNumAuthorized', 'specialNumAuthorized', 'commonNumUsed', 'specialNumAuthorized'],
            grid: true,
        }, {
            type: 'category',
            position: 'bottom',
            fields: 'name',
            grid: true
        }],
        series: {
            type: 'bar',
            stacked: false,
            title: ['行政执法用车(一般)编制', '行政执法用车(特殊)编制', '行政执法用车(一般)现有', '行政执法用车(特殊)现有'],
            xField: 'name',
            yField: ['commonNumAuthorized', 'specialNumAuthorized', 'commonNumUsed', 'specialNumAuthorized'],
            label: {
                field: ['commonNumAuthorized', 'specialNumAuthorized', 'commonNumUsed', 'specialNumAuthorized'],
                display: 'insideEnd',
            },
            highlight: true,
            style: {
                inGroupGapWidth: -7
            }
        }
    }],
   
});
