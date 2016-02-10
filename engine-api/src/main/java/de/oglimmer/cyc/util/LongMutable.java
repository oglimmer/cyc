package de.oglimmer.cyc.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = LongMutableDeserializer.class)
@JsonSerialize(using = LongMutableSerializer.class)
public class LongMutable extends Number {

	private static final long serialVersionUID = 1L;

	public long val;

	@Override
	public String toString() {
		return Long.toString(val);
	}

	@Override
	public int intValue() {
		return (int) val;
	}

	@Override
	public long longValue() {
		return val;
	}

	@Override
	public float floatValue() {
		return val;
	}

	@Override
	public double doubleValue() {
		return val;
	}
}

class LongMutableDeserializer extends JsonDeserializer<LongMutable> {

	@Override
	public LongMutable deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		long valueMin = node.getLongValue();
		return new LongMutable(valueMin);
	}

}

class LongMutableSerializer extends JsonSerializer<LongMutable> {

	@Override
	public void serialize(LongMutable value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeNumber(value.val);
	}

}