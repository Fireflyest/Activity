package com.fireflyest.activity.util;

import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Fireflyest
 * Yaml数据管理
 */
public class YamlUtils {

    private static JavaPlugin plugin;
    private static FileConfiguration config;
    private static File dataFolder;

    private YamlUtils(){
    }

    /**
     * 初始化
     * @param javaPlugin 插件class
     */
    public static void iniYamlUtils(JavaPlugin javaPlugin){
        plugin = javaPlugin;
        dataFolder = javaPlugin.getDataFolder();
        loadConfig();
    }

    public static void initFile(FileConfiguration config, Class<?> clazz){
        for (Field field : clazz.getDeclaredFields()){
            if("instance".equals(field.getName())) continue;
            String key = StringUtils.toLowerCase(field.getName());
            String type = StringUtils.toFirstUpCase(field.getType().getSimpleName());
            Method method = null;
            try {
                method = FileConfiguration.class.getMethod("get" + type, String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if(method == null) continue;
            try {
                field.set(null, method.invoke(config, key));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加/加载一个yml文件
     * @param ymlName 不带后缀文件名
     * @return FileConfiguration
     */
    public static FileConfiguration setup(String ymlName) {
        File file = new File(dataFolder, ymlName+".yml");
        if (!file.exists()) {
            try {
                boolean mkdirs = file.getParentFile().mkdirs();
                boolean create =  file.createNewFile();
                if (Config.DEBUG) plugin.getLogger().info(String.format("mkdirs=%s create=%s!", mkdirs, create));
                plugin.saveResource(ymlName+".yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe(String.format("无法创建文件 %s!", ymlName+".yml"));
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    /**
     * 保存配置数据
     * @param key 据键值
     * @param value 数据值
     */
    public static void setConfigData(String key, Object value) {
        if (config == null) {
            return;
        }
        config.set(key, value);
        File file = new File(dataFolder, "config.yml");

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe(String.format("无法保存数据 %s!", "config.yml"));
        }
    }

    /**
     * 加载配置文件
     */
    public static void loadConfig() {
        if (config != null) plugin.reloadConfig();

        config = setup("config");
        initFile(config, Config.class);

        FileConfiguration lang = setup("language");
        initFile(lang, Language.class);
        
    }

}
