package com.minecubedmc.modules.Ranking;
import com.minecubedmc.Tweaks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GlickoRanking {

    private static final ConcurrentHashMap<UUID, GlickoPlayer> rankedPlayers = new ConcurrentHashMap<>();
    private static long gamesInRatingPeriod = 0;
    public static final double Q = Math.log(10) / 400.0;    // q - conversion factor
    public static final double DEFAULT_RATING = 1500;                       // μ - base rating
    public static final double DEFAULT_RATING_DEVIATION = 350;              // φ - base rating deviation
    public static final double TAU = 30;                                    // τ - const

    public static boolean hasPlayer(Player player) {
        return rankedPlayers.containsKey(player.getUniqueId());
    }

    // Add player to map
    public static void addPlayer(Player player) {
        rankedPlayers.put(player.getUniqueId(), new GlickoPlayer());
    }

    // Get player from map
    public static GlickoPlayer getPlayer(Player player) {
        return rankedPlayers.get(player.getUniqueId());
    }

    // Get number of games played in rating period
    public static long getGamesInRatingPeriod() {
        return gamesInRatingPeriod;
    }

    // Increment number of games played in rating period
    public static void incrementGamesInRatingPeriod() {
        gamesInRatingPeriod++;
    }

    // Reset number of games played in rating period
    private static void resetGamesInRatingPeriod() {
        gamesInRatingPeriod = 0;
    }

    // Determine rating deviation for new rating period
    private static double calculateNewRatingDeviation(double oldRatingDeviation) {
        return Math.min(Math.sqrt(oldRatingDeviation * oldRatingDeviation + GlickoRanking.TAU * GlickoRanking.TAU), GlickoRanking.DEFAULT_RATING_DEVIATION);
    }

    // Calculate rating deviation impact g(RD) = 1 / sqrt(1 + 3 * q^2 * (RD^2) / pi^2)
    private static double ratingDeviationImpact(double ratingDeviation) {
        return 1 / Math.sqrt( 1 + 3 * (GlickoRanking.Q * GlickoRanking.Q) * (ratingDeviation * ratingDeviation) / (Math.PI * Math.PI));
    }

    // Calculate expected match outcome E = 1 / (1 + 10^(-g(RDj) * (Ri - Rj) / 400))
    private static double expectedOutcome(double rating, double opponentRating, double opponentRatingDeviation) {
        return 1 / (1 + Math.pow(10, -ratingDeviationImpact(opponentRatingDeviation) * (rating - opponentRating) / 400));
    }


    // Calculate new player rating based on rating period
    private static void calculateNewPlayerRating(GlickoPlayer player) {
        // Step 1: Determine rating deviation for new rating period
        if (player.getRatingDeviation() == -1){
            player.setNewRatingDeviation(GlickoRanking.DEFAULT_RATING_DEVIATION);
        }
        else {
            player.setNewRatingDeviation(calculateNewRatingDeviation(player.getRatingDeviation()));
        }
        player.updateRating();

        // Step 2: Calculate the new rating and rating deviation
        double sumNumerator = 0.0;
        double sumDenominator = 0.0;
        for (Match match : player.getMatches()) {
            double s = match.result();
            double gRDj = ratingDeviationImpact(match.opponent().getRatingDeviation());
            double E = expectedOutcome(player.getRating(), match.opponent().getRating(), match.opponent().getRatingDeviation());

            sumNumerator += gRDj * (s - E);
            sumDenominator += Math.pow(gRDj, 2) * E * (1 - E);
        }

        double d2 = Math.pow(Q * Q * sumDenominator, -1);
        double postPeriodRating = player.getRating() + (Q / ( 1/(player.getRatingDeviation()*player.getRatingDeviation()) + 1/d2 )) * sumNumerator;
        double postPeriodRatingDeviation = 1 / Math.sqrt(1 / Math.pow(player.getRatingDeviation(), 2) + 1 / d2);

        // Set the new rating and rating deviation to temporary holders
        player.setNewRating(postPeriodRating);
        player.setNewRatingDeviation(postPeriodRatingDeviation);
    }

    public static void endRatingPeriod() {
        // Calculate the new rating and rating deviation for each player
        for (GlickoPlayer player : rankedPlayers.values()) {
            calculateNewPlayerRating(player);
        }

        // Once all players have been calculated, update their ratings
        for (GlickoPlayer player : rankedPlayers.values()) {
            player.updateRating();
            player.resetMatches();
            resetGamesInRatingPeriod();
        }

        // TODO: Remove testing code
        // Print out all players' ratings
        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        rankedPlayers.forEach((uuid, player) -> {
            plugin.getLogger().info("Player: " + Bukkit.getPlayer(uuid).getName() + " Rating: " + player.getRating() + " Rating Deviation: " + player.getRatingDeviation());
        });
    }
}
