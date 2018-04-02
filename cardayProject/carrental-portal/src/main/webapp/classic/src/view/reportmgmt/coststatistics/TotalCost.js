Ext.define('Admin.view.reportmgmt.coststatistics.TotalCost', {
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
    
    controller: 'coststatisticscontroller',
    id: 'coststatistics_totalcost_id',
    tbar: [
        {
         xtype:'tbtext',
           text: '单位：元',
       },
       {
         xtype:'component',
         flex:1,
         html:''
       },
       {
         xtype:'tbtext',
           text:'费用统计',
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
		   store: '{totalCost}'
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
            fields: ['maintainCost', 'insuranceCost', 'trafficCost', 'totalCost'],
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
            title: ['维修保养', '保险', '通行费用', '油耗费用'],
            xField: 'name',
            yField: ['maintainCost', 'insuranceCost', 'trafficCost', 'totalCost'],
            label: {
                field: ['maintainCost', 'insuranceCost', 'trafficCost', 'totalCost'],
                display: 'insideEnd',
            },
            highlight: true,
            style: {
                inGroupGapWidth: -7
            }
        }
    }],
   
});
