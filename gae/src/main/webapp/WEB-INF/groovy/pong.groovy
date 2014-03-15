import domain.Game

def targetUrl = 'http://innebandy-tider.rhcloud.com/?sportig=pong'

log.info("pong "+targetUrl)

URL url = new URL(targetUrl)

def response = url.head()

log.info("response code: "+response.statusCode)
log.info("response headers: "+response.headersMap)

println("response code: "+response.statusCode)
println("response headers: "+response.headersMap)

Game game = new Game();
game.homeTeam = "testing";
println game;

