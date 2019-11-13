package net.development.mitw.reboost;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.utils.LobbiesUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class ReboostTask implements Runnable {

    @Getter
    @Setter
    private int time = 301;
    private int taskId;
    @Getter
    private boolean reboosting = false;

    @Override
    public void run() {
        reboosting = true;
        if (time - 1 <= 0) {

            new BukkitRunnable() {
                Iterator<? extends Player> playerIterator;

                @Override
                public void run() {
                    if (playerIterator == null) {
                        playerIterator = Mitw.getInstance().getServer().getOnlinePlayers().iterator();
                    }
                    if (!playerIterator.hasNext()) {
                        Mitw.getInstance().getServer().shutdown();
                        this.cancel();
                        return;
                    }
                    Player player = playerIterator.next();
                    if (player.isOnline()) {
                        LobbiesUtil.sendToLobby(player);
                    }
                }
            }.runTaskTimer(Mitw.getInstance(), 300L, 3L);

        }

        time--;

        if (time == 300 || time == 180 || time == 120 || time == 60 || time == 30 || time == 15 || time == 10 || time < 10) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6自動&f重啟&7]&f 伺服器將在&c&l " + time + "秒 &f後自動重啟!"));
        }
    }

    public void run(int hours) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Mitw.getInstance(), this,20 * 60 * 60 * hours,20L);
        setTime(300);
        reboosting = false;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
        setTime(300);
        reboosting = false;
    }
}
