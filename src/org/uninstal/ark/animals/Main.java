package org.uninstal.ark.animals;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.uninstal.ark.animals.commands.AbstractCommand;
import org.uninstal.ark.animals.commands.DragonAccept;
import org.uninstal.ark.animals.commands.DragonInfo;
import org.uninstal.ark.animals.commands.DragonKill;
import org.uninstal.ark.animals.commands.DragonTp;
import org.uninstal.ark.animals.commands.MobsGive;
import org.uninstal.ark.animals.commands.MobsList;
import org.uninstal.ark.animals.data.AnimalsManager;
import org.uninstal.ark.animals.db.Database;
import org.uninstal.ark.animals.db.Operator;
import org.uninstal.ark.animals.handlers.AbilityHandler;
import org.uninstal.ark.animals.handlers.Handler;
import org.uninstal.ark.animals.util.Values;

import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin {
	
	private static Permission permission          = null;
	private Map<String, AbstractCommand> commands = new HashMap<>();
	private Operator operator                     = null;
	private Files files                           = null;

	@Override
	public void onEnable() {
		
		try {
			
			//Load files
			this.files = new Files(this);
			YamlConfiguration config = files.registerNewFile("config");
			YamlConfiguration animals = files.registerNewFile("animals");
			
			//Load values
			Values v = new Values();
			v.setConfig(config);
			v.setAnimals(animals);
			v.read();
			
			//Manager
			new AnimalsManager();
			
			//Database
			Database db = new Database(Values.HOST, Values.BASE, Values.USER, Values.PASS);
			this.operator = new Operator(db);
			
			db.createTable(Operator.table_tamed_animals, 
					"animal VARCHAR(36) PRIMARY KEY",
					"owner VARCHAR(36)",
					"level INT");
			
			db.createTable(Operator.table_taming_animals, 
					"animal VARCHAR(36) PRIMARY KEY",
					"owner VARCHAR(36)",
					"progress INT");
			
			this.operator.load();
			
			//Vault-permissions
			RegisteredServiceProvider<Permission> reg = 
			Bukkit.getServicesManager().getRegistration(Permission.class);
			if(reg != null) this.permission = reg.getProvider();
			
			//Register events
			Bukkit.getPluginManager().registerEvents(new Handler(), this);
			Bukkit.getPluginManager().registerEvents(new AbilityHandler(), this);
			
			//Commands
			this.commands.put("mobs list", new MobsList(1));
			this.commands.put("mobs give", new MobsGive(2));
			this.commands.put("dragon accept", new DragonAccept(1));
			this.commands.put("dragon info", new DragonInfo(1));
			this.commands.put("dragon kill", new DragonKill(1));
			this.commands.put("dragon tp", new DragonTp(1));
			this.commands.put("dragon trade", new DragonAccept(2));
			this.commands.put("dragon get", new DragonAccept(1));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return;
		}
	}
	
	public static Permission getPermission() {
		return permission;
	}
	
	@Override
	public void onDisable() {
		this.operator.save();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//Main commands /mobs and /dragon
		if(command.getName().equalsIgnoreCase("mobs")
				|| command.getName().equalsIgnoreCase("dragon")) {
			
			//Out information of commands
			if(args.length == 0) 
				sender.sendMessage(Values.INFO);
			
			else{
				
				//Entry: /command <arg0>
				String entry = command.getName() + " " + args[0];
				//Registered command from entry
				AbstractCommand cmd = commands.get(entry);
				
				//Check and run
				if(cmd == null) sender.sendMessage(Values.INFO);
				else cmd.run(sender, args);
				
				return false;
			}
		}
		
		return false;
	}
}
