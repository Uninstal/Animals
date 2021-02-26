package org.uninstal.ark.animals.util;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	
	private static Random rand = new Random();
	
	public static int random(int start, int end) {
		return rand.nextInt(end) + start;
	}
	
	public static Block searchEmptyBlockNearby(Entity entity) {
		
		Block block = entity.getLocation().getBlock();
		if(block.getRelative(1, 0, 0).getType() == Material.AIR) return block.getRelative(1, 0, 0);
		if(block.getRelative(-1, 0, 0).getType() == Material.AIR) return block.getRelative(-1, 0, 0);
		if(block.getRelative(0, 0, 1).getType() == Material.AIR) return block.getRelative(0, 0, 1);
		if(block.getRelative(0, 0, -1).getType() == Material.AIR) return block.getRelative(0, 0, -1);
		
		return null;
	}

	public static String secSub(long time) {
		String v = String.valueOf(time);
		
		if(v.endsWith("1")) return "секунду";
		if(v.endsWith("2") || v.endsWith("3") || v.endsWith("4")) return "секунды";
		else return "секунд";
	}
	
	public static String playerName(UUID uuid) {
		
		Player player = Bukkit.getPlayer(uuid);
		if(player != null) return player.getName();
		
		OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(uuid);
		if(offPlayer != null) return offPlayer.getName();
		
		return new String();
	}
	
	@SuppressWarnings("deprecation")
	public static UUID playerUUID(String name) {
		
		Player player = Bukkit.getPlayer(name);
		if(player != null) return player.getUniqueId();
		
		OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(name);
		if(offPlayer != null) return offPlayer.getUniqueId();
		
		return null;
	}
	
	public static String toText(Location location) {
		return "";
	}
	
	public static Location toLocation(String text) {
		return null;
	}
	
	public static Material material(String format) {
		return Material.valueOf(format);
	}
	
	public static ItemStack item(Material type, String name) {
		
		ItemStack stack = new ItemStack(type);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name.replace("&", "§"));
		stack.setItemMeta(meta);
		
		return stack;
	}
}
