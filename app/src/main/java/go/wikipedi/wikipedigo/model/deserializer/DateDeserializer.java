package go.wikipedi.wikipedigo.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by E460 on 12/01/2017.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)).parse(json.getAsString());
		} catch (ParseException e) {
			e.printStackTrace();
			return context.deserialize(json, Date.class);
		}
	}
}
