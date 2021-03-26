package org.uninstal.ark.animals.data.abilities;

import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.animals.util.Values;

public class AbilityBreak extends Ability {

	private int boost;

	public AbilityBreak() {
		super(true);
		
		this.boost = 
		Utils.random(1, 3);
	}
	
	public AbilityBreak(int boost) {
		super(true);
		this.boost = boost;
	}

	public String toBoostKey() {
		return "break:" + boost;
	}

	@Override
	public AbilityType getType() {
		return AbilityType.BREAK;
	}

	@Override
	public Object getValue() {
		return Values.BREAK_BOOSTS.get(boost);
	}
	
	@Override
	public String toString() {
		return "break:" + ((Integer) getValue()).intValue();
	}
}