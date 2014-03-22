import logic.Scoreboard
import groovy.json.JsonBuilder

response.contentType = 'application/json'
String name = "Team Stats"
try {
    String year = params.get("year", "");
    String league = params.get("league", "");
    log.info(name + " " + year + " " + league)

    def teams = Scoreboard.teamStatsForLeagueYear(league, Integer.parseInt(year))
    def json = new JsonBuilder(teams)
    println json
} catch (IllegalArgumentException e) {
    log.info(e.class.simpleName + " : " + e.message)
    println "{}"
}