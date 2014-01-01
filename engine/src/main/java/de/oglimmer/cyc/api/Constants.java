package de.oglimmer.cyc.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import lombok.SneakyThrows;
import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import de.oglimmer.cyc.api.Grocer.BulkOrderDiscount;
import de.oglimmer.cyc.util.EvalSignumFunction;

/**
 * Evaluator IS NOT thread-safe
 * 
 * @author oli
 */
public class Constants {

	public enum Mode {
		FULL, SINGLE
	}

	private Properties prop = new Properties();
	private Evaluator e;

	@SneakyThrows(value = IOException.class)
	public Constants(Mode mode) {
		prop.load(Constants.class.getResourceAsStream(mode == Mode.FULL ? "/cyc-engine-full.properties"
				: "/cyc-engine-single.properties"));
		e = new Evaluator(EvaluationConstants.SINGLE_QUOTE, false, true, false, true);
		e.putFunction(new EvalSignumFunction());
	}

	private Evaluator getEval() {
		return e;
	}

	public int getStartCredit() {
		return Integer.parseInt(prop.getProperty("startCredit"));
	}

	public int getCreditPayback() {
		return Integer.parseInt(prop.getProperty("creditPayback"));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getNumberApplicationProfiles(String jobPosition, int noCompanies) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("numberApplicationProfiles." + jobPosition.toLowerCase()));
		eval.putVariable("noCompanies", Integer.toString(noCompanies));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getQualification() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("qualification"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getSalary(int qualification) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("salary"));
		eval.putVariable("qualification", Integer.toString(qualification));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	public String getEmployeesFirstname() {
		String[] tmp = prop.getProperty("employeesFirstname").split(",");
		return tmp[(int) (tmp.length * Math.random())];
	}

	public String getEmployeesLastname() {
		String[] tmp = prop.getProperty("employeesLastname").split(",");
		return tmp[(int) (tmp.length * Math.random())];
	}

	public String getCity(Collection<City> exclude) {
		String[] tmp = prop.getProperty("cities").split(",");
		String cityName = null;
		while (cityName == null || contains(exclude, cityName)) {
			cityName = tmp[(int) (tmp.length * Math.random())];
		}
		return cityName;
	}

	private boolean contains(Collection<City> colToSearch, String cityName) {
		for (City c : colToSearch) {
			if (c.getName().equals(cityName)) {
				return true;
			}
		}
		return false;
	}

	public float getSellFactorInteriorAccessories() {
		return Float.parseFloat(prop.getProperty("sellFactorInteriorAccessories"));
	}

	public BulkOrderDiscount[] getBulkOrderDiscounts() {
		String[] tmp = prop.getProperty("bulkOrderDiscounts").split(",");
		BulkOrderDiscount[] bods = new BulkOrderDiscount[tmp.length];
		int i = 0;
		for (String bodDefinition : tmp) {
			String[] innerTmp = bodDefinition.split("\\>");
			bods[i++] = new BulkOrderDiscount(Integer.parseInt(innerTmp[0]), Double.parseDouble(innerTmp[1]));
		}
		return bods;
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized double getFoodPriceChange(double currentPrice) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("foodPriceChange"));
		eval.putVariable("currentPrice", Double.toString(currentPrice));
		return Double.parseDouble(eval.evaluate());
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getBaseGuests(int noCompanies) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("baseGuests"));
		eval.putVariable("noCompanies", Integer.toString(noCompanies));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getRndGuests(int noCompanies) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("rndGuests"));
		eval.putVariable("noCompanies", Integer.toString(noCompanies));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getLocationQuality() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("locationQuality"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getLocationSize() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("locationSize"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getSalePrice(int locationQuality, int locationSize) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("salePrice"));
		eval.putVariable("locationQuality", Integer.toString(locationQuality));
		eval.putVariable("locationSize", Integer.toString(locationSize));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getLeaseCosts(int locationQuality, int locationSize) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("leaseCosts"));
		eval.putVariable("locationQuality", Integer.toString(locationQuality));
		eval.putVariable("locationSize", Integer.toString(locationSize));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getNumberCities(int noPlayer) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("numberCities"));
		eval.putVariable("noPlayer", Integer.toString(noPlayer));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	public int getRuntimeYear() {
		return Integer.parseInt(prop.getProperty("runtime.year"));
	}

	public int getRuntimeMonth() {
		return Integer.parseInt(prop.getProperty("runtime.month"));
	}

	public int getRuntimeDay() {
		return Integer.parseInt(prop.getProperty("runtime.day"));
	}

	@SneakyThrows(value = EvaluationException.class)
	public synchronized int getNumberRealEstateProfiles(int noCompanies) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("numberRealEstateProfiles"));
		eval.putVariable("noCompanies", Integer.toString(noCompanies));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	public double getMenuPriceFactor() {
		return Double.parseDouble(prop.getProperty("menuPriceFactor"));
	}
}
