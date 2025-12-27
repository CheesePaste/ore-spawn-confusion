package com.cp.orespawnconfusion.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.Random;

import static com.cp.orespawnconfusion.OreSpawnConfusion.*;


@Mixin(OreFeature.class)
public class MixinOreFeature {
	@ModifyVariable(
			method = "generate",
			at = @At("HEAD"),
			argsOnly = true
	)
	private FeatureContext<OreFeatureConfig> modifyContext(FeatureContext<OreFeatureConfig> context) {
		if (isOpen && isOreGeneration(context.getConfig())) {
			// 使用纳秒随机数来生成偏移量
			Random random = new Random(System.nanoTime());
			int offsetX = random.nextInt(XConfusion) - XConfusion/2;
			int offsetY = random.nextInt(YConfusion) - YConfusion/2;
			int offsetZ = random.nextInt(ZConfusion) - ZConfusion/2;
			BlockPos origin = context.getOrigin();
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