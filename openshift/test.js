var request = require("request");

console.log("running...");

request({
    uri: "http://l-tribe.appspot.com/stats/team-stats/2014/M1/",
    method: "GET",
    timeout: 10000,
    followRedirect: true,
    maxRedirects: 10
}, function(error, response, body) {
    console.log(body);
});

/*
var options = {
    host: "l-tribe.appspot.com",
    port: 80,
    path: "/stats/team-stats/2014/M1/"

};

http.get(options, function (response) {
    console.log(response.statusCode);
    response.on('data', function (data) {
        console.log(data);
    });
});*/

