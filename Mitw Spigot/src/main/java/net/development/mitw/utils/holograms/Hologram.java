package net.development.mitw.utils.holograms;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.development.mitw.utils.holograms.touch.TouchHandler;
import net.development.mitw.utils.holograms.view.ViewHandler;

public interface Hologram {
	boolean isSpawned();

	void spawn(@Nonnull @Nonnegative long paramLong);

	boolean spawn();

	boolean despawn();

	String getText();

	void setText(@Nullable String paramString);

	void update();

	void update(long paramLong);

	Location getLocation();

	void setLocation(@Nonnull Location paramLocation);

	void move(@Nonnull Location paramLocation);

	boolean isTouchable();

	void setTouchable(boolean paramBoolean);

	void addTouchHandler(@Nonnull TouchHandler paramTouchHandler);

	void removeTouchHandler(@Nonnull TouchHandler paramTouchHandler);

	Collection<UUID> getRendered();

	Collection<TouchHandler> getTouchHandlers();

	void clearTouchHandlers();

	void addViewHandler(@Nonnull ViewHandler paramViewHandler);

	void removeViewHandler(@Nonnull ViewHandler paramViewHandler);

	@Nonnull
	Collection<ViewHandler> getViewHandlers();

	void clearViewHandlers();

	@Nonnull
	Hologram addLineBelow(String paramString);

	@Nullable
	Hologram getLineBelow();

	boolean removeLineBelow();

	@Nonnull
	Collection<Hologram> getLinesBelow();

	@Nonnull
	Hologram addLineAbove(String paramString);

	@Nullable
	Hologram getLineAbove();

	boolean removeLineAbove();

	@Nonnull
	Collection<Hologram> getLinesAbove();

	@Nonnull
	Collection<Hologram> getLines();

	@Nullable
	Entity getAttachedTo();

	void setAttachedTo(@Nullable Entity paramEntity);
}
