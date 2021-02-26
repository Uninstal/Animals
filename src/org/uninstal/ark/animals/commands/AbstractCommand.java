package org.uninstal.ark.animals.commands;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

	private int minArgs;

	public AbstractCommand(int minArgs) {
		this.minArgs = minArgs;
	}
	
	public int getMinArgs() {
		return minArgs;
	}
	
	public abstract void run(CommandSender sender, String[] args);
}
