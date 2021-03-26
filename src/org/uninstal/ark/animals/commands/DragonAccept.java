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
			
			AnimalTamedDragon dragon1 = AnimalsManager.getDragonOwned(uuid);
			AnimalTamedDragon dragon2 = AnimalsManager.getDragonOwned(owner);
			
			dragon1.setOwner(owner);
			dragon2.setOwner(uuid);
			
			AnimalsManager.add(dragon1);
			AnimalsManager.add(dragon2);
			
			sender.sendMessage("§aВы успешно обменялись драконами!");
			if(Bukkit.getPlayer(owner) != null) Bukkit.getPlayer(owner)
			.sendMessage("§aВы успешно обменялись драконами!");
			
			return;
		}
	}
}
