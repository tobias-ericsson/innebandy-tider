package parser

/**
 * Created by Tobias on 2014-03-23.
 */
class SportResultsXMLParserTest extends GroovyTestCase {
    void testParseGames() {
        println 'testParseGames start'
        String text = domain.Properties.RESOURCE_SCHEDULE_URL.toURL().getText("windows-1252")
        def games = SportResultsXMLParser.parseGames(text)
        /*
        games.each {
            println it
        }*/
        println 'testParseGames stop'
    }
}