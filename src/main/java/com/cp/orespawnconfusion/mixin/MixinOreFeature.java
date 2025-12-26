package com.cp.orespawnconfusion.mixin;

import com.cp.orespawnconfusion.OreSpawnConfusion;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OreFeature.class)
public class MixinOreFeature {
	@Redirect(
			method = "generate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/gen/feature/util/FeatureContext;getRandom()Lnet/minecraft/util/math/random/Random;"
			)
	)
	private Random redirectFeatureRandom(FeatureContext context) {
		if(OreSpawnConfusion.isOpen) {
			return Random.create(System.nanoTime());
		}
		return context.getRandom();
	}
}