package org.uninstal.ark.animals.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.uninstal.ark.animals.data.tame.CrackShotTame;
import org.uninstal.ark.animals.data.tame.DefaultTame;
import org.uninstal.ark.animals.data.tame.EnderDragonTame;
import org.uninstal.ark.animals.data.tame.TameProcess;

public class Values {

	private static YamlConfiguration config;
	private static YamlConfiguration animals;
	
	public void setConfig(YamlConfiguration file) {
		config = file;
	}
	
	public void setAnimals(YamlConfiguration file) {
		animals = file;
	}
	
	public void read() {
		
		HOST = config.getString("settings.db.host");
		USER = config.getString("settings.db.user");
		PASS = config.getString("settings.db.pass");
		BASE = config.getString("settings.db.base");
		
		COOLDOWN = config.getString("messages.cooldown").replace("&", "§");
		LIMIT = config.getString("messages.limit").replace("&", "§");
		TAME = config.getString("messages.tame").replace("&", "§");
		GIVE = config.getString("messages.give").replace("&", "§");
		INFO = config.getString("messages.info").replace("&", "§");
		
		WEAPONS = readSubMap(animals, "", "weapon");
		LIMITS = readMap(config, "settings.limits");
		
		Map<String, String> types = readSubMap(animals, "", "type");
		types.forEach((k, v) -> {
			
			if(k.equalsIgnoreCase("default")) TAMES.put(k, new DefaultTame());
			else if(k.equalsIgnoreCase("crackshot")) TAMES.put(k, new CrackShotTame());
			else if(k.equalsIgnoreCase("dragon")) TAMES.put(k, new EnderDragonTame());
			else TAMES.put(k, new DefaultTame());
			
		});
		
		animals.getConfigurationSection("").getKeys(false).forEach(k -> {
			
			if(animals.contains(k + ".item")) {
				
				String typeString = animals.getString(k + ".item");
				Material material = Utils.material(typeString.toUpperCase());
				EATS.put(k.toLowerCase(), material);
			}
			
			if(animals.contains(k + ".cooldown")) {
				
				int cooldown = animals.getInt(k + ".cooldown");
				COOLDOWNS.put(k.toLowerCase(), cooldown);
			}
			
			if(animals.contains(k + ".amount")) {
				
				int amount = animals.getInt(k + ".amount");
				AMOUNTS.put(k.toLowerCase(), amount);
			}
		});
	}
	
	public static String HOST;
	public static String USER;
	public static String PASS;
	public static String BASE;
	
	public static String COOLDOWN;
	public static String LIMIT;
	public static String INFO;
	public static String GIVE;
	public static String TAME;
	
	public static Material TOTEM;
	
	public static Map<Integer, Integer> SPEED_BOOSTS = new HashMap<>();
	public static Map<Integer, Double> HEALTH_BOOSTS = new HashMap<>();
	
	public static Map<String, Material> EATS = new HashMap<>();
	public static Map<String, TameProcess> TAMES = new HashMap<>();
	public static Map<String, Integer> COOLDOWNS = new HashMap<>();
	public static Map<String, Integer> LIMITS = new HashMap<>();
	public static Map<String, String> WEAPONS = new HashMap<>();
	public static Map<String, Integer> AMOUNTS = new HashMap<>();
	
	private static <T, K> Map<T, K> readMap(YamlConfiguration file, String path) {
		Map<Object, Object> map = new HashMap<>();
		
        for(String key : file.getConfigurationSection(path)
        		.getKeys(false)) {
        	
			Object value = file.get(path + "." + key);
			map.put(key.toLowerCase(), value);
			
			continue;
		}
        
        return (Map<T, K>) map;
	}
	
	private static <T, K> Map<T, K> readSubMap(YamlConfiguration file, String section, String way) {
		Map<Object, Object> map = new HashMap<>();
		
        for(String key : file.getConfigurationSection(section)
        		.getKeys(false)) {
        	
        	String valueWay = key + "." + way;
        	if(!file.contains(valueWay)) continue;
        	
			Object value = file.get(valueWay);
			map.put(key.toLowerCase(), value);
			
			continue;
		}
        
        return (Map<T, K>) map;
	}
}
