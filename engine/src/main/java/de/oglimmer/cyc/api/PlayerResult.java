package de.oglimmer.cyc.api;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PlayerResult {
	private String name;
	private long totalAssets;
	private long totalOnRent;
	private long totalRealEstate;
	private long establishmentsByDays;

	private CountMap<String> totalOnSalaries = new CountMap<>();
	private CountMap<String> staffByDays = new CountMap<>();

	private CountMap<String> totalPurchasedFoodUnits = new CountMap<>();
	private CountMap<String> totalPurchasedFoodCosts = new CountMap<>();
	private CountMap<String> totalRottenFood = new CountMap<>();

	private CountMap<String> servedFoodPerTypeUnits = new CountMap<>();
	private CountMap<String> servedFoodPerEstablishmentUnits = new CountMap<>();
	private CountMap<String> servedFoodPerTypeRevenue = new CountMap<>();
	private CountMap<String> servedFoodPerEstablishmentRevenue = new CountMap<>();

	private CountMap<String> guestsYouPerCity = new CountMap<>();
	private CountMap<String> guestsLeftPerCity = new CountMap<>();
	private CountMap<String> guestsOutOfIngPerCity = new CountMap<>();

	private CountMap<String> missingIngredients = new CountMap<>();

	public PlayerResult() {
	}

	public PlayerResult(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTotalOnRent() {
		return totalOnRent;
	}

	public void setTotalOnRent(long totalOnRent) {
		this.totalOnRent = totalOnRent;
	}

	public void addTotalOnRent(long rent) {
		this.totalOnRent += rent;
	}

	public long getTotalRealEstate() {
		return totalRealEstate;
	}

	public void setTotalRealEstate(long totalRealEstate) {
		this.totalRealEstate = totalRealEstate;
	}

	public void addTotalRealEstate(long realEstate) {
		this.totalRealEstate += realEstate;
	}

	public long getEstablishmentsByDays() {
		return establishmentsByDays;
	}

	public void setEstablishmentsByDays(long establishmentsByDays) {
		this.establishmentsByDays = establishmentsByDays;
	}

	public void addEstablishmentsByDays(long l) {
		this.establishmentsByDays += l;
	}

	public long getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(long totalAssets) {
		this.totalAssets = totalAssets;
	}

	public CountMap<String> getStaffByDays() {
		return staffByDays;
	}

	public void setStaffByDays(CountMap<String> staffByDays) {
		this.staffByDays = staffByDays;
	}

	public void addTotalNumSalariesPaid(String jobPosition, int salary, int days) {
		totalOnSalaries.add(jobPosition, salary);
		staffByDays.add(jobPosition, days);
	}

	public CountMap<String> getTotalOnSalaries() {
		return totalOnSalaries;
	}

	public void setTotalOnSalaries(CountMap<String> totalOnSalaries) {
		this.totalOnSalaries = totalOnSalaries;
	}

	public CountMap<String> getTotalPurchasedFoodUnits() {
		return totalPurchasedFoodUnits;
	}

	public void setTotalPurchasedFoodUnits(CountMap<String> totalFood) {
		this.totalPurchasedFoodUnits = totalFood;
	}

	public CountMap<String> getTotalPurchasedFoodCosts() {
		return totalPurchasedFoodCosts;
	}

	public void setTotalPurchasedFoodCosts(CountMap<String> totalFood) {
		this.totalPurchasedFoodCosts = totalFood;
	}

	public void addTotalPurchasedFood(String food, int units, int totalCost) {
		totalPurchasedFoodUnits.add(food, units);
		totalPurchasedFoodCosts.add(food, totalCost);
	}

	public CountMap<String> getTotalRottenFood() {
		return totalRottenFood;
	}

	public void setTotalRottenFood(CountMap<String> totalRottenFood) {
		this.totalRottenFood = totalRottenFood;
	}

	public CountMap<String> getServedFoodPerTypeUnits() {
		return servedFoodPerTypeUnits;
	}

	@JsonIgnore
	public long getServedFoodUnitsTotal() {
		long total = 0;
		for (Long l : servedFoodPerTypeUnits.values()) {
			total += l;
		}
		return total;
	}

	public void setServedFoodPerTypeUnits(CountMap<String> servedFoodPerType) {
		this.servedFoodPerTypeUnits = servedFoodPerType;
	}

	public CountMap<String> getServedFoodPerEstablishmentUnits() {
		return servedFoodPerEstablishmentUnits;
	}

	public void setServedFoodPerEstablishmentUnits(CountMap<String> servedFoodPerEstablishment) {
		this.servedFoodPerEstablishmentUnits = servedFoodPerEstablishment;
	}

	@JsonIgnore
	public long getServedFoodEstablishmentUnitsTotal() {
		long total = 0;
		for (Long l : servedFoodPerEstablishmentUnits.values()) {
			total += l;
		}
		return total;
	}

	public CountMap<String> getServedFoodPerTypeRevenue() {
		return servedFoodPerTypeRevenue;
	}

	@JsonIgnore
	public long getServedFoodRevenueTotal() {
		long total = 0;
		for (Long l : servedFoodPerTypeRevenue.values()) {
			total += l;
		}
		return total;
	}

	public void setServedFoodPerTypeRevenue(CountMap<String> servedFoodPerTypeRevenue) {
		this.servedFoodPerTypeRevenue = servedFoodPerTypeRevenue;
	}

	public CountMap<String> getServedFoodPerEstablishmentRevenue() {
		return servedFoodPerEstablishmentRevenue;
	}

	public void setServedFoodPerEstablishmentRevenue(CountMap<String> servedFoodPerEstablishmentRevenue) {
		this.servedFoodPerEstablishmentRevenue = servedFoodPerEstablishmentRevenue;
	}

	@JsonIgnore
	public long getServedFoodEstablishmentRevenueTotal() {
		long total = 0;
		for (Long l : servedFoodPerEstablishmentRevenue.values()) {
			total += l;
		}
		return total;
	}

	public void addServedFoodServed(String est, String name, int price) {
		servedFoodPerEstablishmentUnits.add(est, 1);
		servedFoodPerTypeUnits.add(name, 1);
		servedFoodPerEstablishmentRevenue.add(est, price);
		servedFoodPerTypeRevenue.add(name, price);
	}

	public CountMap<String> getGuestsYouPerCity() {
		return guestsYouPerCity;
	}

	public void setGuestsYouPerCity(CountMap<String> guestsYouPerCity) {
		this.guestsYouPerCity = guestsYouPerCity;
	}

	public CountMap<String> getGuestsLeftPerCity() {
		return guestsLeftPerCity;
	}

	public void setGuestsLeftPerCity(CountMap<String> guestsLeftPerCity) {
		this.guestsLeftPerCity = guestsLeftPerCity;
	}

	public CountMap<String> getGuestsOutOfIngPerCity() {
		return guestsOutOfIngPerCity;
	}

	public void setGuestsOutOfIngPerCity(CountMap<String> guestsOutOfIngPerCity) {
		this.guestsOutOfIngPerCity = guestsOutOfIngPerCity;
	}

	public CountMap<String> getMissingIngredients() {
		return missingIngredients;
	}

	public void setMissingIngredients(CountMap<String> missingIngredients) {
		this.missingIngredients = missingIngredients;
	}

	public void addGuestsOutOfIngPerCity(String city, Set<Food> missingIngredients) {
		this.guestsOutOfIngPerCity.add(city, 1);
		for (Food f : missingIngredients) {
			this.missingIngredients.add(f.toString(), 1);
		}
	}

}
