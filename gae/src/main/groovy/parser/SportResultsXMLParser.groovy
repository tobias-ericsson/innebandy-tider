package parser

import domain.Game
import groovy.json.JsonBuilder
import groovy.util.slurpersupport.GPathResult
import org.xml.sax.SAXParseException
import org.ccil.cowan.tagsoup.*

/**
 *
 * Created by Tobias Ericsson
 */
class SportResultsXMLParser {

    static def parseGames(String text) {
        try {
            def rootNode = new XmlSlurper(new Parser()).parseText(text)
            return parseGames(rootNode)
        } catch (SAXParseException e) {
            println "Error in XML file: " + e.class.simpleName + ": " + e.message
            System.exit(1)
        }
    }

    static def parseGames(File xmlFile) {
        String text = xmlFile.getText("windows-1252")
        parseGames(text)
    }

    static def parseGames(GPathResult rootNode) {
        //println rootNode
        def allTables = rootNode.depthFirst().findAll { it.name().toString().toLowerCase().equals('table') }
        //println allTables

        Map teams = [:]
        String currentDate = '-'
        Map headers = [:]
        List games = []
        int maybeOne = 0
        allTables.eachWithIndex { table, tableIndex ->
            //todo investigate removed tbody here
            table.tr.eachWithIndex { tr, trIndex ->
                if (trIndex == 0) {
                    println '*********************'
                }
                Game game = new Game()
                tr.td.eachWithIndex { td, tdIndex ->
                    String s = td.toString().replaceAll("&nbsp;", " ").replaceAll("\u00a0", "").trim()

                    if (!s) {
                        maybeOne = 1
                    }

                    if (tableIndex < 2 && s.size() > 2 && tdIndex % 2 == 0) {
                        def number = s.substring(0, 2)
                        def name = s.substring(3)
                        teams.put(number, name)
                    }
                    if (tableIndex > 1) {
                        td.p.font.b.each { it ->
                            String date = it.toString().replaceAll("&nbsp;", " ").replaceAll("\u00a0", "").trim().find(~/.+\d+-\d+-\d+/)
                            if (date) {
                                currentDate = date

                                println "date " + date
                            }

                        }
                    }
                    if (trIndex == 1) {
                        headers.put(tdIndex, s)
                    } else if (trIndex > 1) {

                        String header = headers.get(tdIndex - maybeOne)

                        switch (header) {
                            case "Klockan":
                                if (game.time) {
                                    games << game
                                    game = new Game()
                                }
                                game.date = currentDate
                                game.year = Integer.parseInt(currentDate.substring(0, 4))
                                game.time = s
                                break
                            case "Bana":
                                game.place = s
                                break
                            case "Serie":
                                game.league = s
                                break
                            case "Hemma":
                                game.homeTeam = teams.get(s) ?: s
                                break
                            case "Borta":
                                game.awayTeam = teams.get(s) ?: s
                                if (game.time) {
                                    games << game
                                    game = new Game()
                                }
                                break
                            default: ""
                        //print "default |" + s + "*" + tdIndex
                        //println "|"

                        }
                    }
                }

                if (trIndex == 0) {
                    println '*********************'

                }
                maybeOne = 0
                //println "headers: " + headers
                //todo not useful?
                if (game.time) {
                    games << game
                }
            }


        }
        println "teams " + teams

        println "games " + games

        //def json = new JsonBuilder(["games": games])
        //def result1 = json {games}
        //println json.toPrettyString()
        return games
    }

    /**
     * matchRows will eventually contain one row for each match according to:
     * Sport | Category | Tournament | Team1Name - Team2Name : Team1Score - Team2Score
     * topList will eventually contain a list with the most common starting
     * letter in the goal scoring teams name.
     * @param rootNode
     * @return [matchRows , topList]
     */
    static def generateMatchRowsAndTopList(GPathResult rootNode) {

        def allMatches = rootNode.Sport.Category.Tournament.Match
        def matchRows = [:]
        def topList = [:]

        allMatches.each { match ->
            def score = match.Scores.Score
            //If “Score” is missing - do not use them i.e the game has not yet started.
            if (score.size() > 0) {
                //The data files sometimes contains several “Score” elements use “current” one.
                def currentScore = match.Scores.Score.find { it.@type.toString().toLowerCase().equals('current') }
                //The data files contains both Swedish and English names – use the English ones.
                String team1Name = match.Team1.Name.find { it.@language == 'en' }.toString()
                String team2Name = match.Team2.Name.find { it.@language == 'en' }.toString()
                try {
                    Integer team1Score = Integer.parseInt(currentScore.Team1.toString())
                    populateTopList(topList, team1Name, team1Score)

                    Integer team2Score = Integer.parseInt(currentScore.Team2.toString())
                    populateTopList(topList, team2Name, team2Score)

                    String scoreString = team1Score + " - " + team2Score
                    Integer totalScore = team1Score + team2Score

                    String rightPartOfMatchRow = team1Name + " - " + team2Name + " : " + scoreString
                    String matchRow = createMatchRowByUsingParents(match, rightPartOfMatchRow)

                    matchRows.put(matchRow, totalScore)
                } catch (NumberFormatException e) {
                    println "Invalid score: " + e.class.simpleName + ": " + e.message
                }
            }
        }
        return [matchRows, topList]
    }

    private static populateTopList(topList, String teamName, Integer teamScore) {
        if (teamScore > 0) {
            def letter = teamName.getAt(0)
            Integer currentValue = topList.get(letter)
            if (currentValue) {
                currentValue = currentValue + teamScore
            } else {
                currentValue = teamScore
            }
            topList.put(letter, currentValue)
        }
    }

    /**
     * Creates a match row by starting with a "match node" child
     * and recursively traversing the parents and adding their names before the
     * "partOfMatchRow" start string.
     * The root node should be LivescoreData but in case it isn't it will return
     * when parent.name() == child.name(), which can happen because the parent of
     * a root node is (also) the root node.
     * @param child
     * @param partOfMatchRow
     * @return matchRow
     */
    protected static String createMatchRowByUsingParents(child, String partOfMatchRow) {
        def parent = child.parent()
        if (parent.name() == child.name() || parent.name().toLowerCase() == 'livescoredata') {
            return partOfMatchRow
        } else {
            def parentName = parent.Name.find { it.@language == 'en' }
            if (!parentName) {
                parentName = parent.name()
            }
            partOfMatchRow = parentName.toString() + " | " + partOfMatchRow
            return createMatchRowByUsingParents(parent, partOfMatchRow)
        }
    }
}