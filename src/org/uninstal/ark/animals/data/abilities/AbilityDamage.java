package org.uninstal.ark.animals.data.abilities;

import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.animals.util.Values;

public class AbilityDamage extends Ability {

	private int boost;

	public AbilityDamage() {
		super(true);
		
		this.boost = 
		Utils.random(1, 3);
	}
	
	public AbilityDamage(int damage) {
		super(true);
		this.boost = damage;
	}

	public String toBoostKey() {
		return "damage:" + boost;
	}

	@Override
	public AbilityType getType() {
		return AbilityType.DAMAGE;
	}

	@Override
	public Object getValue() {
		return Values.DAMAGE_BOOSTS.get(boost);
	}
	
	@Override
	public String toString() {
		return "damage:" + ((Integer) getValue()).intValue();
	}
}