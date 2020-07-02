package com.amazon.ask.helloworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnect {
	private final String DBDRIVER = "org.postgresql.Driver";

	private final String DBURL = "jdbc:postgresql://127.0.0.1:5432/postgres";
	private final String DBUSER = "postgres";
	private final String DBPASSWORD = "000625";
	private static String message = "";
	private Connection conn = null;

	public DBConnect() {
		try {
			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
			// System.out.println("Database connected.");
		} catch (Exception e) {
			System.out.println("Fail connection:" + e.getMessage());
			message = e.getMessage();
			e.printStackTrace();
		}
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

	public static Dish getDish(String name) {
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
				ings.add(rs.getString("name"));
			}
			stmt.close();
			rs.close();
			dish = new Dish(name, effect, heal, ings);
		} catch (Exception e) {
			ArrayList<String> arr = new ArrayList<String>();
			dish = new Dish(message, "1", false, arr);
			e.printStackTrace();
		} finally {
			db.close();
		}

		return dish;
	}

	public static void main(String[] args) {
		String dish = "hearty fried wild greens";
		Dish dishObj = getDish(dish);
		String speechText = dish + "is made of these ingredients: ";
		for (String s : dishObj.getIngredients()) {
			speechText = speechText + s + ", ";
		}
		speechText += "and its effect is " + dishObj.getEffect() + ". ";
		if (!dishObj.isHeal()) {
			speechText += "But it can not heal you.";
		}
		System.out.println(speechText);
	}

}
