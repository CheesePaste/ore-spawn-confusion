package com.cp.orespawnconfusion;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OreSpawnConfusion implements ModInitializer {
	public static final String MOD_ID = "ore-spawn-confusion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean isOpen=true;

	@Override
	public void onInitialize() {
		if(isOpen) {
			LOGGER.info("OreSpawnConfusion is open!");
		}
		else
		{
			LOGGER.info("OreSpawnConfusion is close!");
		}
	}
}