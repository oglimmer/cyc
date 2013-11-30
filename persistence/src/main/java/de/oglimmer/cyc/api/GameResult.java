package de.oglimmer.cyc.api;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.oglimmer.cyc.util.Average;
import de.oglimmer.cyc.util.CountMap;

public class GameResult {

	@Getter
	@Setter
	private int totalDays;

	@Getter
	@Setter
	private Map<String, PlayerResult> playerResults = new HashMap<>();

	@Getter
	@Setter
	private CountMap<String> guestsTotalPerCity = new CountMap<>();

	/** names of all companies went bankrupt in this run */
	@Getter
	@Setter
	private Set<String> errors = new HashSet<>();

	/** contains all stack traces */
	private StringBuilder error = new StringBuilder();

	public PlayerResult get(String name) {
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
		long maxMoney = -1;
		String desc = "all bankrupt";
		for (PlayerResult pr : playerResults.values()) {
			if (pr.getTotalAssets() > maxMoney) {
				maxMoney = pr.getTotalAssets();
				desc = pr.getName() + " (" + nf.format(maxMoney) + ")";
			}
		}
		return desc;
	}

	public void addError(Throwable t) {
		CharArrayWriter caw = new CharArrayWriter();
		t.printStackTrace(new PrintWriter(caw));

		if (error.length() == 0) {
			error.append("<hr/>");
		}
		error.append(caw.toString().replace("\n", "<br/>"));
	}

	@JsonIgnore
	public StringBuilder getError() {
		return error;
	}

	@JsonIgnore
	public void setError(StringBuilder error) {
		this.error = error;
	}

	public void sortPlayers() {
		Map<String, PlayerResult> tmpMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		tmpMap.putAll(playerResults);
		playerResults = tmpMap;
	}

	@JsonIgnore
	public Map<String, Double> getFoodChart() {
		HashMap<String, Double> base = new HashMap<>();

		double max = -1;
		for (String playName : playerResults.keySet()) {
			PlayerResult pr = playerResults.get(playName);
			for (Entry<String, Average> en : pr.getMenuEntryScore().entrySet()) {
				base.put(en.getKey() + " (" + playName + ")", en.getValue().average());
				if (max < en.getValue().average()) {
					max = en.getValue().average();
				}
			}
		}
		for (String s : base.keySet()) {
			base.put(s, base.get(s) / max);
		}

		ValueComparator vc = new ValueComparator(base);
		TreeMap<String, Double> sorted = new TreeMap<>(vc);
		sorted.putAll(base);

		return sorted;
	}

	@JsonIgnore
	public Map<String, Double> getEstablishmentChart() {
		HashMap<String, Double> base = new HashMap<>();

		double max = -1;
		for (String playName : playerResults.keySet()) {
			PlayerResult pr = playerResults.get(playName);
			for (Entry<String, Average> en : pr.getEstablishmentScore().entrySet()) {
				base.put(en.getKey() + " (" + playName + ")", en.getValue().average());
				if (max < en.getValue().average()) {
					max = en.getValue().average();
				}
			}
		}
		for (String s : base.keySet()) {
			base.put(s, base.get(s) / max);
		}

		ValueComparator vc = new ValueComparator(base);
		TreeMap<String, Double> sorted = new TreeMap<>(vc);
		sorted.putAll(base);

		return sorted;
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
