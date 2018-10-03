package net.development.mitw.utils.holograms.view;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import net.development.mitw.utils.holograms.Hologram;

public interface ViewHandler {
    String onView(@Nonnull Hologram paramHologram, @Nonnull Player paramPlayer, @Nonnull String paramString);
}