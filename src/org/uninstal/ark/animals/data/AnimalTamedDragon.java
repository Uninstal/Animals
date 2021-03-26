package org.uninstal.ark.animals.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
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

public class AnimalTamedDragon implements Animal, AnimalRanked {
	
	private List<Ability> abilities;
	private boolean baby;
	
	private Entity entity;
	private UUID owner;
	private int level;
	private int health;
	private String displayName;
	
	public AnimalTamedDragon(Entity entity, UUID owner) {
		
		this.abilities = new ArrayList<>();
		this.baby = true;
		this.entity = entity;
		this.owner = owner;
		this.health = 200;
		this.randomLevel();
		this.generateAbilities();
		this.defaultDisplayName();
		this.updateDisplayName();
		
		((EnderDragon) entity).setAI(false);
		((EnderDragon) entity).setAI(true);
	}
	
	public AnimalTamedDragon(Entity entity, UUID owner, int level, boolean baby, int health) {

		this.abilities = new ArrayList<>();
		this.baby = baby;
		this.entity = entity;
		this.owner = owner;
		this.level = level;
		this.health = health;
		this.defaultDisplayName();
		this.updateDisplayName();
		
		((EnderDragon) entity).setAI(false);
		((EnderDragon) entity).setAI(true);
	}
	
	public AnimalTamedDragon(Entity entity, UUID owner, int level, boolean baby, int health, String displayName) {

		this.abilities = new ArrayList<>();
		this.baby = baby;
		this.entity = entity;
		this.owner = owner;
		this.level = level;
		this.health = health;
		this.displayName = displayName;
		this.updateDisplayName();
		
		((EnderDragon) entity).setAI(false);
		((EnderDragon) entity).setAI(true);
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public boolean isBaby() {
		return baby;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void randomLevel() {
		this.level = Utils.random(1, 15);
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
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
	
	private void defaultDisplayName() {
		String typeName = getTypeName();
		typeName = String.valueOf(typeName.charAt(0))
				.toUpperCase() + typeName.substring(1);
		
		this.displayName = "§f" + typeName + "§2: §a" 
				+ getLevel() + " level";
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
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
				
				if(k == 30) {
					
					Utils.sync(new Runnable() {
						
						@Override
						public void run() {
							entity.remove();
							
							EnderDragon dragon = block.getWorld().spawn(block.getLocation().add(0D, 3D, 0D), EnderDragon.class);
							setEntity(dragon);
							updateDisplayName();
							
							dragon.setAI(false);
							block.setType(Material.AIR);
							dragon.setAI(true);
							
							AnimalsManager.add(thisClass());
						}
					});
					
					if(player != null) player.sendMessage("§aДракон перемещен!");
					
					cancel();
					return;
				}
				
				k++;
				System.out.println(k);
			}
			
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L, 20L);
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
		
		AnimalsManager.
		delete(this);
		
		LivingEntity entity = 
		(LivingEntity) this.entity;
		entity.remove();
	}
	
	public void generateAbilities() {
		int count = Math.min(3, level / 5 + 1);
		
		while(abilities.size() < count) {
			
			AbilityType[] types = AbilityType.values();
			AbilityType type = types[Utils.random(1, types.length) - 1];
			
			if(type == AbilityType.BREAK) abilities.add(new AbilityBreak());
			if(type == AbilityType.DAMAGE) abilities.add(new AbilityDamage());
			if(type == AbilityType.HEALTH) abilities.add(new AbilityHealth());
			
			if(type == AbilityType.EFFECT) {
				boolean added = false;
				
				while(!added) {
					PotionEffectType potion = Values.EFFECTS_TYPES_BOOSTS
							.get(Utils.random(1, Values.EFFECTS_TYPES_BOOSTS
							.size()) - 1);
					
					if(abilities
						.stream()
						.anyMatch(a -> a.getType() == AbilityType.EFFECT
						&& ((PotionEffect) a.getValue())
						.getType() == potion)) continue;
					
					else {
						
						abilities.add(new AbilityEffect(potion));
						added = true;
					}
				}
			}
		}
	}
	
	public List<Ability> getAbilities() {
		return abilities;
	}
	
	public String hashAbilities() {
		return String.join(";", abilities
				.stream()
				.map(a -> a.toBoostKey())
				.collect(Collectors.toList()))
				.trim();
	}
	
	public void setAbilities(String hash) {
		
		for(String h : hash.split(";")) {
			String[] h2 = h.split(":");
			
			if(h2[0].equalsIgnoreCase("break")) 
				abilities.add(new AbilityBreak(Integer.parseInt(h2[1])));
			if(h2[0].equalsIgnoreCase("damage")) 
				abilities.add(new AbilityDamage(Integer.parseInt(h2[1])));
			if(h2[0].equalsIgnoreCase("health")) 
				abilities.add(new AbilityHealth(Integer.parseInt(h2[1])));
			if(h2[0].equalsIgnoreCase("effect")) 
				abilities.add(new AbilityEffect(PotionEffectType
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

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void updateDisplayName() {
		
		this.entity.setCustomName(getDisplayName());
		this.entity.setCustomNameVisible(true);
	}
	
	private AnimalTamedDragon thisClass() {
		return this;
	}
}
