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
		assert cash > -1;
		if (cash <= -1) {
			log.error("{} got money even if bankrupt", name);
		} else {
			cash += change;
		}
	}

	void decCash(int change) throws OutOfMoneyException {
		assert change >= 0;
		assert cash > -1;
		if (cash - change < 0) {
			setBankrupt();
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

	void setBankruptFromError(Throwable t) {
		game.getResult().getErrors().add(getName());
		setBankrupt();
	}

	private void setBankrupt() {
		log.debug("{} became bankrupt on {}", name, game.getCurrentDay());
		game.getResult().get(getName()).setBankruptOnDay(game.getCurrentDay());
		cash = -1;
	}

	Game getGame() {
		return game;
	}
}
