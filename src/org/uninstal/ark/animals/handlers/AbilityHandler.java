package org.uninstal.ark.animals.handlers;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
	public void abilityDamage(EntityDamageByEntityEvent e) {
		
		Entity damager = e.getDamager();
		if(damager.getType() != EntityType.PLAYER) return;
		
		Player player = (Player) damager;
		int damage = 0;
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null || !animal.isNearby()) return;
		
		List<Ability> abilities = animal.getAbilities();
		for(Ability ability : abilities) {
			
			if(ability.getType() == AbilityType.DAMAGE)
				damage += (int) ability.getValue();
		}
		
		e.setDamage(damage);
	}
	
	@EventHandler
	public void abilityEffect(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null || !animal.isNearby()) return;
		
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
				
				else player.addPotionEffect(effect);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void abilityHealth(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		double incHealth = 0.0;
		
		AnimalTamedDragon animal = AnimalsManager
		.getDragonOwned(player.getUniqueId());
		
		if(animal != null && animal.isNearby()) {
			
			List<Ability> abilities = animal.getAbilities();
			for(Ability ability : abilities) {
				
				if(ability.getType() == AbilityType.HEALTH)
					incHealth += (double) ability.getValue();
			}
		}
		
		player.setMaxHealth(20.0 + incHealth);
	}
	
	@EventHandler
	public void abilityBreak(BlockDamageEvent e) {
		Player player = e.getDamager();
		int damage = e.getDamage();
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null || animal.isNearby()) return;
		
		List<Ability> abilities = animal.getAbilities();
		for(Ability ability : abilities) {
			
			if(ability.getType() == AbilityType.BREAK)
				damage *= (int) ability.getValue();
		}
		
		e.setDamage(damage);
		return;
	}
}
