package org.uninstal.ark.animals.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;

public class DragonKill extends AbstractCommand {

	public DragonKill(int minArgs) {
		super(minArgs);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage
			("Only for players.");
			return;
		}
		
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		
		AnimalTamedDragon dragon = AnimalsManager.getDragonOwned(uuid);
		if(dragon == null) sender.sendMessage("§cУ вас нет дракона.");
		else dragon.kill();
		
		return;
	}
}