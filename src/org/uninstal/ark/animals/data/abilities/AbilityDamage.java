package org.uninstal.ark.animals.data.abilities;

import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.animals.util.Values;

public class AbilityDamage extends Ability {

	private int damage;

	public AbilityDamage() {
		super(true);
		
		this.damage = 
		Utils.random(1, 3);
	}
	
	public AbilityDamage(int damage) {
		super(true);
		this.damage = damage;
	}

	@Override
	public AbilityType getType() {
		return AbilityType.DAMAGE;
	}

	@Override
	public Object getValue() {
		return Values.DAMAGE_BOOSTS.get(damage);
	}
	
	@Override
	public String toString() {
		return "damage:" + damage;
	}
}