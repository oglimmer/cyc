package de.oglimmer.cyc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class GameWinners {

	private String id;
	private String revision;

	private int t = 1;
	private String refGameRunId;
	private Date startTime;
	private List<GameWinnerEntry> participants = new ArrayList<>();

	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}

	@JsonProperty("_rev")
	public void setRevision(String revision) {
		this.revision = revision;
	}

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
