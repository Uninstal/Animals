package org.uninstal.ark.animals.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;

public class DragonAccept extends AbstractCommand {

	public DragonAccept(int minArgs) {
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
		UUID owner = DragonTrade.getTrader(uuid);
		
		if(owner != null) {
			
			AnimalTamedDragon dragon1 = AnimalsManager.getDragon(uuid);
			AnimalTamedDragon dragon2 = AnimalsManager.getDragon(owner);
			
			dragon1.setOwner(dragon2.getOwner());
			dragon2.setOwner(dragon1.getOwner());
			
			sender.sendMessage("§aВы успешно обменялись драконами!");
			if(Bukkit.getPlayer(owner) != null) Bukkit.getPlayer(owner)
			.sendMessage("§aВы успешно обменялись драконами!");
			
			return;
		}
	}
}
