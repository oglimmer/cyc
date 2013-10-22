package de.oglimmer.cyc.api;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GameResult {

	private int totalDays;
	private Map<String, PlayerResult> playerResults = new HashMap<>();
	private CountMap<String> guestsTotalPerCity = new CountMap<>();

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public void incTotalDays() {
		totalDays++;
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
}
