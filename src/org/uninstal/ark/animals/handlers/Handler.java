package org.uninstal.ark.animals.handlers;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.uninstal.ark.animals.Main;
import org.uninstal.ark.animals.data.AnimalNonTamed;
import org.uninstal.ark.animals.data.AnimalTamedDefault;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.data.gui.AnimalEggGui;
import org.uninstal.ark.animals.data.gui.Gui;
import org.uninstal.ark.animals.util.Values;

public class Handler implements Listener {

	@EventHandler
	public void debug(PlayerInteractAtEntityEvent e) {
		
		Player player = e.getPlayer(); //who clicked
		ItemStack stack = player.getInventory()
				.getItemInMainHand();
		
		if(e.getHand() == EquipmentSlot.HAND) {
			
			if(player.isOp() && stack != null) {
				
				if(stack.getType() == Material.STICK &&
						stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() &&
						stack.getItemMeta().getDisplayName().equalsIgnoreCase("debug")) {
					
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void damageTameAnimal(EntityDamageByEntityEvent e) {
		
		Entity entity = e.getEntity();
		Entity damager = e.getDamager();
		
		if(damager.getType() == EntityType.PLAYER) {
			
			AnimalTamedDefault a = AnimalsManager.getAnimal(entity.getUniqueId());
			if(a == null || a.getOwner() != damager.getUniqueId()) return;
			
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void tryTame(PlayerInteractAtEntityEvent e) {
		Entity entity = e.getRightClicked();
		
		Player player = e.getPlayer(); //who clicked
		UUID uuid = entity.getUniqueId();
		
		if(AnimalsManager.isTame(entity.getUniqueId())) return;
		AnimalNonTamed a = AnimalsManager.getNonTamedAnimal(uuid);
		
		if(a != null) {
			
			if(a.getTameHandler().check(e, a)
					&& e.getHand() == EquipmentSlot.HAND)
				a.getTameHandler()
				.updateTame(e, a);
			
			e.setCancelled(true);
			return;
		}
		
		else {
			e.setCancelled(true);
			
			if(e.getHand() == EquipmentSlot.HAND) {
				
				String groupName = Main.getPermission().getPrimaryGroup(player);
				int limit = Values.LIMITS.containsKey(groupName) 
						? Values.LIMITS.get(groupName) 
						: Values.LIMITS.get("default");
				
				int animals = AnimalsManager.getAnimalAmount(player.getUniqueId());
				
				if(limit <= animals) {
					
					player.sendMessage(Values.LIMIT);
					return;
				}
				
				String type = entity.getType().name().toLowerCase();
				Material eat = Values.EATS.get(type);
				
				if(eat != null && e.getHand() == EquipmentSlot.HAND) {
					
					a = new AnimalNonTamed(entity, player.getUniqueId());
					a.getTameHandler().startTame(e, a);
					
					e.setCancelled(true);
					AnimalsManager.add(a);
					
					return;
				}
				
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		
		Entity entity = e.getEntity();
		UUID animal = entity.getUniqueId();
		boolean delete = false;
		
		AnimalNonTamed a = AnimalsManager.getNonTamedAnimal(animal);
		if(a != null) {
			AnimalsManager.delete(a);
			delete = true;
		}
		
		AnimalTamedDefault a2 = AnimalsManager.getAnimal(animal);
		if(a2 != null) {
			AnimalsManager.delete(a2);
			delete = true;
		}
		
		AnimalTamedDragon a3 = AnimalsManager.getDragon(animal);
		if(a3 != null) {
			AnimalsManager.delete(a3);
			delete = true;
		}
		
		if(delete) System.out.println("Entity " + entity.getType().name() + " deleted.");
		return;
	}
	
	@EventHandler
	public void tryTame3(PlayerInteractEvent e) {
		
		Block b = e.getClickedBlock();
		if(b == null) return;
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& b.getType() == Material.DRAGON_EGG) {
			
			Player player = e.getPlayer();
			AnimalTamedDragon dragon = AnimalsManager.getDragonOwned(player.getUniqueId());
			
			if(dragon != null) {
				
				player.sendMessage("§cВы уже имеете дракона!");
				return;
			}
					
			AnimalEggGui gui = new AnimalEggGui(player);
			
			gui.setEgg(b.getLocation());
			gui.open();
			
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		Player player = (Player) e.getWhoClicked();
		UUID uuid = player.getUniqueId();
		
		if(Gui.guis.containsKey(uuid)) {
			
			Gui gui = Gui.guis.get(uuid);
			gui.click(e);
			
			return;
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		
		Player player = (Player) e.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(Gui.guis.containsKey(uuid)) {
			
			Gui gui = Gui.guis.get(uuid);
			gui.close(e);
			
			return;
		}
	}
	
//	@EventHandler
//	public void tryTame2(WeaponDamageEntityEvent e) {
//		
//		Entity entity = e.getVictim();
//		UUID animal = entity.getUniqueId();
//		String type = entity.getType().name().toLowerCase();
//		String weapon = e.getWeaponTitle();
//		
//		if(Values.TAMES.containsKey(type)) {
//			
//			if(ARKAnimalsManager.isTame(entity)) return;
//			
//		}
//	}
}
