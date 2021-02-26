package org.uninstal.ark.animals.db;

import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.uninstal.ark.animals.data.AnimalNonTamed;
import org.uninstal.ark.animals.data.AnimalTamedDefault;
import org.uninstal.ark.animals.data.AnimalsManager;

public class Operator {

	private Database db;
	public static String table_taming_animals = "ark_taming_animals";
	public static String table_tamed_animals = "ark_tamed_animals";
	public static String types_taming_animals = "animal, owner, progress";
	public static String types_tamed_animals = "animal, owner, level";
	
	public Operator(Database db) {
		this.db = db;
	}
	
	public void load() {
		
		try {
			
			ResultSet tamed = db.getFull(table_tamed_animals);
			ResultSet taming = db.getFull(table_taming_animals);
			
			while(tamed.next()) {
				
				UUID animal = UUID.fromString(tamed.getString(1));
				UUID owner = UUID.fromString(tamed.getString(2));
				int level = tamed.getInt(3);
				
				Entity entity = searchEntity(animal);
				if(entity == null) continue;
				
				AnimalTamedDefault a = new AnimalTamedDefault(entity, owner, level);
				AnimalsManager.add(a);
				
				continue;
			}
			
			while(taming.next()) {
				
				UUID animal = UUID.fromString(taming.getString(1));
				UUID owner = UUID.fromString(taming.getString(2));
				int progress = taming.getInt(3);
				
				Entity entity = searchEntity(animal);
				if(entity == null) continue;
				
				AnimalNonTamed a = new AnimalNonTamed(entity, owner, progress);
				AnimalsManager.add(a);
				
				continue;
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return;
		}
	}
	
	public void save() {
		
		db.deleteFull(table_tamed_animals);
		db.deleteFull(table_taming_animals);
		
		for(AnimalTamedDefault a : AnimalsManager.getTamedDefault()) {
			
			String animal = "'" + a.getEntity().getUniqueId().toString() + "'";
			String owner = "'" + a.getOwner().toString() + "'";
			int level = a.getLevel();
			
			String values = animal + ", " + owner + ", " + level;
			
			db.send("INSERT INTO " + table_tamed_animals + " (" + 
			types_tamed_animals + ") VALUES (" + values + ")");
			continue;
		}
		
		for(AnimalNonTamed a : AnimalsManager.getNonTamedAnimals()) {
			
			String animal = "'" + a.getEntity().getUniqueId().toString() + "'";
			String owner = "'" + a.getOwner().toString() + "'";
			int progress = a.getProgress();
			
			String values = animal + ", " + owner + ", " + progress;
			
			db.send("INSERT INTO " + table_taming_animals + " (" + 
			types_taming_animals + ") VALUES (" + values + ")");
			continue;
		}
	}
	
	private Entity searchEntity(UUID animal) {
		return Bukkit.getEntity(animal);
	}
}
