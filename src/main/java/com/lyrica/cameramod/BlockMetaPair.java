package com.lyrica.cameramod;

import net.minecraft.block.Block;

public class BlockMetaPair {
	public final Block block;
	public final int meta;

	public BlockMetaPair(Block block, int meta) {
		this.block = block;
		this.meta = meta;
	}
}
