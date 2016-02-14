package de.oglimmer.cyc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.CouchDbDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class GameWinners extends CouchDbDocument {

	private static final long serialVersionUID = 1L;

	private int t = 1;
	private String refGameRunId;
	private Date startTime;
	private List<GameWinnerEntry> participants = new ArrayList<>();

	@JsonIgnore
	public String getWinnerName() {
		double maxMoney = -1;
		String desc = "";
		for (GameWinnerEntry pr : participants) {
			if (pr.getTotalAssets() > maxMoney) {
				maxMoney = pr.getTotalAssets();
				desc = pr.getName();
			}
		}
		return desc;
	}

	@JsonIgnore
	public double getWinnerTotal() {
		double maxMoney = -1;
		for (GameWinnerEntry pr : participants) {
			if (pr.getTotalAssets() > maxMoney) {
				maxMoney = pr.getTotalAssets();
			}
		}
		return maxMoney;
	}

	@Data
	@NoArgsConstructor
	public static class GameWinnerEntry {
		public GameWinnerEntry(String name, double totalAssets) {
			this.name = name;
			this.totalAssets = totalAssets;
		}

		private String name;
		private double totalAssets;
	}
}
