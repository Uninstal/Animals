package org.uninstal.ark.animals.data.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.uninstal.ark.animals.data.Animal;
import org.uninstal.ark.animals.data.AnimalsManager;

public class AnimalListGui implements Gui {
	
	private Inventory inventory;
	private Player player;
	private boolean open;

	public AnimalListGui(Player player) {
		this.player = player;
		this.open = false;
		
		guis.put(player.getUniqueId(), 
				this);
	}
	
	@Override
	public void open() {
		
		this.inventory = Bukkit.createInventory(null, 6*9, "Питомцы");
		
		int k = 0;
		for(Animal a : AnimalsManager
				.getClanAnimals(player.getUniqueId())) {
			
			ItemStack stack = new ItemStack(Material.MONSTER_EGG);
			SpawnEggMeta meta = (SpawnEggMeta) stack.getItemMeta();
			meta.setDisplayName("§f" + a.getDisplayName());
			meta.setSpawnedType(a.getEntity().getType());
			stack.setItemMeta(meta);
			
			this.inventory.setItem(k, stack);
			
			k++;
			continue;
		}
		
		this.player.closeInventory();
		this.player.openInventory(inventory);
		
		open = true;
	}

	@Override
	public void click(InventoryClickEvent e) { e.setCancelled(true); }

	@Override
	public void close(InventoryCloseEvent e) {
		if(open) guis.remove(player.getUniqueId());
	}
}
