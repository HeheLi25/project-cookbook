package com.amazon.ask.helloworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

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

	public void close() {
		try {
			this.conn.close();
			// System.out.println("Connection closed.");
		} catch (Exception e) {
		}
	}

	
	/**
	 * This getDish method query the database for the information of a dish. 
	 * @param name - The name of the dish, used to generate the query. 
	 * @return - the Dish object that contains the information of the dish.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Dish getDish(String name) throws ClassNotFoundException, SQLException {
		if(name == "") return null;
		DBConnect db = new DBConnect();
		Dish dish = null;
		try {
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
	 * This searchIngredient method query the database for the dishes that include this ingredient. 
	 * @param ingredient - The name of an ingredient.
	 * @return - The arrayList that contains all related dishes
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ArrayList<String> searchIngredient(String ingredient) throws ClassNotFoundException, SQLException{
		if(ingredient == "") return null;
		DBConnect db = new DBConnect();
		ArrayList<String> dishes = new ArrayList<String>();
		try {
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
	
	public static Dish findEffect(String effect) throws ClassNotFoundException, SQLException{
		if(effect == "") return null;
		DBConnect db = new DBConnect();
		ArrayList<String> dishArr = new ArrayList<String>();
		Dish dish = null;
		String name = "";
		try {
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
	

	
	
	public static void main(String[] args) {
		String test = "apple";
		Dish dish = null;
		ArrayList<String> arr = null;
		try {
			arr = searchIngredient(test);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Name:" + dish.getName());
		for(String n: arr) {
			System.out.println(n);
		}
	}

}
