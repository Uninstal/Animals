package org.uninstal.ark.animals.data.tame;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.uninstal.ark.animals.Main;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;

public class EnderDragonTame extends TameProcess {

	public static Map<Location, Integer> progresses = new HashMap<>();
	
	@Override
	public void startTame(Object... objects) {} //dont working

	@Override
	public void updateTame(Object... objects) {
		Player player = (Player) objects[0];
		Location location = (Location) objects[1];
		int newProgress = (int) objects[2];
		
		if(newProgress >= 100) endTame(objects);
		
		else {
			
			progresses.put(location, newProgress);
			player.sendMessage("§aПрогресс: " + 
			progresses.get(location));
			
			return;
		}
		
		return;
	}

	@Override
	public void endTame(Object... objects) {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				Player player = (Player) objects[0];
				player.closeInventory();
				
				Location location = (Location) objects[1];
				location.getBlock().setType(Material.AIR);
				
				EnderDragon eg = location.getWorld().spawn(location, EnderDragon.class);
				eg.setAI(false);
				eg.setGravity(false);
				
				AnimalTamedDragon a = new AnimalTamedDragon(eg, player.getUniqueId());
				AnimalsManager.add(a);
				
				return;
			}
			
		}.runTask(Main.getPlugin(Main.class));
	}

	@Override
	public boolean check(Object... objects) {
		
		if(objects.length < 2) return false;
		if(!objects[0].getClass().getName().endsWith("Player")) return false;
		if(!objects[1].getClass().getName().endsWith("Location")) return false;
		if(!objects[2].getClass().getName().endsWith("Integer")) return false;
		
		return true;
	}
}
