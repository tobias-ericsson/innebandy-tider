package domain

import com.google.appengine.api.datastore.*

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit

/**
 * Created by Tobias on 2014-03-16.
 */
public abstract class Crud {

    static void saveGame(Game game) {
        Entity entity = new Entity("game", game.key)
        entity.date = game.date
        entity.time = game.time
        entity.homeTeam = game.homeTeam
        entity.awayTeam = game.awayTeam
        entity.homeGoals = game.homeGoals
        entity.awayGoals = game.awayGoals
        entity.shoutOut = game.shoutOut
        entity.division = game.division
        entity.save()
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
        g.date = game.date
        g.time = game.time
        g.homeTeam = game.homeTeam
        g.awayTeam = game.awayTeam
        g.homeGoals = game.homeGoals
        g.awayGoals = game.awayGoals
        g.shoutOut = game.shoutOut
        g.division = game.division
        return g
    }
}
