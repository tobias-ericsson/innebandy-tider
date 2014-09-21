var mySpace = function () {

    $(document).ready(function () {
        log("doc is ready");
        ko.applyBindings(myViewModel);
        fetchSearchStringFromLocalStorage();
        pushGamesToViewModel(fetchGamesFromLocalStorage());
        fetchGames({'url': 'search/?search_field=' + myViewModel.searchString() + "&time_filter=" + myViewModel.timeFilter()});
    });

    var myViewModel = {
        timeFilter: ko.observable("forward"),
        games: ko.observableArray([]),
        createNew: createNew,
        save: save,
        search: search,
        showPlanetElement: showPlanetElement,
        hidePlanetElement: hidePlanetElement,
        fetchGames: function (data, event) {
            fetchGames(data)
        },
        searchString: ko.observable("")
    };

    function fetchSearchStringFromLocalStorage() {
        if (typeof(Storage) !== "undefined" && localStorage.search_field) {
            log("fetching search_field: " + localStorage.search_field);
            myViewModel.searchString(localStorage.search_field);
            myViewModel.timeFilter(localStorage.timeFilter);
            //return localStorage.search_field;
        } else {
            //return "";
            myViewModel.searchString("");
            myViewModel.timeFilter("forward");
        }
    }

    function saveSearchStringInLocalStorage() {
        if (typeof(Storage) !== "undefined") {
            log("saving search_field: " + myViewModel.searchString() + " " + myViewModel.timeFilter());
            localStorage.search_field = myViewModel.searchString();
            localStorage.timeFilter = myViewModel.timeFilter();
        }
    }

    function fetchGamesFromLocalStorage() {
        if (typeof(Storage) !== "undefined" && localStorage.games) {
            log("fetching games");
            return JSON.parse(localStorage.games);
        } else {
            return {};
        }
    }

    function saveGamesInLocalStorage(games) {
        if (typeof(Storage) !== "undefined") {
            log("saving games");
            localStorage.games = games;
        }
    }

    function search(htmlFormElement) {
        var data = $(htmlFormElement).serialize();
        log("search for " + data);
        fetchGames({'url': 'search/?' + data});
    }

    function save(data) {
        var json = ko.toJSON(data);
        log("save" + json);
        log(data.title);
        log(data.body);

        $.ajax({
            type: "POST",
            data: json,
            dataType: "json",
            url: "es/create_new",
            statusCode: {
                404: function () {
                    log("got 404");
                },
                200: function (data) {
                    log(data);
                    //var j = JSON.parse(data);
                    //log(j);

                    for (index in data) {
                        for (type in data[index]) {
                            log(index + type);
                            myViewModel.categories.push(new Category({category: index, type: type}));

                        }

                    }
                }
            }
        });
    }

    function showPlanetElement(elem) {
        //if (elem.nodeType === 1) $(elem).hide().slideDown()
    }

    function hidePlanetElement(elem) {
        /*
         if (elem.nodeType === 1) $(elem).slideUp(function () {
         $(elem).remove();
         })*/
    }

    function fetchGames(data) {
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
                    log("data " + JSON.stringify(data));
                    myViewModel.games.removeAll();
                    if (data.total > 0) {
                        saveSearchStringInLocalStorage();
                        saveGamesInLocalStorage(JSON.stringify(data.hits));
                        pushGamesToViewModel(data.hits);
                    }
                }
            }
        });
    }

    function pushGamesToViewModel(games) {
        for (index in games) {
            log("game " + index);
            var source = games[index]._source.doc;
            log("source "+JSON.stringify(source));
            myViewModel.games.push(new Game(source));
        }
    }

    function Game(data) {
        var self = this;
        if (data) {
            self.id = data.id;
            self.game = data;
            self.dayOfWeek = dayOfWeek(data.date);
            self.niceDate = niceDate(data.date);
        }
    }

    function niceDate(date) {
        var d = new Date(date);
        var year = "";
        if (d.getFullYear() != new Date().getFullYear()) {
            year = d.getFullYear();
        }
        return d.getDate() + "/" + (d.getMonth() + 1) + " " + year;
    }

    function dayOfWeek(date) {
        var d = new Date(date);
        //log("datum " + d);
        var weekday = new Array(7);
        weekday[0] = "söndag";
        weekday[1] = "måndag";
        weekday[2] = "tisdag";
        weekday[3] = "onsdag";
        weekday[4] = "torsdag";
        weekday[5] = "fredag";
        weekday[6] = "lördag";
        //log("datum " + n);
        return weekday[d.getDay()];
    }

    function log(message) {
        if (typeof console == "object") {
            console.log(message);
        }
    }

    function createNew(htmlFormElement) {
        log("createNew " + $(htmlFormElement).serialize());
        $.ajax({
            type: "POST",
            data: $(htmlFormElement).serialize(),
            dataType: "json",
            url: "es/create_new",
            statusCode: {
                404: function () {
                    log("got 404");
                },
                200: function (data) {
                    log(data);
                    for (index in data) {
                        for (type in data[index]) {
                            log(index + type);
                            myViewModel.categories.push(new Category({category: index, type: type}));

                        }

                    }
                }
            }
        });
    }
}();





