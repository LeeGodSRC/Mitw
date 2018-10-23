package net.development.mitw.utils.holograms.touch;

import org.bukkit.entity.Player;

import net.development.mitw.utils.holograms.Hologram;

import javax.annotation.Nonnull;

public interface TouchHandler {
    void onTouch(@Nonnull Hologram paramHologram, @Nonnull Player paramPlayer, @Nonnull TouchAction paramTouchAction);
}


