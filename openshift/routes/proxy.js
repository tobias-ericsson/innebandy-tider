var prop = require('./properties');
var httpProxy = require('http-proxy');
//var http = require("http");
var request = require("request");
/*
 var options = {

 router: {
 'localhost:8080/matcher': '127.0.0.1:9000',
 'localhost:8080/stats': 'www.test.com',
 'domaintwo.net/differentapp': '127.0.0.1:9002'
 }
 };*/

/*
 var options = {
 changeOrigin: true,
 target: {
 https: true
 }
 }*/

var proxy = httpProxy.createProxyServer();

exports.interceptRequest = function (request, response, next) {

    for (var index in prop.interceptPaths) {
        if (request.url.indexOf(prop.interceptPaths[index].path) != -1) {
            console.log(request.url + " sent to " + prop.interceptPaths[index].server);

            if (prop.interceptPaths[index].server.indexOf("appspot") != -1) {
                console.log("appspot special case");
                return requestFromAppspot(request, prop.interceptPaths[index].server, function (resp) {
                    response.send(resp);
                });
            } else {
                return proxy.web(request, response, {
                    target: prop.interceptPaths[index].server

                });
            }
        }
    }
    console.log(request.url + " sent to next");
    next();
    return null;
};

proxy.on('error', function (e) {
    console.log("error", e);
});

requestFromAppspot = function (req, server, callback) {

    var uri = server+req.path;
    console.log("path "+uri);

    request({
        uri: uri,
        method: "GET",
        timeout: 10000,
        followRedirect: true,
        maxRedirects: 10
    }, function (error, response, body) {
        console.log(body);
        callback(body);
    });
};