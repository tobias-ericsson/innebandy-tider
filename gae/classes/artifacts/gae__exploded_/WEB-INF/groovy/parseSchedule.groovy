import domain.Crud
import domain.Game
import parser.SportResultsXMLParser

log.info("parseSchedule")


File file = new File("d:\\dev\\git\\m-ligan\\data\\m-ligan spel14.htm")
List games = SportResultsXMLParser.parseGames(file)

games.eachWithIndex { game, i ->
    def json = new groovy.json.JsonBuilder(game)
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

}