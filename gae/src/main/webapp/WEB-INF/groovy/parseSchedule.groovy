import domain.Crud
import domain.Game

log.info("parseSchedule")

List<Game> games = Crud.readAllGames()

games.each {
    println it
}

println "parseSchema"