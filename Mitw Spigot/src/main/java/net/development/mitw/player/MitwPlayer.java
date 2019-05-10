package net.development.mitw.player;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.language.types.SQLLanguageData;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class MitwPlayer extends PlayerInfo {

    private static Map<UUID, MitwPlayer> playerDatas = new HashMap<>();

    public static MitwPlayer getByUuid(UUID uuid) {
        return playerDatas.get(uuid);
    }

    private int coins = 0;
    private int mitwpass_level = 1;
    private int mitwpass_exp = 0;

    public MitwPlayer(UUID uuid, String name) {
        super(uuid, name);
        playerDatas.put(uuid, this);
    }

    private boolean exists() {
        return Mitw.getInstance().getPlayerDatabase().getPlayerTable().executeSelect("uuid = ?")
                .dataSource(Mitw.getInstance().getPlayerDatabase().getDatabase().getDataSource())
                .statement(s -> s.setString(1, getUuid().toString()))
                .resultNext(r -> true)
                .run(false, false);
    }

    public void load() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Mitw.getInstance(), () -> {
            if (exists()) {
                Mitw.getInstance().getPlayerDatabase().getPlayerTable().executeSelect("uuid = ?")
                        .dataSource(Mitw.getInstance().getPlayerDatabase().getDatabase().getDataSource())
                        .statement(s -> s.setString(1, getUuid().toString()))
                        .result(r -> {
                            if (r.next()) {
                                this.coins = r.getInt("coins");
                                this.mitwpass_level = r.getInt("mitwpass_level");
                                this.mitwpass_exp = r.getInt("mitwpass_exp");
                            }
                            return null;
                        }).run();
            }
        }, 10L);
    }

    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(Mitw.getInstance(), () -> {
            if (exists()) {
                Mitw.getInstance().getPlayerDatabase().getPlayerTable().executeSelect("UPDATE data SET " +
                        "coins = ?, mitwpass_level = ?, mitwpass_exp = ? WHERE uuid = ?")
                        .dataSource(Mitw.getInstance().getPlayerDatabase().getDatabase().getDataSource())
                        .statement(s -> {
                            s.setInt(1, coins);
                            s.setInt(2, mitwpass_level);
                            s.setInt(3, mitwpass_exp);
                            s.setString(4, getUuid().toString());
                        })
                        .run();
            } else if (coins != 0 && mitwpass_level != 1 && mitwpass_exp != 0) {
                Mitw.getInstance().getPlayerDatabase().getPlayerTable().executeInsert("?, ?, ?, ?")
                        .dataSource(Mitw.getInstance().getPlayerDatabase().getDatabase().getDataSource())
                        .statement(s -> {
                            s.setString(1, getUuid().toString());
                            s.setInt(2, coins);
                            s.setInt(3, mitwpass_level);
                            s.setInt(4, mitwpass_exp);
                        });
            }
        });
    }

    public void unhandle() {
        playerDatas.remove(getUuid());
    }

}
