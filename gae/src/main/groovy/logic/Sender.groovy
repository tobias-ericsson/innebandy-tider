package logic

import domain.Game
import domain.Team
import groovy.json.JsonBuilder

/**
 * Created by Tobias on 2014-03-23.
 */
class Sender {

    static void sendGames(List<Game> games) {
        send(games, domain.Properties.TARGET_URL + domain.Properties.TARGET_GAMES_PATH);
    }

    static void sendTeams(List<Team> teams) {
        send(teams, domain.Properties.TARGET_URL + domain.Properties.TARGET_TEAMS_PATH);
    }

    private static void send(List games,String targetUrl) {
        games.eachWithIndex { game, i ->
            try {
                def json = new JsonBuilder(game)
                //def result1 = json {games}
                //String key = game.id.replaceAll('"', "") - "game(" - ")"

                //println json.toString()
                String jsonString = '{"doc":' + json + ', "doc_as_upsert":true}'

                if (game.id) {
                    //println "start"
                    String urlString = targetUrl + game.id
                    println "sending to "+urlString
                    def url = new URL(urlString)
                    def connection = url.openConnection()
                    connection.setRequestProperty("Content-type", "application/json")
                    connection.doOutput = true
                    Writer writer = new OutputStreamWriter(connection.outputStream)
                    writer.write(jsonString)
                    writer.flush()
                    writer.close()
                    connection.connect()
                    println "result is "+connection.content.text
                }
            } catch (Exception e) {
                println e.getClass().getName()+" "+e.getMessage()+" index: "+i+" id: "+game.id
            }

        }
    }
}
