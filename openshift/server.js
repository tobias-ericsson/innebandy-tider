/**
 * Module dependencies.
 */

var express = require('express');
var routes = require('./routes');
var prop = require('./routes/properties');
var user = require('./routes/user');
var esClient = require('./routes/es_client');
var proxy = require('./routes/proxy');
var http = require('http');
var path = require('path');

var app = express();

// all environments
app.use(express.logger('dev'));
app.use(proxy.interceptRequest);
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

app.get('/search/*', esClient.search);
app.post('/search/*', esClient.search);

try {
    http.createServer(app).listen(prop.portNode, prop.ipNode, function () {
        console.log(new Date() + '\nServer running on ' + prop.ipNode + ':' + prop.portNode);
    });
} catch (e) {
    console.log(e);
}
