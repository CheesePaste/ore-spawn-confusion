package com.cp.orespawnconfusion.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.cp.orespawnconfusion.OreSpawnConfusion.*;
import static java.lang.Math.floor;


@Mixin(OreFeature.class)
public class MixinOreFeature {

	@ModifyVariable(
			method = "generate",
			at = @At("HEAD"),
			argsOnly = true
	)
	private FeatureContext<OreFeatureConfig> modifyContext(FeatureContext<OreFeatureConfig> context) {
		if (isOpen && isOreGeneration(context.getConfig())) {
			// 定期清理过期的Random对象（每5分钟）
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastClearTime > CLEAR_INTERVAL) {
				clearCache();
				lastClearTime = currentTime;
			}

			// 获取区块坐标
			BlockPos origin = context.getOrigin();
			int chunkX = (int) floor(origin.getX() / 16.0);
			int chunkZ = (int) floor(origin.getZ() / 16.0);

			// 获取或创建Random对象
			long chunkKey = getChunkKey(chunkX, chunkZ);
			Random random = chunkRandomMap.get(chunkKey);

			if (random == null) {
				// 如果不存在，创建新的Random并存入Map
				random=Random.create(System.nanoTime());

				chunkRandomMap.put(chunkKey, random);
				LOGGER.info("Created new Random for chunk [{}, {}]", chunkX, chunkZ);
			} else {
				// 如果已存在，使用存储的Random
				LOGGER.debug("Using existing Random for chunk [{}, {}]", chunkX, chunkZ);
			}

			// 使用获取到的Random生成偏移量
			int offsetX = random.nextInt(XConfusion) - XConfusion / 2;
			int offsetY = random.nextInt(YConfusion) - YConfusion / 2;
			int offsetZ = random.nextInt(ZConfusion) - ZConfusion / 2;

			BlockPos newOrigin = origin.add(offsetX, offsetY, offsetZ);

			// 创建一个新的FeatureContext
			return new FeatureContext<>(
					context.getFeature(),
					context.getWorld(),
					context.getGenerator(),
					context.getRandom(),
					newOrigin,
					context.getConfig()
			);
		}
		return context;
	}


	@Unique
	private boolean isOreGeneration(OreFeatureConfig config) {
		for (OreFeatureConfig.Target target : config.targets) {
			String blockName = target.state.getBlock().getTranslationKey().toLowerCase();
			if (blockName.contains("ore") ||
					blockName.contains("debris") ||
					blockName.contains("矿") ||
					blockName.contains("远古残骸")) {
				return true;
			}
		}
		return false;
	}
}