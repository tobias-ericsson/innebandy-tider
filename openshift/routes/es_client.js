var elasticsearch = require('elasticsearch');
var url = require('url');
var util = require('./util');

var ip = process.env.OPENSHIFT_DIY_IP || "127.0.0.1";
var port = process.env.ES_PORT || 9200;

var client = elasticsearch.Client({
    hosts: [
        ip + ':' + port
    ],
    sniffOnStart: true,
    sniffInterval: 300000
});

client.cluster.health(function (err, resp) {
    if (err) {
        console.error(err.message);
    } else {
        console.dir(resp);
    }
});

exports.search = function (request, response) {

    //var pathname = url.parse(request.url, true).pathname;
    var queryData = url.parse(request.url, true).query;
    var dateString = util.getCurrentDateString();
    console.log("request: " + JSON.stringify(queryData) + " at " + dateString);

    var date;
    var sort;
    if (queryData.time_filter && queryData.time_filter == "backward") {
        date = { "lt": dateString};
        sort = "date:desc,time:desc";
    } else {
        date = { "gte": dateString };
        sort = "date:asc,time:asc";
    }

    var query = {query_string: {
        default_field: "_all",
        query: queryData.search_field
    }};
    if (!queryData.search_field) {
        query = {"match_all": {}};
    }

    var q = {
        filtered: {
            query: query,
            filter: {
                numeric_range: {
                    "date": date
                }
            }
        }
    };

    client.search({
        index: 'matcher',
        size: 70,
        sort: sort,
        body: {
            query: q
        }
    }, function (error, resp) {
        if (error) {
            console.log(error);
            response.send(error);
        } else {
            var hits = resp.hits;
            console.log("response from es: " + hits.total + " hits");
            //console.log("hits " + JSON.stringify(hits));
            response.send(hits);
        }
    });
};

exports.post = function (request, response) {
    console.log("** post **");
    console.dir(request.body);
    var data = request.body;
    if (data.date && data.time && data.place) {
        var id = data.date + ":" + data.time + ":" + data.place;
        client.create({
            index: 'matcher',
            type: 'innebandy-match',
            id: id,
            body: data
        }, function (error, resp) {
            if (error) {
                console.log(error);
                response.send(error);
            } else {
                console.dir(resp);
                response.send(resp);
            }
        });
    } else {
        response.send("missing params");
    }
};



