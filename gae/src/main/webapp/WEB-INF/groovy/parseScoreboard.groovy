import domain.Crud
import domain.Game

String name = "Parse Scoreboard 2"
String fileName = params.get("file", "nothing");
String league = fileName.substring(0, 2);
log.info(name + " " + league + " " + fileName)

try {

    String urlString = domain.Properties.RESOURCE_URL + fileName
    def games = []
    String date = "";
    Integer year = 2014;
    log.info("url: "+urlString)
    String text = urlString.toURL().getText("ISO-8859-1")
    log.info("text: "+text)
    text.eachLine { String line ->
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


                        def goals = matcher.group().split("-")

                        game.score = matcher.group()
                        game.date = date
                        game.homeTeam = lineSplitted[0].trim();
                        game.awayTeam = lineSplitted[1].substring(0, matcher.start()).trim()
                        game.homeGoals = Integer.parseInt(goals[0])
                        game.awayGoals = Integer.parseInt(goals[1].replace("sd", "").trim())
                        game.shoutOut = goals[1].contains("sd")
                        game.league = league
                        game.year = year
                        //game.id = game.league + "-" + game.date + "-" + game.homeTeam + "-" + game.awayTeam
                        games << game
                    }
                    //println line
                }
            }
        }
    }

    games.each { Game game ->
        Crud.saveGame(game)
    }

    request.setAttribute 'header', name
    request.setAttribute 'games', games
    forward '/WEB-INF/pages/cronresult.gtpl'

} catch (Exception e) {
    log.info(e.getClass().getSimpleName() + e.getMessage())
    println "OHH NOOO!!" + e.message
    e.printStackTrace()
}

public static String maybeAddZero(String datePart) {
    if (datePart?.length() < 2) {
        return "0" + datePart
    }
    return datePart
}