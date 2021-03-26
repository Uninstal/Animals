package org.uninstal.ark.animals.data.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import org.uninstal.ark.animals.util.Values;

public class AnimalGiveGui implements Gui {

	private Map<Integer, Animal> animals = new HashMap<>();
	
	private Player player;
	private boolean open;
	private Inventory inventory;
	private UUID newOwner;

	public AnimalGiveGui(Player player, UUID newOwner) {
		this.player = player;
		this.newOwner = newOwner;
		this.open = false;
		
		guis.put(player.getUniqueId(), 
				this);
	}
	
	@Override
	public void open() {
		this.inventory = Bukkit.createInventory(null, 6*9, "Питомцы");
		int k = 0;
		
		for(Animal a : AnimalsManager
				.getTamedAnimals(player.getUniqueId())) {
			if(AnimalsManager.isDragon(a.getEntityId())) continue;
			
			ItemStack stack = new ItemStack(Material.MONSTER_EGG);
			SpawnEggMeta meta = (SpawnEggMeta) stack.getItemMeta();
			meta.setDisplayName("§f" + a.getDisplayName());
			meta.setSpawnedType(a.getEntity().getType());
			stack.setItemMeta(meta);
			
			this.animals.put(k, a);
			this.inventory.setItem(k, stack);
			
			k++;
			continue;
		}
		
		this.player.closeInventory();
		this.player.openInventory(inventory);
		
		open = true;
	}

	@Override
	public void click(InventoryClickEvent e) {
		
		int slot = e.getSlot();
		if(!animals.containsKey(slot)) return;
		
		Animal a = animals.get(slot);
		a.setOwner(newOwner);
		
		player.sendMessage(Values.GIVE);
		player.closeInventory();
		
		return;
	}

	@Override
	public void close(InventoryCloseEvent e) {
		if(open) guis.remove(player.getUniqueId());
	}
}
