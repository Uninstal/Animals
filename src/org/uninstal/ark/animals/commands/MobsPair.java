package org.uninstal.ark.animals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.gui.AnimalPairGui;
import org.uninstal.ark.animals.data.gui.Gui;

public class MobsPair extends AbstractCommand {

	public MobsPair(int minArgs) {
		super(minArgs);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		
		Gui gui = new AnimalPairGui((Player) sender);
		gui.open();
		
		return;
	}
}
