package net.development.mitw.utils.holograms.touch;

import org.bukkit.entity.Player;

import net.development.mitw.utils.holograms.Hologram;

import javax.annotation.Nonnull;

public interface TouchHandler {
    void onTouch(@Nonnull Hologram paramHologram, @Nonnull Player paramPlayer, @Nonnull TouchAction paramTouchAction);
}


/* Location:              C:\Users\msi\Downloads\HologramAPI_v1.6.2.jar!\de\inventivegames\hologram\touch\TouchHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */