package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonIgnore;

@Data
public class Statistics {

	private List<StatValue> cash = new ArrayList<>();
	private Map<Integer, List<StatValue>> custom = new HashMap<>();
	private Map<Integer, String> customNames = new HashMap<>();

	@JsonIgnore
	public void setCustomStatistics(int day, int type, double value) {
		if (type >= 0 && type <= 5) {
			List<StatValue> list = custom.get(type);
			if (list == null) {
				list = new ArrayList<>();
				custom.put(type, list);
			}
			list.add(new StatValue(day, value));
		}
	}

	@JsonIgnore
	public void setCustomStatisticsName(int type, String name) {
		if (name.length() > 256) {
			name = name.substring(0, 256);
		}
		customNames.put(type, name);
	}

	@JsonIgnore
	public String getCustomStatisticsName(int type) {
		String name = customNames.get(type);
		if (name == null) {
			name = "index " + type;
		}
		return name;
	}

	public void addCash(int day, double value) {
		if (cash.isEmpty()) {
			cash.add(new StatValue(day, value));
		} else {
			StatValue sv = cash.get(cash.size() - 1);
			if (sv.getDay() == day) {
				sv.updateEntry(value);
			} else {
				cash.add(new StatValue(day, value));
			}
		}
	}

	@JsonIgnore
	public String getCustomHtml() {
		StringBuilder buff = new StringBuilder();
		for (Integer type : custom.keySet()) {
			if (buff.length() != 0) {
				buff.append(",");
			}
			List<Long> fixedList = new ArrayList<>(360);
			for (StatValue sv : custom.get(type)) {
				fixedList.add((long) sv.getValueMin());
			}
			buff.append(fixedList.toString());
		}
		return buff.toString();
	}

	@JsonIgnore
	public List<Long> getCashHtml() {
		List<Long> fixedList = new ArrayList<>(360);
		for (StatValue sv : cash) {
			fixedList.add((long) sv.getValueMin());
		}
		for (int i = fixedList.size(); i < 360; i++) {
			fixedList.add(Long.valueOf(-1L));
		}
		return fixedList;
	}

	@Data
	@NoArgsConstructor
	public static class StatValue {
		@JsonIgnore
		private int day;
		private double valueMin;

		public StatValue(int day, double value) {
			super();
			this.day = day;
			this.valueMin = value;
		}

		@JsonIgnore
		public int getDay() {
			return day;
		}

		@JsonIgnore
		public void setDay(int day) {
			this.day = day;
		}

		private void updateEntry(double value) {
			if (value < getValueMin()) {
				setValueMin(value);
			}
		}

	}

}
