package net.development.mitw.listener;

import litebans.api.Entry;
import litebans.api.Events;
import net.development.mitw.Mitw;
import net.development.mitw.common.JedisPackets;
import net.development.mitw.json.JsonChain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class LiteBansListener {

    public static void register(Mitw core) {

        //Register ban listener to LiteBans
        core.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPluginEnabled(PluginEnableEvent event) {

                if (event.getPlugin().getName().equals("LiteBans")) {

                    Events.get().register(new Events.Listener() {
                        @Override
                        public void entryAdded(Entry entry) {
                            String type = entry.getType();

                            if (type.equals("ban") || type.equals("kick")) {

                                core.getMitwJedis().write(JedisPackets.LITE_BANS_ENTRY_ADD,
                                        new JsonChain().addProperty("uuid", entry.getUuid())
                                                       .addProperty("entry", type)
                                                .get());

                            }
                        }
                    });

                    core.getLogger().info("LiteBans listener registered");
                    event.getHandlers().unregister(this);

                }
            }
        }, core);
    }

}
