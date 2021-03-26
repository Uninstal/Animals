package org.uninstal.ark.animals.data;

import java.util.UUID;

import org.bukkit.entity.Entity;

public interface Animal {

	public Entity getEntity();
	public String getTypeName();
	public String getDisplayName();
	public void setDisplayName(String displayName);
	public void updateDisplayName();
	public boolean isType(String type);
	public UUID getOwner();
	public void setOwner(UUID owner);
	public UUID getEntityId();
	
}
