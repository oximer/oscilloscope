<%@ page import="edu.cmu.lpsoca.model.Board" %>
<%@ page import="edu.cmu.lpsoca.oscilloscope.services.PowerService" %>
<%@ page import="edu.cmu.lpsoca.oscilloscope.servlet.dashboardImpl.GeneralDashboard" %>
<%@ page import="edu.cmu.lpsoca.util.chart.PreparePowerChart" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE HTML>
<html>
<head>
    <title>Baxster an Admin Panel Category Flat Bootstrap Responsive Website Template | Home :: w3layouts</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="keywords" content="Baxster Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template,
SmartPhone Compatible web template, free WebDesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design"/>
    <script type="application/x-javascript"> addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);
    function hideURLbar() {
        window.scrollTo(0, 1);
    } </script>
    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.css" rel='stylesheet' type='text/css'/>
    <!-- Custom CSS -->
    <link href="css/style.css" rel='stylesheet' type='text/css'/>
    <!-- font CSS -->
    <link rel="icon" href="favicon.ico" type="image/x-icon">
    <!-- font-awesome icons -->
    <link href="css/font-awesome.css" rel="stylesheet">
    <!-- //font-awesome icons -->
    <!-- chart -->
    <script src="js/Chart.js"></script>
    <!-- //chart -->
    <!-- js-->
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/modernizr.custom.js"></script>
    <!--webfonts-->
    <link href='//fonts.googleapis.com/css?family=Roboto+Condensed:400,300,300italic,400italic,700,700italic'
          rel='stylesheet' type='text/css'>
    <!--//webfonts-->
    <!--animate-->
    <link href="css/animate.css" rel="stylesheet" type="text/css" media="all">
    <script src="js/wow.min.js"></script>
    <script>
        new WOW().init();
    </script>
    <!--//end-animate-->
    <!-- Metis Menu -->
    <script src="js/metisMenu.min.js"></script>
    <script src="js/custom.js"></script>
    <link href="css/custom.css" rel="stylesheet">
    <!--//Metis Menu -->
    <!-- HighCharts -->
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/data.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>
</head>
<body class="cbp-spmenu-push">
<div class="main-content">
    <!--left-fixed -navigation-->
    <div class="sidebar" role="navigation">
        <div class="navbar-collapse">
            <nav class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right dev-page-sidebar mCustomScrollbar _mCS_1 mCS-autoHide mCS_no_scrollbar"
                 id="cbp-spmenu-s1">
                <div class="scrollbar scrollbar1">
                    <ul class="nav" id="side-menu">
                        <li>
                            <a href="/web/dashboard" class="active"><i class="fa fa-home nav_icon"></i>Dashboard</a>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-cogs nav_icon"></i>Boards<span class="fa arrow"></span></a>
                            <ul id="boardMenu" class="nav nav-second-level collapse">
                                <script type="text/javascript">
                                    $(function () {
                                        var updateMenu = function () {
                                            $.get("../rest/board", function (data) {
                                                $("#boardMenu").empty();
                                                for (var i = 0; i < data.length; i++) {
                                                    $("#boardMenu").append("<li><a href=\"/web/dashboard?board=" + data[i].id + "\"> Board " + data[i].id + "</a></li>")
                                                }
                                            });
                                        }

                                        updateMenu();

                                        setInterval(function () {
                                            updateMenu();
                                        }, <%out.print(PowerService.SAMPLE_RATE);%>);
                                    });
                                </script>
                            </ul>
                        </li>
                        <li>
                            <a href="maps.html"><i class="fa fa-book nav_icon"></i>Messages</a>
                        </li>

                    </ul>
                </div>
                <!-- //sidebar-collapse -->
            </nav>
        </div>
    </div>
    <!--left-fixed -navigation-->
    <!-- header-starts -->
    <div class="sticky-header header-section ">
        <div class="header-left">
            <!--logo -->
            <div class="logo">
                <a href="dashboard">
                    <ul>
                        <li><img src="images/logo1.png" alt=""/></li>
                        <li><h1>Low Power</h1></li>
                        <div class="clearfix"></div>
                    </ul>
                </a>
            </div>

            <div class="clearfix"></div>
        </div>

        <div class="header-right">
            <!--toggle button start-->
            <button id="showLeftPush"><i class="fa fa-bars"></i></button>
            <!--toggle button end-->
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
    </div>
    <!-- //header-ends -->
    <!-- main content start-->
    <div id="page-wrapper">
        <div class="main-page">

            <!-- four-grids -->
            <div class="row four-grids">
                <div class="col-md-6">
                    <div class="tickets">
                        <div class="grid-left">
                            <div class="book-icon">
                                <i class="fa fa-book"></i>
                            </div>
                        </div>
                        <div class="grid-right">
                            <h3>Boards <span>Connected</span></h3>
                            <p id="numberOfBoards">
                                <script type="text/javascript">
                                    $(function () {
                                        var getNumberOfBoards = function () {
                                            $.get("../rest/board", function (data) {
                                                $('#numberOfBoards').empty()
                                                $('#numberOfBoards').append(data.length);
                                            });
                                        }
                                        getNumberOfBoards();

                                        setInterval(function () {
                                            getNumberOfBoards();
                                        }, <%out.print(PowerService.SAMPLE_RATE);%>);
                                    });
                                </script>
                            </p>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="tickets">
                        <div class="grid-left">
                            <div class="book-icon">
                                <i class="fa fa-rocket"></i>
                            </div>
                        </div>
                        <div class="grid-right">
                            <h3>Application <span>Running</span></h3>
                            <p>
                                <%
                                    Map<String, List<Board>> applicationMap = (Map<String, List<Board>>) request.getAttribute(GeneralDashboard.APPLICATION_LIST);
                                    out.print(applicationMap.keySet().size());
                                %>
                            </p>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- //four-grids -->
            <!--row-->
            <div class="row">
                <div class="col-md-3">
                    <div class="panel panel-widget">
                        <div class="panel-title">
                            List of </br> Boards
                            <ul class="panel-tools">
                                <li><a class="icon minimise-tool"><i class="fa fa-minus"></i></a></li>
                                <li><a class="icon expand-tool"><i class="fa fa-expand"></i></a></li>
                                <li><a class="icon closed-tool"><i class="fa fa-times"></i></a></li>
                            </ul>
                        </div>
                        <div id="listOfBoards" class="panel-body">
                            <script type="text/javascript">
                                $(function () {
                                    var getListOfBoards = function () {
                                        $.get("../rest/board", function (data) {
                                            var i;
                                            $('#listOfBoards').empty();
                                            for (i = 0; i < data.length; i++) {
                                                if ($('#listOfBoards').find('#' + data[i].applicationId).length == 0) {
                                                    $('#listOfBoards').append("<h3 id=" + data[i].applicationId + ">" + data[i].applicationId + "</h3></br>");
                                                }
                                                if ($('#' + data[i].applicationId).find('#' + data[i].id).length == 0) {
                                                    $('#listOfBoards').append("<h4 id=" + data[i].id + "><p>" + data[i].id + " - " + data[i].name + "</p></h4></br>");
                                                }
                                            }
                                        });
                                    }
                                    getListOfBoards();

                                    setInterval(function () {
                                        getListOfBoards();
                                    }, <%out.print(PowerService.SAMPLE_RATE);%>);
                                });
                            </script>
                        </div>
                    </div>
                </div>

                <div class="col-md-9">
                    <div class="panel panel-widget">
                        <div class="panel-title">
                            Power Consumption
                            <ul class="panel-tools">
                                <li><a class="icon minimise-tool"><i class="fa fa-minus"></i></a></li>
                                <li><a class="icon expand-tool"><i class="fa fa-expand"></i></a></li>
                                <li><a class="icon closed-tool"><i class="fa fa-times"></i></a></li>
                            </ul>
                        </div>
                        <div class="panel-body">
                            <div class="lines-points">
                                <div id="power_container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                                <script type="text/javascript">
                                    $(function () {
                                        var chart2 = new Highcharts.Chart({
                                            chart: {
                                                type: 'column',
                                                renderTo: 'power_container',
                                                events: {
                                                    load: function () {
                                                        var getEnergyPerTask = function () {
                                                            $.get("../rest/board/power/task?lastMilliseconds=-1", function (data) {
                                                                while (chart2.series.length > 0)
                                                                    chart2.series[0].remove(true);

                                                                var i;
                                                                for (i = 0; i < data.length; i++) {
                                                                    chart2.addSeries({
                                                                        name: data[i].name,
                                                                        data: data[i].data
                                                                    });
                                                                }
                                                                chart2.redraw();
                                                            });
                                                        }
                                                        getEnergyPerTask();
                                                        setInterval(function () {
                                                            getEnergyPerTask();
                                                        }, <%out.print(PowerService.SAMPLE_RATE);%>);
                                                    }
                                                }
                                            },
                                            title: {
                                                text: 'Average Power Consumption per Board'
                                            },
                                            xAxis: {
                                                categories: <% out.print((String) request.getAttribute(PreparePowerChart.BOARD_CATEGORIES));%>,
                                                crosshair: true
                                            },
                                            yAxis: {
                                                min: 0,
                                                title: {
                                                    text: 'Power (Watts)'
                                                }
                                            },
                                            tooltip: {
                                                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                                '<td style="padding:0"><b>{point.y:.3f} W</b></td></tr>',
                                                footerFormat: '</table>',
                                                shared: true,
                                                useHTML: true
                                            },
                                            plotOptions: {
                                                column: {
                                                    pointPadding: 0.2,
                                                    borderWidth: 0
                                                }
                                            },
                                            series: []

                                        });
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="clearfix"></div>
            </div>
            <!--//row-->

            <!--row-->
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-widget">
                        <div class="panel-title">
                            Power Consumption over time
                            <ul class="panel-tools">
                                <li><a class="icon minimise-tool"><i class="fa fa-minus"></i></a></li>
                                <li><a class="icon expand-tool"><i class="fa fa-expand"></i></a></li>
                                <li><a class="icon closed-tool"><i class="fa fa-times"></i></a></li>
                            </ul>
                        </div>
                        <div class="panel-body">
                            <div class="lines-points">
                                <div id="power_overtime_container"
                                     style="min-width: 310px; height: 400px; margin: 0 auto">
                                </div>
                                <div id="error_message"></div>
                                <script type="text/javascript">
                                    $(function () {
                                        var chart = new Highcharts.Chart({
                                            chart: {
                                                type: 'line',
                                                renderTo: 'power_overtime_container',
                                                events: {
                                                    load: function () {
                                                        setInterval(function () {
                                                            var x;
                                                            var y;
                                                            y = $.get("../rest/board/power", function (data) {
                                                                $("#error_message").empty();
                                                                for (var i = 0; i < data.length; i++) {
                                                                    var serie = chart.series[i];
                                                                    if (data[i].y == -1) {
                                                                        $("#error_message").append("Communication instability over " + chart.series[i].userOptions.name + "</br>");
                                                                        data[i].y = 0;
                                                                    }
                                                                    if (i == data.length - 1) {
                                                                        serie.addPoint([data[i].x, data[i].y], true, true);
                                                                    }
                                                                    else {
                                                                        serie.addPoint([data[i].x, data[i].y], false, true);
                                                                    }
                                                                }
                                                            });
                                                        }, <%out.print(PowerService.SAMPLE_RATE);%>);

                                                        var x;
                                                        var y;
                                                        y = $.get("../rest/board/power/historic", function (data) {
                                                            for (var i = 0; i < data.length; i++) {
                                                                chart.addSeries({
                                                                    name: data[i].name,
                                                                    data: (function () {
                                                                        var data2 = [];
                                                                        var j;
                                                                        for (j = 0; j < data[i].x.length; j++) {
                                                                            if (data[i].y[j] == -1) {
                                                                                data2.push({
                                                                                    x: data[i].x[j],
                                                                                    y: 0
                                                                                });
                                                                            } else {
                                                                                data2.push({
                                                                                    x: data[i].x[j],
                                                                                    y: data[i].y[j]
                                                                                });
                                                                            }
                                                                        }
                                                                        return data2;
                                                                    }())
                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            },
                                            title: {
                                                text: 'Average Power Consumption over Time',
                                                x: -20 //center
                                            },
                                            xAxis: {
                                                type: 'datetime',
                                                tickPixelInterval: 100
                                            },
                                            yAxis: {
                                                title: {
                                                    text: 'Watts (W)'
                                                },
                                                plotLines: [{
                                                    value: 0,
                                                    width: 1,
                                                    color: '#808080'
                                                }],
                                                labels: {
                                                    format: '{value:.2f}'
                                                }
                                            },
                                            tooltip: {
                                                valueSuffix: 'W',
                                                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                                '<td style="padding:0"><b>{point.y:.2f} W</b></td></tr>',
                                                footerFormat: '</table>',
                                                shared: true,
                                                useHTML: true
                                            },
                                            legend: {
                                                layout: 'vertical',
                                                align: 'right',
                                                verticalAlign: 'middle',
                                                borderWidth: 0
                                            },
                                            series: {}
                                        });
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="clearfix"></div>
            </div>
            <!--//row-->

            <div class="clearfix"></div>
        </div>
        <!--//row-->
    </div>
</div>
</div>
<!-- Classie -->
<script src="js/classie.js"></script>
<script>
    var menuLeft = document.getElementById('cbp-spmenu-s1'),
            showLeftPush = document.getElementById('showLeftPush'),
            body = document.body;

    showLeftPush.onclick = function () {
        classie.toggle(this, 'active');
        classie.toggle(body, 'cbp-spmenu-push-toright');
        classie.toggle(menuLeft, 'cbp-spmenu-open');
        disableOther('showLeftPush');
    };


    function disableOther(button) {
        if (button !== 'showLeftPush') {
            classie.toggle(showLeftPush, 'disabled');
        }
    }
</script>
<!-- Bootstrap Core JavaScript -->

<script type="text/javascript" src="js/bootstrap.min.js"></script>

<script type="text/javascript" src="js/dev-loaders.js"></script>
<script type="text/javascript" src="js/dev-layout-default.js"></script>
<script type="text/javascript" src="js/jquery.marquee.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">

<!--max-plugin-->
<script type="text/javascript" src="js/plugins.js"></script>
<!--//max-plugin-->

<!--scrolling js-->
<script src="js/jquery.nicescroll.js"></script>
<script src="js/scripts.js"></script>
<!--//scrolling js-->

</body>
</html>