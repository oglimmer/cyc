package de.oglimmer.cyc.api;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GameResult {

	private int totalDays;
	private Map<String, PlayerResult> playerResults = new HashMap<>();
	private CountMap<String> guestsTotalPerCity = new CountMap<>();
	/** names of all companies went bankrupt in this run */
	private Set<String> errors = new HashSet<>();

	/** contains all stack traces */
	@JsonIgnore
	private StringBuilder error = new StringBuilder();

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public Map<String, PlayerResult> getPlayerResults() {
		return playerResults;
	}

	public void setPlayerResults(Map<String, PlayerResult> playerResults) {
		this.playerResults = playerResults;
	}

	public PlayerResult get(String name) {
		PlayerResult pr = playerResults.get(name);
		if (pr == null) {
			pr = new PlayerResult(name);
			playerResults.put(name, pr);
		}
		return pr;
	}

	public CountMap<String> getGuestsTotalPerCity() {
		return guestsTotalPerCity;
	}

	public void setGuestsTotalPerCity(CountMap<String> guestsTotalPerCity) {
		this.guestsTotalPerCity = guestsTotalPerCity;
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

	public Set<String> getErrors() {
		return errors;
	}

	public void setErrors(Set<String> errors) {
		this.errors = errors;
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

}
