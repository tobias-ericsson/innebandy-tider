package logic

import domain.Game
import domain.Team

/**
 * Created by Tobias on 2014-03-16.
 */
class ScoreboardTest extends GroovyTestCase {

    void testTeamStatsForGames() {

        Game game1 = new Game(homeTeam: "Norrlands Guld",awayTeam: "Bitmine",awayGoals: 3,homeGoals: 2)
        Game game2 = new Game(homeTeam: "Vejby",awayTeam: "Norrlands Guld",awayGoals: 5,homeGoals: 1)
        Game game3 = new Game(homeTeam: "Snakebite",awayTeam: "Norrlands Guld",awayGoals: 1,homeGoals: 0, shoutOut: true)
        List<Game> games = [game1,game2,game3]

        def teams = Scoreboard.teamStatsForGames(games)

        Team norrlandsGuld = teams.get("Norrlands Guld")

        assert norrlandsGuld.goalsFor == 8
        assert norrlandsGuld.points == 5
        assert norrlandsGuld.goalsAgainst == 4
        assert norrlandsGuld.gamesPlayed == 3
        assert norrlandsGuld.win == 2
        assert norrlandsGuld.loss == 1

        teams.each {
            println it
        }
    }
}
