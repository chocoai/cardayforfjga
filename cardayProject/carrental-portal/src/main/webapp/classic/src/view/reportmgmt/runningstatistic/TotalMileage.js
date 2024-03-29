Ext.define('Admin.view.reportmgmt.runningstatistic.TotalMileage', {
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
    
    controller: 'runningstatisticcontroller',
    id: 'vrunningstatistic_totalmileage_id',

    tbar: [
        {
         xtype:'tbtext',
           text: '单位：公里',
       },
       {
         xtype:'component',
         flex:1,
         html:''
       },
       {
         xtype:'tbtext',
           text:'行驶公里',
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
		   store: '{totalMileage}'
	   },

	   width: '100%',
       height: 450,
		
       //x轴与y轴的声明
	    axes: [
	    {
	        type: 'numeric',
	        position: 'left',
	        title: false,
	        grid: true,
	        minimum: 0,
	    }, 
	    {
	    	type: 'category',
	    }],
	    
	   series: [{
	       type: 'bar',
	       colors: ['#389BD2'],//柱状颜色
	       style: {
               minGapWidth: 75
           },
	       axis: 'left',
	       xField: 'name',
	       yField: 'data',
   
	       tooltip: {
              trackMouse: true,
              renderer: function(tooltip, record, item) {
           	   tooltip.setHtml(record.get('name') + ": " + record.get('data') + "公里");
              }
            },
           
           label: {
        	   display: 'insideEnd',
               field: 'data',
               orientation: 'horizontal',
           }
	   }]
    }],
   
});
