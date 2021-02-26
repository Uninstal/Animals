package org.uninstal.ark.animals.data;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.uninstal.ark.animals.data.tame.TameProcess;
import org.uninstal.ark.animals.util.Values;

public class AnimalNonTamed implements Animal {

	private Entity entity;
	private UUID owner;
	private int progress;

	public AnimalNonTamed(Entity entity, UUID owner) {
		
		this.entity = entity;
		this.owner = owner;
	}

	public AnimalNonTamed(Entity entity, UUID owner, int progress) {
		
		this.entity = entity;
		this.owner = owner;
		this.progress = progress;
	}
	
	public int getNeedEatAmount() {
		return Values.AMOUNTS.get(getTypeName());
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public TameProcess getTameHandler() {
		return Values.TAMES.get(getTypeName());
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
		+ getProgress() + "/" 
		+ getNeedEatAmount();
	}

	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
}
