import logic.Scoreboard

response.contentType = 'application/json'
def teams = Scoreboard.teamStatsForLeagueYear("M1", 2014)

def json = new groovy.json.JsonBuilder(teams)
println json