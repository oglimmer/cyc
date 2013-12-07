package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Company {

	public Runnable launch;
	public Runnable doDaily;
	public Runnable doWeekly;
	public Runnable doMonthly;
	public CallbackFunction realEstateAgent;
	public CallbackFunction foodDelivery;

	@Getter(AccessLevel.PACKAGE)
	private Game game;

	@Getter
	private String name;

	@Getter
	private int cash;

	@Getter
	private HumanResources humanResources = new HumanResources();

	@Getter
	private Grocer grocer;

	@Getter
	private Menu menu = new Menu();

	private List<Establishment> establishments = new ArrayList<>();

	public Company(Game game, String name, Grocer grocer) {
		this.game = game;
		this.name = name;
		this.cash = game.getConstants().getStartCredit();
		this.grocer = grocer;
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

	public JavaScriptList<Establishment> getEstablishments() {
		return CycCollections.unmodifiableList(establishments);
	}

	List<Establishment> getEstablishmentsInt() {
		return establishments;
	}

	boolean isBankrupt() {
		return cash == -1;
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

}
