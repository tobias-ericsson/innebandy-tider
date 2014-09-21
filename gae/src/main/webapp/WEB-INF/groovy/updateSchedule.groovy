import domain.Crud
import domain.Game
import logic.Sender

String name = "Update Schedule"
log.info(name)

List<Game> games = Crud.readAllGamesForYear(domain.Properties.YEAR)
Sender.sendGames(games)
request.setAttribute 'header', name
request.setAttribute 'games', games
forward '/WEB-INF/pages/cronresult.gtpl'

//curl -XPOST http://localhost:9200/matcher/innebandy/2/_update -d "{"""doc""": {test:"""2"""},"""doc_as_upsert""":true}"
/*
private static send(List<Game> games) {
    games.eachWithIndex { game, i ->
        def json = new groovy.json.JsonBuilder(game)
        //def result1 = json {games}
        String key = game.id.replaceAll('"', "") - "game(" - ")"

        println json.toString()
        String jsonString = '{"doc":' + json + ', "doc_as_upsert":true}'

        if (i == 2) {
            //println "start"
            def url = new URL("http://innebandy-tider.rhcloud.com/matcher/innebandy-match/" + key)
            def connection = url.openConnection()
            connection.setRequestProperty("Content-type", "application/json")
            connection.doOutput = true
            Writer writer = new OutputStreamWriter(connection.outputStream)
            writer.write(jsonString)
            writer.flush()
            writer.close()
            connection.connect()
            println connection.content.text
        }

    }
}*/


