package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.collections.CycCollections;
import de.oglimmer.cyc.collections.JavaScriptList;
import de.oglimmer.cyc.util.PublicAPI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class MenuEntry implements IMenuEntry {

	@Getter
	private String name;

	@Getter
	@Setter
	private double price;

	private List<Food> ingredients = new ArrayList<>();

	@Getter(AccessLevel.PACKAGE)
	private Game game;

	@Getter(AccessLevel.PACKAGE)
	private _Cache cache;
	
	@Getter
	private MenuEntrySecret secret = new MenuEntrySecret(this);

	MenuEntry(Game game, String name, String[] ingredients, double price) {
		this.game = game;
		this.name = HtmlEscapers.htmlEscaper().escape(name);
		this.price = price;
		for (String i : ingredients) {
			this.ingredients.add(Food.valueOf(i));
		}
		cache = new _Cache(game);
	}

	@PublicAPI
	public JavaScriptList<Food> getIngredients() {
		return CycCollections.unmodifiableList(ingredients);
	}

	Collection<Food> getIngredientsInt() {
		return ingredients;
	}

	@PublicAPI
	public void addIngredient(String i) {
		this.ingredients.add(Food.valueOf(i));
	}

	@PublicAPI
	public void removeIngredient(String i) {
		for (Iterator<Food> it = ingredients.iterator(); it.hasNext();) {
			Food food = it.next();
			if (food.equals(i)) {
				it.remove();
			}
		}
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
			return MenuEntryRule.INSTACE.getDeliciousness(ingredients);
		}

	}
}