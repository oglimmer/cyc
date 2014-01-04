package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	private double price;

	private List<Food> ingredients = new ArrayList<>();

	private Game game;

	private _Cache cache;

	MenuEntry(Game game, String name, String[] ingredients, double price) {
		this.game = game;
		this.name = HtmlEscapers.htmlEscaper().escape(name);
		this.price = price;
		for (String i : ingredients) {
			this.ingredients.add(Food.valueOf(i));
		}
		cache = new _Cache(game);
	}

	public JavaScriptList<Food> getIngredients() {
		return CycCollections.unmodifiableList(ingredients);
	}

	Collection<Food> getIngredientsInt() {
		return ingredients;
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

	/**
	 * a value of 1 is a perfect price<br/>
	 * a value of 0.5 means it is too expensive by factor 2 a value of 2 means it is too cheap by factor 2
	 */
	double getScore() {
		double netCost = 0;
		for (Food f : ingredients) {
			netCost += f.getBasePrice();
		}
		return netCost / (price / game.getConstants().getMenuPriceFactor());
	}

	/**
	 * base deliciousness is 5. max 10, min 0.
	 */
	int getDeliciousness() {
		return cache.getValue();
	}

	@Override
	public String toString() {
		return "MenuEntry [name=" + name + ", ingredients=" + Arrays.toString(ingredients.toArray()) + ", price="
				+ price + "]";
	}

	class _Cache extends Cache<Integer> {

		public _Cache(Game game) {
			super(Type.DAILY, game, "MenuEntry");
		}

		@Override
		protected Integer fetchValue() {
			return MenuEntryRule.INSTACE.getDeliciousness(ingredients, price);
		}

	}
}