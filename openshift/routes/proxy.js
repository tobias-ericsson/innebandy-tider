var prop = require('./properties');
var httpProxy = require('http-proxy');
var proxy = httpProxy.createProxyServer();

exports.interceptRequest = function (request, response, next) {
    if (request.url.indexOf(prop.interceptPath) != -1) {
        console.log(request.url + " sent to " + prop.interceptServer);
        return proxy.web(request, response, {
            target: prop.interceptServer
        });
    } else {
        console.log(request.url + " sent to next");
        next();
        return null;
    }
};