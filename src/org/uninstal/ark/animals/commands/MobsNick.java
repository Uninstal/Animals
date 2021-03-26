package org.uninstal.ark.animals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.gui.AnimalNickGui;
import org.uninstal.ark.animals.data.gui.Gui;
import org.uninstal.ark.animals.util.Utils;

public class MobsNick extends AbstractCommand {

	public MobsNick(int minArgs) {
		super(minArgs);
	}
	
	@Override
	public void run(CommandSender sender, String[] args) {
		
		String text = Utils.join(args, 1);
		Gui gui = new AnimalNickGui((Player) sender, text);
		
		gui.open();
		return;
	}
}