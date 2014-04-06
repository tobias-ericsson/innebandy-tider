package domain
//import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
//import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * Created by Tobias on 2014-04-06.
 */
class CrudTest extends GroovyTestCase {

    // private final LocalServiceTestHelper helper =
    //      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());


    void testFindTeamByNameNoTeams() {
        def teams = []
        Crud.metaClass.'static'.saveTeam = { Team team -> println "saveTeam " + team }
        Team team = Crud.findOrUpdateTeamByName("fullName", teams)
        assert team.fullName == "fullName"
        assert team.shortName == "fullName"
        println team;
    }

    void testFindTeamByName() {
        Crud.metaClass.'static'.saveTeam = { Team team -> println "saveTeam " + team }
        Crud.metaClass.'static'.deleteTeam = { Team team -> println "deleteTeam " + team }
        def teams = [new Team(fullName: "Norrlands Guld", shortName: "Norrlands Guld"),
                new Team(fullName: "Bitmine Dwarfs", shortName: "Bitmine D"),
                new Team(fullName: "Vejbys", shortName: "Vejbys")]

        def team = Crud.findOrUpdateTeamByName("fullName", teams)
        assert team.fullName == "fullName"
        assert team.shortName == "fullName"
        println team;

        team = Crud.findOrUpdateTeamByName("Bitmine D", teams)
        assert team.fullName == "Bitmine Dwarfs"
        assert team.shortName == "Bitmine D"
        println team;

        team = Crud.findOrUpdateTeamByName("Norrlands G", teams)
        assert team.fullName == "Norrlands Guld"
        assert team.shortName == "Norrlands G"
        println team;

        team = Crud.findOrUpdateTeamByName("Vejbys Ängar", teams)
        assert team.fullName == "Vejbys Ängar"
        assert team.shortName == "Vejbys"
        println team;
    }
}
