package com.cp.orespawnconfusion.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Random;

import static com.cp.orespawnconfusion.OreSpawnConfusion.*;

@Mixin(OreFeature.class)
public class MixinOreFeature {

	/**
	 * @param blockPos 方法参数中的 BlockPos
	 * @param world 对应 generate 方法的第一个参数
	 * @param generator 对应 generate 方法的第二个参数
	 * @param random 对应 generate 方法的第三个参数
	 * @param originalPos 对应 generate 方法的第四个参数 (用于逻辑判断)
	 * @param config 对应 generate 方法的第五个参数
	 */
	@ModifyVariable(
			method = "generate",
			at = @At("HEAD"),
			argsOnly = true,
			ordinal = 0 // 指向参数中的第一个 BlockPos
	)
	private BlockPos modifyBlockPos(BlockPos blockPos, StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos originalPos, OreFeatureConfig config) {

		if (isOpen && isOreGeneration(config)) {
			// 使用系统时间纳秒级随机，打破种子固定化
			java.util.Random sysRandom = new java.util.Random(System.nanoTime());

			int offsetX = sysRandom.nextInt(XConfusion + 1) - XConfusion / 2;
			int offsetY = sysRandom.nextInt(YConfusion + 1) - YConfusion / 2;
			int offsetZ = sysRandom.nextInt(ZConfusion + 1) - ZConfusion / 2;

			// 返回偏移后的位置，后续方法逻辑将基于此位置生成
			return blockPos.add(offsetX, offsetY, offsetZ);
		}

		return blockPos;
	}

	@Unique
	private boolean isOreGeneration(OreFeatureConfig config) {
		if (config == null || config.state == null) return false;

		// 1.16.5 的 OreFeatureConfig 直接通过 .state 获取 BlockState
		String blockName = config.state.getBlock().getTranslationKey().toLowerCase();

		return blockName.contains("ore") ||
				blockName.contains("debris") ||
				blockName.contains("矿") ||
				blockName.contains("远古残骸");
	}
}
