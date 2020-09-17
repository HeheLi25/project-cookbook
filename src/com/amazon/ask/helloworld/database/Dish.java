package com.amazon.ask.helloworld.database;

import java.util.ArrayList;

/**
 * Class Dish: the class of dishes. Used when a dish is retrieved from database.
 * @author YirongLi
 *
 */
public class Dish {
	private String name;
	private String effect;
	private ArrayList<String> ingredients;
	private boolean heal;
	
	public Dish(String name, String effect, boolean heal, ArrayList<String> ingredients) {
		this.name = name;
		this.effect = effect;
		this.heal = heal;
		this.ingredients = ingredients;
	}

	public String getName() {
		return name;
	}

	public String getEffect() {
		return effect;
	}

	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public boolean isHeal() {
		return heal;
	}
	

}
