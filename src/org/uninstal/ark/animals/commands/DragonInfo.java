package org.uninstal.ark.animals.commands;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.data.abilities.Ability;
import org.uninstal.ark.animals.util.Values;

public class DragonInfo extends AbstractCommand {

	public DragonInfo(int minArgs) {
		super(minArgs);
	}
	
	@Override
	public void run(CommandSender sender, String[] args) {
		
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		
		AnimalTamedDragon dragon = AnimalsManager.getDragonOwned(uuid);
		if(dragon == null) sender.sendMessage("§cУ вас нет дракона.");
		
		else {
			
			int level = dragon.getLevel();
			
			//Месторасположение дракона
			Location location = dragon.getEntity().getLocation();
			int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
			
			//Здоровье дракона
			int health = dragon.getHealth();
			int maxHealth = 200;
			
			//Способности дракона
			List<Ability> abilities = dragon.getAbilities();
			String join = String.join(System.lineSeparator() + " ", 
					abilities
					.stream()
					.map(a -> a.toString())
					.collect(Collectors.toList()));

			String message = Values.DRAGON_INFO
					.replace("<x>", String.valueOf(x))
					.replace("<y>", String.valueOf(y))
					.replace("<z>", String.valueOf(z))
					.replace("<hp>", String.valueOf(health))
					.replace("<max-hp>", String.valueOf(maxHealth))
					.replace("<level>", String.valueOf(level))
					.replace("<abilities>", join);
			
			sender.sendMessage(message);
			return;
		}
	}
}
