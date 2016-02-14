package de.oglimmer.cyc.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.Data;
import lombok.NoArgsConstructor;

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
			long l = (long) sv.getValueMin();
			if (l < 0) {
				l = 0;
			}
			fixedList.add(l);
		}
		for (int i = fixedList.size(); i < 360; i++) {
			fixedList.add(Long.valueOf(0L));
		}
		return fixedList;
	}

	@Data
	@NoArgsConstructor
	@JsonDeserialize(using = StatValueDeserializer.class)
	@JsonSerialize(using = StatValueSerializer.class)
	public static class StatValue {
		@JsonIgnore
		private int day;
		private double valueMin;

		public StatValue(double value) {
			super();
			this.valueMin = value;
		}
		
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

	public static class StatValueDeserializer extends JsonDeserializer<StatValue> {

		@Override
		public StatValue deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			JsonNode node = jp.getCodec().readTree(jp);
			double valueMin;
			if (node.isObject()) {
				valueMin = node.get("valueMin").getDoubleValue();
			} else {
				valueMin = node.getDoubleValue();
			}
			return new StatValue(0, valueMin);
		}

	}

	public static class StatValueSerializer extends JsonSerializer<StatValue> {

		@Override
		public void serialize(StatValue value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			jgen.writeNumber(value.getValueMin());
		}

	}

}
