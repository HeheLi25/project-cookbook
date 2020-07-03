package com.amazon.ask.helloworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnect {
	private final String DBDRIVER = "org.postgresql.Driver";

	private final String DBURL = "jdbc:postgresql://someURL:5434/postgres";
	private final String DBUSER = "postgres";
	private final String DBPASSWORD = "password";
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
				ings.add(rs.getInt("num") + " " + rs.getString("name"));
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

	public static void main(String[] args) {
		String dish = "fried egg and rice";
		String speechText = "";
		Dish dishObj = null;
		try {
			dishObj = DBConnect.getDish(dish);
		} catch (Exception e) {
			speechText = "Sorry, something wrong happened with the database. I'll fix it as soon as I can";
		}
		if (dishObj == null) {
			speechText = "Currently, I don't know how to make "+ dish +", I promise I will keep learning. ";
		} else {
			speechText = dishObj.getName() + " is made of these ingredients: ";
			for (String s : dishObj.getIngredients()) {
				speechText = speechText + s + ", ";
			}
			speechText += "and its effect is " + dishObj.getEffect() + ". ";
			if (!dishObj.isHeal()) {
				speechText += "But it can not heal you.";
			}
		}
		System.out.println(speechText);
	}

}
