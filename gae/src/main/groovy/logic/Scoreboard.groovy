package logic

import domain.Crud
import domain.Game
import domain.Team
import groovyx.gaelyk.logging.GroovyLogger

/**
 * Created by Tobias on 2014-03-16.
 */
class Scoreboard {

    static def log = new GroovyLogger(Scoreboard.simpleName)

    static Map<String, Team> teamStatsForLeagueYear(String league, Integer year) {
        log.info league
        def games = Crud.readAllPlayedGamesForLeagueYear(league, year)
        return teamStatsForGames(games)
    }

    static Map<String, Team> teamStatsForGames(def games) {
        Map<String, Team> teams = new HashMap<>()
        games.each { Game game ->
            Team homeTeam = teams.get(game.homeTeam, new Team(name: game.homeTeam, league: game.league))
            Team awayTeam = teams.get(game.awayTeam, new Team(name: game.awayTeam, league: game.league))
            int points = game.shoutOut ? 2 : 3
            if (game.homeGoals > game.awayGoals) {
                homeTeam.win += 1
                awayTeam.loss += 1
                homeTeam.points += points
            } else {
                homeTeam.loss += 1
                awayTeam.win += 1
                awayTeam.points += points
            }
            homeTeam.gamesPlayed += 1
            awayTeam.gamesPlayed += 1
            homeTeam.goalsFor += game.homeGoals
            homeTeam.goalsAgainst += game.awayGoals
            awayTeam.goalsFor += game.awayGoals
            awayTeam.goalsAgainst += game.homeGoals

            teams.put(homeTeam.name, homeTeam)
            teams.put(awayTeam.name, awayTeam)


        }
        return teams
    }


}
