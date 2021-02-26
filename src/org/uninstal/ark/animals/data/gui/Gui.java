package org.uninstal.ark.animals.data.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface Gui {

	public static Map<UUID, Gui> guis = new HashMap<>();
	
	public abstract void open();
	public abstract void click(InventoryClickEvent e);
	public abstract void close(InventoryCloseEvent e);
}
