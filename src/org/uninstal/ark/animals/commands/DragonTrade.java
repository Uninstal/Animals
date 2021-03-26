package org.uninstal.ark.animals.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.util.Utils;

public class DragonTrade extends AbstractCommand {
	
	private static Map<UUID, UUID> trades = new HashMap<>();

	public DragonTrade(int minArgs) {
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
		UUID target = Utils.playerUUID(args[1]);
		
		if(AnimalsManager.getDragonOwned(uuid) == null) sender.sendMessage("§cВы не имеете дракона.");
		else if(AnimalsManager.getDragonOwned(target) == null) sender.sendMessage("§cИгрок не найден, либо не имеет дракона.");
		else {
			
			trades.put(target, uuid);
			sender.sendMessage("§aЗапрос отправлен!");
			
			Player player2 = Bukkit.getPlayer(target);
			if(player2 != null) player2.sendMessage("§aВам пришел запрос на обмен драконами с Uninstal, "
					+ "чтобы принять используйте §e/dragon accept§a.");
			
			return;
		}
		
		return;
	}
	
	public static UUID getTrader(UUID target) {
		return trades.get(target);
	}
}
