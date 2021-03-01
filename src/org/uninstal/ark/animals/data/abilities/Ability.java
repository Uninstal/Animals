package org.uninstal.ark.animals.data.abilities;

import org.uninstal.ark.animals.data.Animal;

public interface Ability {

	public int getLevel();
	public AbilityType getType();
	public String getDisplayName();
	public Object run(Animal animal);
}
