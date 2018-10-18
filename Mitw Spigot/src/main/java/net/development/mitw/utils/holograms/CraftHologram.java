
package net.development.mitw.utils.holograms;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.mitw.Mitw;
import net.development.mitw.utils.reflection.NMSClass;
import net.development.mitw.utils.reflection.Reflection;
import net.development.mitw.utils.reflection.minecraft.DataWatcher;
import net.development.mitw.utils.reflection.minecraft.Minecraft;
import net.development.mitw.utils.reflection.resolver.FieldResolver;
import net.development.mitw.utils.reflection.util.AccessUtil;

public abstract class CraftHologram implements Hologram {
	static FieldResolver PacketPlayOutSpawnEntityLivingFieldResolver = new FieldResolver(
			NMSClass.PacketPlayOutSpawnEntityLiving);

	protected int[] hologramIDs;

	protected int[] touchIDs;

	protected boolean packetsBuilt;

	protected Object spawnPacketArmorStand;

	protected Object spawnPacketWitherSkull;

	protected Object spawnPacketHorse_1_7;

	protected Object spawnPacketHorse_1_8;

	protected Object attachPacket;

	protected Object teleportPacketArmorStand;

	protected Object teleportPacketSkull;

	protected Object teleportPacketHorse_1_7;

	protected Object teleportPacketHorse_1_8;

	protected Object destroyPacket;
	protected Object ridingAttachPacket;
	protected Object ridingEjectPacket;
	protected Object spawnPacketTouchSlime;
	protected Object spawnPacketTouchVehicle;
	protected Object attachPacketTouch;
	protected Object destroyPacketTouch;
	protected Object teleportPacketTouchSlime;
	protected Object teleportPacketTouchVehicle;
	protected Object dataWatcherArmorStand;
	protected Object dataWatcherWitherSkull;
	protected Object dataWatcherHorse_1_7;
	protected Object dataWatcherHorse_1_8;
	protected Object dataWatcherTouchSlime;
	protected Object dataWatcherTouchVehicle;

	protected boolean matchesTouchID(final int id) {
		if ((!isTouchable()) || (this.touchIDs == null))
			return false;
		for (final int i : this.touchIDs) {
			if (i == id)
				return true;

		}
		return false;

	}

	public boolean matchesHologramID(final int id) {
		if ((!HologramAPI.packetsEnabled()) || (this.hologramIDs == null))
			return false;
		for (final int i : this.hologramIDs) {
			if (i == id)
				return true;

		}
		return false;

	}

	protected void buildPackets(final boolean rebuild) throws Exception {
		if ((!rebuild) && (this.packetsBuilt))
			throw new IllegalStateException("packets already built");
		if ((rebuild) && (!this.packetsBuilt))
			throw new IllegalStateException("cannot rebuild packets before building once");
		final Object world = Reflection.getHandle(getLocation().getWorld());

		final Object horse_1_7 = ClassBuilder.buildEntityHorse_1_7(world, getLocation().add(0.0D, 54.56D, 0.0D),
				getText());
		final Object horse_1_8 = ClassBuilder.buildEntityHorse_1_8(world, getLocation().add(0.0D, -2.25D, 0.0D),
				getText());
		final Object witherSkull_1_7 = ClassBuilder.buildEntityWitherSkull(world,
				getLocation().add(0.0D, 54.56D, 0.0D));
		this.dataWatcherWitherSkull = AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("datawatcher"))
				.get(witherSkull_1_7);
		if (rebuild) {
			/* 139 */
			AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).set(witherSkull_1_7,
					Integer.valueOf(this.hologramIDs[0]));
			/* 140 */
			AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).set(horse_1_7,
					Integer.valueOf(this.hologramIDs[1]));
			/* 141 */
			AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).set(horse_1_8,
					Integer.valueOf(this.hologramIDs[2]));

			/* 143 */
			final Field entityCountField = AccessUtil
					.setAccessible(NMSClass.Entity.getDeclaredField("entityCount"));
			/* 144 */
			entityCountField.set(null, Integer.valueOf(((Integer) entityCountField.get(null)).intValue() - 3));

		}

		else {

			/* 151 */
			this.hologramIDs = new int[] {
					AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).getInt(witherSkull_1_7),
					AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).getInt(horse_1_7),
					AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).getInt(horse_1_8) };

		}

		/* 157 */
		this.spawnPacketHorse_1_7 = ClassBuilder.buildHorseSpawnPacket_1_7(horse_1_7, getText());
		/* 158 */
		this.dataWatcherHorse_1_7 = AccessUtil
				.setAccessible(PacketPlayOutSpawnEntityLivingFieldResolver.resolveByFirstType(NMSClass.DataWatcher))
				.get(this.spawnPacketHorse_1_7);

		/* 161 */
		this.spawnPacketHorse_1_8 = ClassBuilder.buildHorseSpawnPacket_1_8(horse_1_8, getText());
		/* 162 */
		this.dataWatcherHorse_1_8 = AccessUtil
				.setAccessible(PacketPlayOutSpawnEntityLivingFieldResolver.resolveByFirstType(NMSClass.DataWatcher))
				.get(this.spawnPacketHorse_1_8);

		/* 165 */
		this.spawnPacketWitherSkull = ClassBuilder.buildWitherSkullSpawnPacket(witherSkull_1_7);

		/* 167 */
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			/* 168 */
			this.attachPacket = NMSClass.PacketPlayOutAttachEntity
					.getConstructor(new Class[] { Integer.TYPE, NMSClass.Entity, NMSClass.Entity })
					.newInstance(Integer.valueOf(0), horse_1_7, witherSkull_1_7);
			/* 169 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b"))
			.set(this.attachPacket, Integer.valueOf(this.hologramIDs[1]));
			/* 170 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c"))
			.set(this.attachPacket, Integer.valueOf(this.hologramIDs[0]));

		} else {
			/* 172 */
			this.attachPacket = NMSClass.PacketPlayOutAttachEntity.newInstance();
			/* 173 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("a"))
			.set(this.attachPacket, Integer.valueOf(this.hologramIDs[1]));
			/* 174 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b"))
			.set(this.attachPacket, Integer.valueOf(this.hologramIDs[0]));

		}

		/* 178 */
		this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0],
				getLocation().add(0.0D, 54.56D, 0.0D), true, false);
		/* 179 */
		this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1],
				getLocation().add(0.0D, 54.56D, 0.0D), true, false);
		/* 181 */
		this.teleportPacketHorse_1_8 = ClassBuilder.buildTeleportPacket(this.hologramIDs[2],
				getLocation().add(0.0D, -2.25D, 0.0D), true, false);

		/* 185 */
		if (isTouchable()) {
			/* 186 */
			final int size = getText() == null ? 1 : getText().length() / 2 / 3;
			/* 187 */
			final Object touchSlime = ClassBuilder.buildEntitySlime(world, getLocation().add(0.0D, -0.4D, 0.0D), size);
			/* 188 */
			this.dataWatcherTouchSlime = AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("datawatcher"))
					.get(touchSlime);

			/* 190 */
			Object touchVehicle = null;

			/* 198 */
			touchVehicle = ClassBuilder.buildEntityWitherSkull(world, getLocation().add(0.0D, -0.4D, 0.0D));
			/* 199 */
			this.dataWatcherTouchVehicle = AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("datawatcher"))
					.get(touchVehicle);

			/* 201 */
			DataWatcher.setValue(this.dataWatcherTouchVehicle, 0, DataWatcher.V1_9.ValueType.ENTITY_FLAG,
					Byte.valueOf((byte) 32));

			/* 204 */
			if (rebuild) {
				/* 205 */
				AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).set(touchSlime,
						Integer.valueOf(this.touchIDs[0]));
				/* 206 */
				AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).set(touchVehicle,
						Integer.valueOf(this.touchIDs[1]));

				/* 208 */
				final Field entityCountField = AccessUtil
						.setAccessible(NMSClass.Entity.getDeclaredField("entityCount"));
				/* 209 */
				entityCountField.set(null, Integer.valueOf(((Integer) entityCountField.get(null)).intValue() - 2));

			}

			else {
				/* 214 */
				this.touchIDs = new int[] {
						AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).getInt(touchSlime),
						AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("id")).getInt(touchVehicle) };

			}

			/* 217 */
			this.spawnPacketTouchSlime = ClassBuilder.buildSlimeSpawnPacket(touchSlime);
			/* 221 */
			this.spawnPacketTouchVehicle = ClassBuilder.buildWitherSkullSpawnPacket(touchVehicle);

			/* 224 */
			if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
				/* 225 */
				this.attachPacketTouch = NMSClass.PacketPlayOutAttachEntity
						.getConstructor(new Class[] { Integer.TYPE, NMSClass.Entity, NMSClass.Entity })
						.newInstance(Integer.valueOf(0), touchSlime, touchVehicle);
				/* 226 */
				AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b"))
				.set(this.attachPacketTouch, Integer.valueOf(this.touchIDs[0]));
				/* 227 */
				AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c"))
				.set(this.attachPacketTouch, Integer.valueOf(this.touchIDs[1]));

			} else {
				/* 229 */
				this.attachPacketTouch = NMSClass.PacketPlayOutAttachEntity.newInstance();
				/* 230 */
				AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("a"))
				.set(this.attachPacketTouch, Integer.valueOf(this.touchIDs[0]));
				/* 231 */
				AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b"))
				.set(this.attachPacketTouch, Integer.valueOf(this.touchIDs[1]));

			}

			/* 234 */
			this.teleportPacketTouchSlime = ClassBuilder.buildTeleportPacket(this.touchIDs[0],
					getLocation().add(0.0D, -0.4D, 0.0D), true, false);

			/* 238 */
			this.teleportPacketTouchVehicle = ClassBuilder.buildTeleportPacket(this.touchIDs[1],
					getLocation().add(0.0D, -0.4D, 0.0D), true, false);

			/* 241 */
			if (!rebuild) {
				/* 242 */
				this.destroyPacketTouch = NMSClass.PacketPlayOutEntityDestroy
						.getConstructor(new Class[] { int[].class }).newInstance(new Object[] { this.touchIDs });

			}

		}

		/* 247 */
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			/* 248 */
			this.ridingAttachPacket = NMSClass.PacketPlayOutAttachEntity.newInstance();
			/* 249 */
			this.ridingEjectPacket = NMSClass.PacketPlayOutAttachEntity.newInstance();

			/* 251 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("a"))
			.set(this.ridingAttachPacket, Integer.valueOf(0));
			/* 252 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("a"))
			.set(this.ridingEjectPacket, Integer.valueOf(0));

			/* 254 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b"))
			.set(this.ridingAttachPacket, Integer.valueOf(this.hologramIDs[0]));
			/* 255 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b"))
			.set(this.ridingEjectPacket, Integer.valueOf(this.hologramIDs[0]));

			/* 257 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c")).set(
					this.ridingAttachPacket,
					Integer.valueOf(getAttachedTo() != null ? getAttachedTo().getEntityId() : -1));
			/* 258 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c"))
			.set(this.ridingEjectPacket, Integer.valueOf(-1));

		} else {
			/* 260 */
			this.ridingAttachPacket = NMSClass.PacketPlayOutMount.newInstance();
			/* 261 */
			this.ridingEjectPacket = NMSClass.PacketPlayOutMount.newInstance();

			/* 263 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutMount.getDeclaredField("a")).set(this.ridingAttachPacket,
					Integer.valueOf((((DefaultHologram) this).isAttached()) && (getAttachedTo() != null)
							? getAttachedTo().getEntityId()
									: -1));
			/* 264 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutMount.getDeclaredField("a")).set(this.ridingEjectPacket,
					Integer.valueOf((((DefaultHologram) this).isAttached()) && (getAttachedTo() != null)
							? getAttachedTo().getEntityId()
									: -1));

			/* 266 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutMount.getDeclaredField("b")).set(this.ridingAttachPacket,
					new int[] { this.hologramIDs[0] });
			/* 267 */
			AccessUtil.setAccessible(NMSClass.PacketPlayOutMount.getDeclaredField("b")).set(this.ridingEjectPacket,
					new int[0]);

		}

		/* 270 */
		if (!rebuild) {
			/* 271 */
			this.destroyPacket = NMSClass.PacketPlayOutEntityDestroy.getConstructor(new Class[] { int[].class })
					.newInstance(new Object[] { this.hologramIDs });

		}

	}

	protected void sendSpawnPackets(final Collection<? extends Player> receivers, final boolean holo,
			final boolean touch) {
		/* 276 */
		if (holo) {
			/* 282 */
			for (final Player p : receivers) {
				/* 283 */
				if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getProtocolVersion().getProtocol() > 5) {
					/* 284 */
					HologramAPI.sendPacket(p, this.spawnPacketHorse_1_8);

				} else {
					/* 286 */
					HologramAPI.sendPacket(p, this.spawnPacketHorse_1_7);
					/* 287 */
					HologramAPI.sendPacket(p, this.spawnPacketWitherSkull);
					/* 288 */
					HologramAPI.sendPacket(p, this.attachPacket);

				}

			}

		}
		/* 293 */
		if ((touch) && (isTouchable())) {
			/* 294 */
			for (final Player p : receivers) {
				/* 295 */
				HologramAPI.sendPacket(p, this.spawnPacketTouchSlime);
				/* 296 */
				HologramAPI.sendPacket(p, this.spawnPacketTouchVehicle);
				/* 297 */
				HologramAPI.sendPacket(p, this.attachPacketTouch);

			}

		}
		/* 300 */
		if ((holo) || (touch)) {

			/* 307 */
			new BukkitRunnable() {
				/* 305 */
				@Override
				public void run() {
					CraftHologram.this.sendTeleportPackets(receivers, holo, touch);
				}
			}

			/* 307 */.runTaskLater(Mitw.getInstance(), 1L);

		}

	}

	protected void sendTeleportPackets(final Collection<? extends Player> receivers, final boolean holo, final boolean touch) {
		/* 312 */
		if ((!holo) && (!touch))
			return;
		/* 313 */
		for (final Player p : receivers) {
			/* 314 */
			if (holo) {
				/* 316 */
				if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getProtocolVersion().getProtocol() > 5) {
					/* 317 */
					HologramAPI.sendPacket(p, this.teleportPacketHorse_1_8);

				} else {
					/* 319 */
					HologramAPI.sendPacket(p, this.teleportPacketHorse_1_7);
					/* 320 */
					HologramAPI.sendPacket(p, this.teleportPacketSkull);

				}

			}
			/* 326 */
			if ((touch) && (isTouchable())) {
				/* 327 */
				HologramAPI.sendPacket(p, this.teleportPacketTouchSlime);
				/* 328 */
				HologramAPI.sendPacket(p, this.teleportPacketTouchVehicle);

			}

		}

	}

	protected void sendNamePackets(final Collection<? extends Player> receivers) {
		/* 334 */
		for (final Player p : receivers) {

			try {
				/* 336 */
				final int id = ((CraftPlayer)p).getHandle().playerConnection.networkManager.getProtocolVersion().getProtocol() > 5 ? this.hologramIDs[2]
						: this.hologramIDs[1];
				/* 337 */
				final Object dataWatcher = ((CraftPlayer)p).getHandle().playerConnection.networkManager.getProtocolVersion().getProtocol() > 5 ? this.dataWatcherHorse_1_8
						: this.dataWatcherHorse_1_7;
				/* 338 */
				final Object packet = ClassBuilder.buildNameMetadataPacket(id, dataWatcher, 2, 3, getText());
				/* 339 */
				HologramAPI.sendPacket(p, packet);
				/* 340 */
				if ((((CraftPlayer)p).getHandle().playerConnection.networkManager.getProtocolVersion().getProtocol() <= 5) &&
						/* 341 */ (this.hologramIDs.length > 1)) {
					/* 342 */
					HologramAPI.sendPacket(p, ClassBuilder.buildNameMetadataPacket(this.hologramIDs[1],
							this.dataWatcherHorse_1_7, 10, 11, getText()));

				}

			} catch (final Exception e) {
				/* 346 */
				e.printStackTrace();

			}

		}

	}

	protected void sendDestroyPackets(final Collection<? extends Player> receivers) {
		/* 352 */
		for (final Player p : receivers) {
			/* 353 */
			HologramAPI.sendPacket(p, this.destroyPacket);
			/* 354 */
			if (isTouchable()) {
				/* 355 */
				HologramAPI.sendPacket(p, this.destroyPacketTouch);

			}

		}

	}

	protected void sendAttachPacket(final Collection<? extends Player> receivers) {
		/* 361 */
		for (final Player p : receivers) {
			/* 362 */
			if (!((DefaultHologram) this).isAttached()) {
				/* 363 */
				HologramAPI.sendPacket(p, this.ridingEjectPacket);

			} else {
				/* 365 */
				HologramAPI.sendPacket(p, this.ridingAttachPacket);

			}

		}

	}

	@Override
	public abstract Location getLocation();

	@Override
	public abstract void setLocation(Location paramLocation);

	@Override
	public abstract String getText();

	@Override
	public abstract void setText(String paramString);

	@Override
	public int hashCode() {
		/* 385 */
		int result = 1;
		/* 386 */
		result = 31 * result + (this.attachPacket == null ? 0 : this.attachPacket.hashCode());
		/* 387 */
		result = 31 * result + (this.attachPacketTouch == null ? 0 : this.attachPacketTouch.hashCode());
		/* 388 */
		result = 31 * result + (this.dataWatcherArmorStand == null ? 0 : this.dataWatcherArmorStand.hashCode());
		/* 389 */
		result = 31 * result + (this.dataWatcherHorse_1_7 == null ? 0 : this.dataWatcherHorse_1_7.hashCode());
		/* 390 */
		result = 31 * result + (this.dataWatcherHorse_1_8 == null ? 0 : this.dataWatcherHorse_1_8.hashCode());
		/* 391 */
		result = 31 * result + (this.dataWatcherTouchSlime == null ? 0 : this.dataWatcherTouchSlime.hashCode());
		/* 392 */
		result = 31 * result + (this.dataWatcherTouchVehicle == null ? 0 : this.dataWatcherTouchVehicle.hashCode());
		/* 393 */
		result = 31 * result + (this.dataWatcherWitherSkull == null ? 0 : this.dataWatcherWitherSkull.hashCode());
		/* 394 */
		result = 31 * result + (this.destroyPacket == null ? 0 : this.destroyPacket.hashCode());
		/* 395 */
		result = 31 * result + (this.destroyPacketTouch == null ? 0 : this.destroyPacketTouch.hashCode());
		/* 396 */
		result = 31 * result + Arrays.hashCode(this.hologramIDs);
		/* 397 */
		result = 31 * result + (this.packetsBuilt ? 1231 : 1237);
		/* 398 */
		result = 31 * result + (this.spawnPacketArmorStand == null ? 0 : this.spawnPacketArmorStand.hashCode());
		/* 399 */
		result = 31 * result + (this.spawnPacketHorse_1_7 == null ? 0 : this.spawnPacketHorse_1_7.hashCode());
		/* 400 */
		result = 31 * result + (this.spawnPacketHorse_1_8 == null ? 0 : this.spawnPacketHorse_1_8.hashCode());
		/* 401 */
		result = 31 * result + (this.spawnPacketTouchSlime == null ? 0 : this.spawnPacketTouchSlime.hashCode());
		/* 402 */
		result = 31 * result + (this.spawnPacketTouchVehicle == null ? 0 : this.spawnPacketTouchVehicle.hashCode());
		/* 403 */
		result = 31 * result + (this.spawnPacketWitherSkull == null ? 0 : this.spawnPacketWitherSkull.hashCode());
		/* 404 */
		result = 31 * result + (this.teleportPacketArmorStand == null ? 0 : this.teleportPacketArmorStand.hashCode());
		/* 405 */
		result = 31 * result + (this.teleportPacketHorse_1_7 == null ? 0 : this.teleportPacketHorse_1_7.hashCode());
		/* 406 */
		result = 31 * result + (this.teleportPacketHorse_1_8 == null ? 0 : this.teleportPacketHorse_1_8.hashCode());
		/* 407 */
		result = 31 * result + (this.teleportPacketSkull == null ? 0 : this.teleportPacketSkull.hashCode());
		/* 408 */
		result = 31 * result + (this.teleportPacketTouchSlime == null ? 0 : this.teleportPacketTouchSlime.hashCode());
		/* 409 */
		result = 31 * result
				+ (this.teleportPacketTouchVehicle == null ? 0 : this.teleportPacketTouchVehicle.hashCode());
		/* 410 */
		result = 31 * result + Arrays.hashCode(this.touchIDs);
		/* 411 */
		return result;

	}

	@Override
	public boolean equals(final Object obj) {
		/* 416 */
		if (this == obj)
			return true;
		/* 417 */
		if (obj == null)
			return false;
		/* 418 */
		if (getClass() != obj.getClass())
			return false;
		/* 419 */
		final CraftHologram other = (CraftHologram) obj;
		/* 420 */
		if (this.attachPacket == null) {
			/* 421 */
			if (other.attachPacket != null)
				return false;
			/* 422 */
		} else if (!this.attachPacket.equals(other.attachPacket))
			return false;
		/* 423 */
		if (this.attachPacketTouch == null) {
			/* 424 */
			if (other.attachPacketTouch != null)
				return false;
			/* 425 */
		} else if (!this.attachPacketTouch.equals(other.attachPacketTouch))
			return false;
		/* 426 */
		if (this.dataWatcherArmorStand == null) {
			/* 427 */
			if (other.dataWatcherArmorStand != null)
				return false;
			/* 428 */
		} else if (!this.dataWatcherArmorStand.equals(other.dataWatcherArmorStand))
			return false;
		/* 429 */
		if (this.dataWatcherHorse_1_7 == null) {
			/* 430 */
			if (other.dataWatcherHorse_1_7 != null)
				return false;
			/* 431 */
		} else if (!this.dataWatcherHorse_1_7.equals(other.dataWatcherHorse_1_7))
			return false;
		/* 432 */
		if (this.dataWatcherHorse_1_8 == null) {
			/* 433 */
			if (other.dataWatcherHorse_1_8 != null)
				return false;
			/* 434 */
		} else if (!this.dataWatcherHorse_1_8.equals(other.dataWatcherHorse_1_8))
			return false;
		/* 435 */
		if (this.dataWatcherTouchSlime == null) {
			/* 436 */
			if (other.dataWatcherTouchSlime != null)
				return false;
			/* 437 */
		} else if (!this.dataWatcherTouchSlime.equals(other.dataWatcherTouchSlime))
			return false;
		/* 438 */
		if (this.dataWatcherTouchVehicle == null) {
			/* 439 */
			if (other.dataWatcherTouchVehicle != null)
				return false;
			/* 440 */
		} else if (!this.dataWatcherTouchVehicle.equals(other.dataWatcherTouchVehicle))
			return false;
		/* 441 */
		if (this.dataWatcherWitherSkull == null) {
			/* 442 */
			if (other.dataWatcherWitherSkull != null)
				return false;
			/* 443 */
		} else if (!this.dataWatcherWitherSkull.equals(other.dataWatcherWitherSkull))
			return false;
		/* 444 */
		if (this.destroyPacket == null) {
			/* 445 */
			if (other.destroyPacket != null)
				return false;
			/* 446 */
		} else if (!this.destroyPacket.equals(other.destroyPacket))
			return false;
		/* 447 */
		if (this.destroyPacketTouch == null) {
			/* 448 */
			if (other.destroyPacketTouch != null)
				return false;
			/* 449 */
		} else if (!this.destroyPacketTouch.equals(other.destroyPacketTouch))
			return false;
		/* 450 */
		if (!Arrays.equals(this.hologramIDs, other.hologramIDs))
			return false;
		/* 451 */
		if (this.packetsBuilt != other.packetsBuilt)
			return false;
		/* 452 */
		if (this.spawnPacketArmorStand == null) {
			/* 453 */
			if (other.spawnPacketArmorStand != null)
				return false;
			/* 454 */
		} else if (!this.spawnPacketArmorStand.equals(other.spawnPacketArmorStand))
			return false;
		/* 455 */
		if (this.spawnPacketHorse_1_7 == null) {
			/* 456 */
			if (other.spawnPacketHorse_1_7 != null)
				return false;
			/* 457 */
		} else if (!this.spawnPacketHorse_1_7.equals(other.spawnPacketHorse_1_7))
			return false;
		/* 458 */
		if (this.spawnPacketHorse_1_8 == null) {
			/* 459 */
			if (other.spawnPacketHorse_1_8 != null)
				return false;
			/* 460 */
		} else if (!this.spawnPacketHorse_1_8.equals(other.spawnPacketHorse_1_8))
			return false;
		/* 461 */
		if (this.spawnPacketTouchSlime == null) {
			/* 462 */
			if (other.spawnPacketTouchSlime != null)
				return false;
			/* 463 */
		} else if (!this.spawnPacketTouchSlime.equals(other.spawnPacketTouchSlime))
			return false;
		/* 464 */
		if (this.spawnPacketTouchVehicle == null) {
			/* 465 */
			if (other.spawnPacketTouchVehicle != null)
				return false;
			/* 466 */
		} else if (!this.spawnPacketTouchVehicle.equals(other.spawnPacketTouchVehicle))
			return false;
		/* 467 */
		if (this.spawnPacketWitherSkull == null) {
			/* 468 */
			if (other.spawnPacketWitherSkull != null)
				return false;
			/* 469 */
		} else if (!this.spawnPacketWitherSkull.equals(other.spawnPacketWitherSkull))
			return false;
		/* 470 */
		if (this.teleportPacketArmorStand == null) {
			/* 471 */
			if (other.teleportPacketArmorStand != null)
				return false;
			/* 472 */
		} else if (!this.teleportPacketArmorStand.equals(other.teleportPacketArmorStand))
			return false;
		/* 473 */
		if (this.teleportPacketHorse_1_7 == null) {
			/* 474 */
			if (other.teleportPacketHorse_1_7 != null)
				return false;
			/* 475 */
		} else if (!this.teleportPacketHorse_1_7.equals(other.teleportPacketHorse_1_7))
			return false;
		/* 476 */
		if (this.teleportPacketHorse_1_8 == null) {
			/* 477 */
			if (other.teleportPacketHorse_1_8 != null)
				return false;
			/* 478 */
		} else if (!this.teleportPacketHorse_1_8.equals(other.teleportPacketHorse_1_8))
			return false;
		/* 479 */
		if (this.teleportPacketSkull == null) {
			/* 480 */
			if (other.teleportPacketSkull != null)
				return false;
			/* 481 */
		} else if (!this.teleportPacketSkull.equals(other.teleportPacketSkull))
			return false;
		/* 482 */
		if (this.teleportPacketTouchSlime == null) {
			/* 483 */
			if (other.teleportPacketTouchSlime != null)
				return false;
			/* 484 */
		} else if (!this.teleportPacketTouchSlime.equals(other.teleportPacketTouchSlime))
			return false;
		/* 485 */
		if (this.teleportPacketTouchVehicle == null) {
			/* 486 */
			if (other.teleportPacketTouchVehicle != null)
				return false;
			/* 487 */
		} else if (!this.teleportPacketTouchVehicle.equals(other.teleportPacketTouchVehicle))
			return false;
		/* 488 */
		return Arrays.equals(this.touchIDs, other.touchIDs);

	}

	@Override
	public String toString() {
		/* 495 */
		return "{\"hologramIDs\":\"" + Arrays.toString(this.hologramIDs) + "\",\"touchIDs\":\""
		+ Arrays.toString(this.touchIDs) + "\",\"packetsBuilt\":\"" + this.packetsBuilt
		+ "\",\"spawnPacketArmorStand\":\"" + this.spawnPacketArmorStand + "\",\"spawnPacketWitherSkull\":\""
		+ this.spawnPacketWitherSkull + "\",\"spawnPacketHorse_1_7\":\"" + this.spawnPacketHorse_1_7
		+ "\",\"spawnPacketHorse_1_8\":\"" + this.spawnPacketHorse_1_8 + "\",\"attachPacket\":\""
		+ this.attachPacket + "\",\"teleportPacketArmorStand\":\"" + this.teleportPacketArmorStand
		+ "\",\"teleportPacketSkull\":\"" + this.teleportPacketSkull + "\",\"teleportPacketHorse_1_7\":\""
		+ this.teleportPacketHorse_1_7 + "\",\"teleportPacketHorse_1_8\":\"" + this.teleportPacketHorse_1_8
		+ "\",\"destroyPacket\":\"" + this.destroyPacket + "\",\"spawnPacketTouchSlime\":\""
		+ this.spawnPacketTouchSlime + "\",\"spawnPacketTouchWitherSkull\":\"" + this.spawnPacketTouchVehicle
		+ "\",\"attachPacketTouch\":\"" + this.attachPacketTouch + "\",\"destroyPacketTouch\":\""
		+ this.destroyPacketTouch + "\",\"teleportPacketTouchSlime\":\"" + this.teleportPacketTouchSlime
		+ "\",\"teleportPacketTouchWitherSkull\":\"" + this.teleportPacketTouchVehicle
		+ "\",\"dataWatcherArmorStand\":\"" + this.dataWatcherArmorStand + "\",\"dataWatcherWitherSkull\":\""
		+ this.dataWatcherWitherSkull + "\",\"dataWatcherHorse_1_7\":\"" + this.dataWatcherHorse_1_7
		+ "\",\"dataWatcherHorse_1_8\":\"" + this.dataWatcherHorse_1_8 + "\",\"dataWatcherTouchSlime\":\""
		+ this.dataWatcherTouchSlime + "\",\"dataWatcherTouchWitherSkull\":\"" + this.dataWatcherTouchVehicle
		+ "\"}";

	}

}