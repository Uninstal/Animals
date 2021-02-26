package org.uninstal.ark.animals.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.gui.AnimalGiveGui;
import org.uninstal.ark.animals.util.Utils;

public class MobsGive extends AbstractCommand {

	@Override
	public void run(CommandSender sender, String[] args) {
		
		UUID newOwner = Utils.playerUUID(args[1]);
		if(newOwner == null) return;
		
		AnimalGiveGui gui = new AnimalGiveGui((Player) sender, newOwner);
		gui.open();
		
		return;
	}
}
