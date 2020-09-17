package com.amazon.ask.helloworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Class for all methods that access the database. 
 * @author YirongLi
 *
 */
public class DBConnect {
	private final String DBDRIVER = "org.postgresql.Driver";

	private final String DBURL = "jdbc:postgresql://project-cookbook.cmi4lqvvhcf7.eu-west-2.rds.amazonaws.com:5434/postgres";
	private final String DBUSER = "postgres";
	private final String DBPASSWORD = "lyr971025";
	private Connection conn = null;

	public DBConnect() throws ClassNotFoundException, SQLException {
			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
			// System.out.println("Database connected.");
	}

	public Connection getConnection() {
		return this.conn;
	}
	
	/**
	 * Close the connection.
	 */
	public void close() {
		try {
			this.conn.close();
			// System.out.println("Connection closed.");
		} catch (Exception e) {
		}
	}

	
	/**
	 * This getDish method queries the database for the information of a dish. 
	 * @param name - The name of the dish, used to generate the query. 
	 * @return - the Dish object that contains the information of the dish.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Dish getDish(String name){
		if(name == "") return null;
		DBConnect db = null;
		Dish dish = null;
		try {
			db = new DBConnect();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from dish where name='" + name + "'");
			rs.next();
			boolean heal = rs.getBoolean("heal");
			String effect = rs.getString("effect");
			ArrayList<String> ings = new ArrayList<String>();

			rs = stmt.executeQuery("select * from ingredient where dish_name='" + name + "'");

			while (rs.next()) {
				int num = rs.getInt("num");
				String ing = rs.getString("name");
				if(num > 1) ing += "s";
				ings.add(num + " " + ing);
			}
			stmt.close();
			rs.close();
			dish = new Dish(name, effect, heal, ings);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return dish;
	}
	
	/**
	 * This searchIngredient method queries the database for the dishes that include this ingredient. 
	 * @param ingredient - The name of an ingredient.
	 * @return - The arrayList that contains all related dishes
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ArrayList<String> searchIngredient(String ingredient){
		if(ingredient == "") return null;
		DBConnect db = null;
		ArrayList<String> dishes = new ArrayList<String>();
		try {
			db = new DBConnect();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from ingredient where name like '%" + ingredient + "%'");
			while(rs.next()) {
				dishes.add(rs.getString("dish_name"));
			}
			stmt.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return dishes;
		
	}
	
	/**
	 * This findEffect method queried the database for a specific effect. 
	 * It first retrieve a list of dishes, shuffle the list, then take the first dish as return value. 
	 * @param effect - the specific effect requested
	 * @return the result dish
	 */
	public static Dish findEffect(String effect){
		if(effect == "") return null;	
		ArrayList<String> dishArr = new ArrayList<String>();
		Dish dish = null;
		String name = "";
		DBConnect db = null;
		try {
			db = new DBConnect();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select name from dish where effect like '%" + effect + "%'");
			while(rs.next()) {
				dishArr.add(rs.getString("name"));
			}
			if(dishArr.size() < 1) return null;
			Collections.shuffle(dishArr);
			name = dishArr.get(0);
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		dish = getDish(name);
		return dish;
	}
	/**
	 * The findMonsterPart effect queries the database for the monsters that drop the specific effect. 
	 * @param part - the monster part that requested
	 * @return The name of the monster
	 */
	public static String findMonsterPart(String part){
		part = part.replace("'", "''");
		if(part == "") return null;	
		String monster = null;
		DBConnect db = null;
		try {
			db = new DBConnect();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select monster from monster where part like '%" + part + "%'");
			rs.next();
			monster = rs.getString("monster");
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return monster;
	}
	
	
	/**
	 * The method findMushroom retrieve all the mushrooms in the ingredients from the database. 
	 * @return a list of mushrooms
	 */
	public static ArrayList<String> findMushroom(){
		DBConnect db = null;
		ArrayList<String> shrooms = null;
		try {
			db = new DBConnect();
			shrooms = new ArrayList<String>();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select distinct name from ingredient where name like '%shroom%'");
			while(rs.next()) {
				shrooms.add(rs.getString("name"));
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return shrooms;
	}
	
	
	
	/**
	 * The method findElixir retrieve all the kinds of elixirs in the dishes from the database. 
	 * @return a list of mushrooms
	 */
	public static ArrayList<String> findElixir(){
		DBConnect db = null;
		ArrayList<String> eli = null;
		try {
			db = new DBConnect();
			eli = new ArrayList<String>();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select distinct name from dish where name like '%elixir%'");
			while(rs.next()) {
				eli.add(rs.getString("name"));
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return eli;
	}
	
	/**
	 * The findMonster method queries the database for the monster parts a specific monster drops.
	 * @param monster - the name of the monster required
	 * @return the list of monster parts
	 */
	public static ArrayList<String> findMonster(String monster){
		DBConnect db = null;
		ArrayList<String> drop = null;
		try {
			db = new DBConnect();
			drop = new ArrayList<String>();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select distinct part from monster where monster like '%"+ monster + "%'");
			while(rs.next()) {
				drop.add(rs.getString("part"));
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return drop;
	}
	
	
	
	/**
	 * The method isMonsterPart queries the database to check whether the input String is a monster part
	 * @param str - the input String which could be a monster part
	 * @return true/false, whether the String is a monster part
	 */
	public static boolean isMonsterPart(String str) {
		str = str.replace("'", "''");
		DBConnect db = null;
		int count = -1;
		try {
			db = new DBConnect();
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select count(*) from monster where part like \'%"+ str + "%\'");
			rs.next();
			count = rs.getInt("count");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		if(count > 0) return true;
		else return false;
	}
	


}
