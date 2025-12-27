package com.cp.orespawnconfusion;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class OreSpawnConfusion implements ModInitializer {
	public static final String MOD_ID = "ore-spawn-confusion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean isOpen = true;
	public static int XConfusion = 16;
	public static int YConfusion = 8;
	public static int ZConfusion = 16;
	public static final Map<Long, Random> chunkRandomMap = new HashMap<>();
	public static long lastClearTime = System.currentTimeMillis();
	public static final long CLEAR_INTERVAL = 5 * 60 * 1000L;

	@Override
	public void onInitialize() {
		SimpleConfig.loadAndApply();

		if (isOpen) {
			LOGGER.info("OreSpawnConfusion 已启用! X:{}, Y:{}, Z:{}",
					XConfusion, YConfusion, ZConfusion);
		} else {
			LOGGER.info("OreSpawnConfusion 已禁用!");
		}

		// 注册退出世界的清理事件
		registerCleanupEvents();
	}

	/**
	 * 注册退出世界的清理事件
	 */
	private void registerCleanupEvents() {
		// 服务器端：服务器停止时清理
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			clearCache();
			LOGGER.info("服务器停止，已清理矿物生成缓存");
		});

		// 服务器端：世界卸载时清理
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
			if (success) {
				clearCache();
				LOGGER.info("数据包重载完成，已清理矿物生成缓存");
			}
		});
	}


	public static void clearCache() {
		chunkRandomMap.clear();
		lastClearTime = System.currentTimeMillis();
		LOGGER.info("clear");
	}

	/**
	 * 获取区块的唯一键（将区块坐标编码为long）
	 */
	public static long getChunkKey(int chunkX, int chunkZ) {
		return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
	}

}