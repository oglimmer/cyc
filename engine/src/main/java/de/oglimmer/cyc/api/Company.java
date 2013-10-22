package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Company {
	private Logger log = LoggerFactory.getLogger(Company.class);

	private Game game;

	private String name;

	public Runnable launch;
	public Runnable doDaily;
	public Runnable doWeekly;
	public Runnable doMonthly;
	public CallbackFunction realEstateAgent;
	public CallbackFunction foodDelivery;

	private int cash;

	private List<Establishment> establishments = new ArrayList<>();

	private HumanResources humanResources = new HumanResources();

	private Grocer grocer;

	private Menu menu = new Menu();

	public Company(Game game, String name, Grocer grocer) {
		this.game = game;
		this.name = name;
		this.cash = 50_000;
		this.grocer = grocer;
	}

	public HumanResources getHumanResources() {
		return humanResources;
	}

	public int getCash() {
		return cash;
	}

	public String getName() {
		return name;
	}

	void incCash(int change) {
		assert change >= 0;
		cash += change;
	}

	void decCash(int change) throws OutOfMoneyException {
		assert change >= 0;
		if (cash - change < 0) {
			cash = -1;
			throw new OutOfMoneyException(this);
		}
		cash -= change;
	}

	public List<Establishment> getEstablishments() {
		return Collections.unmodifiableList(establishments);
	}

	List<Establishment> getEstablishmentsInt() {
		return establishments;
	}

	public Grocer getGrocer() {
		return grocer;
	}

	boolean isBankrupt() {
		return cash == -1;
	}

	public Menu getMenu() {
		return menu;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + "]";
	}

	void setBankrupt() {
		log.debug(getName() + " went bankrupt due to a coding error.");
		cash = -1;
	}

	Game getGame() {
		return game;
	}
}
