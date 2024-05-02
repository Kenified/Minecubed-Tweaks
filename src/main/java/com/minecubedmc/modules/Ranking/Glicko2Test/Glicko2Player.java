//package com.minecubedmc.util.Ranking.Glicko2Test;
//
//import com.minecubedmc.util.Ranking.GlickoRanking;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.UUID;
//
//public class Glicko2Player implements Serializable {
//    private final UUID uuid;
//    private final ArrayList<Glicko2Match> matches;
//    private double rating;
//    private double ratingDeviation;
//    private double volatility;
//    private double ratingHolder;
//    private double ratingDeviationHolder;
//    private double volatilityHolder;
//
//    public Glicko2Player() {
//        this.uuid = null;
//        matches = new ArrayList<>();
//        this.rating = Glicko2Ranking.DEFAULT_RATING;
//        this.ratingDeviation = Glicko2Ranking.DEFAULT_RATING_DEVIATION;
//        this.volatility = Glicko2Ranking.DEFAULT_VOLATILITY;
//    }
//
//    public Glicko2Player(UUID uuid) {
//        this.uuid = uuid;
//        matches = new ArrayList<>();
//        this.rating = Glicko2Ranking.DEFAULT_RATING;
//        this.ratingDeviation = Glicko2Ranking.DEFAULT_RATING_DEVIATION;
//        this.volatility = Glicko2Ranking.DEFAULT_VOLATILITY;
//    }
//
//    public UUID getUuid() {
//        return uuid;
//    }
//
//    public ArrayList<Glicko2Match> getMatches() {
//        return matches;
//    }
//
//    public void addMatchWon(Glicko2Player opponent, double playerHealth, double playerMaxHealth) {
//        this.matches.add(new Glicko2Match(opponent, (0.85 + 0.15 * (playerHealth / playerMaxHealth) )));
//    }
//
//    public void addMatchLoss(Glicko2Player opponent) {
//        this.matches.add(new Glicko2Match(opponent, 0));
//    }
//
//    public void resetMatches() {
//        this.matches.clear();
//    }
//
//    public int getMatchesPlayed() {
//        return this.matches.size();
//    }
//
//    public int getMatchesWon() {
//        return (int) matches.stream().parallel()
//                .filter(match -> match.result() > 0)
//                .count();
//    }
//
//    public int getMatchesLost() {
//        return getMatchesPlayed() - getMatchesWon();
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public void setRating(double rating) {
//        this.ratingHolder = rating;
//    }
//
//    public double getRatingDeviation() {
//        return ratingDeviation;
//    }
//
//    public void setRatingDeviation(double ratingDeviation) {
//        this.ratingDeviationHolder = ratingDeviation;
//    }
//
//    public double getVolatility() {
//        return volatility;
//    }
//
//    public void setVolatility(double volatility) {
//        this.volatilityHolder = volatility;
//    }
//
//    public void updateRating() {
//        this.rating = this.ratingHolder;
//        this.ratingDeviation = this.ratingDeviationHolder;
//        this.volatility = this.volatilityHolder;
//    }
//
//
//
//}
