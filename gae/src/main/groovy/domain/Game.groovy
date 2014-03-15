package domain

public class Game {
	String key;
    String division;
    String homeTeam;
    String awayTeam;
    Integer homeGoals;
    String awayGoals;
    String time;
    String date;
    String score;
    Boolean shoutOut;

    @Override
    public String toString() {
        return "Game{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeGoals='" + homeGoals + '\'' +
                ", awayGoals='" + awayGoals + '\'' +
                ", date='" + date + '\'' +
                ", score='" + score + '\'' +
                ", shoutOut=" + shoutOut +
                '}\n';
    }
}