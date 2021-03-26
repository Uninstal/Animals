package org.uninstal.ark.animals.handlers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.data.abilities.Ability;
import org.uninstal.ark.animals.data.abilities.AbilityType;
import org.uninstal.ark.raids.events.BlockDamageEvent;

public class AbilityHandler implements Listener, Runnable {
	
	private JavaPlugin javaPlugin;

	public AbilityHandler(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(
				javaPlugin, this, 20, 20);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			
			AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
			if(animal == null) return;
			double maxHealth = 20.0;
			
			if(animal.isNearby()) {
				
				List<Ability> abilities = animal.getAbilities();
				for(Ability ability : abilities) {
					
					if(ability.getType() == AbilityType.EFFECT) {
						
						PotionEffect effect = (PotionEffect) ability.getValue();
						PotionEffectType effectType = effect.getType();
						
						if(player.hasPotionEffect(effectType)) {
							
							PotionEffect playerEffect = player.getPotionEffect(effectType);
							if(playerEffect.getDuration() < 40) {
								
								sync(new Runnable() {
									
									@Override
									public void run() {
										
										player.removePotionEffect(effectType);
										player.addPotionEffect(effect);
									}
								});
								
								continue;
							}
						}
						
						else sync(new Runnable() {
							
							@Override
							public void run() {
								
								player.addPotionEffect(effect);
							}
						});
					}
					
					if(ability.getType() == AbilityType.HEALTH)
						maxHealth += (double) ability.getValue();
				}
			}
			
			player.setMaxHealth(maxHealth);
		}
	}

	@EventHandler
	public void abilityDamage(EntityDamageByEntityEvent e) {
		
		Entity damager = e.getDamager();
		if(damager.getType() != EntityType.PLAYER) return;
		
		Player player = (Player) damager;
		double damage = e.getFinalDamage();
		
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
	public void abilityBreak(BlockDamageEvent e) {
		Player player = e.getDamager();
		int damage = e.getDamage();
		
		AnimalTamedDragon animal = AnimalsManager.getDragonOwned(player.getUniqueId());
		if(animal == null || !animal.isNearby()) return;
		
		List<Ability> abilities = animal.getAbilities();
		for(Ability ability : abilities) {
			
			if(ability.getType() == AbilityType.BREAK)
				damage *= (int) ability.getValue();
		}
		
		e.setDamage(damage);
		return;
	}
	
	private void sync(Runnable run) {
		Bukkit.getScheduler().runTask(javaPlugin, run);
	}
}
