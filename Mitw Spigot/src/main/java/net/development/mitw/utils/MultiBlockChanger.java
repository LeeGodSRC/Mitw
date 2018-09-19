package net.development.mitw.utils;

import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;

/**
 * High Efficiency Multi Block Changer
 * @author LeeGod
 *
 * my first util like this, hope its great :P
 */
public class MultiBlockChanger {

	private String worldName;

	private int maxChanges = 100;

	private boolean async = false;

	/* not sure does this thing have to exists at here, so i didnt code this */
	private boolean javaThread = false;

	private Runnable callback = null;

	private long tick = 5L;

	private final Queue<BlockChange> blockChanges = new ArrayDeque<>();

	public MultiBlockChanger(World world) {
		this.worldName = world.getName();
	}

	public MultiBlockChanger(String worldName) {
		this.worldName = worldName;
	}

	public String getWorldName() {
		return worldName;
	}

	public int getMaxChanges() {
		return maxChanges;
	}

	public boolean isAsync() {
		return async;
	}

	public boolean isJavaThread() {
		return javaThread;
	}

	public Runnable getCallback() {
		return callback;
	}

	public long getTick() {
		return tick;
	}

	public Queue<BlockChange> getBlockChanges(){
		return blockChanges;
	}

	public MultiBlockChanger setWorldName(String worldName) {
		this.worldName = worldName;
		return this;
	}

	public MultiBlockChanger setMaxChanges(int maxChanges) {
		this.maxChanges = maxChanges;
		return this;
	}

	public MultiBlockChanger async() {
		this.async = true;
		return this;
	}

	public MultiBlockChanger javaThread() {
		this.javaThread = true;
		return this;
	}

	public MultiBlockChanger tick(long tick) {
		this.tick = tick;
		return this;
	}

	public MultiBlockChanger callback(Runnable callback) {
		this.callback = callback;
		return this;
	}

	public MultiBlockChanger addBlockChanges(Block block, MaterialData materialData) {
		this.blockChanges.add(new BlockChange(BlockVector.toBlockVector(block), materialData));
		return this;
	}

	public MultiBlockChanger addBlockChanges(Location location, MaterialData materialData) {
		this.blockChanges.add(new BlockChange(BlockVector.toBlockVector(location), materialData));
		return this;
	}

	public MultiBlockChanger addBlockChanges(Block block, Material material, byte data) {
		this.blockChanges.add(new BlockChange(BlockVector.toBlockVector(block), material, data));
		return this;
	}

	public MultiBlockChanger addBlockChanges(Location location, Material material, byte data) {
		this.blockChanges.add(new BlockChange(BlockVector.toBlockVector(location), material, data));
		return this;
	}

	public MultiBlockChanger addBlockChanges(Material material, byte data, Location... locations) {
		for (final Location location : locations) {
			this.blockChanges.add(new BlockChange(BlockVector.toBlockVector(location), material, data));
		}
		return this;
	}

	public MultiBlockChanger addBlockChanges(Material material, byte data, Block... blocks) {
		for (final Block block : blocks) {
			this.blockChanges.add(new BlockChange(BlockVector.toBlockVector(block), material, data));
		}
		return this;
	}

	private BukkitTask bukkitTask = null;

	public void start(JavaPlugin plugin) {

		final World world = Bukkit.getWorld(worldName);

		final Runnable runnable = new Runnable() {
			private final Queue<SimpleChunk> chunksToRefresh = new ArrayDeque<SimpleChunk>() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean add(SimpleChunk simpleChunk) {
					if (this.contains(simpleChunk)) {
						return false;
					}
					return super.add(simpleChunk);
				}

			};
			private final Queue<BlockChange> blockChanges = new ArrayDeque<>(MultiBlockChanger.this.blockChanges);

			private boolean blockSetDone = false;

			@Override
			public void run() {

				if (!blockChanges.isEmpty()) {
					for (int i = 0; i < maxChanges; i++) {

						if (blockChanges.isEmpty()) {
							blockSetDone = true;
							return;
						}

						final BlockChange blockChange = blockChanges.poll();
						final BlockVector blockVector = blockChange.getBlockVector();
						chunksToRefresh.add(new SimpleChunk(blockVector.getX() >> 4, blockVector.getZ() >> 4));

						/*
							i make the block set to nms because its pertty great for performance
							you can change it to bukkit api if you want
						*/
						final net.minecraft.server.v1_8_R3.World w = ((CraftWorld) world).getHandle();
				        final net.minecraft.server.v1_8_R3.Chunk chunk = w.getChunkAt(blockVector.getX() >> 4, blockVector.getZ() >> 4);
				        final BlockPosition bp = new BlockPosition(blockVector.getX(), blockVector.getY(), blockVector.getZ());
				        final int combined = blockChange.getMaterialData().getItemTypeId() + (blockChange.getMaterialData().getData() << 12);
				        final IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
				        w.setTypeAndData(bp, ibd, 2);
				        chunk.a(bp, ibd);

					}
				}

				if (!blockSetDone) {
					return;
				}

				while (!chunksToRefresh.isEmpty()) {
					final SimpleChunk simpleChunk = chunksToRefresh.poll();
					world.refreshChunk(simpleChunk.getX(), simpleChunk.getZ());
				}

				if (callback != null) {
					callback.run();
				}
				bukkitTask.cancel();
				return;
			}
		};

		if (async) {
			bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, tick);
			return;
		}
		bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0L, tick);
		return;
	}

	private static class BlockVector {

		int x = 0, y = 0, z = 0;

		private BlockVector() {}

		public BlockVector(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

		private static BlockVector toBlockVector(Block block) {
			return new BlockVector(block.getX(), block.getY(), block.getZ());
		}

		private static BlockVector toBlockVector(Location location) {
			return new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		}

	}

	private static class BlockChange {

		private BlockVector blockVector;
		private MaterialData materialData;

		private BlockChange() {}

		public BlockChange(BlockVector blockVector, MaterialData materialData) {
			this.blockVector = blockVector;
			this.materialData = materialData;
		}

		public BlockChange(BlockVector blockVector, Material material, byte data) {
			this.blockVector = blockVector;
			this.materialData = new MaterialData(material, data);
		}

		public BlockVector getBlockVector() {
			return blockVector;
		}

		public MaterialData getMaterialData() {
			return materialData;
		}

	}

	private static class SimpleChunk {

		private int x = 0, z = 0;

		private SimpleChunk() {}

		public SimpleChunk(int x, int z) {
			this.x = x;
			this.z = z;
		}

		public int getX() {
			return x;
		}

		public int getZ() {
			return z;
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof SimpleChunk)) {
				return false;
			}
			return ((SimpleChunk) object).getX() == this.x && ((SimpleChunk) object).getZ() == this.z;
		}
	}

}
