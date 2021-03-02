package org.uninstal.ark.animals.commands;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.AnimalTamedDragon;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.data.abilities.Ability;

public class DragonInfo extends AbstractCommand {

	public DragonInfo(int minArgs) {
		super(minArgs);
	}

	@SuppressWarnings("deprecation")
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
			LivingEntity livingEntity = (LivingEntity) dragon.getEntity();
			int health = (int) livingEntity.getHealth();
			int maxHealth = (int) livingEntity.getMaxHealth();
			
			//Способности дракона
			List<Ability> abilities = dragon.getAbilities();
			String join = String.join(", ", abilities
					.stream()
					.map(a -> a.getType().name() + ":" + a.getValue())
					.collect(Collectors.toList()));

			sender.sendMessage("");
			sender.sendMessage("§dDragon:");
			sender.sendMessage("Location: " + x + " " + y + " " + z);
			sender.sendMessage("Health: " + health + "/" + maxHealth);
			sender.sendMessage("Level: " + level);
			sender.sendMessage("§1Abilities: §f" + join);
			sender.sendMessage("");
			
			return;
		}
	}
}
