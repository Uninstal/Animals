package org.uninstal.ark.animals.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

public class Database {

	private static String url = "jdbc:mysql://<host>/";
	private String user;
	private String password;
	private String host;
	private String basename;
	
	private Connection connection;

	public Database(String host, String basename, String user, String password) {
		
		this.user = user;
		this.host = host;
		this.password = password;
		this.basename = basename;
		
		resumeConnection();
	}
	
	public void resumeConnection() {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
	        this.connection = DriverManager.getConnection(url.replace("<host>", host) + basename + "?autoReconnect=true&useSSL=false", user, password);
	        
	        Bukkit.getConsoleSender().sendMessage("[ARKAnimals] §a§lDatabase successfully connected!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return;
	}
	
	/*
	 * 
	 * TYPES EXAMPLE:
	 * 
	 * "uuid VARCHAR(36) PRIMARY KEY", 
	 * "name VARCHAR(15)", 
	 * "lastIP VARCHAR(90)", 
	 * "code VARCHAR(6)"
	 * 
	 */
	public void createTable(String tableName, String... types) throws SQLException {
		if(connection == null) resumeConnection();
		
		String typesFormat = String.join(", ", types);
		
		String sqlcommand = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + typesFormat + ")";
		String sqlcommand2 = "ALTER TABLE " + tableName + " CONVERT TO CHARACTER SET utf8";
		
		Statement statement = connection.createStatement();
		
		statement.executeUpdate(sqlcommand);
		statement.executeUpdate(sqlcommand2);
		statement.close();
		
		return;
	}
	
	public static String getDefaultURL() {
		return url;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUser() {
		return user;
	}
	
	public int send(String command) {
		if(connection == null) resumeConnection();
		
		try {
			
			Statement statement = connection.createStatement();
			int rows = statement.executeUpdate(command);
			
			statement.close();
			return rows;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return 0;
		}
	}
	
	public int send(String table, LinkedHashMap<String, Object> set) {
		if(connection == null) resumeConnection();
		
		try {
			
			int rows = 0;
			
			for(Entry<String, Object> entry : set.entrySet()) 
				if(entry.getValue().getClass().getName().contains("String")) 
					entry.setValue("'" + entry.getValue().toString() + "'");
			
			String parameters = String.join(", ", set.keySet());
			String values = String.join(", ", set.values().stream().map(t -> t.toString()).collect(Collectors.toList()));
			
			String sqlcommand = "INSERT INTO " + table.toLowerCase() + " (" + parameters + ") VALUES (" + values + ")";
			
			Statement statement = connection.createStatement();
			rows = statement.executeUpdate(sqlcommand);
			
			statement.close();
			return rows;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return 0;
		}
	}
	
	public ResultSet getFull(String table) {
		if(connection == null) resumeConnection();
		
		try {
			
			String sqlcommand = "SELECT * FROM " + table.toLowerCase();
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(sqlcommand);
			return rs;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public void delete(String table, String key, Object specific) {
		if(connection == null) resumeConnection();
		
		try {
			
			String spec = specific.getClass().getName().endsWith("String") ? "'" + specific.toString() + "'" : specific.toString();
			String sqlcommand = "DELETE FROM " + table.toLowerCase() + " WHERE " + key + " = " + spec;
			Statement statement = connection.createStatement();
			
			statement.executeUpdate(sqlcommand);
			statement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void deleteFull(String table) {
    	if(connection == null) resumeConnection();
    	
		try {
			
			String sqlcommand = "DELETE FROM " + table.toLowerCase();
			Statement statement = connection.createStatement();
			
			statement.executeUpdate(sqlcommand);
			statement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet get(String table, String key, Object specific) {
		if(connection == null) resumeConnection();
		
		try {
			
			String spec = specific.getClass().getName().endsWith("String") ? "'" + specific.toString() + "'" : specific.toString();
			String sqlcommand = "SELECT * FROM " + table.toLowerCase() + " WHERE " + key + " = " + spec;
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(sqlcommand);
			return rs;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
}