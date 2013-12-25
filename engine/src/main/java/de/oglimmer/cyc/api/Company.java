package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.mozilla.javascript.RhinoException;

import de.oglimmer.cyc.collections.CycCollections;
import de.oglimmer.cyc.collections.JavaScriptList;

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
	private double cash;

	@Getter
	private HumanResources humanResources = new HumanResources();

	@Getter
	private Grocer grocer;

	@Getter
	private Menu menu;

	private List<Establishment> establishments = new ArrayList<>();

	public Company(Game game, String name, Grocer grocer) {
		this.game = game;
		this.name = name;
		this.grocer = grocer;
		this.menu = new Menu(game);
		this.cash = game.getConstants().getStartCredit();
	}

	void incCash(double change) {
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

	public JavaScriptList<Establishment> getEstablishments(String city) {
		List<Establishment> cityList = new ArrayList<>();
		for (Establishment est : establishments) {
			if (est.getAddress().startsWith(city)) {
				cityList.add(est);
			}
		}
		return CycCollections.unmodifiableList(cityList);
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

	void paySalaries() {
		try {
			for (Employee e : getHumanResources().getEmployees()) {
				game.getResult().get(getName()).addTotalOnSalaries(e.getJobPosition().toString(), e.getSalary());
				decCash(e.getSalary());
				log.debug("{} payed ${} for {}", getName(), e.getSalary(), e.getName());
			}
		} catch (OutOfMoneyException e) {
			log.debug("Company {} is bankrupt", e.getCompany());
		}
	}

	void payRents() {
		try {
			for (Establishment e : establishments) {
				if (e.isRented()) {
					game.getResult().get(getName()).addTotalOnRent(e.getLeaseCost());
					decCash(e.getLeaseCost());
					log.debug("{} payed ${} for {}", getName(), e.getLeaseCost(), e.getAddress());
				}
			}
		} catch (OutOfMoneyException e) {
			log.debug("Company {} is bankrupt", e.getCompany());
		}
	}

	void callLaunch() {
		try {
			if (launch != null) {
				ThreadLocal.setCompany(this);
				launch.run();
			}
		} catch (RhinoException e) {
			if (!(e.getCause() instanceof GameException)) {
				game.getResult().addError(e);
				log.error("Failed to call the company.launch handler. Player " + name + " bankrupt", e);
				setBankruptFromError(e);
			}
		}
	}

	void callWeekly() {
		try {
			if (doWeekly != null) {
				ThreadLocal.setCompany(this);
				doWeekly.run();
			}
		} catch (RhinoException e) {
			if (!(e.getCause() instanceof GameException)) {
				game.getResult().addError(e);
				log.error("Failed to call the company.launch handler. Player " + name + " bankrupt", e);
				setBankruptFromError(e);
			}
		}
		ThreadLocal.resetCompany();
	}

	void callDaily() {
		try {
			if (doDaily != null) {
				ThreadLocal.setCompany(this);
				doDaily.run();
			}
		} catch (RhinoException e) {
			if (!(e.getCause() instanceof GameException)) {
				game.getResult().addError(e);
				log.error("Failed to call the company.launch handler. Player " + name + " bankrupt", e);
				setBankruptFromError(e);
			}
		}
		ThreadLocal.resetCompany();
	}

	void callMonthly() {
		try {
			if (doMonthly != null) {
				ThreadLocal.setCompany(this);
				doMonthly.run();
			}
		} catch (RhinoException e) {
			if (!(e.getCause() instanceof GameException)) {
				game.getResult().addError(e);
				log.error("Failed to call the company.doMonthly handler. Player " + name + " bankrupt", e);
				setBankruptFromError(e);
			}
		}
		ThreadLocal.resetCompany();
	}

	void callHiringProcessCompany(ApplicationProfiles ap) {
		humanResources.callHiringProcessCompany(this, ap);
	}

	void callRealEstateAgent(RealEstateProfiles ap) {
		if (realEstateAgent != null) {
			try {
				ThreadLocal.setCompany(this);
				realEstateAgent.run(ap);
			} catch (RhinoException e) {
				if (!(e.getCause() instanceof GameException)) {
					game.getResult().addError(e);
					log.error("Failed to call the company.realEstateAgent handler. Player " + name + " bankrupt", e);
					setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	void callFoodDelivery(FoodDelivery fd) {
		log.debug("Food delivery for {} = {}", name, fd);
		try {
			if (foodDelivery != null) {
				ThreadLocal.setCompany(this);
				foodDelivery.run(fd);
			}
		} catch (RhinoException e) {
			if (!(e.getCause() instanceof GameException)) {
				game.getResult().addError(e);
				log.error("Failed to call the company.launch handler. Player " + name + " bankrupt", e);
				setBankruptFromError(e);
			}
		}
		ThreadLocal.resetCompany();
	}

	double getTotalAssets() {
		double totalAssets = cash;
		for (Establishment est : establishments) {
			if (!est.isRented()) {
				totalAssets += est.getSalePrice();
			}
		}
		return totalAssets;
	}

	void payCredit() {
		try {
			log.debug("Company {} paid the initial bank credit ${}", name, game.getConstants().getCreditPayback());
			decCash(game.getConstants().getCreditPayback());
		} catch (OutOfMoneyException e) {
			log.debug("Company [] is bankrupt", e.getCompany());
		}
	}

}
