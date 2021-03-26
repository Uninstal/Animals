package org.uninstal.ark.animals.data.abilities;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbilityEffect extends Ability {
	
	private PotionEffect effect;
	
	public AbilityEffect(PotionEffectType effectType) {
		super(true);
		
		this.effect = new PotionEffect(
			effectType, 20 * 30, 0);
	}

	@Override
	public AbilityType getType() {
		return AbilityType.EFFECT;
	}

	@Override
	public Object getValue() {
		return effect;
	}
	
	@Override
	public String toString() {
		return "effect:" + effect.getType().getName();
	}

	@Override
	public String toBoostKey() { return toString(); }
}