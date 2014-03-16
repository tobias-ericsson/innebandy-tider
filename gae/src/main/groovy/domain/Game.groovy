package domain

public class Game {
	String key;
    String league;
    String homeTeam;
    String awayTeam;
    Integer homeGoals;
    Integer awayGoals;
    String time;
    String date;
    Integer year;
    String score;
    Boolean shoutOut;


    @Override
    public String toString() {
        return "Game{" +
                "key='" + key + '\'' +
                ", league='" + league + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeGoals=" + homeGoals +
                ", awayGoals=" + awayGoals +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", year=" + year +
                ", score='" + score + '\'' +
                ", shoutOut=" + shoutOut +
                '}';
    }
}