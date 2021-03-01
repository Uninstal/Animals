package org.uninstal.ark.animals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.gui.AnimalListGui;

public class MobsList extends AbstractCommand {

	public MobsList(int minArgs) {
		super(minArgs);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		
		AnimalListGui list = new AnimalListGui((Player) sender);
		list.open();
		
		return;
	}
}
