package com.cp.orespawnconfusion;

import net.fabricmc.api.ModInitializer;

public class OreSpawnConfusion implements ModInitializer {
	public static final String MOD_ID = "ore-spawn-confusion";
	//public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean isOpen = true;
	public static int XConfusion = 16;
	public static int YConfusion = 8;
	public static int ZConfusion = 16;

	@Override
	public void onInitialize() {
		SimpleConfig.loadAndApply();

		if (isOpen) {
			//LOGGER.info("OreSpawnConfusion 已启用! X:{}, Y:{}, Z:{}",
					//XConfusion, YConfusion, ZConfusion);
		} else {
			//LOGGER.info("OreSpawnConfusion 已禁用!");
		}
	}
}