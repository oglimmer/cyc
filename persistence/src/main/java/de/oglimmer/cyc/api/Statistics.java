package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonIgnore;

@Data
public class Statistics {

	private List<StatValue> cash = new ArrayList<>();

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

	@Data
	@NoArgsConstructor
	public static class StatValue {
		@JsonIgnore
		private int day;
		private double valueMin;

		@JsonIgnore
		public int getDay() {
			return day;
		}

		@JsonIgnore
		public void setDay(int day) {
			this.day = day;
		}

		public StatValue(int day, double value) {
			super();
			this.day = day;
			this.valueMin = value;
		}

		private void updateEntry(double value) {
			if (value < getValueMin()) {
				setValueMin(value);
			}
		}

	}
}
