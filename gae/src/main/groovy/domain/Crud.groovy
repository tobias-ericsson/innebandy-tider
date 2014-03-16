package domain

import com.google.appengine.api.datastore.*
import parser.Util

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit

/**
 * Created by Tobias on 2014-03-16.
 */
public abstract class Crud {

    static void saveGame(Game game) {
        Entity entity = new Entity("game", game.key)
        entity.year = game.year
        entity.date = game.date
        entity.time = game.time
        entity.homeTeam = game.homeTeam
        entity.awayTeam = game.awayTeam
        entity.homeGoals = game.homeGoals
        entity.awayGoals = game.awayGoals
        entity.shoutOut = game.shoutOut
        entity.league = game.league
        entity.save()
    }

    static List<Game> readAllPlayedGamesForLeagueYear(String league,Integer year) {
        // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()
        def query = new Query("game")

        Query.FilterPredicate filter = new Query.FilterPredicate("league",Query.FilterOperator.EQUAL,league)
        query.setFilter(filter)
        filter = new Query.FilterPredicate("year",Query.FilterOperator.EQUAL,year)
        query.setFilter(filter)
        /*
        filter = new Query.FilterPredicate("date",Query.FilterOperator.LESS_THAN_OR_EQUAL, Util.today())
        query.setFilter(filter)*/

        PreparedQuery preparedQuery = datastore.prepare(query)
        def entities = preparedQuery.asList(withLimit(100))

        List<Game> gameList = []
        entities.each {
            gameList.add(mapEntityToGame(it))
        }
        return gameList
    }

    static List<Game> readAllGames() {
        // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()

        // query the scripts stored in the datastore
// "savedscript" corresponds to the entity table containing the scripts' text

        def query = new Query("game")
// sort results by descending order of the creation date
        //query.addSort("dateCreated", Query.SortDirection.DESCENDING)

// filters the entities so as to return only scripts by a certain author
        //query.addFilter("author", Query.FilterOperator.EQUAL, params.author)

        PreparedQuery preparedQuery = datastore.prepare(query)

// return only the first 10 results
        def entities = preparedQuery.asList(withLimit(100))

        List<Game> gameList = []
        entities.each {
            gameList.add(mapEntityToGame(it))
        }

        return gameList
    }

    private static Game mapEntityToGame(Entity game) {
        Game g = new Game()
        g.key = game.key
        g.year = game.year
        g.date = game.date
        g.time = game.time
        g.homeTeam = game.homeTeam
        g.awayTeam = game.awayTeam
        g.homeGoals = game.homeGoals
        if (game.awayGoals instanceof String) {
            //todo fix
            g.awayGoals = 99
        } else {
            g.awayGoals = game.awayGoals
        }
        g.shoutOut = game.shoutOut
        g.league = game.league
        return g
    }
}
