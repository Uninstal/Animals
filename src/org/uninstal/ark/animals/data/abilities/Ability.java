package org.uninstal.ark.animals.data.abilities;

public abstract class Ability {
	
	private boolean intersectionable;

	public Ability(boolean intersectionable) {
		this.intersectionable = intersectionable;
	}
	
	public boolean isIntersectionable() {
		return intersectionable;
	}
	
	public abstract String toBoostKey();
	public abstract AbilityType getType();
	public abstract Object getValue();
}
