var stats = function () {

    $(document).ready(function () {
        log("doc is ready stats");
        fetchStats({'url':'/stats/team-stats/2014/M1/'});
    });

    function log(message) {
        if (typeof console == "object") {
            console.log(message);
        }
    }

    function fetchStats(data) {
        log(data);

        $.ajax({
            type: "GET",
            dataType: "json",
            url: data.url,
            statusCode: {
                404: function () {
                    log("got 404");
                },
                200: function (data) {
                    log("hits " + JSON.stringify(data));
                }
            }
        });
    }
}();