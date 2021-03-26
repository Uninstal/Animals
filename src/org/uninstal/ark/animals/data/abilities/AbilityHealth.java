package org.uninstal.ark.animals.data.abilities;

import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.animals.util.Values;

public class AbilityHealth extends Ability {

	private int boost;

	public AbilityHealth() {
		super(true);
		
		this.boost =
		Utils.random(1, 3);
	}
	
	public AbilityHealth(int boost) {
		super(true);
		this.boost = boost;
	}

	public String toBoostKey() {
		return "health:" + boost;
	}
	
	@Override
	public AbilityType getType() {
		return AbilityType.HEALTH;
	}

	@Override
	public Object getValue() {
		return Values.HEALTH_BOOSTS.get(boost);
	}
	
	@Override
	public String toString() {
		return "health:+" + ((int) ((Double) getValue()).doubleValue());
	}
}
