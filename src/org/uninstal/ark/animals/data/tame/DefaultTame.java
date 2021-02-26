package org.uninstal.ark.animals.data.tame;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.uninstal.ark.animals.data.AnimalNonTamed;
import org.uninstal.ark.animals.data.AnimalTamedDefault;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.util.Values;

public class DefaultTame extends TameProcess {
	
	@Override
	public void startTame(Object... objects) { updateTame(objects); }

	@Override
	public void updateTame(Object... objects) {
		PlayerInteractAtEntityEvent e = (PlayerInteractAtEntityEvent) objects[0];
		if(e.getPlayer().getInventory().getItemInMainHand() == null) return;
		
		Player player = e.getPlayer();
		ItemStack stack = player.getInventory().getItemInMainHand();
		AnimalNonTamed animal = (AnimalNonTamed) objects[1];
		UUID u = animal.getEntity().getUniqueId();
		String type = animal.getTypeName();
		
		Material material = Values.EATS.get(type);
		if(material == null || stack.getType() != material) return;
		
		if(System.currentTimeMillis() < 
				cooldowns.getOrDefault(u, (long) 0)) {
			
			long sec = cooldowns.get(u) - System.currentTimeMillis();
			player.sendMessage(Values.COOLDOWN.replace("<time>", String.valueOf(sec / 1000)));
			
			return;
		}
		
		int newProgress = animal.getProgress() + 1;
		
		if(newProgress >= animal.getNeedEatAmount()) endTame(e, animal);
		else animal.setProgress(newProgress);
		
		long c = System.currentTimeMillis() + Values.COOLDOWNS.get(type) * 1000;
		cooldowns.put(u, c);
		
		return;
	}

	@Override
	public void endTame(Object... objects) {
		
		AnimalNonTamed o = (AnimalNonTamed) objects[1];
		AnimalTamedDefault a = new AnimalTamedDefault(o.getEntity(), o.getOwner());
		
		AnimalsManager.delete(o);
		AnimalsManager.add(a);
		
		Player player = Bukkit.getPlayer(a.getOwner());
		player.sendMessage(Values.TAME);
		
		return;
	}

	@Override
	public boolean check(Object... objects) {
		
		if(objects.length < 2) return false;
		if(!objects[0].getClass().getName().endsWith("PlayerInteractAtEntityEvent")) return false;
		if(!objects[1].getClass().getName().endsWith("AnimalNonTamed")) return false;
		
		return true;
	}
}
