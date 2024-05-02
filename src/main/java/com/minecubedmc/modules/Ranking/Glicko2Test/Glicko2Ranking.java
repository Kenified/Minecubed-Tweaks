//package com.minecubedmc.util.Ranking.Glicko2Test;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.Bukkit;
//
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//
//public class Glicko2Ranking {
//
//    private static ConcurrentHashMap<UUID, Glicko2Player> rankedPlayers;
//    private static final double CONVERSION_FACTOR = Math.log(10.0f) / 400;  // q         - conversion factor
//    public static final double DEFAULT_RATING = 1500.0d;                    // μ (mu)    - rating
//    public static final double DEFAULT_RATING_DEVIATION = 350.0d;           // φ (phi)   - rating deviation
//    public static final double DEFAULT_VOLATILITY = 0.06d;                  // σ (sigma) - volatility
//    public static final double TAU = 0.5d;
//    public static final double EPSILON = 0.000001d; // termination condition
//
//    public Glicko2Ranking() {
//        if (rankedPlayers == null) {
//            rankedPlayers = new ConcurrentHashMap<>();
//        }
//    }
//
//    public static void addPlayer(UUID uuid) {
//        rankedPlayers.put(uuid, new Glicko2Player());
//    }
//
//    public static Glicko2Player getPlayer(UUID uuid) {
//        return rankedPlayers.get(uuid);
//    }
//
//    // Scale rating to Glicko-2 scale (μ)
//
//    // Calculate rating deviation impact g(φ)
//    private static double ratingDeviationImpact(double ratingDeviation) {
//        return 1 / Math.sqrt(1 + 3 * Math.pow(CONVERSION_FACTOR * ratingDeviation, 2) * Math.pow(ratingDeviation, 2) / Math.pow(Math.PI, 2));
//    }
//
//    // Calculate expected match outcome E
//    private static double expectedOutcome(double rating, double opponentRating, double opponentRatingDeviation) {
//        return 1 / (1 + Math.pow(10, -ratingDeviationImpact(opponentRatingDeviation) * (rating - opponentRating) / 400));
//    }
//
//    // Calculate variance v
//    private static double calculateVariance(Glicko2Player player){
//        double sum = 0.0d;
//
//        for (Glicko2Match match : player.getMatches()) {
//            double g = Math.pow(ratingDeviationImpact(match.opponent().getRatingDeviation()), 2);
//            double E = expectedOutcome(player.getRating(), match.opponent().getRating(), match.opponent().getRatingDeviation());
//
//            sum += g * E * (1 - E);
//        }
//
//        return 1 / sum;
//    }
//
//    // Calculate delta (Δ)
//    private static double calculateDelta(Glicko2Player player) {
//        double sum = 0.0d;
//
//        for (Glicko2Match match : player.getMatches()) {
//            double g = ratingDeviationImpact(match.opponent().getRatingDeviation());
//            double E = expectedOutcome(player.getRating(), match.opponent().getRating(), match.opponent().getRatingDeviation());
//
//            sum += g * (match.result() - E);
//        }
//
//        return calculateVariance(player) * sum;
//    }
//
//    public java.util.function.Function<Double, Double> getFunction(double delta, double v, double a) {
//        return (Double x) -> (Math.exp(x) * (Math.pow(delta, 2) - Math.pow(this.__rd, 2) - v - Math.exp(x)) / (2 * Math.pow(Math.pow(this.__rd, 2) + v + Math.exp(x), 2))) - ((x - a) / Math.pow(this._tau, 2));
//    }
//
//    // Calculate new volatility (σ') using Illinois algorithm
//    private static double calculateNewVolatility(Glicko2Player player) {
//        double delta = calculateDelta(player);
//        double a = Math.log(player.getVolatility() * player.getVolatility());
//        double A = a;
//        double B;
//        double fA;
//        double fB;
//
//        if (delta * delta > player.getRatingDeviation() * player.getRatingDeviation() + player.getVolatility() * player.getVolatility()) {
//            B = Math.log(delta * delta - player.getRatingDeviation() * player.getRatingDeviation() - player.getVolatility() * player.getVolatility());
//        } else {
//            int k = 1;
//
//            while (f(A - k * TAU, player) < 0) {
//                k++;
//            }
//
//            B = A - k * TAU;
//        }
//
//        fA = f(A, player);
//        fB = f(B, player);
//
//        while (Math.abs(B - A) > EPSILON) {
//            double C = A + (A - B) * fA / (fB - fA);
//            double fC = f(C, player);
//
//            if (fC * fB < 0) {
//                A = B;
//                fA = fB;
//            } else {
//                fA /= 2;
//            }
//
//            B = C;
//            fB = fC;
//        }
//
//        return Math.exp(A / 2);
//    }
//
////    private static double calculateNewVolatility(Glicko2Player player, double d2, double sumNumerator) {
////        double a = Math.log(player.getVolatility() * player.getVolatility());
////        double delta = sumNumerator * sumNumerator - player.getRatingDeviation() * player.getRatingDeviation() - d2;
////
////        double x0 = a; // initial guess
////        double x = a; // will hold the updated guess
////
////        // Newton-Raphson iteration
////        while (true) {
////            double dx = (Math.exp(x) * (delta - Math.exp(x) * (d2 + Math.exp(x))) - x + x0) /
////                    (2 * Math.exp(x) * (d2 + Math.exp(x)) - 1);
////            x -= dx;
////
////            if (Math.abs(dx) < EPSILON) {
////                break;
////            }
////        }
////
////        return Math.exp(x / 2);
////    }
//
//    // Calculate new player rating based on rating period
////    private static void calculateNewPlayerRating(Glicko2Player player) {
////        double q = Math.log(10) / 400.0;
////        double sumNumerator = 0.0;
////        double sumDenominator = 0.0;
////
////        for (Glicko2Match match : player.getMatches()) {
////            double gRDj = ratingDeviationImpact(match.opponent().getRatingDeviation());
////            double expectedResult = expectedOutcome(player.getRating(), match.opponent().getRating(), match.opponent().getRatingDeviation());
////
////            sumNumerator += gRDj * (match.result() - expectedResult);
////            sumDenominator += Math.pow(gRDj, 2) * expectedResult * (1 - expectedResult);
////        }
////
////        double d2 = 1 / sumDenominator;
////        double newRating = player.getRating() + q * sumNumerator / (1 / Math.pow(player.getRating(), 2) + 1 / d2);
////        double newVolatility = calculateNewVolatility(player, d2, sumNumerator);
////        double newRatingDeviation = 1 / Math.sqrt(1 / Math.pow(player.getRatingDeviation(), 2) + 1 / d2);
////
////        // Set the new rating and rating deviation to temporary holders
////        player.setRating(newRating);
////        player.setRatingDeviation(decayRatingDeviation(newRatingDeviation, newVolatility));
////        player.setVolatility(newVolatility);
////    }
//
//
//    public static void endRatingPeriod() {
//        // Calculate the new rating and rating deviation for each player
//        for (Glicko2Player player : rankedPlayers.values()) {
//            calculateNewPlayerRating(player);
//        }
//
//        // Once all players have been calculated, update their ratings
//        for (Glicko2Player player : rankedPlayers.values()) {
//            player.updateRating();
//            player.resetMatches();
//
//            // TODO: Remove this debug message
//            Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
//            plugin.getLogger().info(Bukkit.getServer().getPlayer(player.getUuid()).getName() + " has a rating of R[" + player.getRating() + "] and a rating deviation of D[" + player.getRatingDeviation() + "].");
//        }
//    }
//
//
//}
