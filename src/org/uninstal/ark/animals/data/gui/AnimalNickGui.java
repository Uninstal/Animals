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

public class AnimalNickGui implements Gui {
	
	private String nick;
	private Player player;
	private UUID uuid;
	private boolean open;
	
	private Inventory inventory;
	private Map<Integer, Animal> animals;

	public AnimalNickGui(Player player, String nick) {
		
		this.animals = new HashMap<>();
		this.nick = nick.replace("&", "§");
		this.player = player;
		this.uuid = player.getUniqueId();
		this.open = false;
		
		guis.put(uuid, this);
	}
	
	@Override
	public void open() {
		this.inventory = Bukkit.createInventory(null, 6*9, "Питомцы");
		int k = 0;
		
		for(Animal a : AnimalsManager
				.getTamedAnimals(player.getUniqueId())) {
			
			ItemStack stack = new ItemStack(Material.MONSTER_EGG);
			SpawnEggMeta meta = (SpawnEggMeta) stack.getItemMeta();
			meta.setDisplayName("§f" + a.getDisplayName());
			meta.setSpawnedType(a.getEntity().getType());
			stack.setItemMeta(meta);
			
			this.animals.put(k, a);
			this.inventory.addItem(stack);
			
			k++;
			continue;
		}
		
		this.player.closeInventory();
		this.player.openInventory(inventory);
		
		open = true;
	}

	@Override
	public void click(InventoryClickEvent e) {
		e.setCancelled(true);
		
		if(e.getClickedInventory() == null
				|| !e.getClickedInventory().equals(inventory)
				|| e.getCurrentItem().getType() == Material.AIR)
			return;
		
		int slot = e.getSlot();
		Animal animal = animals.get(slot);
		
		animal.setDisplayName("§f" + nick);
		animal.updateDisplayName();
		
		player.closeInventory();
		return;
	}

	@Override
	public void close(InventoryCloseEvent e) {
		if(open) guis.remove(uuid);
	}
}
