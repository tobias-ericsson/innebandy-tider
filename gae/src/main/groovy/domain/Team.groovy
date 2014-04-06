package domain

/**
 * Created by Tobias on 2014-03-16.
 */
class Team {
    String fullName, shortName, league
    int win, loss, draw, goalsAgainst, goalsFor, gamesPlayed, points

    @Override
    public String toString() {
        return "Team{" +
                "fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
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
