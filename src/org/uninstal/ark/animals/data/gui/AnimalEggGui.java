package org.uninstal.ark.animals.data.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.uninstal.ark.animals.Main;
import org.uninstal.ark.animals.data.tame.EnderDragonTame;
import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.animals.util.Values;

public class AnimalEggGui implements Gui {
	
	private static Map<Location, Long> cooldowns = new HashMap<>();
	
	private Player player;
	private Location loc;
	private UUID uuid;
	private boolean open;
	private Inventory inventory;

	public AnimalEggGui(Player player) {
		
		this.player = player;
		this.uuid = player.getUniqueId();
		this.open = false;
		
		guis.put(player.getUniqueId(), 
				this);
	}
	
	public void setEgg(Location loc) {
		this.loc = loc;
	}
	
	@Override
	public void open() {
		
		int progress = EnderDragonTame.progresses.getOrDefault(loc, 0);
		this.inventory = Bukkit.createInventory(null, InventoryType.DROPPER, "Dragon Egg: " + progress + "/100");
		for(int k = 0; k < 9; k++) if(k != 4) inventory.setItem(k, Utils.item(Material.STAINED_GLASS_PANE, ""));
		
		player.closeInventory();
		player.openInventory(inventory);
		
		open = true;
		return;
	}
	
	@Override
	public void click(InventoryClickEvent e) {
		
		Player player = (Player) e.getWhoClicked();
		Material type = Values.EATS.get("dragon");
		
		if(e.getClickedInventory() == inventory
				&& e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) e.setCancelled(true);
		
		else new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(inventory.getItem(4) == null) return;
				if(inventory.getItem(4).getType() != type) return;
				
				if(cooldowns.containsKey(loc)) {
					
					long c = cooldowns.get(loc);
					long c2 = System.currentTimeMillis();
					
					if(c > c2) {
						
						player.sendMessage(Values.COOLDOWN.replace("<time>", 
								String.valueOf((c - c2) / 1000)));
						
						e.setCancelled(true);
						return;
					}
				}
				
				ItemStack curr = inventory.getItem(4);
				curr.setAmount(curr.getAmount() - 1);
				
				EnderDragonTame tame = (EnderDragonTame) Values.TAMES.get("dragon");
				tame.updateTame(player, loc, tame.progresses.getOrDefault(loc, 0) + 1);
				
				cooldowns.put(loc, System.currentTimeMillis() + Values.COOLDOWNS.get("dragon") * 1000);
				return;
			}
			
		}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 1L);
	}

	@Override
	public void close(InventoryCloseEvent e) {
		
		ItemStack curr = inventory.getItem(4);
		if(curr != null) loc.getWorld().dropItem(loc, curr);
		
		if(open) guis.remove(uuid);
	}
}
