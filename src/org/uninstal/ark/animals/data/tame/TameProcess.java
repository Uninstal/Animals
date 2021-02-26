package org.uninstal.ark.animals.data.tame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TameProcess {

	public static Map<UUID, Long> cooldowns = new HashMap<>();
	public abstract void startTame(Object... objects);
	public abstract void updateTame(Object... objects);
	public abstract void endTame(Object... objects);
	public abstract boolean check(Object... objects);
}
