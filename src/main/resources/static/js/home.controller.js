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
        console.log(data);
    }).error(function() {
        console.log("Noo!!");
    });

});