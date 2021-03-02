package org.uninstal.ark.animals.handlers;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.data.abilities.Ability;
import org.uninstal.ark.animals.data.abilities.AbilityType;
import org.uninstal.ark.raids.events.BlockDamageEvent;

public class AbilityHandler implements Listener {

	@EventHandler
	public void abilityEffect(PlayerMoveEvent e) {
		
		Player player = e.getPlayer();
		Location location = player.getLocation();
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null) return;
		
		Entity entity = animal.getEntity();
		if(entity.getLocation().distance(location) > 50) return;
		
		List<Ability> abilities = animal.getAbilities();
		for(Ability ability : abilities) {
			
			if(ability.getType() == AbilityType.EFFECT) {
				
				PotionEffect effect = (PotionEffect) ability.getValue();
				PotionEffectType effectType = effect.getType();
				
				if(player.hasPotionEffect(effectType)) {
					
					PotionEffect playerEffect = player.getPotionEffect(effectType);
					if(playerEffect.getDuration() < 40) {
						
						player.removePotionEffect(effectType);
						player.addPotionEffect(effect);
						
						continue;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void abilityHealth(PlayerMoveEvent e) {
		
		Player player = e.getPlayer();
		Location location = player.getLocation();
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null) return;
		
		Entity entity = animal.getEntity();
		if(entity.getLocation().distance(location) > 50) return;
		
		List<Ability> abilities = animal.getAbilities();
		for(Ability ability : abilities) {
			
			if(ability.getType() == AbilityType.HEALTH)
				player.setMaxHealth(player.getMaxHealth() 
						+ (double) ability.getValue());
		}
	}
	
	@EventHandler
	public void abilityBreak(BlockDamageEvent e) {
		
		Player player = e.getDamager();
		Location location = player.getLocation();
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null) return;
		
		Entity entity = animal.getEntity();
		if(entity.getLocation().distance(location) > 50) return;
		
		List<Ability> abilities = animal.getAbilities();
		for(Ability ability : abilities) {
			
			if(ability.getType() == AbilityType.BREAK)
				e.setDamage(e.getDamage() * (int) ability.getValue());
		}
		
		return;
	}
}
