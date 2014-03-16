package domain

/**
 * Created by Tobias on 2014-03-16.
 */
class Team {
    String name, league
    int win, loss, draw, goalsAgainst, goalsFor, gamesPlayed, points

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", league='" + league + '\'' +
                ", win=" + win +
                ", loss=" + loss +
                ", draw=" + draw +
                ", goalsAgainst=" + goalsAgainst +
                ", goalsFor=" + goalsFor +
                ", gamesPlayed=" + gamesPlayed +
                ", points=" + points +
                '}';
    }
}
