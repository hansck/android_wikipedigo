package go.wikipedi.wikipedigo.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.LocalTime;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by E460 on 12/01/2017.
 */
public class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
	@Override
	public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		try {
			return LocalTime.parse(json.getAsString());
		} catch (IllegalArgumentException e) {
			// May be it came in formatted as a java.util.Date, so try that
			Date date = context.deserialize(json, Date.class);
			return new LocalTime(date);
		}
	}
}
