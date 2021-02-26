package org.uninstal.ark.animals.data;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.uninstal.ark.animals.util.Utils;

public class AnimalTamedDefault implements Animal {

	private Entity entity;
	private UUID owner;
	private int level;

	public AnimalTamedDefault(Entity entity, UUID owner) {
		
		this.entity = entity;
		this.owner = owner;
		this.randomLevel();
	}
	
	public AnimalTamedDefault(Entity entity, UUID owner, int level) {
		
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
}
