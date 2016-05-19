package de.oglimmer.cyc.api;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.util.AverageMap;
import de.oglimmer.cyc.util.CountDoubleMap;
import de.oglimmer.cyc.util.CountMap;
import de.oglimmer.cyc.util.HtmlUtil;
import de.oglimmer.cyc.util.LongMutable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thread-safe (parallel access in OpenHours/Guest)
 * 
 * @author oli
 */
@NoArgsConstructor
@Data
public class PlayerResult {
	private static final int MAX_DEBUG_OUTPUT = 12288;

	private String name;
	private double totalAssets;
	private long totalOnRent;
	private long totalRealEstate;
	private long establishmentsByDays;

	private long totalBribe;
	private long totalInterior;

	private int bankruptOnDay;

	private int codeLength;

	private CountMap<String> totalOnSalaries = new CountMap<>();
	private CountMap<String> staffByDays = new CountMap<>();

	private CountMap<String> totalPurchasedFoodUnits = new CountMap<>();
	private CountMap<String> totalPurchasedFoodCosts = new CountMap<>();
	private CountMap<String> totalRottenFood = new CountMap<>();

	private CountMap<String> servedFoodPerTypeUnits = new CountMap<>();
	private CountMap<String> servedFoodPerEstablishmentUnits = new CountMap<>();
	private CountDoubleMap<String> servedFoodPerTypeRevenue = new CountDoubleMap<>();
	private CountDoubleMap<String> servedFoodPerEstablishmentRevenue = new CountDoubleMap<>();

	private CountMap<String> guestsYouPerCity = new CountMap<>();
	private CountMap<String> guestsLeftPerCity = new CountMap<>();
	private CountMap<String> guestsOutOfIngPerCity = new CountMap<>();

	private CountMap<String> missingIngredients = new CountMap<>();
	private AverageMap<String> runtimes = new AverageMap<>();

	private StringBuilder debug = new StringBuilder();
	@JsonIgnore
	private int debugLength;

	private AverageMap<String> menuEntryScore = new AverageMap<>();
	private AverageMap<String> establishmentScore = new AverageMap<>();

	private Statistics statistics = new Statistics();

	public PlayerResult(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	public String getNameSimplified() {
		return HtmlUtil.calcHtmlSafeRepresentation(name);
	}

	public void addTotalOnRent(long rent) {
		this.totalOnRent += rent;
	}

	public void addTotalRealEstate(long realEstate) {
		this.totalRealEstate += realEstate;
	}

	public void addEstablishmentsByDays(long l) {
		this.establishmentsByDays += l;
	}

	public void addStaffByDays(String jobPosition) {
		staffByDays.add(jobPosition, 1);
	}

	public void addTotalOnSalaries(String jobPosition, int salary) {
		totalOnSalaries.add(jobPosition, salary);
	}

	@JsonIgnore
	public long getSalariesTotal() {
		long total = 0;
		for (LongMutable l : totalOnSalaries.values()) {
			total += l.val;
		}
		return total;
	}

	public void addTotalPurchasedFood(String food, int units, int totalCost) {
		totalPurchasedFoodUnits.add(food, units);
		totalPurchasedFoodCosts.add(food, totalCost);
	}

	@JsonIgnore
	public long getPurchasedFoodCostsTotal() {
		long total = 0;
		for (LongMutable l : totalPurchasedFoodCosts.values()) {
			total += l.val;
		}
		return total;
	}

	@JsonIgnore
	public long getServedFoodUnitsTotal() {
		long total = 0;
		for (LongMutable l : servedFoodPerTypeUnits.values()) {
			total += l.val;
		}
		return total;
	}

	@JsonIgnore
	public long getServedFoodEstablishmentUnitsTotal() {
		long total = 0;
		for (LongMutable l : servedFoodPerEstablishmentUnits.values()) {
			total += l.val;
		}
		return total;
	}

	@JsonIgnore
	public double getServedFoodRevenueTotal() {
		double total = 0;
		for (Double l : servedFoodPerTypeRevenue.values()) {
			total += l;
		}
		return total;
	}

	@JsonIgnore
	public double getServedFoodEstablishmentRevenueTotal() {
		double total = 0;
		for (Double l : servedFoodPerEstablishmentRevenue.values()) {
			total += l;
		}
		return total;
	}

	public void addServedFoodServed(String est, String name, double price) {
		servedFoodPerEstablishmentUnits.add(est, 1);
		servedFoodPerTypeUnits.add(name, 1);
		servedFoodPerEstablishmentRevenue.add(est, price);
		servedFoodPerTypeRevenue.add(name, price);
	}

	public void addGuestsOutOfIngPerCity(String city) {
		guestsOutOfIngPerCity.add(city, 1);
	}

	public void addMissingIngredients(Collection<?> missingIngredients) {
		for (Object f : missingIngredients) {
			this.missingIngredients.add(f.toString(), 1);
		}
	}

	public void addTotalBribe(long totalBribe) {
		this.totalBribe += totalBribe;
	}

	public void addTotalInterior(long totalInterior) {
		this.totalInterior += totalInterior;
	}

	public synchronized void addDebug(String debug) {
		if (debug == null) {
			debug = "null";
		}
		if (debugLength < MAX_DEBUG_OUTPUT) {
			debug = HtmlEscapers.htmlEscaper().escape(debug);
			this.debug.append(debug).append("<br/>");
			debugLength += debug.length();
			if (debugLength >= MAX_DEBUG_OUTPUT) {
				this.debug.append(debug).append("[...]");
			}
		}
	}

	public synchronized void overwriteDebug(String debug) {
		debug = HtmlEscapers.htmlEscaper().escape(debug);
		this.debug = new StringBuilder(debug);
	}

	public void addMenuEntryScore(String menuName, int deli) {
		menuEntryScore.add(menuName, deli);
	}

	public void addEstablishmentScore(String address, int score) {
		establishmentScore.add(address, score);
	}

	public void addRunTime(String category, long runTime) {
		runtimes.add(category, runTime / 1000);
	}

	@JsonIgnore
	public void setCode(String mainJavaScript) {
		mainJavaScript = mainJavaScript.replace(" ", "");
		mainJavaScript = mainJavaScript.replace("\n", "");
		mainJavaScript = mainJavaScript.replace("\r", "");
		mainJavaScript = mainJavaScript.replace("\t", "");
		codeLength = mainJavaScript.length();
	}

}
