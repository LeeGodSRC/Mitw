package net.development.mitw.player;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class MitwPlayer extends PlayerInfo {

    private static final Map<UUID, MitwPlayer> playerDatas = new HashMap<>();
    public static final String DEFAULT_LANGUAGE = "zh_tw";

    public static MitwPlayer getByUuid(UUID uuid) {
        return playerDatas.get(uuid);
    }

    private boolean loaded = false;
    private String language = DEFAULT_LANGUAGE;
    private int coins = 0;
    private int mitwpass_level = 1;
    private int mitwpass_exp = 0;

    public MitwPlayer(UUID uuid, String name) {
        super(uuid, name);
        playerDatas.put(uuid, this);
    }

    public boolean load() {
        Document document = Mitw.getInstance().getPlayerMongo().getPlayer(this.getUuid());

        boolean first = false;

        if (document != null) {
            coins = document.getInteger("coins");

            Document mitwPass = (Document) document.get("mitwpass");

            if (mitwPass != null) {
                mitwpass_level = mitwPass.getInteger("level");
                mitwpass_exp = mitwPass.getInteger("exp");
            }
        } else {
            first = true;
        }

        language = Mitw.getInstance().getLanguageData().getLang(this.getUuid());

        this.setLoaded(true);
        return first;
    }

    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(Mitw.getInstance(), () -> {
            Document document = new Document();

            document.put("coins", this.coins);

            if (false) {

                Document mitwPass = new Document();

                mitwPass.put("level", this.mitwpass_level);
                mitwPass.put("exp", this.mitwpass_exp);

                document.put("mitwPass", mitwPass);
            }

            Mitw.getInstance().getPlayerMongo().replacePlayer(this.getUuid(), document);
        });
    }

    public void unhandle() {
        playerDatas.remove(getUuid());
    }

}
