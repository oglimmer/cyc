package de.oglimmer.cyc.api;

import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;
import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import de.oglimmer.cyc.api.Grocer.BulkOrderDiscount;
import de.oglimmer.cyc.util.EvalSignumFunction;

public enum Constants {
	INSTACE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private Constants() {
		prop.load(Constants.class.getResourceAsStream("/cyc-engine.properties"));
	}

	private Evaluator getEval() {
		Evaluator e = new Evaluator(EvaluationConstants.SINGLE_QUOTE, false, true, false, true);
		e.putFunction(new EvalSignumFunction());
		return e;
	}

	public int getStartCredit() {
		return Integer.parseInt(prop.getProperty("startCredit"));
	}

	public int getCreditPayback() {
		return Integer.parseInt(prop.getProperty("creditPayback"));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getNumberApplicationProfiles(String jobPosition, int noCompanies) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("numberApplicationProfiles." + jobPosition.toLowerCase()));
		eval.putVariable("noCompanies", Integer.toString(noCompanies));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getQualification() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("qualification"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getSalary(int qualification) {
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
	public double getFoodPriceChange(double currentPrice) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("foodPriceChange"));
		eval.putVariable("currentPrice", Double.toString(currentPrice));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getBaseGuests() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("baseGuests"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getRndGuests() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("rndGuests"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getLocationQuality() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("locationQuality"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getLocationSize() {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("locationSize"));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getSalePrice(int locationQuality, int locationSize) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("salePrice"));
		eval.putVariable("locationQuality", Integer.toString(locationQuality));
		eval.putVariable("locationSize", Integer.toString(locationSize));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

	@SneakyThrows(value = EvaluationException.class)
	public int getLeaseCosts(int locationQuality, int locationSize) {
		Evaluator eval = getEval();
		eval.parse(prop.getProperty("leaseCosts"));
		eval.putVariable("locationQuality", Integer.toString(locationQuality));
		eval.putVariable("locationSize", Integer.toString(locationSize));
		return (int) (Double.parseDouble(eval.evaluate()));
	}

}
