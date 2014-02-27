/**
 * Module dependencies.
 */

var express = require('express');
var routes = require('./routes');
var user = require('./routes/user');
var esClient = require('./routes/es_client');
var esCRUDClient = require('./routes/es_crud_client');
var http = require('http');
var path = require('path');

var app = express();
var ip = process.env.OPENSHIFT_DIY_IP || "127.0.0.1";
var port = process.env.OPENSHIFT_DIY_PORT || 8080;
var crudURL = '/matcher/innebandy-match*';

// all environments
app.use(express.logger('dev'));
app.use(esCRUDClient.esProxy);
app.use(express.favicon());
app.use(express.json());
app.use(express.urlencoded());
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));


// development only
if ('development' == app.get('env')) {
    app.use(express.errorHandler());
}
app.use(require('connect-restreamer')());

//app.get('/', routes.index);
//app.get('/users', user.list);
app.get('/search/*', esClient.search);
//app.all(crudURL, esCRUDClient.esProxy);
/*
app.post(crudURL, esCRUDClient.updateGame);
app.get(crudURL, esCRUDClient.readGame);
app.delete(crudURL, esCRUDClient.deleteGame);
app.put(crudURL, esCRUDClient.createGame);
*/



http.createServer(app).listen(port, ip, function () {
    console.log(new Date()+'\nServer running on ' + ip + ':' + port);
});
