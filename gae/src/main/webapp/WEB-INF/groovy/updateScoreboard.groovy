import domain.Crud
import domain.Game
import logic.Sender

String name = "Update Scoreboard"
log.info(name)

try {
    List<Game> games = Crud.readAllGamesForYear(domain.Properties.YEAR)
    Sender.sendGames(games)
    request.setAttribute 'header', name
    request.setAttribute 'games', games
    forward '/WEB-INF/pages/cronresult.gtpl'

} catch (Exception e) {
    println(e.class.simpleName + " : " + e.message)
    println "OHH NOOO!!"+e.message
    e.printStackTrace()
}
