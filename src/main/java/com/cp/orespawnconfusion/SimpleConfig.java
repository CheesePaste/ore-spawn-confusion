package com.cp.orespawnconfusion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SimpleConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("ore-spawn-confusion.json");

    public static class Config {
        public boolean isOpen = true;
        public int XConfusion = 16;
        public int YConfusion = 8;
        public int ZConfusion = 16;
    }

    public static void loadAndApply() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config config;

        if (!Files.exists(CONFIG_PATH)) {
            // 创建默认配置
            config = new Config();
            String defaultJson = gson.toJson(config);
            try {
                Files.writeString(CONFIG_PATH, defaultJson,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                OreSpawnConfusion.LOGGER.info("创建默认配置文件");
            } catch (IOException e) {
                OreSpawnConfusion.LOGGER.error("创建配置文件失败", e);
            }
        } else {
            try {
                String json = Files.readString(CONFIG_PATH);
                config = gson.fromJson(json, Config.class);
            } catch (IOException e) {
                OreSpawnConfusion.LOGGER.error("读取配置文件失败，使用默认值", e);
                config = new Config();
            }
        }

        // 应用到主类
        OreSpawnConfusion.isOpen = config.isOpen;
        OreSpawnConfusion.XConfusion = config.XConfusion;
        OreSpawnConfusion.YConfusion = config.YConfusion;
        OreSpawnConfusion.ZConfusion = config.ZConfusion;
    }
}