package com.amazon.ask.helloworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	
	
//	public static void main(String[] args) {
//		String speechText = "";
//		ArrayList<String> dishes = null;
//		String ingredient = "apple";
//		try {
//			dishes = DBConnect.searchIngredient(ingredient);
//			if(dishes == null)
//				speechText = "Sorry, I don't know a lot about "+ ingredient 
//				+ ". If it is a monster part or an animal, try to ask me about animal types, "
//				+ "such as \"tough animal\", \"mighty animal\" or just \"monster part\".";
//			else {
//				speechText = "With " + ingredient + ", you can make ";
//				if(dishes.size() == 1)
//					speechText += dishes.get(0);
//				else {
//					for(int i = 0; i < dishes.size(); i++) {
//						if(i == dishes.size() - 1)
//							speechText += "and " + dishes.get(i) + ". ";
//						else
//							speechText += dishes.get(i) + ", ";
//					}
//				}
//				speechText += "Ask me if you want to know more about any dish. ";
//			}
//			System.out.println(speechText);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}

}
