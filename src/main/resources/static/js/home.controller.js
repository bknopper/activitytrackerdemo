angular.module("app", []).controller("home", function ($http) {
    var self = this;

    $http.get("/user").success(function (data) {
        self.user = data.userAuthentication.details.user;
        self.authenticated = true;
    }).error(function () {
        self.user = "N/A";
        self.authenticated = false;
    });

    self.logout = function() {
        $http.post('/logout', {}).success(function () {
            self.authenticated = false;
            $location.path("/");
        }).error(function (data) {
            console.log("Logout failed")
            self.authenticated = false;
        });
    };


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

});