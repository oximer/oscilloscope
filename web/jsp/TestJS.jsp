<script src="http://code.highcharts.com/highcharts.js"></script>
<div id="container" style="height: 400px; width: 500px;"></div>
<link rel='stylesheet' href='../webjars/bootstrap/3.1.0/css/bootstrap.min.css'>
<script type="text/javascript" src="../webjars/jquery/1.9.0/jquery.js"></script>
<script type="text/javascript" src="../scripts/dashboard.js"></script>

<script>
    $(function () {
        var h = new Highcharts.Chart({
            chart: {
                //alignTicks: false,
                type: 'line',
                renderTo: "container"
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: [{
                title: {
                    text: 'Primary Axis'
                },
                gridLineWidth: 0
            }, {
                title: {
                    text: 'Secondary Axis'
                },
                opposite: true
            }],
            legend: {
                layout: 'vertical',
                backgroundColor: '#FFFFFF',
                floating: true,
                align: 'left',
                x: 100,
                verticalAlign: 'top',
                y: 70
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                            this.x + ': ' + this.y;
                }
            },
            plotOptions: {},
            series: [{
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]

            }, {
                data: [129.9, 271.5, 306.4, 29.2, 544.0, 376.0, 435.6, 348.5, 216.4, 294.1, 35.6, 354.4],
                yAxis: 1

            }]
        });
    });
</script>