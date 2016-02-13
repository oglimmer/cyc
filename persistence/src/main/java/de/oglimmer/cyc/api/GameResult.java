package de.oglimmer.cyc.api;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.oglimmer.cyc.api.Statistics.StatValue;
import de.oglimmer.cyc.util.Average;
import de.oglimmer.cyc.util.CountMap;
import lombok.Getter;
import lombok.Setter;

/**
 * Thread-safe (parallel access in OpenHours/Guest)
 * 
 * @author oli
 */
public class GameResult {

	@Getter
	@Setter
	private int totalDays;

	@Getter
	private Map<String, PlayerResult> playerResults = Collections.synchronizedMap(new HashMap<String, PlayerResult>());

	@Getter
	private CountMap<String> guestsTotalPerCity = new CountMap<>();

	/** names of all companies went bankrupt in this run */
	@Getter
	@Setter
	private Set<String> errors = Collections.synchronizedSet(new HashSet<String>());

	/** contains all stack traces */
	private StringBuilder error = new StringBuilder();

	public PlayerResult getCreateNotExists(String name) {
		PlayerResult pr = playerResults.get(name);
		if (pr == null) {
			pr = new PlayerResult(name);
			playerResults.put(name, pr);
		}
		return pr;
	}

	@JsonIgnore
	public String getWinner() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		double maxMoney = -1;
		String desc = "all bankrupt";
		for (PlayerResult pr : playerResults.values()) {
			if (pr.getTotalAssets() > maxMoney) {
				maxMoney = pr.getTotalAssets();
				desc = pr.getName() + " (" + nf.format(maxMoney) + ")";
			}
		}
		return desc;
	}

	@JsonIgnore
	public int getUpperCashBoundary() {
		double maxMoney = -1;
		for (PlayerResult pr : playerResults.values()) {
			for (StatValue sv : pr.getStatistics().getCash()) {
				if (sv.getValueMin() > maxMoney) {
					maxMoney = sv.getValueMin();
				}
			}
		}
		if (maxMoney <= 100_000) {
			return 100_000;
		}
		return ((int) (maxMoney / 100_000) + 1) * 100_000;
	}

	public synchronized void addError(String formattedStackTrace) {
		if (error.length() == 0) {
			error.append("<hr/>");
		}
		error.append(formattedStackTrace);		
	}

	@JsonIgnore
	public StringBuilder getError() {
		return error;
	}

	@JsonIgnore
	public void setError(StringBuilder error) {
		this.error = error;
	}

	public void sortByPlayerName() {
		Map<String, PlayerResult> tmpMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		tmpMap.putAll(playerResults);
		playerResults = Collections.synchronizedMap(tmpMap);
	}

	public void sortByTotalAssetsDesc() {
		Map<String, PlayerResult> tmpMap = new LinkedHashMap<>();		
		playerResults.entrySet()
				.stream().sorted(Comparator
						.comparing((Map.Entry<String, PlayerResult> e) -> e.getValue().getTotalAssets()).reversed())
				.forEachOrdered(e -> tmpMap.put(e.getKey(), e.getValue()));
		playerResults = Collections.synchronizedMap(tmpMap);				
	}
	
	@JsonIgnore
	public Map<String, PlayerResult> getPlayerResultsSortedByCashDesc() {
		final Map<String, PlayerResult> nonSyncedMap = new HashMap<>(playerResults);
		Map<String, PlayerResult> tmpMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				PlayerResult pr1 = nonSyncedMap.get(o1);
				PlayerResult pr2 = nonSyncedMap.get(o2);
				return -1 * Double.compare(pr1.getTotalAssets(), pr2.getTotalAssets());
			}
		});
		tmpMap.putAll(playerResults);
		return tmpMap;
	}

	@JsonIgnore
	public Map<String, Double> getFoodChart() {
		Map<String, Double> totalMap = new HashMap<>();
		double max = -1;

		for (String playName : playerResults.keySet()) {
			PlayerResult pr = playerResults.get(playName);
			max = buildTotalMap(totalMap, playName, pr.getMenuEntryScore(), max);
		}

		buildResultMap(totalMap, max);

		return sortByValue(totalMap);
	}

	@JsonIgnore
	public Map<String, Double> getEstablishmentChart() {
		Map<String, Double> totalMap = new HashMap<>();
		double max = -1;

		for (String playName : playerResults.keySet()) {
			PlayerResult pr = playerResults.get(playName);
			max = buildTotalMap(totalMap, playName, pr.getEstablishmentScore(), max);
		}

		buildResultMap(totalMap, max);

		return sortByValue(totalMap);
	}

	private Map<String, Double> sortByValue(Map<String, Double> totalMap) {
		ValueComparator vc = new ValueComparator(totalMap);
		TreeMap<String, Double> sorted = new TreeMap<>(vc);
		sorted.putAll(totalMap);
		return sorted;
	}

	private void buildResultMap(Map<String, Double> totalMap, double max) {
		for (String s : totalMap.keySet()) {
			double val = Math.round(totalMap.get(s) / max * 20) / 20d;
			totalMap.put(s, val);
		}
	}

	private double buildTotalMap(Map<String, Double> totalMap, String playerName, Map<String, Average> map,
			double baseValue) {
		double max = baseValue;
		for (Entry<String, Average> en : map.entrySet()) {
			double val = en.getValue().average();
			totalMap.put(en.getKey() + " (" + playerName + ")", val);
			if (max < en.getValue().average()) {
				max = en.getValue().average();
			}
		}
		return max;
	}
	
	@JsonIgnore
	public Map<String, PlayerResult> getPlayerResultsTop15() {
		Map<String, PlayerResult> top15 = new HashMap<>();
		int counter = 0;
		for (Map.Entry<String, PlayerResult> en : getPlayerResultsSortedByCashDesc().entrySet()) {
			top15.put(en.getKey(), en.getValue());
			counter++;
			if (counter == 15) {
				break;
			}
		}
		return top15;
	}

}

class ValueComparator implements Comparator<String> {

	private Map<String, Double> base;

	ValueComparator(Map<String, Double> base) {
		this.base = base;
	}

	@Override
	public int compare(String a, String b) {
		Double x = base.get(a);
		Double y = base.get(b);
		if (x.equals(y)) {
			return a.compareTo(b);
		}
		return -1 * x.compareTo(y);
	}
}
