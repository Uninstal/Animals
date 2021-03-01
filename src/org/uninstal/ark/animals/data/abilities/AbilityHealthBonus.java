package org.uninstal.ark.animals.data.abilities;

import org.uninstal.ark.animals.data.Animal;
import org.uninstal.ark.animals.util.Utils;

public class AbilityHealthBonus implements Ability {

	private int level;

	public AbilityHealthBonus() {
		this.level = Utils.random(1, 3);
	}
	
	public AbilityHealthBonus(int level) {
		this.level = level;
	}
	
	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public Object run(Animal animal) {
		
		switch (level) {
		
		case 1: return 5.0;
		case 2: return 10.0;
		case 3: return 15.0;
			
		default:
			return 
			new String();
		}
	}

	@Override
	public String getDisplayName() {
		
		switch (level) {
		
		case 1: return "";
		case 2: return "";
		case 3: return "";
			
		default:
			return 
			new String();
		}
	}

	@Override
	public AbilityType getType() {
		return AbilityType.BreakingSpeed;
	}
}
