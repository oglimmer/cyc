package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.collections.CycCollections;
import de.oglimmer.cyc.collections.JavaScriptList;

public class MenuEntry {

	@Getter
	private String name;

	@Getter
	@Setter
	private int price;

	private List<Food> ingredients = new ArrayList<>();

	private Integer deliCache;

	public MenuEntry(String name, String[] ingredients, int price) {
		this.name = HtmlEscapers.htmlEscaper().escape(name);
		this.price = price;
		for (String i : ingredients) {
			this.ingredients.add(Food.valueOf(i));
		}
	}

	public JavaScriptList<Food> getIngredients() {
		return CycCollections.unmodifiableList(ingredients);
	}

	public void addIngredient(String i) {
		this.ingredients.add(Food.valueOf(i));
		deliCache = MenuEntryRule.INSTACE.getDeliciousness(ingredients, price);
	}

	public void removeIngredient(String i) {
		for (Iterator<Food> it = ingredients.iterator(); it.hasNext();) {
			Food food = it.next();
			if (food.equals(i)) {
				it.remove();
			}
		}
		deliCache = MenuEntryRule.INSTACE.getDeliciousness(ingredients, price);
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
		return netCost / (getPrice() / 3);
	}

	/**
	 * base deliciouness is 5. max 10, min 0.
	 */
	int getDeliciousness() {
		if (deliCache == null) {
			deliCache = MenuEntryRule.INSTACE.getDeliciousness(ingredients, price);
		}
		return deliCache;
	}

	@Override
	public String toString() {
		return "MenuEntry [name=" + name + ", ingredients=" + Arrays.toString(ingredients.toArray()) + ", price="
				+ price + "]";
	}

}