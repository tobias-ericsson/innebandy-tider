var elasticsearch = require('./es_client');
var httpProxy = require('http-proxy');
var proxy = httpProxy.createProxyServer();
var es_server = 'http://localhost:9200';
var crudURL = '/matcher/innebandy-match/';
      

exports.esProxy = function (request, response, next) {
	if (request.url.indexOf(crudURL) != -1) {
		console.log("es proxy intercepted "+request.url);
		return proxy.web(request, response ,{
			target: es_server
		});
	} else {
	  console.log("forward "+request.url+" to next");
      next();
	}   
};

exports.createGame = function (request, response) {
    console.log("create game");
	return proxy.web(request, response ,{
        target: 'http://localhost:9200'
    });
    //response.send("create game");
};

exports.readGame = function (request, response) {
    console.log("read game");
    return proxy.web(request, response ,{
        target: 'http://localhost:9200'
    });
    //response.send("read game");
};

exports.updateGame = function (request, response) {
    console.log("update game");
	return proxy.web(request, response ,{
        target: 'http://localhost:9200'
    });
    //response.send("update game");
};

exports.deleteGame = function (request, response) {
    console.log("delete game");
	return proxy.web(request, response ,{
        target: 'http://localhost:9200'
    });
    //response.send("delete game");
};