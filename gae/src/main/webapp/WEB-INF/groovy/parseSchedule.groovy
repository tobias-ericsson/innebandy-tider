import domain.Crud
import domain.Game
import groovy.json.JsonBuilder
import parser.SportResultsXMLParser

String name = "Parse Schedule"
log.info(name)

//File file = new File("d:\\dev\\git\\m-ligan\\data\\m-ligan spel14.htm")
String text = domain.Properties.RESOURCE_SCHEDULE_URL.toURL().getText("windows-1252")
def games = SportResultsXMLParser.parseGames(text)
games.each { Game game ->
    Crud.saveGame(game)
}

request.setAttribute 'header', name
request.setAttribute 'games', games
forward '/WEB-INF/pages/cronresult.gtpl'
/*
games.eachWithIndex { game, i ->
    def json = new JsonBuilder(game)
    //def result1 = json {games}
    //println json.toString()

    if (true) {
        //println "start"
        def url = new URL("http://innebandy-tider.rhcloud.com/matcher/innebandy-match")
        def connection = url.openConnection()
        connection.setRequestProperty("Content-type","application/json")
        connection.doOutput = true
        Writer writer = new OutputStreamWriter(connection.outputStream)
        writer.write(json)
        writer.flush()
        writer.close()
        connection.connect()
        println connection.content.text
    }

}*/