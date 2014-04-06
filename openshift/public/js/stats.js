var stats = function () {
    var myViewModel = {
        teams: ko.observableArray([]),
        addSort: addSort,
        fetchLeagueStats: fetchLeagueStats,
        selectTab: selectTab,
        currentTab: ko.observable(1),
        tabs: ko.observableArray([])
    };
    myViewModel.addSort('name');
    myViewModel.addSort('gamesPlayed');
    myViewModel.addSort('win');
    myViewModel.addSort('loss');
    myViewModel.addSort('points');
    pushTabsToViewModel();

    function pushTabsToViewModel() {
        var nbrOfLeagues = 5;
        for (var k = 1; k <= nbrOfLeagues; k++) {
            myViewModel.tabs.push(new Tab(k));
        }
    }

    function Tab(id) {
        var self = this;
        self.id = id;
        self.name = "M"+id;
        self.selected = ko.observable();
        self.setSelected = selectTab;
    }

    $(document).ready(function () {
        log("doc is ready stats");
        ko.applyBindings(myViewModel);


        fetchStats({'url': '/stats/team-stats/2014/M1/'});
    });

    function selectTab(tab) {
        console.log("selected " + tab.name);
        myViewModel.currentTab(tab.id);
        var url = '/stats/team-stats/2014/' + tab.name;
        fetchStats({'url': url});
    }

    function fetchLeagueStats() {

    }

    function log(message) {
        if (typeof console == "object") {
            console.log(message);
        }
    }

    function addSort(field) {
        var self = this;
        self['sortDirection_' + field] = 1;
        console.log("field " + field);
        self['sortBy_' + field] = function () {
            self['sortDirection_' + field] = -this['sortDirection_' + field];
            self.teams.sort(function (a, b) {
                if (a.team[field] > b.team[field]) return 1 * self['sortDirection_' + field];
                if (a.team[field] < b.team[field]) return -1 * self['sortDirection_' + field];
                return 0;
            });
        };
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
                    myViewModel.teams.removeAll();
                    pushTeamsToViewModel(data);
                    myViewModel.sortDirection_points = 1;
                    myViewModel.sortBy_points();
                }
            }
        });
    }

    function pushTeamsToViewModel(teams) {
        for (index in teams) {
            log("team " + index);
            var team = teams[index];
            log(JSON.stringify(team));
            myViewModel.teams.push(new Team(team));
        }
    }

    function Team(data) {
        var self = this;
        self.team = data;
    }


}();