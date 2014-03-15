import domain.Game
import com.google.appengine.api.datastore.Entity

log.info("parseScoreboard")

String urlString = "http://www.m-ligan.net/data/M1v14sd.txt"
def games = []
String date = "";
String division = "M1";
String year = "2014";

public static String maybeAddZero(String datePart) {
    if (datePart?.length() < 2) {
        return "0" + datePart
    }
    return datePart
}

String text = urlString.toURL().getText("ISO-8859-1")
text.eachLine { line ->
    try {
        if (line.size() > 2) {
            if (line.contains("/")) {
                //we have a date
                def lineSplitted = line.split("/");
                String day = lineSplitted[0].substring(lineSplitted[0].size() - 2).trim();
                //println day;
                String month = lineSplitted[1].trim();
                //println month;
                date = year + "-" + maybeAddZero(month) + "-" + maybeAddZero(day);

            } else {
                def lineSplitted = line.split(" - ");
                if (lineSplitted.size() > 1) {
                    Game game = new Game()
                    game.homeTeam = lineSplitted[0].trim()
                    def regExp = /\d*-\d*.*/
                    def matcher = (lineSplitted[1] =~ regExp)

                    if (matcher.find()) {
                        //println matcher.group()

                        def goals = matcher.group().split("-")

                        game.score = matcher.group()
                        game.date = date
                        game.homeTeam = lineSplitted[0].trim();
                        game.awayTeam = lineSplitted[1].substring(0, matcher.start()).trim()
                        game.homeGoals = Integer.parseInt(goals[0])
                        game.awayGoals = Integer.parseInt(goals[1].replace("sd", "").trim())
                        game.shoutOut = goals[1].contains("sd")
                        game.division = division
                        game.key = game.division + "-" + game.date + "-" + game.homeTeam + "-" + game.awayTeam
                        games << game
                    }
                    //println line
                }
            }
        }
    } catch (Exception e) {
        println e.getClass().getSimpleName() + e.getMessage()
        e.printStackTrace()
    }
}

games.each { game ->
    println game.toString() + "<br/>"
    Entity entity = new Entity("game", game.key)
    //entity << game
    entity.key = game.key
    entity.date = game.date
    entity.homeTeam = game.homeTeam
    entity.awayTeam = game.awayTeam
    entity.homeGoals = game.homeGoals
    entity.awayGoals = game.awayGoals
    entity.shoutOut = game.shoutOut
    entity.division = game.division
    entity.save()
}
println "<p>total: " + games.size() + "</p>"