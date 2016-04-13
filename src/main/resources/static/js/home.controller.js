angular.module("app", []).controller("home", function ($http) {
    var self = this;

    $http.get("/user").success(function (data) {
        self.user = data.userAuthentication.details.user;
        self.authenticated = true;
    }).error(function () {
        self.user = "N/A";
        self.authenticated = false;
    });


    // FitBit API calls through backend

    $http.get("/lifetime").success(function(data) {
        self.lifetime = data.lifetime;
        self.personalBests = data.best;
    }).error(function() {
        console.log("Noo!!");
    });

    $http.get("/summarytoday").success(function(data) {
        self.summaryToday = data.summary;
    }).error(function() {
        console.log("Noo!!");
    });

    $http.get("/lastweek/steps").success(function(data) {
        var steps = data["activities-tracker-steps"];
        var plotData = [];

        for(var i = 0; i < steps.length; i++) {
            plotData.push([i+1, parseInt(steps[i].value)]);
        }

        plot(plotData, 0, 10000, "last-week-steps", "Last Week Steps");
    }).error(function() {
        console.log("Noo!!");
    });

    $http.get("/lastweek/distance").success(function(data) {
        var distance = data["activities-tracker-distance"];
        var plotData = [];

        for(var i = 0; i < distance.length; i++) {
            plotData.push([i+1, parseInt(distance[i].value)]);
        }

        plot(plotData, 0, 20, "last-week-distance", "Last Week Distance");
    }).error(function() {
        console.log("Noo!!");
    });

    $http.get("/lastweek/floors").success(function(data) {
        var floors = data["activities-tracker-floors"];
        var plotData = [];

        for(var i = 0; i < floors.length; i++) {
            plotData.push([i+1, parseInt(floors[i].value)]);
        }

        plot(plotData, 0, 40, "last-week-floors", "Last Week Floors");
    }).error(function() {
        console.log("Noo!!");
    });

    $http.get("/today/heartrate").success(function(data) {
        var hr = data["activities-heart-intraday"];
        var plotData = [];

        var today = getTodayAsString();
        for(var i = 0; i < hr.dataset.length; i++) {
            plotData.push([new Date(today + " " + hr.dataset[i].time).getTime(), hr.dataset[i].value]);
        }

        plotTime(plotData, 40, 180, "today-hr", "Todays Heart Rate");
    }).error(function() {
        console.log("Noo!!");
    });


    // Helper functions

    function plot(data, yMin, yMax, plotId, label) {

        var options = {
            series: {
                lines: {
                    show: true
                },
                points: {
                    show: true
                }
            },
            grid: {
                hoverable: true //IMPORTANT! this is needed for tooltip to work
            },
            yaxis: {
                min: yMin,
                max: yMax
            },
            xaxis: {
                min: 1,
                max: 7
            },
            tooltip: false
        };

        var plotObj = $.plot($("#flot-line-chart-" + plotId), [{
                data: data,
                label: label
            }],
            options);
    }

    function plotTime(data, yMin, yMax, plotId, label) {


        var options = {
            series: {
                lines: {
                    show: true
                },
                points: {
                    show: true
                }
            },
            grid: {
                hoverable: true //IMPORTANT! this is needed for tooltip to work
            },
            yaxis: {
                min: yMin,
                max: yMax
            },
            xaxis: {
                mode: "time",
                minTickSize: [5, "second"]
            },
            tooltip: false
        };

        var plotObj = $.plot($("#flot-line-chart-" + plotId), [{
                data: data,
                label: label
            }],
            options);
    }

    function getTodayAsString() {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth()+1; //January is 0!

        var yyyy = today.getFullYear();
        if(dd<10){
            dd='0'+dd
        }
        if(mm<10){
            mm='0'+mm
        }
        var today = yyyy+'/'+mm+'/'+dd;
        return today;
    }

});