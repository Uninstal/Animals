package org.uninstal.ark.animals.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.uninstal.ark.animals.Main;
import org.uninstal.ark.animals.data.abilities.Ability;
import org.uninstal.ark.animals.data.abilities.AbilityBreak;
import org.uninstal.ark.animals.data.abilities.AbilityDamage;
import org.uninstal.ark.animals.data.abilities.AbilityEffect;
import org.uninstal.ark.animals.data.abilities.AbilityHealth;
import org.uninstal.ark.animals.data.abilities.AbilityType;
import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.animals.util.Values;

public class AnimalTamedDragon implements Animal {
	
	private List<Ability> abilities;
	
	private Entity entity;
	private UUID owner;
	private int level;

	public AnimalTamedDragon(Entity entity, UUID owner) {
		
		this.abilities = new ArrayList<>();
		this.entity = entity;
		this.owner = owner;
		this.randomLevel();
		this.generateAbilities();
	}
	
	public AnimalTamedDragon(Entity entity, UUID owner, int level) {
		
		this.entity = entity;
		this.owner = owner;
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void randomLevel() {
		this.level = Utils.random(1, 15);
	}
	
	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public String getTypeName() {
		return entity.getType()
			.name().toLowerCase();
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public boolean isType(String type) {
		return type.equalsIgnoreCase(getTypeName());
	}

	@Override
	public UUID getEntityId() {
		return entity.getUniqueId();
	}
	
	@Override
	public String getDisplayName() {
		
		String typeName = getTypeName();
		typeName = String.valueOf(typeName.charAt(0))
				.toUpperCase() + typeName.substring(1);
		
		return "§f" + typeName + "§2: §a" 
		+ getLevel() + " level";
	}

	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	public void get() {
		
		Player player = Bukkit.getPlayer(owner);
		if(player == null) return;
		
		Block block = Utils
		.searchEmptyBlockNearby(player);
		
		if(block == null) {
			
			player.sendMessage("§cНет свободного места рядом.");
			return;
		}
		
		else {
			block.setType(Values.TOTEM);
			player.sendMessage("§eНачалось перемещение дракона...");
			
			new BukkitRunnable() {
				private int k = 0;
				
				@Override
				public void run() {
					
					if(block.getType() != Values.TOTEM) {
						if(player != null) player.sendMessage("§cТотем сломан!");
						
						cancel();
						return;
					}
					
					if(k == 300) {
						
						entity.teleport(block.getLocation());
						if(player != null) player.sendMessage("§aДракон перемещен!");
						
						cancel();
						return;
					}
				}
				
			}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 
					20L, 20L);
		}
	}
	
	public void teleport() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				Player player = Bukkit.getPlayer(owner);
				if(player != null) {
					player.sendMessage("§aВы были телеопртированы.");
					player.teleport(entity);
				}
				
				return;
			}
			
		}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 100L);
	}
	
	public void kill() {
		
		LivingEntity entity = (LivingEntity) this.entity;
		entity.damage(entity.getHealth());
	}
	
	public void generateAbilities() {
		int count = Math.min(3, level % 5 + 1);
		
		while(abilities.size() < count) {
			
			AbilityType[] types = AbilityType.values();
			AbilityType type = types[Utils.random(1, types.length) - 1];
			
			if(type == AbilityType.BREAK) abilities.add(new AbilityBreak());
			if(type == AbilityType.DAMAGE) abilities.add(new AbilityDamage());
			if(type == AbilityType.HEALTH) abilities.add(new AbilityHealth());
			
			if(type == AbilityType.EFFECT) abilities.add(new AbilityEffect(
					Values.EFFECTS_TYPES_BOOSTS
					.get(Utils.random(1, Values.EFFECTS_TYPES_BOOSTS
					.size()) - 1)));
		}
	}
	
	public List<Ability> getAbilities() {
		return abilities;
	}
	
	public String hashAbilities() {
		return String.join(";", abilities
				.stream()
				.map(a -> a.toString())
				.collect(Collectors.toList()))
				.trim();
	}
	
	public void setAbilities(String hash) {
		
		for(String h : hash.split(";")) {
			String[] h2 = h.split(":");
			
			if(h2[0].equalsIgnoreCase("break")) abilities.add(new AbilityBreak(Integer.parseInt(h2[1])));
			if(h2[0].equalsIgnoreCase("damage")) abilities.add(new AbilityDamage(Integer.parseInt(h2[1])));
			if(h2[0].equalsIgnoreCase("health")) abilities.add(new AbilityHealth(Integer.parseInt(h2[1])));
			if(h2[0].equalsIgnoreCase("break")) abilities.add(new AbilityEffect(PotionEffectType
					.getByName(h2[1].toUpperCase())));
			
			continue;
		}
	}
	
	public boolean isNearby() {
		
		Player player = Bukkit.getPlayer(owner);
		if(player == null) return false;
		
		Location location = player.getLocation();
		return entity.getLocation().distance(location) < 50.0;
	}
}
