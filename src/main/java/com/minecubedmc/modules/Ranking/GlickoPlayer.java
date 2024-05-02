package com.minecubedmc.modules.Ranking;

import java.io.Serializable;
import java.util.ArrayList;

public class GlickoPlayer implements Serializable {
    private long gamesWon;
    private long gamesLost;
    private final ArrayList<Match> matches;
    private double rating;
    private double ratingDeviation;

    // Temporarily hold the new rating and rating deviation until all players' ratings are calculated and rating period is over
    private double ratingHolder;
    private double ratingDeviationHolder;

    public GlickoPlayer() {
        matches = new ArrayList<>();
        this.rating = -1;
        this.ratingDeviation = -1;
        this.ratingHolder = GlickoRanking.DEFAULT_RATING;
        this.ratingDeviationHolder = GlickoRanking.DEFAULT_RATING_DEVIATION;

    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void addMatchWon(GlickoPlayer opponent, double playerHealth, double playerMaxHealth) {
        addMatchWon(opponent, (0.90 + 0.10 * (playerHealth / playerMaxHealth) ));
    }

    public void addMatchWon(GlickoPlayer opponent, double score) {
        this.gamesWon++;
        this.matches.add(new Match(opponent, score));
        // Only increment won games since you cannot only play a game and lose
        GlickoRanking.incrementGamesInRatingPeriod();
    }

    public void addMatchLoss(GlickoPlayer opponent) {
        this.gamesLost++;
        this.matches.add(new Match(opponent, 0));
    }

    public void resetMatches() {
        this.matches.clear();
    }

    public long getMatchesPlayed() {
        return getMatchesWon() + getMatchesLost();
    }

    public long getMatchesWon() {
        return gamesWon;
    }

    public long getMatchesLost() {
        return gamesLost;
    }

    public double getRating() {
        return rating;
    }

    public void setNewRating(double rating) {
        this.ratingHolder = rating;
    }

    public double getRatingDeviation() {
        return ratingDeviation;
    }

    public void setNewRatingDeviation(double ratingDeviation) {
        this.ratingDeviationHolder = ratingDeviation;
    }

    public void updateRating() {
        this.rating = this.ratingHolder;
        this.ratingDeviation = this.ratingDeviationHolder;
    }
}
