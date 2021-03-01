package org.uninstal.ark.animals.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.uninstal.ark.animals.data.map.DoubleKeysHashMap;
import org.uninstal.ark.animals.util.Utils;
import org.uninstal.ark.clans.data.ARKClan;
import org.uninstal.ark.clans.data.ARKClansManager;
import org.uninstal.ark.clans.data.ARKUser;

public class AnimalsManager {

	private static DoubleKeysHashMap<UUID, AnimalTamedDragon> dragons = new DoubleKeysHashMap<>();
	private static Map<UUID, AnimalTamedDefault> tamed_def = new HashMap<>();
	private static Map<UUID, AnimalNonTamed> non_tamed = new HashMap<>();
	
	public static boolean isTame(UUID animal) {
		return !tamed_def.containsKey(animal) 
				&& !dragons.containsKey(animal);
	}
	
	public static boolean isDragon(UUID animal) {
		return dragons.containsKey(animal);
	}
	
	public static AnimalNonTamed getNonTamedAnimal(UUID animal) {
		return non_tamed.get(animal);
	}
	
	public static AnimalTamedDefault getAnimal(UUID animal) {
		return tamed_def.get(animal);
	}
	
	public static AnimalTamedDragon getDragon(UUID animal) {
		return dragons.getValueFromKey1(animal);
	}
	
	public static AnimalTamedDragon getDragonOwned(UUID owner) {
		return dragons.getValueFromKey2(owner);
	}
	
	public static void add(AnimalNonTamed a) {
		non_tamed.put(a.getEntityId(), a);
	}
	
	public static void add(AnimalTamedDragon a) {
		dragons.put(a.getEntityId(), a.getOwner(), a);
	}
	
	public static void add(AnimalTamedDefault a) {
		tamed_def.put(a.getEntityId(), a);
	}

	public static void delete(AnimalNonTamed o) {
		non_tamed.remove(o.getOwner());
	}
	
	public static void delete(AnimalTamedDefault o) {
		tamed_def.remove(o.getOwner());
	}
	
	public static void delete(AnimalTamedDragon o) {
		dragons.remove(o.getEntityId(), o.getOwner());
	}
	
	public static Collection<AnimalTamedDefault> getTamedDefault() {
		return tamed_def.values();
	}
	
	public static Collection<AnimalNonTamed> getNonTamedAnimals() {
		return non_tamed.values();
	}
	
	public static List<Animal> getTamedAnimals(){
		
		List<Animal> animals = new ArrayList<>();
		animals.addAll(tamed_def.values());
		animals.addAll(dragons.values());
		
		return animals;
	}
	
    public static List<Animal> getTamedAnimals(UUID owner){
		
		List<Animal> animals = new ArrayList<>();
		animals.addAll(tamed_def.values());
		animals.addAll(dragons.values());
		
		return  animals
				.stream()
				.filter(a -> a.getOwner().equals(owner))
				.collect(Collectors.toList());
	}

	public static int getAnimalAmount(UUID owner) {
		
		List<Animal> animals = new ArrayList<>();
		animals.addAll(tamed_def.values());
		animals.addAll(dragons.values());
		
		return (int) animals
				.stream()
				.filter(a -> a.getOwner().equals(owner))
				.count();
	}
	
	public static List<Animal> getClanAnimals(UUID owner) {
		
		List<Animal> animals = new ArrayList<>();
		String playerName = Utils.playerName(owner);
		
		ARKUser arkUser = ARKClansManager.getUser(playerName);
		ARKClan arkClan = arkUser == null ? null : arkUser.getClan();
		
		for(Animal animal : getTamedAnimals()) {
			
			if(animal.getOwner().equals(owner)) animals.add(animal);
			else if(arkClan
					.full()
					.stream()
					.map(u -> u.getUsername())
					.anyMatch(u -> animal.getOwner().equals(Utils.playerUUID(u))))
				animals.add(animal);
			
			continue;
		}
		
		return animals;
	}
}
