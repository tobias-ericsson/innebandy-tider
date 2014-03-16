import domain.Crud
import domain.Game

String name = "Update Schedule"
log.info(name)

try {
    List<Game> games = Crud.readAllGames()

    request.setAttribute 'header', name
    request.setAttribute 'games', games
    forward '/WEB-INF/pages/cronresult.gtpl'

} catch (Exception e) {
    log.error(e.class.simpleName + " : " + e.message)
    println "OHH NOOO!!"+e.message
    e.printStackTrace()
}


