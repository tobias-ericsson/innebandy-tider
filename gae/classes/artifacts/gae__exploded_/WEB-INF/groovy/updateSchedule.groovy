import domain.Crud
import domain.Game

String name = "Update Schedule"
log.info(name)

try {


    def targetUrl = 'http://innebandy-tider.rhcloud.com/?sportig=ping'





    List<Game> games = Crud.readAllGames()

    //curl -XPOST http://localhost:9200/matcher/innebandy/2/_update -d "{"""doc""": {test:"""2"""},"""doc_as_upsert""":true}"
    println "Sending..."
    send(games)

    //request.setAttribute 'header', name
    //request.setAttribute 'games', games
    //forward '/WEB-INF/pages/cronresult.gtpl'

} catch (Exception e) {
    log.error(e.class.simpleName + " : " + e.message)
    println "OHH NOOO!!"+e.message
    e.printStackTrace()
}

private static send(List<Game> games) {
    games.eachWithIndex { game, i ->
        def json = new groovy.json.JsonBuilder(game)
        //def result1 = json {games}
        String key = game.key.replaceAll('"',"") - "game(" - ")"

        println json.toString()
        String jsonString = '{"doc":' +json+', "doc_as_upsert":true}'

        if (i==2) {
            //println "start"
            def url = new URL("http://innebandy-tider.rhcloud.com/matcher/innebandy-match/"+key)
            def connection = url.openConnection()
            connection.setRequestProperty("Content-type","application/json")
            connection.doOutput = true
            Writer writer = new OutputStreamWriter(connection.outputStream)
            writer.write(jsonString)
            writer.flush()
            writer.close()
            connection.connect()
            println connection.content.text
        }

    }
}


