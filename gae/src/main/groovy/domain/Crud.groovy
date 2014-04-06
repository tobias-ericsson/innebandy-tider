package domain

import com.google.appengine.api.datastore.*
import groovyx.gaelyk.logging.GroovyLogger
import parser.Util

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit

/**
 * Created by Tobias on 2014-03-16.
 */
public abstract class Crud {

    static def log = new GroovyLogger(Crud.simpleName)
    static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()

    static Game updateTeams(Game game) {
        List<Team> teams = readAllTeamsForLeagueYear(game.league, game.year)
        def homeTeam = findOrUpdateTeamByName(game.homeTeam,teams)
        def awayTeam = findOrUpdateTeamByName(game.awayTeam,teams)
        game.homeTeam = homeTeam.fullName
        game.awayTeam = awayTeam.fullName
        game.homeTeamShort = homeTeam.shortName
        game.awayTeamShort = awayTeam.shortName
        String id = game.league + "-" + game.date + "-" + game.homeTeam + "-" + game.awayTeam
        game.id = URLEncoder.encode(id.replaceAll(" ", ""), "UTF-8")
        return game
    }

    static def findOrUpdateTeamByName(String teamName, def teams) {
        Team team = teams.find({ it.fullName.equalsIgnoreCase(teamName) })
        if (team) {
            return team;
        }
        team = teams.find({ it.shortName.equalsIgnoreCase(teamName) })
        if (team) {
            return team;
        }
        team = teams.find({ it.fullName.contains(teamName) })
        if (team) {
            team.shortName = teamName
            saveTeam(team);
            return team;
        }
        team = teams.find({ teamName.contains(it.fullName) })
        if (team) {
            deleteTeam(team)
            team.fullName = teamName
            saveTeam(team)
            return team;
        }
        team = new Team(fullName: teamName, shortName: teamName)
        saveTeam(team);
        return team;
    }

    static void deleteTeam(Team team) {
        Entity entity = datastore.get("team", team.fullName)
        log.info("delete team " +team.fullName)
        entity.delete()
    }

    static void saveTeam(Team team) {
        Entity entity = new Entity("team", team.fullName)
        entity.fullName = team.fullName
        entity.shortName = team.shortName
        log.info("save team " +team.fullName)
        entity.save()
    }

    static void saveGame(Game game) {
        game = updateTeams(game)
        String today = Util.today()
        Entity entity
        try {
            entity = datastore.get("game", game.id)
        } catch (EntityNotFoundException e) {
            log.info(e.getMessage() + ", will create new...")
            entity = new Entity("game", game.id)
            entity.id = game.id
            entity.year = game.year
            entity.date = game.date
            entity.homeTeam = game.homeTeam
            entity.awayTeam = game.awayTeam
            entity.homeTeamShort = game.homeTeamShort
            entity.awayTeamShort = game.awayTeamShort
            entity.league = game.league
            entity.updated = today
        }

        if (shouldUpdate(entity.time, game.time)) {
            entity.time = game.time
            entity.updated = today
        }

        if (shouldUpdate(entity.homeGoals, game.homeGoals)) {
            entity.homeGoals = game.homeGoals
            entity.updated = today
        }

        if (shouldUpdate(entity.awayGoals, game.awayGoals)) {
            entity.awayGoals = game.awayGoals
            entity.updated = today
        }

        if (shouldUpdate(entity.shoutOut, game.shoutOut)) {
            entity.shoutOut = game.shoutOut
            entity.updated = today
        }

        if (today.equals(entity.updated)) {
            log.info("saving " + entity.id + " : " + entity.updated)
            entity.save()
        }
    }

    static boolean shouldUpdate(oldParam, newParam) {
        if (newParam) {
            if (newParam != oldParam) {
                return true
            }
        }
        return false
    }

    static List<Team> readAllTeamsForLeagueYear(String league, int year) {
        // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()

        //Query.FilterPredicate leagueFilter = new Query.FilterPredicate("league", Query.FilterOperator.EQUAL, league)
        //Query.FilterPredicate yearFilter = new Query.FilterPredicate("year", Query.FilterOperator.EQUAL, year)
        //Query.Filter compositeFilter = Query.CompositeFilterOperator.and(leagueFilter, yearFilter)
        def query = new Query("team") //.setFilter(compositeFilter)

        /*
        filter = new Query.FilterPredicate("date",Query.FilterOperator.LESS_THAN_OR_EQUAL, Util.today())
        query.setFilter(filter)*/

        PreparedQuery preparedQuery = datastore.prepare(query)
        def entities = preparedQuery.asList(withLimit(100))

        List<Team> teamList = []
        entities.each {
            log.info(it.toString())
            teamList.add(mapEntityToTeam(it))
        }
        return teamList

    }

    static List<Game> readAllPlayedGamesForLeagueYear(String league, int year) {

        Query.FilterPredicate leagueFilter = new Query.FilterPredicate("league", Query.FilterOperator.EQUAL, league)
        Query.FilterPredicate yearFilter = new Query.FilterPredicate("year", Query.FilterOperator.EQUAL, year)
        Query.FilterPredicate beforeTodayFilter = new Query.FilterPredicate("date",
                Query.FilterOperator.LESS_THAN_OR_EQUAL, Util.today())
        Query.Filter compositeFilter = Query.CompositeFilterOperator.and(leagueFilter, yearFilter, beforeTodayFilter)
        def query = new Query("game").setFilter(compositeFilter)

        /*
        filter = new Query.FilterPredicate("date",Query.FilterOperator.LESS_THAN_OR_EQUAL, Util.today())
        query.setFilter(filter)*/

        PreparedQuery preparedQuery = datastore.prepare(query)
        def entities = preparedQuery.asList(withLimit(500))

        List<Game> gameList = []
        entities.each {
            log.info(it.toString())
            gameList.add(mapEntityToGame(it))
        }
        return gameList
    }

    static List<Game> readAllGamesUpdatedAfter(String updatedDateTime) {
        return []
    }

    static List<Game> readAllGames() {

        // query the scripts stored in the datastore
// "savedscript" corresponds to the entity table containing the scripts' text

        def query = new Query("game")
// sort results by descending order of the creation date
        //query.addSort("dateCreated", Query.SortDirection.DESCENDING)

// filters the entities so as to return only scripts by a certain author
        //query.addFilter("author", Query.FilterOperator.EQUAL, params.author)

        PreparedQuery preparedQuery = datastore.prepare(query)

// return only the first 10 results
        def entities = preparedQuery.asList(withLimit(500))

        List<Game> gameList = []
        entities.each {
            gameList.add(mapEntityToGame(it))
        }

        return gameList
    }

    private static Game mapEntityToGame(Entity entity) {
        Game g = new Game()
        g.year = entity.year
        g.date = entity.date ? entity.date : "-"
        g.time = entity.time ? entity.time : "-"
        g.homeTeam = entity.homeTeam
        g.awayTeam = entity.awayTeam
        g.homeGoals = entity.homeGoals
        g.awayGoals = entity.awayGoals
        g.shoutOut = entity.shoutOut
        g.league = entity.league
        g.id = entity.id //? createId(g) : createId(g)
        return g
    }

    private static Team mapEntityToTeam(Entity entity) {
        Team t = new Team()
        t.fullName = entity.fullName
        t.shortName = entity.shortName
        t.league = entity.league
        return t
    }
}
