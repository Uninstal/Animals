package org.uninstal.ark.animals.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.uninstal.ark.animals.Main;
import org.uninstal.ark.animals.data.abilities.Ability;
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
	
	public List<Ability> getAbilities() {
		return abilities;
	}
}
