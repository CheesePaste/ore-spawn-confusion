package com.cp.orespawnconfusion.mixin;

import com.cp.orespawnconfusion.OreSpawnConfusion;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(OreFeature.class)
public class MixinOreFeature {
	@ModifyVariable(
			method = "generate",
			at = @At("HEAD"),
			argsOnly = true
	)
	private FeatureContext<OreFeatureConfig> modifyContext(FeatureContext<OreFeatureConfig> context) {
		if (OreSpawnConfusion.isOpen && isOreGeneration(context.getConfig())) {
			// 使用纳秒随机数来生成偏移量
			Random random = Random.create(System.nanoTime());
			// 水平偏移范围，比如-8到7
			int offsetX = random.nextInt(16) - 8;
			int offsetY = random.nextInt(8) - 4;  // 垂直偏移范围-4到3
			int offsetZ = random.nextInt(16) - 8;
			BlockPos origin = context.getOrigin();
			BlockPos newOrigin = origin.add(offsetX, offsetY, offsetZ);
			// 创建一个新的FeatureContext，保持其他部分不变，只替换origin
			return new FeatureContext<>(
					context.getFeature(),
					context.getWorld(),
					context.getGenerator(),
					random,
					newOrigin,
					context.getConfig()
			);
		}
		return context;
	}

	@Unique
	private boolean isOreGeneration(OreFeatureConfig config) {
		for (OreFeatureConfig.Target target : config.targets) {
			Block block = target.state.getBlock();
			String blockName = block.getTranslationKey().toLowerCase();
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