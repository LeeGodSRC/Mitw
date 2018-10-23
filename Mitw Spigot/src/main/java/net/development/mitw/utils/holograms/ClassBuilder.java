package net.development.mitw.utils.holograms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import net.development.mitw.utils.reflection.NMSClass;
import net.development.mitw.utils.reflection.minecraft.DataWatcher;
import net.development.mitw.utils.reflection.minecraft.DataWatcher.V1_9.ValueType;
import net.development.mitw.utils.reflection.minecraft.Minecraft;
import net.development.mitw.utils.reflection.minecraft.Minecraft.Version;
import net.development.mitw.utils.reflection.resolver.FieldResolver;
import net.development.mitw.utils.reflection.resolver.minecraft.NMSClassResolver;
import net.development.mitw.utils.reflection.util.AccessUtil;

public abstract class ClassBuilder {
	static NMSClassResolver nmsClassResolver = new NMSClassResolver();
	static FieldResolver EntitySlimeFieldResolver;
	static FieldResolver EntityFieldResolver;
	static FieldResolver PacketPlayOutSpawnEntityLivingFieldResolver;
	static FieldResolver DataWatcherFieldResolver;

	static {
		EntitySlimeFieldResolver = new FieldResolver(NMSClass.EntitySlime);
		EntityFieldResolver = new FieldResolver(NMSClass.Entity);
		PacketPlayOutSpawnEntityLivingFieldResolver = new FieldResolver(NMSClass.PacketPlayOutSpawnEntityLiving);
		DataWatcherFieldResolver = new FieldResolver(NMSClass.DataWatcher);
	}

	public ClassBuilder() {
	}

	public static Object buildEntityWitherSkull(final Object world, final Location loc) throws Exception {
		final Object witherSkull = NMSClass.EntityWitherSkull.getConstructor(new Class[] { NMSClass.World }).newInstance(world);
		updateEntityLocation(witherSkull, loc);
		return witherSkull;
	}

	public static Object buildEntityHorse_1_7(final Object world, final Location loc, final String name) throws Exception {
		final Object horse_1_7 = NMSClass.EntityHorse.getConstructor(new Class[] { NMSClass.World }).newInstance(world);
		updateEntityLocation(horse_1_7, loc);
		if (HologramAPI.is1_8()) {
			if (name != null) {
				NMSClass.Entity.getDeclaredMethod("setCustomName", new Class[] { String.class }).invoke(horse_1_7, name);
			}

			NMSClass.Entity.getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE }).invoke(horse_1_7,
					Boolean.valueOf(name != null && !name.isEmpty()));
		} else {
			if (name != null) {
				NMSClass.EntityInsentient.getDeclaredMethod("setCustomName", new Class[] { String.class }).invoke(horse_1_7, name);
			}

			NMSClass.EntityInsentient.getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE }).invoke(horse_1_7,
					Boolean.valueOf(name != null && !name.isEmpty()));
		}

		final Object horseDataWatcher = AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("datawatcher")).get(horse_1_7);
		NMSClass.EntityAgeable.getDeclaredMethod("setAge", new Class[] { Integer.TYPE }).invoke(horse_1_7, Integer.valueOf(-1700000));
		DataWatcher.setValue(horseDataWatcher, 12, ValueType.ENTITY_FLAG, Byte.valueOf((byte) 96));
		return horse_1_7;
	}

	public static Object buildEntityHorse_1_8(final Object world, final Location loc, final String name) throws Exception {
		final Object horse_1_8 = NMSClass.EntityHorse.getConstructor(new Class[] { NMSClass.World }).newInstance(world);
		updateEntityLocation(horse_1_8, loc);
		if (HologramAPI.is1_8()) {
			if (name != null) {
				NMSClass.Entity.getDeclaredMethod("setCustomName", new Class[] { String.class }).invoke(horse_1_8, name);
			}

			NMSClass.Entity.getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE }).invoke(horse_1_8, Boolean.valueOf(true));
		} else {
			NMSClass.EntityInsentient.getDeclaredMethod("setCustomName", new Class[] { String.class }).invoke(horse_1_8, name);
			NMSClass.EntityInsentient.getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE }).invoke(horse_1_8,
					Boolean.valueOf(name != null && !name.isEmpty()));
		}

		return horse_1_8;
	}

	public static Object buildEntityArmorStand(final Object world, final Location loc, final String name) throws Exception {
		final Object armorStand = NMSClass.EntityArmorStand.getConstructor(new Class[] { NMSClass.World }).newInstance(world);
		updateEntityLocation(armorStand, loc);
		if (name != null) {
			NMSClass.Entity.getDeclaredMethod("setCustomName", new Class[] { String.class }).invoke(armorStand, name);
		}

		NMSClass.Entity.getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE }).invoke(armorStand,
				Boolean.valueOf(name != null && !name.isEmpty()));
		return armorStand;
	}

	public static Object setupArmorStand(final Object armorStand) throws Exception {
		NMSClass.EntityArmorStand.getDeclaredMethod("setInvisible", new Class[] { Boolean.TYPE }).invoke(armorStand, Boolean.valueOf(true));
		NMSClass.EntityArmorStand.getDeclaredMethod("setSmall", new Class[] { Boolean.TYPE }).invoke(armorStand, Boolean.valueOf(true));
		NMSClass.EntityArmorStand.getDeclaredMethod("setArms", new Class[] { Boolean.TYPE }).invoke(armorStand, Boolean.valueOf(false));
		if (Minecraft.VERSION.olderThan(Version.v1_10_R1)) {
			NMSClass.EntityArmorStand.getDeclaredMethod("setGravity", new Class[] { Boolean.TYPE }).invoke(armorStand, Boolean.valueOf(false));
		} else {
			NMSClass.Entity.getDeclaredMethod("setNoGravity", new Class[] { Boolean.TYPE }).invoke(armorStand, Boolean.valueOf(true));
		}

		NMSClass.EntityArmorStand.getDeclaredMethod("setBasePlate", new Class[] { Boolean.TYPE }).invoke(armorStand, Boolean.valueOf(false));
		return armorStand;
	}

	public static Object buildEntitySlime(final Object world, final Location loc, final int size) throws Exception {
		final Object slime = NMSClass.EntitySlime.getConstructor(new Class[] { NMSClass.World }).newInstance(world);
		updateEntityLocation(slime, loc);
		final Object dataWatcher = AccessUtil.setAccessible(NMSClass.Entity.getDeclaredField("datawatcher")).get(slime);
		DataWatcher.setValue(dataWatcher, 0, ValueType.ENTITY_FLAG, Byte.valueOf((byte) 32));
		DataWatcher.setValue(dataWatcher, 16, ValueType.ENTITY_SLIME_SIZE, Integer.valueOf(size < 1 ? 1 : (size > 100 ? 100 : size)));
		return slime;
	}

	public static Object buildWitherSkullSpawnPacket(final Object skull) throws Exception {
		final Object spawnPacketSkull = NMSClass.PacketPlayOutSpawnEntity.getConstructor(new Class[] { NMSClass.Entity, Integer.TYPE })
				.newInstance(skull, Short.valueOf(EntityType.WITHER_SKULL.getTypeId()));
		if (Minecraft.VERSION.olderThan(Version.v1_9_R1)) {
			AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntity.getDeclaredField("j")).set(spawnPacketSkull, Integer.valueOf(66));
		} else {
			AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntity.getDeclaredField("k")).set(spawnPacketSkull, Integer.valueOf(66));
		}

		return spawnPacketSkull;
	}

	public static Object buildSkullMetaPacket(final int id, final Object dataWatcher) throws Exception {
		DataWatcher.setValue(dataWatcher, 0, ValueType.ENTITY_FLAG, Byte.valueOf((byte) 32));
		final Object packet = NMSClass.PacketPlayOutEntityMetadata.getConstructor(new Class[] { Integer.TYPE, NMSClass.DataWatcher, Boolean.TYPE })
				.newInstance(Integer.valueOf(id), dataWatcher, Boolean.valueOf(true));
		return packet;
	}

	public static Object buildHorseSpawnPacket_1_7(final Object horse, final String name) throws Exception {
		if (horse == null)
			throw new IllegalArgumentException("horse cannot be null");
		else {
			final Object spawnPacketHorse_1_7 = NMSClass.PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { NMSClass.EntityLiving })
					.newInstance(horse);
			final Object dataWatcher_1_7 = AccessUtil
					.setAccessible(PacketPlayOutSpawnEntityLivingFieldResolver.resolveByFirstType(NMSClass.DataWatcher)).get(spawnPacketHorse_1_7);
			if (name != null) {
				DataWatcher.setValue(dataWatcher_1_7, 10, ValueType.ENTITY_NAME, name);
			}

			DataWatcher.setValue(dataWatcher_1_7, 11, ValueType.ENTITY_NAME_VISIBLE, Byte.valueOf((byte) (name != null && !name.isEmpty() ? 1 : 0)));
			DataWatcher.setValue(dataWatcher_1_7, 12, ValueType.ENTITY_FLAG, Integer.valueOf(-1700000));
			return spawnPacketHorse_1_7;
		}
	}

	@SuppressWarnings("unchecked")
	public static Object buildHorseSpawnPacket_1_8(final Object horse, final String name) throws Exception {
		if (horse == null)
			throw new IllegalArgumentException("horse cannot be null");
		else {
			final Object spawnPacketHorse_1_8 = NMSClass.PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { NMSClass.EntityLiving })
					.newInstance(horse);
			try {
				AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("b")).setInt(spawnPacketHorse_1_8, 30);
				final Object dataWatcher_1_8 = AccessUtil
						.setAccessible(PacketPlayOutSpawnEntityLivingFieldResolver.resolveByFirstType(NMSClass.DataWatcher))
						.get(spawnPacketHorse_1_8);
				final Map map_1_8 = (Map) DataWatcherFieldResolver.resolveByLastType(Map.class).get(dataWatcher_1_8);
				map_1_8.put(Integer.valueOf(10), NMSClass.WatchableObject.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Object.class })
						.newInstance(Integer.valueOf(0), Integer.valueOf(10), Byte.valueOf((byte) 1)));
				final ArrayList toRemove = new ArrayList();
				Iterator var6 = map_1_8.entrySet().iterator();

				while (var6.hasNext()) {
					final Entry i = (Entry) var6.next();
					final Object current = i.getValue();
					if (current != null) {
						final int index = AccessUtil.setAccessible(NMSClass.WatchableObject.getDeclaredField("b")).getInt(current);
						if (index == 2) {
							AccessUtil.setAccessible(NMSClass.WatchableObject.getDeclaredField("c")).set(current, name);
						} else if (index != 3) {
							toRemove.add(Integer.valueOf(index));
						}
					}
				}

				var6 = toRemove.iterator();

				while (var6.hasNext()) {
					final Integer i1 = (Integer) var6.next();
					map_1_8.remove(i1);
				}

				map_1_8.put(Integer.valueOf(0), NMSClass.WatchableObject.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Object.class })
						.newInstance(Integer.valueOf(0), Integer.valueOf(0), Byte.valueOf((byte) 32)));
			} catch (final Exception e) {
				AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("type")).setInt(spawnPacketHorse_1_8, 30);
				final Object dataWatcher_1_8 = AccessUtil
						.setAccessible(PacketPlayOutSpawnEntityLivingFieldResolver.resolveByFirstType(NMSClass.DataWatcher))
						.get(spawnPacketHorse_1_8);
				final Map map_1_8 = (Map) DataWatcherFieldResolver.resolveByLastType(Map.class).get(dataWatcher_1_8);
				map_1_8.put(Integer.valueOf(10), NMSClass.WatchableObject.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Object.class })
						.newInstance(Integer.valueOf(0), Integer.valueOf(10), Byte.valueOf((byte) 1)));
				final ArrayList toRemove = new ArrayList();
				Iterator var6 = map_1_8.entrySet().iterator();

				while (var6.hasNext()) {
					final Entry i = (Entry) var6.next();
					final Object current = i.getValue();
					if (current != null) {
						final int index = AccessUtil.setAccessible(NMSClass.WatchableObject.getDeclaredField("b")).getInt(current);
						if (index == 2) {
							AccessUtil.setAccessible(NMSClass.WatchableObject.getDeclaredField("c")).set(current, name);
						} else if (index != 3) {
							toRemove.add(Integer.valueOf(index));
						}
					}
				}

				var6 = toRemove.iterator();

				while (var6.hasNext()) {
					final Integer i1 = (Integer) var6.next();
					map_1_8.remove(i1);
				}

				map_1_8.put(Integer.valueOf(0), NMSClass.WatchableObject.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Object.class })
						.newInstance(Integer.valueOf(0), Integer.valueOf(0), Byte.valueOf((byte) 32)));
			}
			return spawnPacketHorse_1_8;
		}
	}

	public static Object buildSlimeSpawnPacket(final Object slime) throws Exception {
		final Object packet = NMSClass.PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { NMSClass.EntityLiving }).newInstance(slime);
		return packet;
	}

	public static Object buildNameMetadataPacket(final int id, final Object dataWatcher, final int nameIndex, final int visibilityIndex,
			final String name) throws Exception {
		DataWatcher.setValue(dataWatcher, nameIndex, ValueType.ENTITY_NAME, name != null ? name : "");
		DataWatcher.setValue(dataWatcher, visibilityIndex, ValueType.ENTITY_NAME_VISIBLE,
				Minecraft.VERSION.olderThan(Version.v1_9_R1) ? Byte.valueOf((byte) (name != null && !name.isEmpty() ? 1 : 0))
						: Boolean.valueOf(name != null && !name.isEmpty()));
		final Object metaPacket = NMSClass.PacketPlayOutEntityMetadata
				.getConstructor(new Class[] { Integer.TYPE, NMSClass.DataWatcher, Boolean.TYPE })
				.newInstance(Integer.valueOf(id), dataWatcher, Boolean.valueOf(true));
		return metaPacket;
	}

	public static Object updateEntityLocation(final Object entity, final Location loc) throws Exception {
		NMSClass.Entity.getDeclaredField("locX").set(entity, Double.valueOf(loc.getX()));
		NMSClass.Entity.getDeclaredField("locY").set(entity, Double.valueOf(loc.getY()));
		NMSClass.Entity.getDeclaredField("locZ").set(entity, Double.valueOf(loc.getZ()));
		return entity;
	}

	public static Object buildArmorStandSpawnPacket(final Object armorStand) throws Exception {
		final Object spawnPacket = NMSClass.PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { NMSClass.EntityLiving })
				.newInstance(armorStand);
		AccessUtil.setAccessible(Minecraft.VERSION.olderThan(Version.v1_9_R1) ? NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("b")
				: NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("c")).setInt(spawnPacket, 30);
		return spawnPacket;
	}

	public static Object buildTeleportPacket(final int id, final Location loc, final boolean onGround, final boolean heightCorrection)
			throws Exception {
		final Object packet = NMSClass.PacketPlayOutEntityTeleport.newInstance();
		AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("a")).set(packet, Integer.valueOf(id));
		if (Minecraft.VERSION.olderThan(Version.v1_9_R1)) {
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("b")).set(packet,
					Integer.valueOf((int) (loc.getX() * 32.0D)));
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("c")).set(packet,
					Integer.valueOf((int) (loc.getY() * 32.0D)));
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("d")).set(packet,
					Integer.valueOf((int) (loc.getZ() * 32.0D)));
		} else {
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("b")).set(packet, Double.valueOf(loc.getX()));
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("c")).set(packet, Double.valueOf(loc.getY()));
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("d")).set(packet, Double.valueOf(loc.getZ()));
			AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("g")).set(packet, Boolean.valueOf(onGround));
		}

		AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("e")).set(packet,
				Byte.valueOf((byte) ((int) (loc.getYaw() * 256.0F / 360.0F))));
		AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("f")).set(packet,
				Byte.valueOf((byte) ((int) (loc.getPitch() * 256.0F / 360.0F))));
		return packet;
	}
}