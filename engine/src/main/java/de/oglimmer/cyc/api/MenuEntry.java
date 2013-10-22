package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MenuEntry {
	private String name;
	private List<Food> ingredients = new ArrayList<>();
	private int price;

	public MenuEntry(String name, String[] ingredients, int price) {
		this.name = name;
		this.price = price;
		for (String i : ingredients) {
			this.ingredients.add(Food.valueOf(i));
		}
	}

	public String getName() {
		return name;
	}

	public List<Food> getIngredients() {
		return Collections.unmodifiableList(ingredients);
	}

	public void addIngredient(String i) {
		this.ingredients.add(Food.valueOf(i));
	}

	public void removeIngredient(String i) {
		for (Iterator<Food> it = ingredients.iterator(); it.hasNext();) {
			Food food = it.next();
			if (food.equals(i)) {
				it.remove();
			}
		}
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * a value of 1 is a perfect price<br/>
	 * a value of 0.5 means it is too expensive by factor 2
	 */
	double getScore() {
		double netCost = 0;
		for (Food f : getIngredients()) {
			netCost += f.getBasePrice();
		}
		double foodScore = netCost / (getPrice() / 3);
		return foodScore;
	}

	/**
	 * base deliciouness is 5. max 10, min 0.
	 */
	int getDeliciousness() {
		MenuEntryRule r = new MenuEntryRule();
		return r.getDeliciousness(ingredients, price, Food.class);
	}

	@Override
	public String toString() {
		return "MenuEntry [name=" + name + ", ingredients=" + Arrays.toString(ingredients.toArray()) + ", price="
				+ price + "]";
	}
}