package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.modules.Ranking.GlickoPlayer;
import com.minecubedmc.modules.Ranking.GlickoRanking;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankingSystem implements Listener {

    Tweaks plugin;

    public RankingSystem(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!GlickoRanking.hasPlayer(player)) {
            GlickoRanking.addPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player losingPlayer = event.getEntity();
        Player winningPlayer  = losingPlayer.getKiller();

        // If the player didn't die to another player, don't do anything
        if (winningPlayer == null) {
            return;
        }

        // Get the health of the winning player
        double winningPlayerHealth = winningPlayer.getHealth();
        double winningPlayerMaxHealth = winningPlayer.getMaxHealth();

        // Get the GlickoPlayer objects for the winning and losing players
        GlickoPlayer glickoWinningPlayer = GlickoRanking.getPlayer(winningPlayer);
        GlickoPlayer glickoLosingPlayer = GlickoRanking.getPlayer(losingPlayer);

        // Add the matches to the players
        glickoWinningPlayer.addMatchWon(glickoLosingPlayer, winningPlayerHealth, winningPlayerMaxHealth);
        glickoLosingPlayer.addMatchLoss(glickoWinningPlayer);

        // TODO: remove test code
        // If the player has played 13 games, end the rating period
//        if (GlickoRanking.getGamesInRatingPeriod() >= 13) {
//            // Calculate new player ratings
//            GlickoRanking.endRatingPeriod();
//        }

        // Heal the winning player
        winningPlayer.setHealth(winningPlayerMaxHealth);
    }
}
