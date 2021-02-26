package org.uninstal.ark.animals.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.Animal;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;

public class DragonTp extends AbstractCommand {

	public DragonTp(int minArgs) {
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
		
		for(Animal animal : AnimalsManager
				.getTamedAnimals(uuid)) {
			
			if(animal instanceof AnimalTamedDragon) {
				
				((AnimalTamedDragon) animal)
				.teleport();
				return;
			}
		}
		
		sender.sendMessage("§cУ вас нет дракона.");
		return;
	}
}
