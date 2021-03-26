package org.uninstal.ark.animals.data.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.uninstal.ark.animals.data.Animal;
import org.uninstal.ark.animals.data.AnimalRanked;
import org.uninstal.ark.animals.data.AnimalTamedDefault;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;

public class AnimalPairGui implements Gui {
	
	private Player player;
	private UUID uuid;
	private boolean open;
	
	private Inventory inventory;
	
	private Animal selected;
	private Map<Integer, Animal> animals;

	public AnimalPairGui(Player player) {

		this.animals = new HashMap<>();
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
		e.setCancelled(true);
		
		if(e.getClickedInventory() == null
				|| !e.getClickedInventory().equals(inventory)
				|| e.getCurrentItem().getType() == Material.AIR)
			return;
		
		if(selected == null) {
			
			Animal animal = animals.get(e.getSlot());
			this.selected = animal;
			
			return;
		}
		
		else {
			
			//Two selected animal
			Animal two = animals.get(e.getSlot());
			
			if(two.equals(selected)) {
				
				player.sendMessage("§cНужно выбрать другого питомца!");
				return;
			}
			
			if(!two.getEntity().getType().equals(
					selected.getEntity().getType())) {
				
				player.sendMessage("§cНужно выбрать питомца одинакового типа!");
				return;
			}
			
			if(((AnimalRanked) two).getLevel() != ((AnimalRanked) selected).getLevel()) {
				
				player.sendMessage("§cНужно выбрать питомца одинакового уровня!");
				return;
			}
			
			if(AnimalsManager.isDragon(selected.getEntityId())) {
				
				Location location = player.getLocation().add(0D, 3D, 0D);
				EnderDragon newDragon = player.getWorld().spawn(location, EnderDragon.class);
				
				newDragon.setAI(true);
				newDragon.setAI(false);
				
				AnimalTamedDragon dragon = new AnimalTamedDragon(
						newDragon, 
						player.getUniqueId(), 
						((AnimalRanked) selected).getLevel() + 1,
						false,
						200);
				
				selected.getEntity().remove();
				two.getEntity().remove();
				
				AnimalsManager.delete((AnimalTamedDragon) selected);
				AnimalsManager.delete((AnimalTamedDragon) two);
				AnimalsManager.add(dragon);
				
				player.closeInventory();
				return;
			}
			
			else {
				
				Location location = player.getLocation();
				Entity entity = player.getWorld().spawnEntity(location, selected.getEntity().getType());
				
				AnimalTamedDefault animal = new AnimalTamedDefault(
						entity, 
						player.getUniqueId(), 
						((AnimalRanked) selected).getLevel() + 1);
				
				selected.getEntity().remove();
				two.getEntity().remove();
				
				AnimalsManager.delete((AnimalTamedDefault) selected);
				AnimalsManager.delete((AnimalTamedDefault) two);
				AnimalsManager.add(animal);
				
				player.closeInventory();
				return;
			}
		}
	}

	@Override
	public void close(InventoryCloseEvent e) {
		if(open) guis.remove(uuid);
	}
}
