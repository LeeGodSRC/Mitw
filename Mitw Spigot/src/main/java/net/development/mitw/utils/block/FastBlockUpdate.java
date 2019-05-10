package net.development.mitw.utils.block;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class FastBlockUpdate {

    public void setBlockFast(final World world, final int x, final int y, final int z, final int blockId,
                             final byte data, final boolean applyPhysics) {
        try {
            final net.minecraft.server.v1_8_R3.World w = ((CraftWorld) world).getHandle();
            final net.minecraft.server.v1_8_R3.Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
            final BlockPosition bp = new BlockPosition(x, y, z);
            final int combined = blockId + (data << 12);
            final IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
            w.setTypeAndData(bp, ibd, applyPhysics ? 3 : 2);
            chunk.a(bp, ibd);
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private boolean canAffectLighting(org.bukkit.World world, int x, int y, int z)
    {
        org.bukkit.block.Block base = world.getBlockAt(x, y, z);
        org.bukkit.block.Block east = base.getRelative(BlockFace.EAST);
        org.bukkit.block.Block west = base.getRelative(BlockFace.WEST);
        org.bukkit.block.Block up = base.getRelative(BlockFace.UP);
        org.bukkit.block.Block down = base.getRelative(BlockFace.DOWN);
        org.bukkit.block.Block south = base.getRelative(BlockFace.SOUTH);
        org.bukkit.block.Block north = base.getRelative(BlockFace.NORTH);

        return (east.getType().isTransparent()) || (west.getType().isTransparent()) || (up.getType().isTransparent()) || (down.getType().isTransparent()) || (south.getType().isTransparent()) || (north.getType().isTransparent());
    }

}
